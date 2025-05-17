package test.command;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import command.ModificaLibroCommand;
import controller.LibroController;
import model.Libro;
import model.StatoLettura;

/**
 * Test unitari per la classe ModificaLibroCommand.
 * Verifica il corretto funzionamento delle operazioni di execute e undo.
 */
public class ModificaLibroCommandTest {

    private TestLibroController controller;
    private Libro libroOriginale;
    private ModificaLibroCommand command;

    /**
     * Classe controller personalizzata per i test.
     * Tiene traccia dei libri modificati per verificare il comportamento del comando.
     */
    private static class TestLibroController extends LibroController {
        private Libro vecchioLibro;
        private Libro nuovoLibro;
        private boolean modificaEseguita = false;

        public TestLibroController() {
            super(null); // Il parametro view non è usato nei test
        }

        @Override
        public boolean modificaLibroInterno(Libro vecchioLibro, Libro nuovoLibro) {
            this.vecchioLibro = vecchioLibro;
            this.nuovoLibro = nuovoLibro;
            this.modificaEseguita = true;
            return true;
        }

        public Libro getVecchioLibro() {
            return vecchioLibro;
        }

        public Libro getNuovoLibro() {
            return nuovoLibro;
        }

        public boolean isModificaEseguita() {
            return modificaEseguita;
        }

        // Reset dello stato per i test
        public void reset() {
            this.vecchioLibro = null;
            this.nuovoLibro = null;
            this.modificaEseguita = false;
        }
    }

    @BeforeEach
    public void setUp() {
        // Crea un controller per il test
        controller = new TestLibroController();

        // Crea un libro originale di test
        libroOriginale = new Libro(
                "Titolo Originale",
                "Autore Originale",
                "123-123",
                "Genere Originale",
                3,
                StatoLettura.LETTO
        );

        // Crea il comando di modifica
        command = new ModificaLibroCommand(
                controller,
                libroOriginale,
                "Titolo Modificato",
                "Autore Modificato",
                "123-456",
                "Genere Modificato",
                5,
                StatoLettura.IN_LETTURA
        );
    }

    @Test
    public void testExecute() {
        // Esegui il comando
        command.execute();

        // Verifica che la modifica sia stata eseguita
        assertTrue(controller.isModificaEseguita());

        // Verifica i parametri passati al controller
        assertEquals(libroOriginale, controller.getVecchioLibro());

        Libro libroModificato = controller.getNuovoLibro();
        assertNotNull(libroModificato);
        assertEquals("Titolo Modificato", libroModificato.getTitolo());
        assertEquals("Autore Modificato", libroModificato.getAutore());
        assertEquals("123-456", libroModificato.getIsbn());
        assertEquals("Genere Modificato", libroModificato.getGenere());
        assertEquals(5, libroModificato.getValutazione());
        assertEquals(StatoLettura.IN_LETTURA, libroModificato.getStatoLettura());
    }

    @Test
    public void testUndo() {
        // Esegui il comando
        command.execute();

        // Verifica che la modifica sia stata eseguita
        assertTrue(controller.isModificaEseguita());

        // Reset del controller per verificare l'undo
        controller.reset();

        // Annulla il comando
        command.undo();

        // Verifica che sia stata chiamata la modifica inversa
        assertTrue(controller.isModificaEseguita());

        // Verifica che il libro originale e il libro modificato siano stati scambiati
        // Durante l'undo, il libro modificato diventa vecchio e l'originale diventa nuovo
        Libro libroOriginaleRipristinato = controller.getNuovoLibro();
        assertNotNull(libroOriginaleRipristinato);
        assertEquals("Titolo Originale", libroOriginaleRipristinato.getTitolo());
        assertEquals("Autore Originale", libroOriginaleRipristinato.getAutore());
        assertEquals("123-123", libroOriginaleRipristinato.getIsbn());
        assertEquals("Genere Originale", libroOriginaleRipristinato.getGenere());
        assertEquals(3, libroOriginaleRipristinato.getValutazione());
        assertEquals(StatoLettura.LETTO, libroOriginaleRipristinato.getStatoLettura());
    }

    @Test
    public void testGetDescription() {
        // Verifica che la descrizione del comando contenga i titoli dei libri
        String description = command.getDescription();

        assertTrue(description.contains("Titolo Originale"));
        assertTrue(description.contains("Titolo Modificato"));
        assertTrue(description.contains("Modifica libro"));
    }
}