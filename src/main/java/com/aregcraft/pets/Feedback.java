package com.aregcraft.pets;

import com.aregcraft.delta.api.FormattingContext;
import org.bukkit.entity.Player;

public class Feedback {
    private final PlayableSound sound;
    private final String message;

    public Feedback(PlayableSound sound, String message) {
        this.sound = sound;
        this.message = message;
    }

    public void give(Player player, String name, Pets plugin) {
        sound.play(player);
        player.sendMessage(getFormattingContext(name, plugin).format(message));
    }

    private FormattingContext getFormattingContext(String name, Pets plugin) {
        return FormattingContext.builder()
                .plugin(plugin)
                .placeholder("name", name)
                .build();
    }
}
