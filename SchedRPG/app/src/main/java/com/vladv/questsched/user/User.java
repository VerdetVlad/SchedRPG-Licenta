package com.vladv.questsched.user;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class User {
    private static String fullName, email;
    private static ArrayList <UserTask> tasks;

    public User() {
    }

    public User(String fullName, String email) {
        User.fullName = fullName;
        User.email = email;
        tasks = new ArrayList<>();
    }

    public User(String fullName, String email, ArrayList<UserTask> tasks) {
        User.fullName = fullName;
        User.email = email;
        User.tasks = tasks;
    }

    public void addTask(UserTask task)
    {
        tasks.add(task);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    public void removeTask(UserTask task)
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
