package com.aregcraft.pets.meta;

import com.aregcraft.delta.meta.MetaDoclet;
import com.aregcraft.delta.meta.replacement.AbilityReplacement;
import com.aregcraft.delta.meta.replacement.BaseReplacement;
import com.aregcraft.delta.meta.replacement.JsonReplacement;

public class PetsMeta extends MetaDoclet {
    public PetsMeta() {
        super(new JsonReplacement("menu"),
                new JsonReplacement("position"),
                new JsonReplacement("pets"),
                new JsonReplacement("experience_boosters"),
                new JsonReplacement("candies"),
                new JsonReplacement("perks"),
                new AbilityReplacement("perks"),
                new BaseReplacement("bases"));
    }
}
