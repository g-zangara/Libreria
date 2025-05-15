package command;

import controller.LibroController;
import model.Libro;

/**
 * Comando per l'eliminazione di un libro dalla libreria.
 * Implementa l'interfaccia Command per supportare le operazioni di undo/redo.
 */
public class EliminaLibroCommand implements Command {

    private final LibroController controller;
    private final Libro libro;

    /**
     * Costruttore che inizializza il comando con il libro da eliminare.
     *
     * @param controller Controller della libreria
     * @param libro Libro da eliminare
     */
    public EliminaLibroCommand(LibroController controller, Libro libro) {
        this.controller = controller;
        this.libro = libro;
    }

    /**
     * Esegue il comando eliminando il libro dalla libreria.
     */
    @Override
    public void execute() {
        controller.eliminaLibroInterno(libro);
    }

    /**
     * Annulla il comando aggiungendo nuovamente il libro alla libreria.
     */
    @Override
    public void undo() {
        controller.aggiungiLibroInterno(libro);
    }

    /**
     * Restituisce una descrizione del comando.
     *
     * @return Descrizione testuale del comando
     */
    @Override
    public String getDescription() {
        return "Eliminazione libro: " + libro.getTitolo() + " (" + libro.getAutore() + ")";
    }

}