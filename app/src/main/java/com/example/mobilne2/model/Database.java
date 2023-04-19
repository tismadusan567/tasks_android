package com.example.mobilne2.model;

import java.util.SortedSet;
import java.util.TreeSet;

public class Database {

    private static Database instance;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {

    }

    private SortedSet<Task> allTasks = new TreeSet<>();

    public void addTask(Task task) {
        allTasks.add(task);
    }

    public SortedSet<Task> getAllTasks() {
        return allTasks;
    }
}
