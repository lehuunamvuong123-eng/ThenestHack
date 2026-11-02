package com.example.addon;

import com.example.addon.modules.StashLogger;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddonTemplate extends MeteorAddon {
    public static final Logger LOG = LoggerFactory.getLogger("Thenest Hack");
    public static final Category THENEST_HACK = new Category("Thenest Hack");
    
    // Thêm lại dòng này để chiều lòng file HudExample.java nếu nó còn tồn tại
    public static final HudGroup HUD_GROUP = new HudGroup("Thenest Hack");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Thenest Hack Addon");

        // Đăng ký module StashLogger
        Modules.get().add(new StashLogger());
    }

    @Override
    public void onRegisterCategories() {
        Modules.get().registerCategory(THENEST_HACK);
    }

    @Override
    public String getPackage() {
        return "com.example.addon";
    }
}
