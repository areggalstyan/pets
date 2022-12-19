package com.aregcraft.pets;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.function.Consumer;

public class SimpleCommand<T extends CommandSender> implements CommandExecutor {
    private final Class<T> senderType;
    private final Consumer<T> action;

    public SimpleCommand(String name, Class<T> senderType, Consumer<T> action) {
        this.senderType = senderType;
        this.action = action;
        Objects.requireNonNull(Bukkit.getPluginCommand(name)).setExecutor(this);
    }

    public static void anySender(String name, Consumer<CommandSender> action) {
        new SimpleCommand<>(name, CommandSender.class, action);
    }

    public static void playerSender(String name, Consumer<Player> action) {
        new SimpleCommand<>(name, Player.class, action);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!senderType.isInstance(sender)) {
            return false;
        }
        action.accept(senderType.cast(sender));
        return true;
    }
}
