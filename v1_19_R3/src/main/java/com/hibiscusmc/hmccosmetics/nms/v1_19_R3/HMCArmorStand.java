package com.hibiscusmc.hmccosmetics.nms.v1_19_R3;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.hibiscusmc.hmccosmetics.nms.PacketArmorStand;
import com.hibiscusmc.hmccosmetics.nms.PacketEntity;
import com.hibiscusmc.hmccosmetics.nms.PacketEquipment;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class HMCArmorStand extends PacketArmorStand /*extends ArmorStand*/ {

    public HMCArmorStand(Set<Player> viewers, int entityId, Location location, PacketEquipment equipment, UUID uuid) {
        super(viewers, entityId, location, equipment, uuid);
    }

    public HMCArmorStand(Set<Player> viewers, int entityId, Location location, PacketEquipment equipment, UUID uuid, @Nullable PacketEntity riding) {
        super(viewers, entityId, location, equipment, uuid, riding);
    }

    @Override
    public void sendToViewers(Collection<? extends Player> viewers) {
        final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        final PacketContainer spawnPacket = manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        spawnPacket.getIntegers().write(0, this.getEntityId());
        spawnPacket.getUUIDs().write(0, this.getUuid());
        spawnPacket.getIntegers().write(1, 35);
        spawnPacket.getDoubles().write(0, this.getLocation().getX());
        spawnPacket.getDoubles().write(1, this.getLocation().getY());
        spawnPacket.getDoubles().write(2, this.getLocation().getZ());
        spawnPacket.getBytes().write(0, (byte) (this.getLocation().getYaw() * 256.0F / 360.0F));
        spawnPacket.getBytes().write(1, (byte) (this.getLocation().getPitch() * 256.0F / 360.0F));
        for (Player viewer : viewers) {
            manager.sendServerPacket(viewer, spawnPacket);
        }
    }

    @Override
    public void sendEquipment() {

    }

    @Override
    public void despawn(Collection<? extends Player> viewers) {

    }

    @Override
    public void teleport(Location location) {

    }

//    public HMCArmorStand(Level world, double x, double y, double z) {
//        super(world, x, y, z);
//    }
//
//    public HMCArmorStand(Location loc) {
//        super(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ());
//        this.setPos(loc.getX(), loc.getY(), loc.getZ());
//        setInvisible(true);
//        setInvulnerable(true);
//        setMarker(true);
//        setSilent(true);
//        getBukkitLivingEntity().setCollidable(false);
//        persist = false;
//    }
}
