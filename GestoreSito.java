import java.util.concurrent.Semaphore;

public class GestoreSito {
    private static GestoreSito instance;
    
    private final String[] categorie = {"CINEMA", "TEATRO", "PALLAVOLO", "CALCIO"};
    private final Semaphore[] semafori;
    
    private GestoreSito() {
        semafori = new Semaphore[categorie.length];
        for (int i = 0; i < categorie.length; i++) {
            semafori[i] = new Semaphore(50); // 50 seats for each category
        }
    }
    
    /**
     * Returns the singleton instance of GestoreSito.
     * Thread-safe implementation.
     */
    public static synchronized GestoreSito getInstance() {
        if (instance == null) {
            instance = new GestoreSito();
        }
        return instance;
    }
    
    /**
     * Returns the index of the category or -1 if not found.
     * Search is case-insensitive.
     */
    public int getIndice(String nome) {
        for (int i = 0; i < categorie.length; i++) {
            if (categorie[i].equalsIgnoreCase(nome)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Attempts to buy tickets for the specified category.
     * Returns true if successful, false otherwise (non-blocking).
     */
    public boolean acquista(int indice, int qta) {
        if (indice < 0 || indice >= semafori.length) {
            return false;
        }
        return semafori[indice].tryAcquire(qta);
    }
    
    /**
     * Returns the number of available seats for the specified category.
     */
    public int getDisponibili(int indice) {
        if (indice < 0 || indice >= semafori.length) {
            return -1;
        }
        return semafori[indice].availablePermits();
    }
}
