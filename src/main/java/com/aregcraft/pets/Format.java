package com.aregcraft.pets;

import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Format {
    private static final String KEY = "%%%s%%";
    private static final Pattern PATTERN = Pattern.compile("%(#[a-fA-F0-9]{6}|[a-zA-Z_]+)%");

    private final Map<String, Object> map;

    private Format(Map<String, Object> map) {
        this.map = map;
    }

    public static Format color() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String format(String string) {
        for (var key : map.keySet()) {
            string = string.replaceAll(KEY.formatted(key), String.valueOf(map.get(key)));
        }
        return PATTERN.matcher(string).replaceAll(it -> {
            try {
                return ChatColor.of(it.group(1)).toString();
            } catch (IllegalArgumentException e) {
                return it.group();
            }
        });
    }

    public static class Builder {
        private final Map<String, Object> map = new HashMap<>();

        private Builder() {
        }

        public Builder entry(String key, Object value) {
            map.put(key, value);
            return this;
        }

        public Format build() {
            return new Format(map);
        }
    }
}
