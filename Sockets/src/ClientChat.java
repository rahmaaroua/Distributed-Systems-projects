import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientChat {

    public static void main(String[] args) throws IOException {
        // Création du Socket
        Socket socket = new Socket("localhost", 50051);

        // Création d'un BufferedReader et d'un PrintWriter pour la communication
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        // Envoi d'un message
        writer.println("EnvoyerMessage salon1 Hello World!");

        // Réception de la réponse
        String reponse = reader.readLine();

        // ... Traitement de la réponse ...

        // Récupération des messages
        writer.println("RecupererMessages salon1");

        // Réception de la réponse
        reponse = reader.readLine();

        // ... Traitement de la réponse ...

        // Fermeture du socket
        socket.close();
    }
}
