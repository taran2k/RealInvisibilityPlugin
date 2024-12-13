package com.github.taran2k.realinvisibility;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.potion.PotionEffectType;

public class InvisibilityMobListener implements Listener {
    private final RealInvisibilityPlugin plugin;

    public InvisibilityMobListener(RealInvisibilityPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        // Check if the target is a player
        if (!(event.getTarget() instanceof Player)) return;

        Player player = (Player) event.getTarget();
        Entity entity = event.getEntity();

        // Check if the player is invisible and the entity is a mob (except bosses)
        if (isInvisiblePlayer(player) && isMobExceptBosses(entity)) {
            // Cancel targeting for these mobs
            event.setCancelled(true);
        }
    }

    private boolean isInvisiblePlayer(Player player) {
        return player.hasPotionEffect(PotionEffectType.INVISIBILITY);
    }

    private boolean isMobExceptBosses(Entity entity) {
        // Check if the entity is a monster but not a boss
        return (entity instanceof Monster) 
            && !entity.getType().name().equals("WITHER")
            && !entity.getType().name().equals("ENDER_DRAGON");
    }
}