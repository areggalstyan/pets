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

    public void give(Player player, String name) {
        sound.play(player);
        player.sendMessage(getFormattingContext(name).format(message));
    }

    private FormattingContext getFormattingContext(String name) {
        return FormattingContext.builder()
                .placeholder("name", name)
                .build();
    }
}
