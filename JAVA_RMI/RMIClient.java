package JAVA_RMI;

import java.rmi.Naming;
import java.util.List;

public class RMIClient {
    public static void main(String[] args) {
        try {
            TaskService taskService = (TaskService) Naming.lookup("//localhost/TaskService");
            taskService.addTask("Task 1");
            taskService.addTask("Task 2");
            List<String> tasks = taskService.getTasks();
            System.out.println("Tasks: " + tasks);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

