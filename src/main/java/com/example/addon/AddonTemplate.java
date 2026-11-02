package com.example.addon;

import com.example.addon.modules.StashLogger;
import meteordevelopment.meteorclient.systems.addons.Addon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddonTemplate extends Addon {
    public static final Logger LOG = LoggerFactory.getLogger("Thenest Hack");
    public static final Category THENEST_HACK = new Category("Thenest Hack");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Thenest Hack Addon");

        Modules.get().addCategory(THENEST_HACK);
        Modules.get().add(new StashLogger());
    }

    @Override
    public void onRegisterCategories() {
        Modules.get().addCategory(THENEST_HACK);
    }

    @Override
    public String getPackage() {
        return "com.example.addon";
    }
}
