package com.aregcraft.pets.command;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.pets.Pet;
import com.aregcraft.pets.Pets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RegisteredCommand("petsgive")
public class PetsGiveCommand implements CommandWrapper {
    @InjectPlugin
    private Pets plugin;

    @Override
    public boolean execute(Player sender, List<String> args) {
        var inventory = switch (args.size()) {
            case 1 -> sender.getInventory();
            case 2 -> Optional.ofNullable(Bukkit.getPlayer(args.get(1)))
                    .map(Player::getInventory).orElse(null);
            default -> null;
        };
        if (inventory == null) {
            return false;
        }
        var id = args.get(0);
        var petType = plugin.getPets().findAny(id);
        if (petType != null) {
            inventory.addItem(new Pet(petType, plugin).getItem(plugin).unwrap());
            return true;
        }
        var experienceBooster = plugin.getExperienceBoosters().findAny(id);
        if (experienceBooster != null) {
            inventory.addItem(experienceBooster.getItem().unwrap());
            return true;
        }

        var candy = plugin.getCandies().findAny(id);
        if (candy != null) {
            inventory.addItem(candy.getItem().unwrap());
            return true;
        }
        var upgrade = plugin.getUpgrades().findAny(id);
        if (upgrade == null) {
            return false;
        }
        inventory.addItem(upgrade.getItem().unwrap());
        return true;
    }

    @Override
    public List<String> suggest(Player sender, List<String> args) {
        if (args.size() != 1) {
            return null;
        }
        var ids = new ArrayList<>(plugin.getPets().getIds());
        ids.addAll(plugin.getExperienceBoosters().getIds());
        ids.addAll(plugin.getCandies().getIds());
        ids.addAll(plugin.getUpgrades().getIds());
        return ids;
    }
}
