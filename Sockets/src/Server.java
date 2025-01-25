import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server
{

static ConcurrentHashMap<Socket, String> clients = new ConcurrentHashMap<>();
static CopyOnWriteArrayList<String> history = new CopyOnWriteArrayList<>();
private static final Logger logger = Logger.getLogger(Server.class.getName());


private static void sendMessage(Socket socket, String message) throws IOException {
    OutputStream outputStream = socket.getOutputStream();
    outputStream.write(message.getBytes());
}

private static String receiveMessage(Socket socket) throws IOException {
    try {
        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[2048];
        int readBytes = inputStream.read(buffer);
        return new String(buffer, 0, readBytes);
    } catch (SocketException e) {
        return null;
    }
}

 public static String generateUsername() {
        return "user" + UUID.randomUUID().toString().substring(0, 5);
    }

private static void handleClient(Socket socket) throws IOException {
    String historyString = String.join("", history);
    sendMessage(socket, historyString);
    Iterator<Map.Entry<Socket, String>> iterator = clients.entrySet().iterator();
    history.add(clients.get(socket) + " joined the chat!\n");
    logger.log(Level.INFO, clients.get(socket) + " connected!");

    while (iterator.hasNext()) {
        Map.Entry<Socket, String> entry = iterator.next();
        Socket client = entry.getKey();
        if (client.equals(socket))  sendMessage(client, clients.get(socket) + "(you) joined the chat!");
        else sendMessage(client, clients.get(socket) + " joined the chat!");
    }
    while (true)
    {
    String message=receiveMessage(socket);
    if (message == null) {
        logger.log(Level.INFO, clients.get(socket) + " disconnected!");
        history.add(clients.get(socket) + " left the chat!\n");
        for(Map.Entry<Socket, String> entry : clients.entrySet()){
            if (entry.getKey().equals(socket)) continue;
            Socket client = entry.getKey();
            sendMessage(client, clients.get(socket) + " left the chat!");
        }
        clients.remove(socket);
        break;
    }
    history.add(clients.get(socket) + ": " + message+"\n");
    for (Map.Entry<Socket, String> entry : clients.entrySet()){
        Socket client = entry.getKey();
        try {
            if (client.equals(socket)) 
                sendMessage(client, "you: " + message);
            else
                sendMessage(client, clients.get(socket) + ": " + message);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error sending message: {0}", e.getMessage());
        }
    }
    }

}

public static void main(String[] args) throws IOException
{
    ServerSocket serverSocket = new ServerSocket(4104);
    logger.log(Level.INFO, "Server started");
    while (true) {
        new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();
                clients.put(socket, "user" + generateUsername());
                handleClient(socket);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error handling client: ", e);
            }
        }).start();

}
}
}
