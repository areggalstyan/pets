package com.aregcraft.pets;

import java.util.ArrayList;
import java.util.List;

public class PetContainer {
    private final List<Pet> pets;
    private Pet selectedPet;
    private boolean showPets;

    public PetContainer() {
        this(new ArrayList<>(), null, true);
    }

    public PetContainer(List<Pet> pets, Pet selectedPet, boolean showPets) {
        this.pets = pets;
        this.selectedPet = selectedPet;
        this.showPets = showPets;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void addPet(Pet pet) {
        if (pet != null) {
            pets.add(pet);
        }
    }

    public void removePet(Pet pet) {
        pets.remove(pet);
    }

    public void clearPets() {
        pets.clear();
    }

    public Pet getSelectedPet() {
        return selectedPet;
    }

    public void setSelectedPet(Pet selectedPet) {
        this.selectedPet = selectedPet;
    }

    public boolean isShowPets() {
        return showPets;
    }

    public void setShowPets(boolean showPets) {
        this.showPets = showPets;
    }
}
