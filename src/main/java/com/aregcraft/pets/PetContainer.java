package com.aregcraft.pets;

import java.util.ArrayList;
import java.util.List;

public class PetContainer {
    private final List<Pet> pets = new ArrayList<>();
    private int selectedPet = -1;
    private boolean showPets = true;

    public List<Pet> getPets() {
        return pets;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
    }

    public void removePet(Pet pet) {
        var selectedPet = getSelectedPet();
        pets.remove(pet);
        selectPet(selectedPet);
    }

    public Pet getSelectedPet() {
        return selectedPet < 0 ? null : pets.get(selectedPet);
    }

    public void selectPet(Pet pet) {
        selectedPet = pets.indexOf(pet);
    }

    public boolean isShowPets() {
        return showPets;
    }

    public void togglePets() {
        showPets = !showPets;
    }

    public void clearPets() {
        pets.clear();
    }
}
