package GRPC.src;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import com.example.grpc.MessagingProto.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GRPCServer {
    private final int port;
    private final Server server;

    public GRPCServer(int port) throws IOException {
        this(ServerBuilder.forPort(port), port);
    }

    public GRPCServer(ServerBuilder<?> serverBuilder, int port) {
        this.port = port;
        server = serverBuilder.addService(new MessagingImpl()).build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                GRPCServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private static class MessagingImpl extends MessagingImplBase {
        private final Map<String, List<String>> messageStore = new HashMap<>();

        @Override
        public void sendMessage(MessageRequest request, StreamObserver<MessageResponse> responseObserver) {
            String receiver = request.getReceiver();
            String message = request.getMessage();
            messageStore.computeIfAbsent(receiver, k -> new ArrayList<>()).add(message);
            MessageResponse response = MessageResponse.newBuilder().setStatus("Message sent to " + receiver).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void getMessagesForUser(UserRequest request, StreamObserver<MessagesResponse> responseObserver) {
            String username = request.getUsername();
            List<String> messages = messageStore.getOrDefault(username, Collections.emptyList());
            MessagesResponse response = MessagesResponse.newBuilder().addAllMessages(messages).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051;
        GRPCServer server = new GRPCServer(port);
        server.start();
        server.awaitTermination();
    }
}

