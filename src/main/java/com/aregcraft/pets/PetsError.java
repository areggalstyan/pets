package com.aregcraft.pets;

import com.aregcraft.delta.api.log.Error;

public enum PetsError implements Error {
    CORRUPTED_DATA("A data corruption was detected while trying to register player %s!");

    private final String message;

    PetsError(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
