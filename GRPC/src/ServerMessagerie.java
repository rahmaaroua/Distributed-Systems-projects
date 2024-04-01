import java.io.IOException;

public class ServerMessagerie {

    public static void main(String[] args) throws IOException {
        // Initialisation du serveur gRPC
        Server server = ServerBuilder.forPort(50051).addService(new MessagerieServiceImpl()).build();

        // Démarrage du serveur
        server.start();
        System.out.println("Serveur de messagerie en cours d'exécution...");

        // Arrêt du serveur au moment opportun (gestion des interruptions)
        server.awaitTermination();
    }
}
