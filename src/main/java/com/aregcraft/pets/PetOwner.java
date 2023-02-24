package com.aregcraft.pets;

import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.delta.api.entity.EntityBuilder;
import com.aregcraft.delta.api.entity.EquipmentWrapper;
import com.aregcraft.delta.api.item.ItemWrapper;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import java.util.logging.Level;

public class PetOwner implements Listener {
    private static final String DATA_CORRUPTION_ERROR =
            "A data corruption was detected while trying to register player %s!";

    private final Player player;
    private final Pets plugin;
    private final PersistentDataWrapper persistentData;
    private final PetContainer container;
    private final Inventory inventory;
    private ArmorStand armorStand;

    public PetOwner(Player player, Pets plugin) {
        this.player = player;
        this.plugin = plugin;
        persistentData = PersistentDataWrapper.wrap(plugin, player);
        container = persistentData.getOrElse("pet_container", new PetContainer());
        checkForDataCorruption();
        setContainer();
        inventory = plugin.getPetMenu().createInventory(player);
        createArmorStand(container.getSelectedPet());
        plugin.registerListener(this);
    }

    private void checkForDataCorruption() {
        var pets = container.getPets();
        pets.stream().filter(it -> it.getRarity() == null).forEach(it -> it.setRarity(plugin.getDefaultRarity()));
        if (pets.stream().noneMatch(it -> it.getType() == null)) {
            return;
        }
        plugin.getLogger().log(Level.SEVERE, DATA_CORRUPTION_ERROR.formatted(player.getDisplayName()));
        plugin.getLogger().log(Level.SEVERE, String.valueOf(container));
        container.selectPet(null);
        pets.removeIf(it -> it.getType() == null);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!checkPlayer(event)) {
            return;
        }
        if (armorStand == null) {
            createArmorStand(container.getSelectedPet());
        }
        if (armorStand != null) {
            armorStand.teleport(getArmorStandLocation());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().equals(player)) {
            removeArmorStand();
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getPlayer().equals(player)) {
            removeArmorStand();
        }
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (checkPlayer(event)) {
            removeArmorStand();
        }
    }

    @EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        if (!checkPlayer(event)) {
            return;
        }
        var selectedPet = container.getSelectedPet();
        if (selectedPet == null) {
            return;
        }
        if (selectedPet.addExperience(Math.max(event.getNewLevel() - event.getOldLevel(), 0))) {
            updateExperience();
        }
        setContainer();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        var action = event.getAction();
        var item = ItemWrapper.wrap(event.getItem());
        if (!checkPlayer(event) || action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK
                || event.getHand() != EquipmentSlot.HAND || item == null) {
            return;
        }
        var pet = Pet.of(item, plugin);
        if (pet != null) {
            item.decrementAmount();
            container.addPet(pet);
            setContainer();
            event.setCancelled(true);
        }
        var selectedPet = container.getSelectedPet();
        if (selectedPet == null) {
            return;
        }
        var experienceBooster = ExperienceBooster.of(item, plugin);
        if (experienceBooster != null) {
            item.decrementAmount();
            selectedPet.setExperienceBooster(experienceBooster);
            setContainer();
            event.setCancelled(true);
        }
        var upgrade = Upgrade.of(item, plugin);
        if (upgrade != null && selectedPet.canUpgrade(upgrade)) {
            item.decrementAmount();
            selectedPet.applyUpgrade(upgrade, player);
            setContainer();
            event.setCancelled(true);
            return;
        }
        var candy = Candy.of(item, plugin);
        if (candy == null || !selectedPet.canUseCandy()) {
            return;
        }
        item.decrementAmount();
        if (selectedPet.useCandy(candy)) {
            updateExperience();
        }
        setContainer();
        event.setCancelled(true);
    }

    private void updateExperience() {
        var selectedPet = container.getSelectedPet();
        selectedPet.removeAttributeModifiers(player);
        selectedPet.addAttributeModifiers(player);
        createArmorStand(selectedPet);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (checkPlayer(event)) {
            removeArmorStand();
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        event.setCancelled(event.getRightClicked().equals(armorStand));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!inventory.equals(event.getClickedInventory())) {
            return;
        }
        var item = ItemWrapper.wrap(event.getCurrentItem());
        if (item == null) {
            return;
        }
        var pet = Pet.of(item, plugin);
        if (pet == null) {
            return;
        }
        event.setCancelled(true);
        var click = event.getClick();
        if (click.isLeftClick()) {
            player.getInventory().addItem(pet.getItem(plugin).unwrap());
            item.decrementAmount();
            removePet(pet);
        } else if (click.isRightClick()) {
            selectPet(pet);
        }
    }

    private void removePet(Pet pet) {
        if (pet.equals(container.getSelectedPet())) {
            selectPet(null);
        }
        container.removePet(pet);
        setContainer();
    }

    public void openPetMenu() {
        inventory.clear();
        container.getPets().stream().map(it -> it.getItem(plugin).unwrap()).forEach(inventory::addItem);
        player.openInventory(inventory);
    }

    private boolean checkPlayer(PlayerEvent event) {
        return event.getPlayer().equals(player);
    }

    public void selectPet(Pet pet) {
        var selectedPet = container.getSelectedPet();
        if (pet == null) {
            removeArmorStand();
            plugin.giveDeselectFeedback(player, selectedPet);
        } else if (pet.equals(selectedPet)) {
            selectPet(null);
            return;
        } else {
            plugin.giveSelectFeedback(player, pet);
            pet.addAttributeModifiers(player);
            pet.applyPerks(player);
            createArmorStand(pet);
        }
        if (selectedPet != null) {
            selectedPet.removeAttributeModifiers(player);
            selectedPet.unapplyPerks(player);
        }
        container.selectPet(pet);
        setContainer();
    }

    public void togglePets() {
        container.togglePets();
        setContainer();
    }

    public void clearPets() {
        selectPet(null);
        container.clearPets();
        setContainer();
    }

    private void setContainer() {
        persistentData.set("pet_container", container);
    }

    private void createArmorStand(Pet pet) {
        if (!container.isShowPets() || pet == null) {
            return;
        }
        if (armorStand == null) {
            armorStand = EntityBuilder.createArmorStand()
                    .nameVisible(true)
                    .persistentData("id", "PET_ARMOR_STAND")
                    .build(getArmorStandLocation(), plugin);
        }
        armorStand.setCustomName(pet.getName(player));
        EquipmentWrapper.wrap(armorStand).setHelmet(pet.getHead());
    }

    private Location getArmorStandLocation() {
        return player.getLocation().add(plugin.getPetPosition());
    }

    public void removeArmorStand() {
        if (armorStand == null) {
            return;
        }
        armorStand.remove();
        armorStand = null;
    }
}
