package io.cyb3rh4ck.github.copilot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new TaskManager();
    }

    @Test
    void testAddTask() {
        taskManager.addTask("Comprar leche");
        assertEquals(1, taskManager.listTasks().size());
        assertEquals("Comprar leche", taskManager.listTasks().get(0).getDescription());
    }

    @Test
    void testMarkTaskAsDone() {
        taskManager.addTask("Estudiar Java");
        assertTrue(taskManager.markTaskAsDone("Estudiar Java"));
        assertTrue(taskManager.listTasks().get(0).isDone());
    }

    @Test
    void testMarkTaskAsDoneTaskNotFound() {
        taskManager.addTask("Hacer ejercicio");
        assertFalse(taskManager.markTaskAsDone("Leer un libro"));
    }

    @Test
    void testListTasks() {
        taskManager.addTask("Comprar leche");
        taskManager.addTask("Estudiar Java");
        assertEquals(2, taskManager.listTasks().size());
    }

    @Test
    void testRemoveTask() {
        taskManager.addTask("Hacer ejercicio");
        taskManager.removeTask("Hacer ejercicio");
        assertEquals(0, taskManager.listTasks().size());
    }
}
