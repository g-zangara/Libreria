package command;

import controller.LibroController;
import model.Libro;
import model.StatoLettura;

/**
 * Comando per la modifica di un libro esistente nella libreria.
 * Implementa l'interfaccia Command per supportare le operazioni di undo/redo.
 */
public class ModificaLibroCommand implements Command {

    private final LibroController controller;
    private final Libro libroOriginale;
    private final String nuovoTitolo;
    private final String nuovoAutore;
    private final String nuovoIsbn;
    private final String nuovoGenere;
    private final int nuovaValutazione;
    private final StatoLettura nuovoStatoLettura;
    private Libro libroModificato; // Riferimento al libro modificato per l'operazione di undo

    /**
     * Costruttore che inizializza il comando con i dati del libro da modificare.
     *
     * @param controller Controller della libreria
     * @param libroOriginale Libro originale da modificare
     * @param nuovoTitolo Nuovo titolo
     * @param nuovoAutore Nuovo autore
     * @param nuovoIsbn Nuovo ISBN
     * @param nuovoGenere Nuovo genere
     * @param nuovaValutazione Nuova valutazione
     * @param nuovoStatoLettura Nuovo stato di lettura
     */
    public ModificaLibroCommand(LibroController controller, Libro libroOriginale,
                                String nuovoTitolo, String nuovoAutore,
                                String nuovoIsbn, String nuovoGenere,
                                int nuovaValutazione, StatoLettura nuovoStatoLettura) {
        this.controller = controller;
        this.libroOriginale = libroOriginale;
        this.nuovoTitolo = nuovoTitolo;
        this.nuovoAutore = nuovoAutore;
        this.nuovoIsbn = nuovoIsbn;
        this.nuovoGenere = nuovoGenere;
        this.nuovaValutazione = nuovaValutazione;
        this.nuovoStatoLettura = nuovoStatoLettura;
    }

    /**
     * Esegue il comando modificando il libro nella libreria.
     * Crea una copia del libro originale per supportare l'operazione di undo.
     */
    @Override
    public void execute() {
        libroModificato = new Libro(nuovoTitolo, nuovoAutore, nuovoIsbn, nuovoGenere,
                nuovaValutazione, nuovoStatoLettura);

        controller.modificaLibroInterno(libroOriginale, libroModificato);
    }

    /**
     * Annulla il comando ripristinando i dati originali del libro.
     */
    @Override
    public void undo() {
        if (libroModificato != null) {
            controller.modificaLibroInterno(libroModificato, libroOriginale);
        }
    }

    /**
     * Restituisce una descrizione del comando.
     *
     * @return Descrizione testuale del comando
     */
    @Override
    public String getDescription() {
        return "Modifica libro: " + libroOriginale.getTitolo() + " -> " + nuovoTitolo;
    }

}
