package com.aregcraft.pets;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PetOwner implements Listener {
    private static final String PET_CONTAINER_KEY = "pet_container";
    private static final Map<UUID, PetOwner> OWNERS = new HashMap<>();
    private static final GsonPersistentDataType<PetContainer> PET_CONTAINER_DATA_TYPE =
            new GsonPersistentDataType<>(PetContainer.class);

    private final Player player;
    private final Pets plugin;
    private final NamespacedKey containerKey;
    private final PetContainer container;
    private ArmorStand armorStand;

    private PetOwner(Player player, Pets plugin) {
        this.player = player;
        this.plugin = plugin;
        containerKey = new NamespacedKey(plugin, PET_CONTAINER_KEY);
        container = Optional.ofNullable(getContainer()).orElseGet(PetContainer::new);
        setArmorStandHead();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static PetOwner getInstance(Player player) {
        return OWNERS.get(player.getUniqueId());
    }

    public static void registerPlayer(Player player, Pets plugin) {
        OWNERS.put(player.getUniqueId(), new PetOwner(player, plugin));
    }

    private PetContainer getContainer() {
        return player.getPersistentDataContainer().get(containerKey, PET_CONTAINER_DATA_TYPE);
    }

    private void setContainer() {
        player.getPersistentDataContainer().set(containerKey, PET_CONTAINER_DATA_TYPE, container);
    }

    public List<Pet> getPets() {
        return container.getPets();
    }

    public void addPet(Pet pet) {
        container.addPet(pet);
        setContainer();
    }

    public void removePet(Pet pet) {
        container.removePet(pet);
        setContainer();
        if (pet.equals(getSelectedPet())) {
            setSelectedPet(null);
        }
    }

    public void clearPets() {
        container.clearPets();
        setSelectedPet(null);
    }

    public Pet getSelectedPet() {
        return container.getSelectedPet();
    }

    public void setSelectedPet(Pet selectedPet) {
        if (selectedPet != null && Objects.equals(getSelectedPet(), selectedPet)) {
            setSelectedPet(null);
            return;
        }
        removeAttributeModifiers();
        container.setSelectedPet(selectedPet);
        setContainer();
        addAttributeModifiers();
        if (isShowPets()) {
            setArmorStandHead();
        }
    }

    private void addAttributeModifiers() {
        if (getSelectedPet() != null) {
            getSelectedPet().addAttributeModifiers(player);
        }
    }

    private void removeAttributeModifiers() {
        if (getSelectedPet() != null) {
            getSelectedPet().removeAttributeModifiers(player);
        }
    }

    private void setArmorStandHead() {
        if (getSelectedPet() == null) {
            removeArmorStand();
            return;
        }
        if (armorStand == null) {
            createArmorStand();
        }
        armorStand.setCustomName(getFormat().format(getSelectedPet().getName()));
        Objects.requireNonNull(armorStand.getEquipment()).setHelmet(getSelectedPet().getHead());
    }

    public boolean isShowPets() {
        return container.isShowPets();
    }

    public void setShowPets(boolean showPets) {
        container.setShowPets(showPets);
        setContainer();
        if (!showPets) {
            removeArmorStand();
            return;
        }
        setArmorStandHead();
    }

    private void createArmorStand() {
        armorStand = player.getWorld().spawn(getArmorStandLocation(), ArmorStand.class);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setCanPickupItems(false);
        armorStand.setCustomNameVisible(true);
    }

    private Format getFormat() {
        return Format.builder()
                .entry("PLAYER", player.getDisplayName())
                .build();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (armorStand != null) {
            armorStand.teleport(getArmorStandLocation());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }
        var item = PetItem.getInstance(event.getItem(), plugin);
        addPet(Pet.getInstance(item, plugin));
        if (item != null) {
            decrementAmount(event.getItem());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        removeArmorStand();
        OWNERS.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().equals(armorStand)) {
            event.setCancelled(true);
        }
    }

    private void removeArmorStand() {
        Optional.ofNullable(armorStand).ifPresent(ArmorStand::remove);
        armorStand = null;
    }

    private void decrementAmount(ItemStack item) {
        item.setAmount(item.getAmount() - 1);
    }

    private Location getArmorStandLocation() {
        return player.getLocation().add(plugin.getPetPosition());
    }

    public Player getPlayer() {
        return player;
    }
}
