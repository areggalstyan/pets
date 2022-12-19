package com.aregcraft.pets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PetsMenu implements Listener {
    private final Pets plugin;
    private final PetOwner owner;
    private final Inventory inventory;

    public PetsMenu(Player player, Pets plugin) {
        this.plugin = plugin;
        owner = PetOwner.getInstance(player);
        inventory = Bukkit.createInventory(player, plugin.getMenuSize(), plugin.getMenuTitle());
        inventory.addItem(owner.getPets().stream().map(this::getPetItemStack).toArray(ItemStack[]::new));
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private ItemStack getPetItemStack(Pet pet) {
        return PetItem.getInstance(pet, plugin).getItemStack(plugin);
    }

    public void open() {
        owner.getPlayer().openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!inventory.equals(event.getClickedInventory()) || event.getCurrentItem() == null) {
            return;
        }
        var item = event.getCurrentItem().clone();
        item.setAmount(1);
        var pet = Pet.getInstance(PetItem.getInstance(item, plugin), plugin);
        if (pet == null) {
            return;
        }
        event.setCancelled(true);
        if (event.getClick().isLeftClick()) {
            owner.removePet(pet);
            decrementAmount(event.getCurrentItem());
            event.getWhoClicked().getInventory().addItem(item);
        } else if (event.getClick().isRightClick()) {
            owner.setSelectedPet(pet);
        }
    }

    private void decrementAmount(ItemStack item) {
        item.setAmount(item.getAmount() - 1);
    }
}
