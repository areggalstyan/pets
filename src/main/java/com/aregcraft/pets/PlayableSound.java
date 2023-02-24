package com.aregcraft.pets;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayableSound {
    private final Sound type;
    private final float volume;
    private final float pitch;

    public PlayableSound(Sound type, float volume, float pitch) {
        this.type = type;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void play(Player player) {
        player.playSound(player, type, volume, pitch);
    }
}
