package command;

import controller.LibroController;
import model.Libro;
import model.StatoLettura;

/**
 * Comando per l'aggiunta di un nuovo libro alla libreria.
 * Implementa l'interfaccia Command per supportare le operazioni di undo/redo.
 */
public class AggiungiLibroCommand implements Command {

    private final LibroController controller;
    private final String titolo;
    private final String autore;
    private final String isbn;
    private final String genere;
    private final int valutazione;
    private final StatoLettura statoLettura;
    private Libro libroAggiunto; // Riferimento al libro aggiunto per l'operazione di undo

    /**
     * Costruttore che inizializza il comando con i dati del libro da aggiungere.
     *
     * @param controller Controller della libreria
     * @param titolo Titolo del libro
     * @param autore Autore del libro
     * @param isbn ISBN del libro
     * @param genere Genere del libro
     * @param valutazione Valutazione del libro
     * @param statoLettura Stato di lettura del libro
     */
    public AggiungiLibroCommand(LibroController controller, String titolo, String autore,
                                String isbn, String genere, int valutazione,
                                StatoLettura statoLettura) {
        this.controller = controller;
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn;
        this.genere = genere;
        this.valutazione = valutazione;
        this.statoLettura = statoLettura;
    }

    /**
     * Esegue il comando aggiungendo il libro alla libreria.
     * Memorizza un riferimento al libro aggiunto per supportare l'operazione di undo.
     */
    @Override
    public void execute() {
        // Crea un nuovo libro con i dati forniti
        libroAggiunto = new Libro(titolo, autore, isbn, genere, valutazione, statoLettura);

        // Aggiungi il libro tramite il controller
        controller.aggiungiLibroInterno(libroAggiunto);
    }

    /**
     * Annulla il comando eliminando il libro aggiunto.
     */
    @Override
    public void undo() {
        if (libroAggiunto != null) {
            controller.eliminaLibroInterno(libroAggiunto);
        }
    }

    /**
     * Restituisce una descrizione del comando.
     *
     * @return Descrizione testuale del comando
     */
    @Override
    public String getDescription() {
        return "Aggiunta libro: " + titolo + " (" + autore + ")";
    }

}
