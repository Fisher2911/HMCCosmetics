package com.hibiscusmc.hmccosmetics.nms;

import com.hibiscusmc.hmccosmetics.cosmetic.CosmeticSlot;
import com.hibiscusmc.hmccosmetics.cosmetic.types.CosmeticBackpackType;
import com.hibiscusmc.hmccosmetics.cosmetic.types.CosmeticBalloonType;
import com.hibiscusmc.hmccosmetics.hooks.modelengine.MegEntityWrapper;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import com.hibiscusmc.hmccosmetics.user.manager.UserBalloonManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface NMSHandler {

    int getNextEntityId();

    @Nullable
    PacketEntity getEntity(int entityId);

    PacketArmorStand getHMCArmorStand(Location loc);

    <T extends PacketEntity> MegEntityWrapper<T> getMEGEntity(Location loc);

    @Nullable
    PacketArmorStand spawnBackpack(
            CosmeticUser user,
            CosmeticBackpackType cosmeticBackpackType,
            Set<Player> viewers
    );

    @Nullable
    PacketParticleCloud spawnHMCParticleCloud(Location location);

    Entity spawnDisplayEntity(Location location, String text);

    UserBalloonManager spawnBalloon(CosmeticUser user, CosmeticBalloonType cosmeticBalloonType);

    void equipmentSlotUpdate(
            int entityId,
            CosmeticUser user,
            CosmeticSlot cosmeticSlot,
            List<Player> sendTo
    );

    void slotUpdate(
            Player player,
            int slot
    );

    void equipmentSlotUpdate(
            int entityId,
            org.bukkit.inventory.EquipmentSlot slot,
            ItemStack item,
            List<Player> sendTo
    );

    void hideNPCName(
            Player player,
            String NPCName);

    default boolean getSupported() {
        return false;
    }

}
