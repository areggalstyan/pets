package com.aregcraft.pets;

import java.util.Collection;

public interface Identifiable {
    static <T extends Identifiable> T find(Collection<T> collection, String id) {
        return collection.stream().filter(it -> it.getId().equals(id)).findAny().orElse(null);
    }

    String getId();
}
