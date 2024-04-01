public class ClientMessagerie {

    public static void main(String[] args) throws Exception {
        // Création du canal gRPC
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();

        // Création d'un stub pour le service de messagerie
        MessagerieGrpc.MessagerieBlockingStub stub = MessagerieGrpc.newBlockingStub(channel);

        // Envoi d'un message
        EnvoyerMessage requestEnvoi = EnvoyerMessage.newBuilder().setMessage("Hello World!").setDestinataire("user1").build();
        MessageEnvoye responseEnvoi = stub.envoyerMessage(requestEnvoi);

        // Récupération des messages
        RecupererMessages requestRecuperation = RecupererMessages.newBuilder().setUtilisateurId("user2").build();
        ListeMessages responseRecuperation = stub.recupererMessages(requestRecuperation);

        // Affichage des résultats
        System.out.println("Envoi réussi : " + responseEnvoi.getSucces());
        for (Message message : responseRecuperation.getMessageList()) {
            System.out.println("Message reçu : " + message.getContenu());
            System.out.println("Expéditeur : " + message.getExpediteur());
            System.out.println("Date d'envoi : " + message.getDateEnvoi().toString());
        }

        // Fermeture du canal
        channel.shutdownNow();
    }
}
