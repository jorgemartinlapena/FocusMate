package com.example.focusmate.Achievement;

import com.google.gson.annotations.SerializedName;

public class Achievement {
    private int id;

    @SerializedName("nombre")
    private String name;

    @SerializedName("descripcion")
    private String description;

    private int days;

    @SerializedName("minutos")
    private int minutes;

    @SerializedName("tipo")
    private String type;


    public Achievement(int id, String name, int days, String description, int minutes, String type) {
        this.id = id;
        this.name = name;
        this.days = days;
        this.description = description;
        this.minutes = minutes;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
