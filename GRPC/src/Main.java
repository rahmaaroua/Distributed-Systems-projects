import java.io.IOException;

public class ServerMessage {

    public static void main(String[] args) throws IOException {
        // Initialisation du serveur gRPC
        Object ServerBuilder;
        Server server = ServerBuilder.forPort(50051).addService(new MessagerieServiceImpl()).build();

        // Démarrage du serveur
        server.start();
        System.out.println("Serveur de messagerie en cours d'exécution...");

        // Arrêt du serveur au moment opportun (gestion des interruptions)
        server.awaitTermination();
    }
}