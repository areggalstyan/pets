package com.aregcraft.pets.command;

import com.aregcraft.delta.api.FormattingContext;
import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.Recipe;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.pets.Pet;
import com.aregcraft.pets.Pets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RegisteredCommand("petsinfo")
public class PetsInfoCommand implements CommandWrapper, Listener {
    private static final List<String> SUBCOMMANDS = List.of("pets", "pet", "recipe");

    private final List<Inventory> inventories = new ArrayList<>();
    @InjectPlugin
    private Pets plugin;

    @Override
    public boolean execute(Player sender, List<String> args) {
        var size = args.size();
        if (size == 0) {
            showUsage(sender);
            return true;
        }
        var subcommand = args.get(0);
        if (size == 1 && subcommand.equals("pets")) {
            listPets(sender);
            return true;
        }
        if (size < 2) {
            showUsage(sender);
            return false;
        }
        var id = args.get(1);
        if (size == 2 && subcommand.equals("recipe")) {
            showRecipe(sender, id);
            return true;
        }
        if (size == 3 && subcommand.equals("pet")) {
            showPet(sender, id, args.get(2));
            return true;
        }
        showUsage(sender);
        return false;
    }

    private void listPets(Player player) {
        sendMessage(player, "%aqua%" + String.join("%gray%, %aqua%", plugin.getPets().getIds()));
    }

    private void showPet(Player player, String id, String rarityId) {
        var petType = plugin.getPets().findAny(id);
        if (petType == null) {
            showUsage(player);
            return;
        }
        var rarity = plugin.getRarities().findAny(rarityId);
        if (rarity == null) {
            showUsage(player);
            return;
        }
        var pet = new Pet(petType, rarity);
        pet.setLevel(100);
        var inventory = Bukkit.createInventory(player, 27, pet.getName(player, plugin));
        inventory.setItem(13, pet.getItem(plugin).unwrap());
        inventories.add(inventory);
        player.openInventory(inventory);
    }

    private void showRecipe(Player player, String id) {
        var recipe = getRecipe(id);
        if (recipe == null) {
            showUsage(player);
            return;
        }
        var inventory = Bukkit.createInventory(player, 45, getTitle(player, id));
        for (var i = 0; i < 9; i++) {
            inventory.setItem(i + i / 3 * 6 + 12, new ItemStack(recipe.get(i)));
        }
        inventories.add(inventory);
        player.openInventory(inventory);
    }

    private Recipe getRecipe(String id) {
        var petType = plugin.getPets().findAny(id);
        if (petType != null) {
            return petType.recipe();
        }
        var experienceBooster = plugin.getExperienceBoosters().findAny(id);
        if (experienceBooster != null) {
            return experienceBooster.getRecipe();
        }
        var candy = plugin.getCandies().findAny(id);
        if (candy != null) {
            return candy.getRecipe();
        }
        var upgrade = plugin.getUpgrades().findAny(id);
        if (upgrade != null) {
            return upgrade.getRecipe();
        }
        return null;
    }

    private String getTitle(Player player, String id) {
        var petType = plugin.getPets().findAny(id);
        if (petType != null) {
            return new Pet(petType, plugin).getName(player, plugin);
        }
        var experienceBooster = plugin.getExperienceBoosters().findAny(id);
        if (experienceBooster != null) {
            return experienceBooster.getItem().getName();
        }
        var candy = plugin.getCandies().findAny(id);
        if (candy != null) {
            return candy.getItem().getName();
        }
        return plugin.getUpgrades().findAny(id).getItem().getName();
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        if (inventories.contains(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    @Override
    public List<String> suggest(Player sender, List<String> args) {
        var size = args.size();
        if (size == 1) {
            return SUBCOMMANDS;
        }
        var subcommand = args.get(0);
        if (size == 2 && subcommand.equals("recipe")) {
            return getRecipeIds();
        }
        if (size == 2 && subcommand.equals("pet")) {
            return new ArrayList<>(plugin.getPets().getIds());
        }
        if (size == 3 && subcommand.equals("pet")) {
            return new ArrayList<>(plugin.getRarities().getIds());
        }
        return null;
    }

    private List<String> getRecipeIds() {
        var ids = new ArrayList<>(plugin.getPets().getIds());
        ids.addAll(plugin.getExperienceBoosters().getIds());
        ids.addAll(plugin.getCandies().getIds());
        ids.addAll(plugin.getUpgrades().getIds());
        return ids;
    }

    private void showUsage(Player sender) {
        sendMessage(sender, plugin.getPetsInfoUsage());
    }

    private void sendMessage(Player player, String... messages) {
        player.sendMessage(Arrays.stream(messages)
                .map(FormattingContext.withPlugin(plugin)::format)
                .toArray(String[]::new));
    }
}
