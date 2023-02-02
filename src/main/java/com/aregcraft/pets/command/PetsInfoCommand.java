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
        if (args.size() == 0) {
            showUsage(sender);
            return true;
        }
        var subcommand = args.get(0);
        if (args.size() == 1 && subcommand.equals("pets")) {
            listPets(sender);
            return true;
        }
        if (args.size() != 2) {
            showUsage(sender);
            return true;
        }
        switch (subcommand) {
            case "pet" -> showPet(sender, args.get(1));
            case "recipe" -> showRecipe(sender, args.get(1));
            default -> showUsage(sender);
        }
        return true;
    }

    private void listPets(Player player) {
        sendMessage(player, "%aqua%" + String.join("%gray%, %aqua%", plugin.getPetTypeIds()));
    }

    private void showPet(Player player, String id) {
        var petType = plugin.getPetType(id);
        if (petType == null) {
            showUsage(player);
            return;
        }
        var pet = new Pet(petType);
        pet.setLevel(100);
        var inventory = Bukkit.createInventory(player, 27, pet.getName(player));
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
        var petType = plugin.getPetType(id);
        if (petType != null) {
            return petType.recipe();
        }
        var experienceBooster = plugin.getExperienceBooster(id);
        if (experienceBooster != null) {
            return experienceBooster.getRecipe();
        }
        var candy = plugin.getCandy(id);
        if (candy != null) {
            return candy.getRecipe();
        }
        return null;
    }

    private String getTitle(Player player, String id) {
        var petType = plugin.getPetType(id);
        if (petType != null) {
            return new Pet(petType).getName(player);
        }
        var experienceBooster = plugin.getExperienceBooster(id);
        if (experienceBooster != null) {
            return experienceBooster.getItem().getName();
        }
        return plugin.getCandy(id).getItem().getName();
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        event.setCancelled(inventories.contains(event.getInventory()));
    }

    @Override
    public List<String> suggest(Player sender, List<String> args) {
        if (args.size() == 1) {
            return SUBCOMMANDS;
        }
        if (args.size() != 2) {
            return null;
        }
        return switch (args.get(0)) {
            case "pet" -> plugin.getPetTypeIds();
            case "recipe" -> getRecipeIds();
            default -> null;
        };
    }

    private List<String> getRecipeIds() {
        var ids = new ArrayList<>(plugin.getPetTypeIds());
        ids.addAll(plugin.getExperienceBoosterIds());
        ids.addAll(plugin.getCandyIds());
        return ids;
    }

    private void showUsage(Player sender) {
        sendMessage(sender, """
                %red%/petsinfo pets %gray%- %yellow%List all pets
                %red%/petsinfo pet <id> %gray%- %yellow%Show info about a pet
                %red%/petsinfo recipe <id> %gray%- %yellow%Show a crafting recipe""");
    }

    private void sendMessage(Player player, String... messages) {
        player.sendMessage(Arrays.stream(messages).map(FormattingContext.DEFAULT::format).toArray(String[]::new));
    }
}
