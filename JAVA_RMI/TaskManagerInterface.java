import java.rmi.Remote;
import java.util.List;

public interface TaskManagerInterface extends Remote {

    String addTask(String task) throws Exception;
    String removeTask(Integer id) throws Exception;
    List<String> getTasks() throws Exception;
}
