package com.aregcraft.pets;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Pets extends JavaPlugin {
    private static final String CONFIG_NAME = "config";
    private static final String PETS_NAME = "pets";
    private static final String PET_ITEMS_NAME = "pet_items";
    private static final String FILE_EXTENSION = ".json";
    private static final Gson GSON = new Gson();

    private final Map<String, Object> cache = new HashMap<>();

    @Override
    public void onEnable() {
        new PetOwnerListener(this);
        SimpleCommand.playerSender("pets", it -> new PetsMenu(it, this).open());
        SimpleCommand.playerSender("showpets", it -> PetOwner.getInstance(it).setShowPets(true));
        SimpleCommand.playerSender("hidepets", it -> PetOwner.getInstance(it).setShowPets(false));
        SimpleCommand.playerSender("clearpets", it -> PetOwner.getInstance(it).clearPets());
        SimpleCommand.anySender("reloadpets", it -> cache.clear());
        getPetItems().values().forEach(it -> it.registerRecipe(this));
    }

    public Vector getPetPosition() {
        return deserialize(CONFIG_NAME, Config.class).getPetPosition();
    }

    public Map<String, Pet> getPets() {
        return getMapById(deserialize(PETS_NAME, Pet[].class));
    }

    public Map<String, PetItem> getPetItems() {
        return getMapById(deserialize(PET_ITEMS_NAME, PetItem[].class));
    }

    public String getMenuTitle() {
        return deserialize(CONFIG_NAME, Config.class).getMenuTitle();
    }

    public int getMenuSize() {
        return deserialize(CONFIG_NAME, Config.class).getMenuSize();
    }

    private <T> T deserialize(String name, Class<T> type) {
        name += FILE_EXTENSION;
        if (cache.containsKey(name)) {
            return type.cast(cache.get(name));
        }
        try {
            var path = Files.createDirectories(getDataFolder().toPath()).resolve(name);
            if (Files.notExists(path)) {
                Files.copy(Objects.requireNonNull(getResource(name)), path);
            }
            try (var reader = Files.newBufferedReader(path)) {
                var obj = GSON.fromJson(reader, type);
                cache.put(name, obj);
                return obj;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends Identified> Map<String, T> getMapById(T[] objs) {
        return Arrays.stream(objs).collect(Collectors.toMap(Identified::getId, Function.identity()));
    }
}
