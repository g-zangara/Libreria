import view.LibroView;

/**
 * Classe principale dell'applicazione di gestione della libreria personale.
 * Avvia l'applicazione creando l'interfaccia utente.
 */
public class Libreria {

    /**
     * Metodo principale che avvia l'applicazione.
     *
     * @param args Argomenti da linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
        // Utilizza SwingUtilities.invokeLater per garantire che l'interfaccia utente
        // venga creata nel thread di eventi Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Crea e visualizza l'interfaccia utente
            LibroView view = new LibroView();
            view.setVisible(true);
        });
    }
}
