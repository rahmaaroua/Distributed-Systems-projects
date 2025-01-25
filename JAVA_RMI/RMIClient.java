package JAVA_RMI;

import TaskManagerInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            TaskManagerInterface stub = (TaskManagerInterface) registry.lookup("TaskManager");
            System.out.println("Client ready");

            Scanner scanner = new Scanner(System.in);
            String choice;
            do {
                System.out.println("1. Add task");
                System.out.println("2. Remove task");
                System.out.println("3. Show tasks");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.print("Enter task: ");
                        String task = scanner.nextLine();
                        System.out.println(stub.addTask(task));
                        break;
                    case "2":
                        for (int i = 0; i < stub.getTasks().size(); i++) {
                            System.out.println(i+1+"- "+stub.getTasks().get(i));
                        }
                        System.out.print("Enter task number to remove: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println(stub.removeTask(id));
                        break;
                    case "3":
                        System.out.println("Tasks:");
                        for (String t : stub.getTasks()) {
                            System.out.println(t);
                        }
                        break;
                    case "4":
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (!choice.equals("4"));

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
        }
    }
}




