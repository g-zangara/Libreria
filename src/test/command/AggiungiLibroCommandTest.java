package test.command;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import command.AggiungiLibroCommand;
import controller.LibroController;
import model.Libro;
import model.StatoLettura;

/**
 * Test unitari per la classe AggiungiLibroCommand.
 * Verifica il corretto funzionamento delle operazioni di execute e undo.
 */
public class AggiungiLibroCommandTest {

    private TestLibroController controller;
    private AggiungiLibroCommand command;

    /**
     * Classe interna per simulare il controller dei libri.
     * Mantiene traccia delle operazioni effettuate per i test.
     */
    private static class TestLibroController extends LibroController {
        private Libro libroAggiunto = null;
        private Libro libroEliminato = null;

        public TestLibroController() {
            super(null); // Il parametro view non è usato nei test
        }

        @Override
        public boolean aggiungiLibroInterno(Libro libro) {
            this.libroAggiunto = libro;
            return true;
        }

        @Override
        public boolean eliminaLibroInterno(Libro libro) {
            this.libroEliminato = libro;
            return true;
        }

        public boolean isLibroAggiunto() {
            return libroAggiunto != null;
        }

        public boolean isLibroEliminato() {
            return libroEliminato != null;
        }

        public Libro getLibroAggiunto() {
            return libroAggiunto;
        }

        public Libro getLibroEliminato() {
            return libroEliminato;
        }
    }

    @BeforeEach
    public void setUp() {
        controller = new TestLibroController();
        command = new AggiungiLibroCommand(
                controller,
                "Nuovo Libro",
                "Nuovo Autore",
                "123-456-789",
                "Nuovo Genere",
                4,
                StatoLettura.DA_LEGGERE
        );
    }

    @Test
    public void testExecute() {
        // Verifica lo stato iniziale
        assertFalse(controller.isLibroAggiunto());

        // Esegui il comando
        command.execute();

        // Verifica che il libro sia stato aggiunto
        assertTrue(controller.isLibroAggiunto());

        // Verifica che i dati del libro aggiunto siano corretti
        Libro libroAggiunto = controller.getLibroAggiunto();
        assertNotNull(libroAggiunto);
        assertEquals("Nuovo Libro", libroAggiunto.getTitolo());
        assertEquals("Nuovo Autore", libroAggiunto.getAutore());
        assertEquals("123-456-789", libroAggiunto.getIsbn());
        assertEquals("Nuovo Genere", libroAggiunto.getGenere());
        assertEquals(4, libroAggiunto.getValutazione());
        assertEquals(StatoLettura.DA_LEGGERE, libroAggiunto.getStatoLettura());
    }

    @Test
    public void testUndo() {
        // Esegui il comando
        command.execute();

        // Verifica che il libro sia stato aggiunto
        assertTrue(controller.isLibroAggiunto());
        Libro libroAggiunto = controller.getLibroAggiunto();

        // Annulla il comando
        command.undo();

        // Verifica che il libro sia stato eliminato
        assertTrue(controller.isLibroEliminato());

        // Verifica che il libro eliminato sia lo stesso che era stato aggiunto
        assertSame(libroAggiunto, controller.getLibroEliminato());
    }

    @Test
    public void testGetDescription() {
        // Verifica che la descrizione contenga il titolo e l'autore del libro
        String description = command.getDescription();

        assertTrue(description.contains("Nuovo Libro"));
        assertTrue(description.contains("Nuovo Autore"));
        assertTrue(description.contains("Aggiunta libro"));
    }
}