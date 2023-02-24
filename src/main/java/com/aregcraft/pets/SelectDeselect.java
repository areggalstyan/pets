package com.aregcraft.pets;

import com.aregcraft.delta.api.json.annotation.JsonConfiguration;

@JsonConfiguration("select_deselect")
public class SelectDeselect {
    private Feedback select;
    private Feedback deselect;

    public Feedback getSelect() {
        return select;
    }

    public Feedback getDeselect() {
        return deselect;
    }
}
