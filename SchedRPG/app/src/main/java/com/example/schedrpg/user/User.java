package com.example.schedrpg.user;

import java.util.ArrayList;

public class User {
    private String fullName, email;
    private ArrayList <UserTask> tasks;

    public User() {
    }

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        tasks = new ArrayList<>();
;
    }

    public User(String fullName, String email, ArrayList<UserTask> tasks) {
        this.fullName = fullName;
        this.email = email;
        this.tasks = tasks;
    }

    public void addTask(UserTask task)
    {
        tasks.add(task);
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    public void remove(UserTask task)
    {
        tasks.remove(task);
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<UserTask> getTasks() {
        return tasks;
    }
}
