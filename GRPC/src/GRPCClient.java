package org.example;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.TimeUnit;


public class Client {
    private final ManagedChannel channel;
    private final  JMessengerServiceGrpc.JMessengerServiceBlockingStub blockingStub;
    private final JMessengerServiceGrpc.JMessengerServiceStub asyncStub;

    public Client(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build());
    }

    Client(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = JMessengerServiceGrpc.newBlockingStub(channel);
        asyncStub = JMessengerServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void send(String username, String message, String receiver) {
            Message request = Message.newBuilder()
                    .setSender(username)
                    .setText(message)
                    .setReceiver(receiver)
                    .build();
            Confirmation response;
            try {
                response = blockingStub.sendMessage(request);
                if (response.getSuccess()) System.out.println("Message sent successfully");
                else System.out.println("Message failed");
            } catch (StatusRuntimeException e) {
                System.err.println("RPC failed: " + e.getStatus());
            }
    }
    public void receive(String username,String sender){
        Inbox request = Inbox.newBuilder()
                .setSender(sender)
                .setReceiver(username)
                .build();
        asyncStub.receiveMessage(request, new StreamObserver<Message>() {   //handle asynchronous communication
            @Override
            public void onNext(Message message) {
                System.out.println(message.getText());
            }
            @Override
            public void onError(Throwable t) {
                System.err.println("Error occurred: " + t.getMessage());
            }
            @Override
            public void onCompleted() {
                System.out.println("Finished receiving messages");
            }
        });
    }
    public static void main(String[] args) throws Exception {
        Client client = new Client("localhost", 50051);
        try {
            client.send("Alex","hi","Jack");
            System.out.println("waiting for messages");
            client.receive("Jack","sabrine");
        } finally {
            client.shutdown();
        }
    }
}
