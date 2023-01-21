package com.aregcraft.pets.perk;

import com.aregcraft.delta.api.PersistentDataWrapper;
import com.aregcraft.pets.Identifiable;
import com.aregcraft.pets.Pets;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class Perk implements Identifiable {
    private String id;
    private String name;
    private transient Pets plugin;

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    protected Pets getPlugin() {
        return plugin;
    }

    protected boolean isPlayerApplied(Entity player) {
        return PersistentDataWrapper.wrap(plugin, player).check(id, true);
    }

    protected void setPlayerApplied(Entity player) {
        PersistentDataWrapper.wrap(plugin, player).set(id, true);
    }

    protected void unsetPlayerApplied(Entity player) {
        PersistentDataWrapper.wrap(plugin, player).remove(id);
    }

    public abstract void apply(Player player);

    public abstract void unapply(Player player);
}
