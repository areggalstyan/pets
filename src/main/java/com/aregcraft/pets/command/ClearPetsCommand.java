package com.aregcraft.pets.command;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.pets.Pets;
import org.bukkit.entity.Player;

import java.util.List;

@RegisteredCommand("clearpets")
public class ClearPetsCommand implements CommandWrapper {
    @InjectPlugin
    private Pets plugin;

    @Override
    public boolean execute(Player sender, List<String> args) {
        if (args.size() != 0) {
            return false;
        }
        plugin.getPetOwner(sender).clearPets();
        return true;
    }
}
