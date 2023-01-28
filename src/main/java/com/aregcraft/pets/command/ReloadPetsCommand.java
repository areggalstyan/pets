package com.aregcraft.pets.command;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.pets.Pets;
import org.bukkit.command.CommandSender;

import java.util.List;

@RegisteredCommand("reloadpets")
public class ReloadPetsCommand implements CommandWrapper {
    @InjectPlugin
    private Pets plugin;

    @Override
    public boolean execute(CommandSender sender, List<String> arguments) {
        plugin.reload();
        return true;
    }
}
