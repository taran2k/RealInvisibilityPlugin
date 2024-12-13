package com.github.taran2k.realinvisibility;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class InvisibilityPacketHandler {
    private final RealInvisibilityPlugin plugin;
    private final ProtocolManager protocolManager;

    public InvisibilityPacketHandler(RealInvisibilityPlugin plugin, ProtocolManager protocolManager) {
        this.plugin = plugin;
        this.protocolManager = protocolManager;

        // Packet for hiding equipment
        registerEquipmentPacketListener();

        // Packet for hiding effect particles
        registerEffectParticlePacketListener();
    }

    private void registerEquipmentPacketListener() {
        protocolManager.addPacketListener(
            new PacketAdapter(plugin, PacketType.Play.Server.ENTITY_EQUIPMENT) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    Player receiver = event.getPlayer();
                    Object entity = event.getPacket().getEntityModifier(receiver.getWorld()).read(0);
                
                    // Ensure the entity is a player
                    if (!(entity instanceof Player)) {
                        return; // Exit if the entity is not a player
                    }
                
                    Player subject = (Player) entity;
                
                    // Check if subject is an invisible player
                    if (!subject.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        return;
                    }
                

                    // Check if subject is an invisible player
                    if (subject == null || !subject.hasPotionEffect(PotionEffectType.INVISIBILITY)) return;

                    // Get the equipment slot
                    EnumWrappers.ItemSlot slot = event.getPacket().getItemSlots().read(0);

                    // Check config and hide equipment based on slot
                    switch (slot) {
                        case HEAD:
                            if (((RealInvisibilityPlugin) plugin).isHelmetHidden()) 
                                event.getPacket().getItemModifier().write(0, new ItemStack(org.bukkit.Material.AIR));
                            break;
                        case CHEST:
                            if (((RealInvisibilityPlugin) plugin).isChestplateHidden()) 
                                event.getPacket().getItemModifier().write(0, new ItemStack(org.bukkit.Material.AIR));
                            break;
                        case LEGS:
                            if (((RealInvisibilityPlugin) plugin).isLeggingsHidden()) 
                                event.getPacket().getItemModifier().write(0, new ItemStack(org.bukkit.Material.AIR));
                            break;
                        case FEET:
                            if (((RealInvisibilityPlugin) plugin).isBootsHidden()) 
                                event.getPacket().getItemModifier().write(0, new ItemStack(org.bukkit.Material.AIR));
                            break;
                        case MAINHAND:
                            if (((RealInvisibilityPlugin) plugin).isMainHandHidden()) 
                                event.getPacket().getItemModifier().write(0, new ItemStack(org.bukkit.Material.AIR));
                            break;
                        case OFFHAND:
                            if (((RealInvisibilityPlugin) plugin).isOffHandHidden()) 
                                event.getPacket().getItemModifier().write(0, new ItemStack(org.bukkit.Material.AIR));
                            break;
                    }
                }
            }
        );
    }

    private void registerEffectParticlePacketListener() {
        protocolManager.addPacketListener(
            new PacketAdapter(plugin, PacketType.Play.Server.WORLD_PARTICLES) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    Player receiver = event.getPlayer();
                    Player subject = receiver.getWorld().getPlayers().stream()
                        .filter(p -> p.hasPotionEffect(PotionEffectType.INVISIBILITY))
                        .findFirst()
                        .orElse(null);

                    // Check if there's an invisible player and particles should be hidden
                    if (subject != null && ((RealInvisibilityPlugin) plugin).areParticlesHidden()) {
                        event.setCancelled(true);
                    }
                }
            }
        );
    }
}