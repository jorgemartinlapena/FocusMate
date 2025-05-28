package com.example.focusmate.UserAchievements;

import java.sql.Timestamp;

public class UserAchievement {
    private int id;
    private int user_id;
    private String achievement_id;
    private Timestamp unlocked_at;

    public UserAchievement(int id, int user_id, String achievement_id, Timestamp unlocked_at) {
        this.id = id;
        this.user_id = user_id;
        this.achievement_id = achievement_id;
        this.unlocked_at = unlocked_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAchievement_id() {
        return achievement_id;
    }

    public void setAchievement_id(String achievement_id) {
        this.achievement_id = achievement_id;
    }

    public Timestamp getUnlocked_at() {
        return unlocked_at;
    }

    public void setUnlocked_at(Timestamp unlocked_at) {
        this.unlocked_at = unlocked_at;
    }
}
