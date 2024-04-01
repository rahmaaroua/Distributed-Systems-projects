import java.rmi.Naming;

public class Server {

    public static void main(String[] args) throws Exception {
        ListeTachesImpl listeTaches = new ListeTachesImpl();
        Naming.rebind("ListeTachesImpl", listeTaches );
        System.out.println("Server ready...");
    }

}