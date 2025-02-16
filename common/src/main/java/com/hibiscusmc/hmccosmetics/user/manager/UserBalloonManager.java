package com.hibiscusmc.hmccosmetics.user.manager;

import com.hibiscusmc.hmccosmetics.HMCCosmeticsPlugin;
import com.hibiscusmc.hmccosmetics.config.Settings;
import com.hibiscusmc.hmccosmetics.cosmetic.types.CosmeticBalloonType;
import com.hibiscusmc.hmccosmetics.hooks.modelengine.MegEntityWrapper;
import com.hibiscusmc.hmccosmetics.nms.NMSHandlers;
import com.hibiscusmc.hmccosmetics.nms.PacketArmorStand;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import com.hibiscusmc.hmccosmetics.util.MessagesUtil;
import com.hibiscusmc.hmccosmetics.util.packets.PacketManager;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class UserBalloonManager {

    private BalloonType balloonType;
    private CosmeticBalloonType cosmeticBalloonType;
    private UserBalloonPufferfish pufferfish;
    private final MegEntityWrapper<PacketArmorStand> modelEntity;

    public UserBalloonManager(@NotNull Location location) {
        this.pufferfish = new UserBalloonPufferfish(NMSHandlers.getHandler().getNextEntityId(), UUID.randomUUID());
        this.modelEntity = NMSHandlers.getHandler().getMEGEntity(location.add(Settings.getBalloonOffset()));
    }

    public void spawnModel(@NotNull CosmeticBalloonType cosmeticBalloonType, Color color) {
        // redo this
        if (cosmeticBalloonType.getModelName() != null && HMCCosmeticsPlugin.hasModelEngine()) {
            balloonType = BalloonType.MODELENGINE;
        } else {
            if (cosmeticBalloonType.getItem() != null) {
                balloonType = BalloonType.ITEM;
            } else {
                balloonType = BalloonType.NONE;
            }
        }
        this.cosmeticBalloonType = cosmeticBalloonType;
        MessagesUtil.sendDebugMessages("balloontype is " + balloonType);

        if (balloonType == BalloonType.MODELENGINE) {
            String id = cosmeticBalloonType.getModelName();
            MessagesUtil.sendDebugMessages("Attempting Spawning for " + id);
            if (ModelEngineAPI.api.getModelRegistry().getBlueprint(id) == null) {
                MessagesUtil.sendDebugMessages("Invalid Model Engine Blueprint " + id, Level.SEVERE);
                return;
            }
            final ModeledEntity modeledEntity;
            if (ModelEngineAPI.isModeledEntity(this.modelEntity.entity().getUniqueId())) {
                modeledEntity = ModelEngineAPI.getModeledEntity(this.modelEntity.entity().getUniqueId());
            } else {
                modeledEntity = ModelEngineAPI.createModeledEntity(this.modelEntity.entity());
            }
            ActiveModel model = ModelEngineAPI.createActiveModel(ModelEngineAPI.getBlueprint(id));
            model.setCanHurt(false);
            modeledEntity.addModel(model, false);
            if (color != null) {
                modeledEntity.getModels().forEach((d, singleModel) -> {
                    if (cosmeticBalloonType.isDyablePart(d)) {
                        singleModel.getRendererHandler().setColor(color);
                        singleModel.getRendererHandler().update();
                    }
                });
            }
            return;
        }
        if (balloonType == BalloonType.ITEM) {
            modelEntity.entity().getOriginal().setHelmet(cosmeticBalloonType.getItem());
        }
    }

    public void remove() {
        if (balloonType == BalloonType.MODELENGINE) {
            final ModeledEntity entity = ModelEngineAPI.api.getModeledEntity(modelEntity.entity().getUniqueId());

            if (entity == null) return;

            for (final Player player : entity.getRangeManager().getPlayerInRange()) {
                entity.hideFromPlayer(player);
            }
            entity.destroy();
        }

        modelEntity.entity().getOriginal().despawn();
        cosmeticBalloonType = null;
    }

    public void addPlayerToModel(final CosmeticUser user, final CosmeticBalloonType cosmeticBalloonType) {
        addPlayerToModel(user, cosmeticBalloonType, null);
    }

    public void addPlayerToModel(final CosmeticUser user, final CosmeticBalloonType cosmeticBalloonType, Color color) {
        if (balloonType == BalloonType.MODELENGINE) {
            final ModeledEntity model = ModelEngineAPI.api.getModeledEntity(modelEntity.entity().getUniqueId());
            if (model == null) {
                spawnModel(cosmeticBalloonType, color);
                MessagesUtil.sendDebugMessages("model is null");
                return;
            }
            //if (model.getRangeManager().getPlayerInRange().contains(player)) return;
            model.showToPlayer(user.getPlayer());
            MessagesUtil.sendDebugMessages("Show to player");
            return;
        }
        if (balloonType == BalloonType.ITEM) {
            modelEntity.entity().getOriginal().setHelmet(user.getUserCosmeticItem(cosmeticBalloonType));
        }
    }
    public void removePlayerFromModel(final Player player) {
        if (balloonType == BalloonType.MODELENGINE) {
            final ModeledEntity model = ModelEngineAPI.api.getModeledEntity(modelEntity.entity().getUniqueId());

            if (model == null) return;

            model.hideFromPlayer(player);
            MessagesUtil.sendDebugMessages("Hidden from player");
            return;
        }
        if (balloonType == BalloonType.ITEM) {
            modelEntity.entity().getOriginal().getEquipment().clear();
            return;
        }
    }

    public MegEntityWrapper<PacketArmorStand> getModelEntity() {
        return this.modelEntity;
    }


    public int getPufferfishBalloonId() {
        return pufferfish.getId();
    }
    public UUID getPufferfishBalloonUniqueId() {
        return pufferfish.getUuid();
    }

    public UUID getModelUnqiueId() {
        return getModelEntity().entity().getUniqueId();
    }

    public int getModelId() {
        return getModelEntity().entity().getEntityId();
    }

    public Location getLocation() {
        return this.getModelEntity().entity().getLocation();
    }

    public void setLocation(Location location) {
        this.getModelEntity().entity().getOriginal().teleport(location);
    }

    public void setVelocity(Vector vector) {
        this.getModelEntity().entity().getOriginal().setVelocity(vector);
    }

    public void sendRemoveLeashPacket(List<Player> viewer) {
        PacketManager.sendLeashPacket(getPufferfishBalloonId(), -1, viewer);
    }

    public void sendRemoveLeashPacket() {
        PacketManager.sendLeashPacket(getPufferfishBalloonId(), -1, getLocation());
    }

    public void sendLeashPacket(int entityId) {
        if (cosmeticBalloonType == null) return;
        if (cosmeticBalloonType.isShowLead()) {
            PacketManager.sendLeashPacket(getPufferfishBalloonId(), entityId, getLocation());
        }
    }

    public UserBalloonPufferfish getPufferfish() {
        return pufferfish;
    }

    public enum BalloonType {
        MODELENGINE,
        ITEM,
        NONE
    }
}
