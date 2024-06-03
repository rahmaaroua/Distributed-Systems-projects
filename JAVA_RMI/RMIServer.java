package JAVA_RMI;

import java.rmi.Naming;

public class RMIServer {
    public static void main(String[] args) {
        try {
            TaskService taskService = new TaskServiceImpl();
            Naming.rebind("TaskService", taskService);
            System.out.println("TaskService bound in registry");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

