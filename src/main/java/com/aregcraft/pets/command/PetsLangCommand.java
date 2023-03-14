package com.aregcraft.pets.command;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.pets.Pets;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@RegisteredCommand("petslang")
public class PetsLangCommand implements CommandWrapper {
    @InjectPlugin
    private Pets plugin;

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (args.size() != 1) {
            return false;
        }
        var locale = args.get(0);
        if (locale.equals("DEFAULT")) {
            plugin.setLocale("");
            return true;
        }
        plugin.setLocale(locale);
        return true;
    }

    @Override
    public List<String> suggest(Player sender, List<String> args) {
        return Collections.singletonList("DEFAULT");
    }
}
