package io.cyb3rh4ck.github.copilot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;




class TaskManager {

    private List<Task> tasks = new ArrayList<>();

    public void addTask(String description) {
        validateDescription(description);
        if (tasks.stream().anyMatch(task -> task.getDescription().equals(description))) {
            throw new IllegalArgumentException("Task with the same description already exists");
        }
        tasks.add(new Task(description));
    }

    public List<Task> listTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public boolean markTaskAsDone(String description) {
        return tasks.stream()
            .filter(task -> task.getDescription().equals(description))
            .findFirst()
            .map(task -> {
                task.setDone(true);
                return true;
            })
            .orElse(false);
    }

    public void removeTask(String description) {
        tasks.removeIf(task -> task.getDescription().equals(description));
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
    }
}

class Task {
    private String description;
    private boolean done;

    public Task(String description) {
        this.description = description;
        this.done = false;
    }

    public Task(String description, boolean done) {
        this.description = description;
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", done=" + done +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
