package com.aregcraft.pets.command;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.pets.Pets;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
        var language = plugin.getLanguages().findAny(args.get(0));
        if (language == null) {
            return false;
        }
        plugin.setLanguage(language);
        return true;
    }

    @Override
    public List<String> suggest(Player sender, List<String> args) {
        if (args.size() == 1) {
            return new ArrayList<>(plugin.getLanguages().getIds());
        }
        return null;
    }
}
