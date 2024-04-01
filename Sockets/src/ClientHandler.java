import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.sun.org.apache.xml.internal.serializer.utils.Utils.messages;

class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Lecture de la requête du client
                String requete = reader.readLine();

                // Traitement de la requête
                if (requete.startsWith("EnvoyerMessage")) {
                    // Envoi d'un message
                    String[] messageParts = requete.split(" ");
                    String salon = messageParts[1];
                    String message = messageParts[2];

                    // ... Traitement de l'envoi du message ...

                    // Envoi de la réponse
                    writer.println("MessageEnvoye true");
                } else if (requete.startsWith("RecupererMessages")) {
                    // Récupération des messages
                    String salon = requete.split(" ")[1];

                    // ... Traitement de la récupération des messages ...

                    // Envoi de la réponse
                    writer.println("ListeMessages " + messages);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}