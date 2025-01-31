import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class MessengerServer {
    private Server server;

    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new MessengerServiceImpl())
                .build()
                .start();
        System.out.println("Server started, listening on " + port);
    }
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final MessengerServer server = new MessengerServer();
        server.start();
        server.blockUntilShutdown();
    }
}
