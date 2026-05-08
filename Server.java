import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 1234;

    public static void main(String[] args) {
        System.out.println("Starting server on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                new GestoreClient(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class GestoreClient extends Thread {
        private final Socket socket;

        GestoreClient(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true)
            ) {
                // Wait for category name
                String categoria = in.readLine();
                if (categoria == null) return;

                // Wait for quantity
                String qtaStr = in.readLine();
                if (qtaStr == null) return;
                int qta = Integer.parseInt(qtaStr);

                // Get GestoreSito instance
                GestoreSito gestore = GestoreSito.getInstance();
                int indice = gestore.getIndice(categoria);
                if (indice == -1) {
                    out.println("Errore: Categoria non valida");
                    return;
                }

                // Try to buy
                boolean success = gestore.acquista(indice, qta);
                int disponibili = gestore.getDisponibili(indice);
                if (success) {
                    out.println("Conferma: Acquisto riuscito. Posti residui: " + disponibili);
                } else {
                    out.println("Errore: Posti insufficienti. Posti disponibili: " + disponibili);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected");
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity format");
            }
        }
    }
}