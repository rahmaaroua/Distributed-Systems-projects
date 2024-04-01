import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurChat {

    public static void main(String[] args) throws IOException {
        // Création duServerSocket
        ServerSocket serverSocket = new ServerSocket(50051);

        while (true) {
            // Attente d'une nouvelle connexion
            Socket clientSocket = serverSocket.accept();

            // Création d'un thread pour gérer le client
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }
}