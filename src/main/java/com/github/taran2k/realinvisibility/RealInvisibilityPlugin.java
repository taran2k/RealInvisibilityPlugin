package com.github.taran2k.realinvisibility;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class RealInvisibilityPlugin extends JavaPlugin {
    private static RealInvisibilityPlugin instance;
    private ProtocolManager protocolManager;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();
        
        // Get the config
        config = getConfig();

        // Initialize ProtocolLib
        Plugin protocolLib = getServer().getPluginManager().getPlugin("ProtocolLib");
        if (protocolLib == null) {
            getLogger().severe("ProtocolLib not found! This plugin requires ProtocolLib.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        protocolManager = ProtocolLibrary.getProtocolManager();

        // Register packet listeners and events
        new InvisibilityPacketHandler(this, protocolManager);
        new InvisibilityMobListener(this);

        instance = this;
        getLogger().info("RealInvisibility plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        if (protocolManager != null) {
            protocolManager.removePacketListeners(this);
        }
        getLogger().info("RealInvisibility plugin has been disabled!");
    }

    public static RealInvisibilityPlugin getInstance() {
        return instance;
    }

    public boolean isHelmetHidden() {
        return config.getBoolean("helmet", true);
    }

    public boolean isChestplateHidden() {
        return config.getBoolean("chestplate", true);
    }

    public boolean isLeggingsHidden() {
        return config.getBoolean("leggings", true);
    }

    public boolean isBootsHidden() {
        return config.getBoolean("boots", true);
    }

    public boolean isMainHandHidden() {
        return config.getBoolean("mainhand", true);
    }

    public boolean isOffHandHidden() {
        return config.getBoolean("offhand", true);
    }

    public boolean areArrowsHidden() {
        return config.getBoolean("arrows", true);
    }

    public boolean areParticlesHidden() {
        return config.getBoolean("particles", true);
    }
}