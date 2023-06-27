package com.hibiscusmc.hmccosmetics.user.manager;

import com.hibiscusmc.hmccosmetics.HMCCosmeticsPlugin;
import com.hibiscusmc.hmccosmetics.cosmetic.CosmeticSlot;
import com.hibiscusmc.hmccosmetics.cosmetic.types.CosmeticBackpackType;
import com.hibiscusmc.hmccosmetics.hooks.modelengine.MegEntityWrapper;
import com.hibiscusmc.hmccosmetics.nms.NMSHandlers;
import com.hibiscusmc.hmccosmetics.nms.PacketArmorStand;
import com.hibiscusmc.hmccosmetics.nms.PacketParticleCloud;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import com.hibiscusmc.hmccosmetics.util.MessagesUtil;
import com.hibiscusmc.hmccosmetics.util.PlayerUtils;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.logging.Level;

public class UserBackpackManager {

    private boolean hideBackpack;
    private PacketArmorStand invisibleArmorStand;
    private PacketParticleCloud particleCloud;
    private final CosmeticUser user;
    private BackpackType backpackType;

    public UserBackpackManager(CosmeticUser user, BackpackType backpackType) {
        this.user = user;
        hideBackpack = false;
        this.backpackType = backpackType;
    }

    public int getFirstArmorStandId() {
        return invisibleArmorStand.getEntityId();
    }

    public PacketArmorStand getArmorStand() {
        return invisibleArmorStand;
    }

    public boolean IsValidBackpackEntity() {
        if (invisibleArmorStand == null) {
            MessagesUtil.sendDebugMessages("InvisibleArmorStand is Null!");
            return false;
        }
        return true;
    }

    public void spawnBackpack(CosmeticBackpackType cosmeticBackpackType) {
        MessagesUtil.sendDebugMessages("spawnBackpack Bukkit - Start");

        if (getBackpackType().equals(BackpackType.NORMAL)) {
            spawnNormalBackpack(cosmeticBackpackType);
        }
        if (getBackpackType().equals(BackpackType.FIRST_PERSON)) {
            spawnFirstPersonBackpack(cosmeticBackpackType);
        }
    }

    private void spawnNormalBackpack(CosmeticBackpackType cosmeticBackpackType) {

        if (this.invisibleArmorStand != null) return;

        this.invisibleArmorStand = NMSHandlers.getHandler().spawnBackpack(
                user,
                cosmeticBackpackType,
                new HashSet<>(PlayerUtils.getNearbyPlayers(user.getPlayer().getLocation()))
        );

        if (cosmeticBackpackType.getModelName() != null && HMCCosmeticsPlugin.hasModelEngine()) {
            if (ModelEngineAPI.api.getModelRegistry().getBlueprint(cosmeticBackpackType.getModelName()) == null) {
                MessagesUtil.sendDebugMessages("Invalid Model Engine Blueprint " + cosmeticBackpackType.getModelName(), Level.SEVERE);
                return;
            }
            final ModeledEntity modeledEntity;
            final MegEntityWrapper wrapper = this.invisibleArmorStand.getMegEntityWrapper();
            if (ModelEngineAPI.isModeledEntity(wrapper.entity().getUniqueId())) {
                modeledEntity = ModelEngineAPI.getModeledEntity(wrapper.entity().getUniqueId());
            } else {
               modeledEntity = ModelEngineAPI.createModeledEntity(wrapper.entity());
            }
            ActiveModel model = ModelEngineAPI.createActiveModel(ModelEngineAPI.getBlueprint(cosmeticBackpackType.getModelName()));
            model.setCanHurt(false);
            modeledEntity.addModel(model, false);
        }

        MessagesUtil.sendDebugMessages("spawnBackpack Bukkit - Finish");
    }

    public void spawnFirstPersonBackpack(CosmeticBackpackType cosmeticBackpackType) {

        if (this.invisibleArmorStand != null) return;

        this.invisibleArmorStand = NMSHandlers.getHandler().spawnBackpack(
                user,
                cosmeticBackpackType,
                new HashSet<>(PlayerUtils.getNearbyPlayers(user.getPlayer().getLocation()))
        );
        this.particleCloud = NMSHandlers.getHandler().spawnHMCParticleCloud(user.getPlayer().getLocation());

        if (cosmeticBackpackType.getModelName() != null && HMCCosmeticsPlugin.hasModelEngine()) {
            if (ModelEngineAPI.api.getModelRegistry().getBlueprint(cosmeticBackpackType.getModelName()) == null) {
                MessagesUtil.sendDebugMessages("Invalid Model Engine Blueprint " + cosmeticBackpackType.getModelName(), Level.SEVERE);
                return;
            }
            final ModeledEntity modeledEntity;
            final MegEntityWrapper wrapper = this.invisibleArmorStand.getMegEntityWrapper();
            if (ModelEngineAPI.isModeledEntity(wrapper.entity().getUniqueId())) {
                modeledEntity = ModelEngineAPI.getModeledEntity(wrapper.entity().getUniqueId());
            } else {
                modeledEntity = ModelEngineAPI.createModeledEntity(wrapper.entity());
            }            ActiveModel model = ModelEngineAPI.createActiveModel(ModelEngineAPI.getBlueprint(cosmeticBackpackType.getModelName()));
            model.setCanHurt(false);
            modeledEntity.addModel(model, false);
        }

        MessagesUtil.sendDebugMessages("spawnBackpack Bukkit - Finish");
    }

    public void despawnBackpack() {
        if (invisibleArmorStand != null) {
            invisibleArmorStand.despawn();
            this.invisibleArmorStand = null;
        }
        if (particleCloud != null) {
            particleCloud.despawn();
            this.particleCloud = null;
        }
    }

    public void hideBackpack() {
        if (user.getHidden()) return;
        getArmorStand().getEquipment().clear();
        getArmorStand().sendEquipment();
        hideBackpack = true;
    }

    public void showBackpack() {
        if (!hideBackpack) return;
        CosmeticBackpackType cosmeticBackpackType = (CosmeticBackpackType) user.getCosmetic(CosmeticSlot.BACKPACK);
        ItemStack item = user.getUserCosmeticItem(cosmeticBackpackType);
        getArmorStand().setHelmet(item);
        hideBackpack = false;
    }

    public void setVisibility(boolean shown) {
        hideBackpack = shown;
    }

    public BackpackType getBackpackType() {
        return backpackType;
    }

    public int getAreaEffectEntityId() {
        return particleCloud.getEntityId();
    }

    public void teleportEffectEntity(Location location) {
        particleCloud.teleport(location);
    }

    public void setItem(ItemStack item) {
        getArmorStand().setHelmet(item);
    }

    public void clearItems() {
        ItemStack item = new ItemStack(Material.AIR);
        getArmorStand().setHelmet(item);
    }

    public enum BackpackType {
        NORMAL,
        FIRST_PERSON // First person not yet implemented
    }

}
