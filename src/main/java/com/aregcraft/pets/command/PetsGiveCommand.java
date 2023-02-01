package com.aregcraft.pets.command;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.pets.Pet;
import com.aregcraft.pets.Pets;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RegisteredCommand("petsgive")
public class PetsGiveCommand implements CommandWrapper {
    @InjectPlugin
    private Pets plugin;

    @Override
    public boolean execute(Player sender, List<String> args) {
        if (args.size() != 1) {
            return false;
        }
        var inventory = sender.getInventory();
        var id = args.get(0);
        var petType = plugin.getPetType(id);
        if (petType != null) {
            inventory.addItem(new Pet(petType).getItem(plugin).unwrap());
            return true;
        }
        var experienceBooster = plugin.getExperienceBooster(id);
        if (experienceBooster != null) {
            inventory.addItem(experienceBooster.getItem().unwrap());
            return true;
        }
        var candy = plugin.getCandy(id);
        if (candy == null) {
            return false;
        }
        inventory.addItem(candy.getItem().unwrap());
        return true;
    }

    @Override
    public List<String> suggest(Player sender, List<String> args) {
        if (args.size() != 1) {
            return null;
        }
        var ids = new ArrayList<>(plugin.getPetTypeIds());
        ids.addAll(plugin.getExperienceBoosterIds());
        ids.addAll(plugin.getCandyIds());
        return ids;
    }
}
