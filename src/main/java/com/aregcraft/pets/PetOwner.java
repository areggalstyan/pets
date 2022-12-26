package com.aregcraft.pets;

import com.aregcraft.delta.api.ItemWrapper;
import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.delta.api.entity.EntityBuilder;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;

import java.util.Objects;
import java.util.Optional;

public class PetOwner implements Listener {
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
        inventory = plugin.getPetMenu().createInventory(player);
        createArmorStand(container.getSelectedPet());
        plugin.registerListener(this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (checkPlayer(event) && armorStand != null) {
            armorStand.teleport(getArmorStandLocation());
        }
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (!checkPlayer(event)) {
            return;
        }
        var selectedPet = container.getSelectedPet();
        if (selectedPet == null) {
            return;
        }
        if (selectedPet.addExperience(event.getAmount())) {
            selectedPet.removeAttributeModifiers(player);
            selectedPet.addAttributeModifiers(player);
            createArmorStand(selectedPet);
        }
        setContainer();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!checkPlayer(event)) {
            return;
        }
        var item = ItemWrapper.wrap(event.getItem());
        if (item == null) {
            return;
        }
        var pet = Pet.of(item, plugin);
        if (pet == null) {
            return;
        }
        item.decrementAmount();
        container.addPet(pet);
        setContainer();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (checkPlayer(event)) {
            removeArmorStand();
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().equals(armorStand)) {
            event.setCancelled(true);
        }
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
        if (pet == null) {
            removeArmorStand();
        } else if (pet.equals(container.getSelectedPet())) {
            selectPet(null);
            return;
        } else {
            pet.addAttributeModifiers(player);
            createArmorStand(pet);
        }
        Optional.ofNullable(container.getSelectedPet()).ifPresent(it -> it.removeAttributeModifiers(player));
        container.selectPet(pet);
        setContainer();
    }

    public void togglePets() {
        container.togglePets();
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
            armorStand = EntityBuilder.createArmorStand().build(getArmorStandLocation());
        }
        armorStand.setCustomName(pet.getName(player));
        Objects.requireNonNull(armorStand.getEquipment()).setHelmet(pet.getHead().unwrap());
    }

    private Location getArmorStandLocation() {
        return player.getLocation().add(plugin.getPetPosition());
    }

    private void removeArmorStand() {
        if (armorStand == null) {
            return;
        }
        armorStand.remove();
        armorStand = null;
    }
}
