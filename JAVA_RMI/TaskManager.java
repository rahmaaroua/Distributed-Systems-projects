package JAVA_RMI;

import java.util.ArrayList;

public class TaskManager implements TaskManagerInterface{


    private final ArrayList<String> tasks=new ArrayList<>();

    @Override
    public String addTask(String task){
        tasks.add(task);
        return "Task added successfully";
    }

    @Override
    public String removeTask(Integer id) {
     if (tasks.size()-id>0) {
         tasks.remove(tasks.get(id));
         return "Task removed successfully";}
     else {
         return "Task not found";
     }
    }
    @Override
    public ArrayList<String> getTasks() {
        return tasks;

    }
}
