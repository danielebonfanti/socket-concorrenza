import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String[] CATEGORIE = {"CINEMA", "TEATRO", "PALLAVOLO", "CALCIO"};
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        System.out.println("Categorie disponibili:");
        for (String cat : CATEGORIE) {
            System.out.println("- " + cat);
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Inserisci la categoria scelta: ");
        String categoria = scanner.nextLine().trim().toUpperCase();

        System.out.print("Inserisci il numero di biglietti: ");
        String qtaStr = scanner.nextLine().trim();
        int qta;
        try {
            qta = Integer.parseInt(qtaStr);
            if (qta <= 0) {
                System.out.println("Errore: Il numero di biglietti deve essere positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Errore: Numero di biglietti non valido.");
            return;
        }

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send category
            out.println(categoria);
            // Send quantity
            out.println(qta);

            // Read response
            String response = in.readLine();
            if (response != null) {
                System.out.println("Risposta dal server: " + response);
            } else {
                System.out.println("Errore: Nessuna risposta dal server.");
            }

        } catch (IOException e) {
            System.out.println("Errore di connessione al server: " + e.getMessage());
        }
    }
}