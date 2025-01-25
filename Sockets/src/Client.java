import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    static volatile boolean connected = true;
    private static final Logger logger = Logger.getLogger(Client.class.getName());


    static void sendMessage(Socket socket, String message) throws IOException {

    OutputStream outputStream = socket.getOutputStream();
    outputStream.write(message.getBytes());
}

static String getMessage(Socket socket) throws IOException {

      try {
          InputStream inputStream = socket.getInputStream();
          byte[] buffer = new byte[1024];
          int bytesRead = inputStream.read(buffer);
          return new String(buffer, 0, bytesRead);
      } catch (SocketException e) {
          logger.log(Level.SEVERE, "Server disconnected!");
          return null;
      }
      catch(IOException e) {
          logger.log(Level.SEVERE, "Error reading message: {0}", e.getMessage());
          return null;
      }
}

    public static void main(String[] args) {
        try {
            String hostname="localhost";
            int port=4104;
            Socket socket = new Socket(hostname, port);
            System.out.println("""
                    Welcome to JDiscord
                    If you want to send a message to the server, please enter the message in the console
                    """);
            Scanner scanner = new Scanner(System.in);

            // Thread for receiving messages
            new Thread(() -> {
                try {
                    while (true) {
                        String lastMessage = getMessage(socket);
                        if (lastMessage == null) {
                            connected = false;
                            break;
                        }
                        System.out.println(lastMessage);
                    }
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error in receiving thread: {0}", e.getMessage());
                }
            }).start();

            // Thread for sending messages
            new Thread(() -> {
                try {
                    while (true) {
                        if (!connected) {
                            socket.close();
                            break;
                        }
                        if (System.in.available() > 0) {
                            String message = scanner.nextLine();
                            sendMessage(socket, message);
                        }
                }} catch (IOException e) {
                    logger.log(Level.SEVERE, "Error in sending thread: {0}", e.getMessage());
                }
            }).start();
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Server is not available!");
        }
}
}
