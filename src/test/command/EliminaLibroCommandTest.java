package test.command;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import command.EliminaLibroCommand;
import controller.LibroController;
import model.Libro;
import model.StatoLettura;

/**
 * Test unitari per la classe EliminaLibroCommand.
 * Verifica il corretto funzionamento delle operazioni di execute e undo.
 */
public class EliminaLibroCommandTest {

    private TestLibroController controller;
    private Libro libro;
    private EliminaLibroCommand command;

    /**
     * Classe interna per simulare il controller dei libri.
     * Mantiene traccia delle operazioni effettuate per i test.
     */
    private static class TestLibroController extends LibroController {
        private Libro libroEliminato = null;
        private boolean libroAggiunto = false;

        public TestLibroController() {
            super(null); // Il parametro view non è usato nei test
        }

        @Override
        public boolean eliminaLibroInterno(Libro libro) {
            this.libroEliminato = libro;
            this.libroAggiunto = false;
            return true;
        }

        @Override
        public boolean aggiungiLibroInterno(Libro libro) {
            this.libroAggiunto = true;
            return true;
        }

        public boolean isLibroEliminato() {
            return libroEliminato != null;
        }

        public boolean isLibroAggiunto() {
            return libroAggiunto;
        }

        public Libro getLibroEliminato() {
            return libroEliminato;
        }
    }

    @BeforeEach
    public void setUp() {
        controller = new TestLibroController();
        libro = new Libro("Test Libro", "Test Autore", "123-23", "Test Genere", 4, StatoLettura.LETTO);
        command = new EliminaLibroCommand(controller, libro);
    }

    @Test
    public void testExecute() {
        // Verifica lo stato iniziale
        assertFalse(controller.isLibroEliminato());

        // Esegui il comando
        command.execute();

        // Verifica che il libro sia stato eliminato
        assertTrue(controller.isLibroEliminato());
        assertSame(libro, controller.getLibroEliminato());
    }

    @Test
    public void testUndo() {
        // Esegui il comando
        command.execute();

        // Verifica che il libro sia stato eliminato
        assertTrue(controller.isLibroEliminato());

        // Annulla il comando
        command.undo();

        // Verifica che il libro sia stato aggiunto nuovamente
        assertTrue(controller.isLibroAggiunto());
    }

    @Test
    public void testGetDescription() {
        // Verifica che la descrizione contenga il titolo e l'autore del libro
        String description = command.getDescription();

        assertTrue(description.contains("Test Libro"));
        assertTrue(description.contains("Test Autore"));
        assertTrue(description.contains("Eliminazione libro"));
    }
}