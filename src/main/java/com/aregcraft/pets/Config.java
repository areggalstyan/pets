package com.aregcraft.pets;

import org.bukkit.util.Vector;

public class Config {
    private Vector petPosition;
    private String menuTitle;
    private int menuSize;

    public Vector getPetPosition() {
        return petPosition;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public int getMenuSize() {
        return menuSize;
    }
}
