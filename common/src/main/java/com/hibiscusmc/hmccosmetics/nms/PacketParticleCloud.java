package com.hibiscusmc.hmccosmetics.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class PacketParticleCloud extends PacketEntity {

    public PacketParticleCloud(Set<Player> viewers, int entityId, Location location, PacketEquipment equipment) {
        super(viewers, entityId, location, equipment);
    }

    public PacketParticleCloud(Set<Player> viewers, int entityId, Location location, PacketEquipment equipment, @Nullable PacketEntity riding) {
        super(viewers, entityId, location, equipment, riding);
    }

}
