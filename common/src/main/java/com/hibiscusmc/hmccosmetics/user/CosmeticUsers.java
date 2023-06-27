package com.hibiscusmc.hmccosmetics.user;

import com.google.common.collect.HashBiMap;
import com.hibiscusmc.hmccosmetics.nms.PacketEntity;
import com.hibiscusmc.hmccosmetics.nms.PacketPlayer;
import com.hibiscusmc.hmccosmetics.util.ServerUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CosmeticUsers {

    private static HashBiMap<UUID, CosmeticUser> COSMETIC_USERS = HashBiMap.create();

    public static void addUser(CosmeticUser user) {
        if (COSMETIC_USERS.containsKey(user.getUniqueId())) return; // do not add if already exists
        COSMETIC_USERS.put(user.getUniqueId(), user);
    }

    public static void removeUser(UUID uuid) {
        COSMETIC_USERS.remove(uuid);
    }

    public static void removeUser(CosmeticUser user) {
        COSMETIC_USERS.remove(user);
    }

    @Nullable
    public static CosmeticUser getUser(UUID uuid) {
        return COSMETIC_USERS.get(uuid);
    }

    @Nullable
    public static CosmeticUser getUser(Player player) {
        return COSMETIC_USERS.get(player.getUniqueId());
    }

    @Nullable
    public static CosmeticUser getUser(int entityId) {
        final PacketEntity entity = ServerUtils.getEntity(entityId);
        if (entity == null) return null;
//        if (entity.getType().equals(EntityType.PLAYER)) return null;
        if (!(entity instanceof final PacketPlayer player)) return null;
        return COSMETIC_USERS.get(player.getUuid());
    }
}
