class MessagerieServiceImpl extends MessagerieGrpc.MessagerieImplBase {

    @Override
    public void envoyerMessage(EnvoyerMessage request, StreamObserver<MessageEnvoye> responseObserver) {
        // Traitement de l'envoi du message
        // ...

        // Envoi de la réponse
        responseObserver.onNext(MessageEnvoye.newBuilder().setSucces(true).build());
        responseObserver.onCompleted();
    }

    @Override
    public void recupererMessages(RecupererMessages request, StreamObserver<ListeMessages> responseObserver) {
        // Traitement de la récupération des messages
        // ...

        // Envoi de la réponse
        ListeMessages.Builder listeBuilder = ListeMessages.newBuilder();
        for (Message message : messagesRecus) {
            listeBuilder.addMessage(message);
        }
        responseObserver.onNext(listeBuilder.build());
        responseObserver.onCompleted();
    }
}