package com.aregcraft.pets.command;

import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.pets.Pets;
import org.bukkit.entity.Player;

import java.util.List;

@RegisteredCommand("togglepets")
public class TogglePetsCommand implements CommandWrapper {
    private Pets plugin;

    @Override
    public boolean execute(Player sender, List<String> arguments) {
        plugin.getPetOwner(sender).togglePets();
        return true;
    }
}
