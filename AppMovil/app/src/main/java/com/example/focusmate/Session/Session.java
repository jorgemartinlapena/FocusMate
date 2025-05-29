package com.example.focusmate.Session;

import com.google.gson.annotations.SerializedName;

public class Session {
    @SerializedName("user_id")
    private int user_id;

    @SerializedName("method_id")
    private int method_id;

    @SerializedName("session_timestamp")
    private String session_timestamp;

    @SerializedName("duration_minutes")
    private int duration_minutes;

    @SerializedName("task_type")
    private String task_type;

    @SerializedName("productivity_level")
    private int productivity_level;

    @SerializedName("average_pulse")
    private int average_pulse;

    @SerializedName("average_movement")
    private int average_movement;

    public Session(int user_id, int method_id, String session_timestamp, int duration_minutes, String task_type, int productivity_level) {
        this.user_id = user_id;
        this.method_id = method_id;
        this.session_timestamp = session_timestamp;
        this.duration_minutes = duration_minutes;
        this.task_type = task_type;
        this.productivity_level = productivity_level;
    }

    public Session(int user_id, int method_id, String session_timestamp, int duration_minutes, String task_type, int productivity_level, int average_pulse, int average_movement) {
        this.user_id = user_id;
        this.method_id = method_id;
        this.session_timestamp = session_timestamp;
        this.duration_minutes = duration_minutes;
        this.task_type = task_type;
        this.productivity_level = productivity_level;
        this.average_pulse = average_pulse;
        this.average_movement = average_movement;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMethod_id() {
        return method_id;
    }

    public void setMethod_id(int method_id) {
        this.method_id = method_id;
    }

    public int getDuration_minutes() {
        return duration_minutes;
    }

    public void setDuration_minutes(int duration_minutes) {
        this.duration_minutes = duration_minutes;
    }

    public String getSession_timestamp() {
        return session_timestamp;
    }

    public void setSession_timestamp(String session_timestamp) {
        this.session_timestamp = session_timestamp;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public int getProductivity_level() {
        return productivity_level;
    }

    public void setProductivity_level(int productivity_level) {
        this.productivity_level = productivity_level;
    }

    public int getAverage_pulse() {
        return average_pulse;
    }

    public void setAverage_pulse(int average_pulse) {
        this.average_pulse = average_pulse;
    }

    public int getAverage_movement() {
        return average_movement;
    }

    public void setAverage_movement(int average_movement) {
        this.average_movement = average_movement;
    }
}
