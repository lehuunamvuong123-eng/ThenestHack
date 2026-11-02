package com.example.addon.modules;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class StashLogger extends Module {
    // === SETTING GROUPS ===
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgBlocks = settings.createGroup("Block Select");

    // --- General Settings ---
    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("range")
        .description("Scan range around player (Max 50 to prevent crashes).")
        .defaultValue(16)
        .min(1).max(50)
        .sliderMin(1).sliderMax(50)
        .build());

    private final Setting<Integer> minContainerCount = sgGeneral.add(new IntSetting.Builder()
        .name("min-container-count")
        .description("Minimum total containers required to trigger logging.")
        .defaultValue(1)
        .min(1).max(50)
        .sliderMin(1).sliderMax(50)
        .build());

    // --- Block Select Settings ---
    private final Setting<Boolean> logChests = sgBlocks.add(new BoolSetting.Builder()
        .name("chests")
        .description("Scan normal chests.")
        .defaultValue(true)
        .build());

    private final Setting<Boolean> logTrappedChests = sgBlocks.add(new BoolSetting.Builder()
        .name("trapped-chests")
        .description("Scan trapped chests.")
        .defaultValue(true)
        .build());

    private final Setting<Boolean> logEnderChests = sgBlocks.add(new BoolSetting.Builder()
        .name("ender-chests")
        .description("Scan ender chests.")
        .defaultValue(true)
        .build());

    private final Setting<Boolean> logShulkers = sgBlocks.add(new BoolSetting.Builder()
        .name("shulker-boxes")
        .description("Scan shulker boxes.")
        .defaultValue(true)
        .build());

    private final Setting<Boolean> logBarrels = sgBlocks.add(new BoolSetting.Builder()
        .name("barrels")
        .description("Scan barrels.")
        .defaultValue(true)
        .build());

    private final Setting<Boolean> logHoppers = sgBlocks.add(new BoolSetting.Builder()
        .name("hoppers")
        .description("Scan hoppers.")
        .defaultValue(false)
        .build());

    private final Setting<Boolean> logDispensers = sgBlocks.add(new BoolSetting.Builder()
        .name("dispensers-droppers")
        .description("Scan dispensers and droppers.")
        .defaultValue(false)
        .build());

    private final Set<BlockPos> loggedPositions = new HashSet<>();

    public StashLogger() {
        super(AddonTemplate.THENEST_HACK, "stash-logger", "Silent background container & stash logger for 6B6T.");
    }

    @Override
    public void onActivate() {
        loggedPositions.clear();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.level == null || mc.player == null) return;

        BlockPos playerPos = mc.player.blockPosition();
        int r = range.get();
        int detectedCount = 0;
        BlockPos lastFoundPos = null;
        String lastFoundType = "";

        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = playerPos.offset(x, y, z);

                    if (loggedPositions.contains(pos)) continue;

                    BlockEntity blockEntity = mc.level.getBlockEntity(pos);
                    if (blockEntity != null) {
                        String type = getContainerType(blockEntity);
                        if (type != null) {
                            detectedCount++;
                            lastFoundPos = pos;
                            lastFoundType = type;
                            loggedPositions.add(pos);
                        }
                    }
                }
            }
        }

        if (detectedCount >= minContainerCount.get() && lastFoundPos != null) {
            handleStashFound(lastFoundType, lastFoundPos, detectedCount);
        }
    }

    private String getContainerType(BlockEntity be) {
        if (be instanceof TrappedChestBlockEntity && logTrappedChests.get()) return "Trapped Chest";
        if (be instanceof ChestBlockEntity && logChests.get()) return "Chest";
        if (be instanceof EnderChestBlockEntity && logEnderChests.get()) return "Ender Chest";
        if (be instanceof ShulkerBoxBlockEntity && logShulkers.get()) return "Shulker Box";
        if (be instanceof BarrelBlockEntity && logBarrels.get()) return "Barrel";
        if (be instanceof HopperBlockEntity && logHoppers.get()) return "Hopper";
        if ((be instanceof DispenserBlockEntity || be instanceof DropperBlockEntity) && logDispensers.get()) return "Dispenser/Dropper";
        return null;
    }

    private void handleStashFound(String type, BlockPos pos, int count) {
        String dim = mc.level.dimension().identifier().getPath();
        String timeStr = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());

        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        String logMessage = String.format("[%s] Stash Found! (%d containers) Near %s at X: %d, Y: %d, Z: %d (Chunk: [%d, %d], %s)",
            timeStr, count, type, pos.getX(), pos.getY(), pos.getZ(), chunkX, chunkZ, dim);

        // 1. Lưu trực tiếp vào file text âm thầm
        try {
            File file = new File("thenest_stashes.txt");
            FileWriter writer = new FileWriter(file, true);
            writer.write(logMessage + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Ngắt kết nối khỏi server ngay lập tức
        if (mc.getConnection() != null) {
            String disconnectReason = String.format(
                "§c[Thenest Hack - Auto Log] Stash Discovered!\n" +
                    "§fCoordinates: X: %d, Y: %d, Z: %d\n" +
                    "§fChunk: [%d, %d] (%d containers)",
                pos.getX(), pos.getY(), pos.getZ(), chunkX, chunkZ, count
            );

            mc.getConnection().getConnection().disconnect(Component.literal(disconnectReason));
        }

        // 3. Tự động tắt (toggle) module ngay sau khi phát hiện và log thành công
        this.toggle();
    }
}
