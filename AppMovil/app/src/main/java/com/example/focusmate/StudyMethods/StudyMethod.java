package com.example.focusmate.StudyMethods;

public class StudyMethod {
    private int id;
    private String name;
    private int repetitions;
    private int studyTime;
    private int restTime;
    private int finalRestTime;
    private String description;
    private int totalStudyTime;

    public StudyMethod(int id, String name, int repetitions, int studyTime, int restTime,
                       int finalRestTime, String description, int totalStudyTime) {
        this.id = id;
        this.name = name;
        this.repetitions = repetitions;
        this.studyTime = studyTime;
        this.restTime = restTime;
        this.finalRestTime = finalRestTime;
        this.description = description;
        this.totalStudyTime = totalStudyTime;
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

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(int studyTime) {
        this.studyTime = studyTime;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public int getFinalRestTime() {
        return finalRestTime;
    }

    public void setFinalRestTime(int finalRestTime) {
        this.finalRestTime = finalRestTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalStudyTime() {
        return totalStudyTime;
    }

    public void setTotalStudyTime(int totalStudyTime) {
        this.totalStudyTime = totalStudyTime;
    }
}
