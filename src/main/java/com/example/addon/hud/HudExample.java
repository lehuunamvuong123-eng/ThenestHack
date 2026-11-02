
package com.example.addon.hud;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;

public class HudExample extends HudElement {
    public static final HudElementInfo<HudExample> INFO = new HudElementInfo<>(
        AddonTemplate.HUD_GROUP, 
        "example", 
        "HUD element example.", 
        HudExample::new
    );

    public HudExample() {
        super(INFO);
    }

    @Override
    public void render(HudRenderer renderer) {
        // Vẽ chữ hiển thị lên màn hình game (nếu bật HUD này trong game)
        renderer.text("Thenest Hack HUD", x, y, COLOR);
        
        // Cập nhật kích thước khung hiển thị HUD
        box.set(renderer.textWidth("Thenest Hack HUD"), renderer.textHeight());
    }
}
