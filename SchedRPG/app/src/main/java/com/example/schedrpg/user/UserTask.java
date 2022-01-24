package com.example.schedrpg.user;

import android.widget.ArrayAdapter;

import java.util.Objects;

public class UserTask {
    // 0 - STR, 1 - DEX, 2 - CON, 3 - WIS, 4 - INT, 5 - CHA
    private int type;

    //minutes
    //private int duration;

    // 0 - very easy, 1 - easy, 2 - medium, 3 - hard, 4 - very hard
    private int difficulty;

    private String name;

    private String description;

    public UserTask() {
    }

    public UserTask(int type, /*int duration,*/ int difficulty, String name, String description) {
        this.type = type;
        //this.duration = duration;
        this.difficulty = difficulty;
        this.name = name;
        this.description = description;
    }

    public UserTask(int type, int difficulty, String name) {
        this.type = type;
        this.difficulty = difficulty;
        this.name = name;
        this.description = "";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTask task = (UserTask) o;
        return type == task.type &&
                difficulty == task.difficulty &&
                name.equals(task.name) &&
                description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, difficulty, name, description);
    }

    @Override
    public String toString() {
        return "Task{" +
                "type=" + type +
                ", difficulty=" + difficulty +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String dificultyStringValue()
    {
        String[] difficultyStrings = new String[]{"very easy", "easy", "medium", "hard", "very hard"};

        return difficultyStrings[difficulty];

    }

    public String typeStringValue() {
        String[] stats = new String[]{"Strength" ,
                "Dexterity" ,
                "Constitution" ,
                "Wisdom" ,
                "Intelligence" ,
                "Charisma"};

        return stats[type];
    }
}
