package GRPC.src;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.example.grpc.MessagingProto.*;

import java.util.concurrent.TimeUnit;

public class GRPCClient {
    private final ManagedChannel channel;
    private final MessagingGrpc.MessagingBlockingStub blockingStub;

    public GRPCClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        blockingStub = MessagingGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void sendMessage(String sender, String receiver, String message) {
        MessageRequest request = MessageRequest.newBuilder()
                .setSender(sender)
                .setReceiver(receiver)
                .setMessage(message)
                .build();
        MessageResponse response = blockingStub.sendMessage(request);
        System.out.println("Response: " + response.getStatus());
    }

    public void getMessagesForUser(String username) {
        UserRequest request = UserRequest.newBuilder().setUsername(username).build();
        MessagesResponse response = blockingStub.getMessagesForUser(request);
        System.out.println("Messages for " + username + ": " + response.getMessagesList());
    }

    public static void main(String[] args) throws Exception {
        GRPCClient client = new GRPCClient("localhost", 50051);
        try {
            client.sendMessage("Alice", "Bob", "Hello Bob!");
            client.getMessagesForUser("Bob");
        } finally {
            client.shutdown();
        }
    }
}
