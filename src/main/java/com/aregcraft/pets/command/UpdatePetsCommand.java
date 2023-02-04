package com.aregcraft.pets.command;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.command.CommandWrapper;
import com.aregcraft.delta.api.command.RegisteredCommand;
import com.aregcraft.pets.Pets;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RegisteredCommand("updatepets")
public class UpdatePetsCommand implements CommandWrapper {
    @InjectPlugin
    private Pets plugin;

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (args.size() != 0) {
            return false;
        }
        CompletableFuture.runAsync(plugin.getUpdater()::tryDownloadLatestVersion);
        return true;
    }
}
