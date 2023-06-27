package com.hibiscusmc.hmccosmetics.nms.v1_19_R1;


import com.hibiscusmc.hmccosmetics.nms.PacketEntity;
import com.hibiscusmc.hmccosmetics.nms.PacketEquipment;
import com.hibiscusmc.hmccosmetics.nms.PacketParticleCloud;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class HMCParticleCloud extends PacketParticleCloud /*extends AreaEffectCloud*/ {

    public HMCParticleCloud(Set<Player> viewers, int entityId, Location location, PacketEquipment equipment) {
        super(viewers, entityId, location, equipment);
    }

    public HMCParticleCloud(Set<Player> viewers, int entityId, Location location, PacketEquipment equipment, @Nullable PacketEntity riding) {
        super(viewers, entityId, location, equipment, riding);
    }

    @Override
    public void sendToViewers(Collection<? extends Player> viewers) {

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

//    public HMCParticleCloud(Level world, double x, double y, double z) {
//        super(world, x, y, z);
//    }
//
//    public HMCParticleCloud(Location loc) {
//        super(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ());
//        this.setPos(loc.getX(), loc.getY(), loc.getZ());
//        setInvisible(true);
//        setInvulnerable(true);
//        setSilent(true);
//        setNoGravity(true);
//        persist = false;
//    }
}
