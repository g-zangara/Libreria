package test.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import model.Libro;
import model.StatoLettura;

/**
 * Test unitari per la classe Libro.
 * Verifica il corretto funzionamento dei metodi di Libro.
 */
public class LibroTest {

    private Libro libro;

    @BeforeEach
    public void setUp() {
        libro = new Libro("Il Nome della Rosa", "Umberto Eco", "978-88-452-6445-5", "Storico", 5, StatoLettura.LETTO);
    }

    @Test
    public void testCostruttore() {
        assertEquals("Il Nome della Rosa", libro.getTitolo());
        assertEquals("Umberto Eco", libro.getAutore());
        assertEquals("978-88-452-6445-5", libro.getIsbn());
        assertEquals("Storico", libro.getGenere());
        assertEquals(5, libro.getValutazione());
        assertEquals(StatoLettura.LETTO, libro.getStatoLettura());
    }

    @Test
    public void testCostruttoreVuoto() {
        Libro libroVuoto = new Libro();
        assertEquals("", libroVuoto.getTitolo());
        assertEquals("", libroVuoto.getAutore());
        assertEquals("", libroVuoto.getIsbn());
        assertEquals("", libroVuoto.getGenere());
        assertEquals(0, libroVuoto.getValutazione());
        assertEquals(StatoLettura.DA_LEGGERE, libroVuoto.getStatoLettura());
    }

    @Test
    public void testSetters() {
        libro.setTitolo("Nuovo Titolo");
        libro.setAutore("Nuovo Autore");
        libro.setIsbn("1234567890");
        libro.setGenere("Nuovo Genere");
        libro.setValutazione(3);
        libro.setStatoLettura(StatoLettura.IN_LETTURA);

        assertEquals("Nuovo Titolo", libro.getTitolo());
        assertEquals("Nuovo Autore", libro.getAutore());
        assertEquals("1234567890", libro.getIsbn());
        assertEquals("Nuovo Genere", libro.getGenere());
        assertEquals(3, libro.getValutazione());
        assertEquals(StatoLettura.IN_LETTURA, libro.getStatoLettura());
    }

    @Test
    public void testGetValutazioneAsString() {
        assertEquals("5", libro.getValutazioneAsString());

        libro.setValutazione(0);
        assertEquals("Da valutare", libro.getValutazioneAsString());
    }

    @Test
    public void testGetStatoLetturaAsString() {
        assertEquals("Letto", libro.getStatoLetturaAsString());

        libro.setStatoLettura(StatoLettura.IN_LETTURA);
        assertEquals("In lettura", libro.getStatoLetturaAsString());

        libro.setStatoLettura(StatoLettura.DA_LEGGERE);
        assertEquals("Da leggere", libro.getStatoLetturaAsString());
    }

    @Test
    public void testIsValidIsbn() {
        assertTrue(libro.isValidIsbn());

        assertThrows(IllegalArgumentException.class, () -> libro.setIsbn(""));

        assertThrows(IllegalArgumentException.class, () -> libro.setIsbn("abcabc"));

        libro.setIsbn("12345-678-90");
        assertTrue(libro.isValidIsbn());
    }

    @Test
    public void testIsValidTitolo() {
        assertTrue(libro.isValidTitolo());

        assertThrows(IllegalArgumentException.class, () -> libro.setTitolo(""));

        assertThrows(IllegalArgumentException.class, () -> libro.setTitolo("   "));

        assertThrows(IllegalArgumentException.class, () -> libro.setTitolo(null));

    }

    @Test
    public void testIsValidAutore() {
        assertTrue(libro.isValidAutore());

        assertThrows(IllegalArgumentException.class, () -> libro.setAutore(""));

        assertThrows(IllegalArgumentException.class, () -> libro.setAutore("   "));

        assertThrows(IllegalArgumentException.class, () -> libro.setAutore(null));
    }

    @Test
    public void testIsValidGenere() {
        assertTrue(libro.isValidGenere());

        assertThrows(IllegalArgumentException.class, () -> libro.setGenere(""));

        assertThrows(IllegalArgumentException.class, () -> libro.setGenere("   "));

        assertThrows(IllegalArgumentException.class, () -> libro.setGenere(null));
    }

    @Test
    public void testIsValidValutazione() {
        assertTrue(libro.isValidValutazione());

        assertThrows(IllegalArgumentException.class, () -> libro.setValutazione(-1));

        assertThrows(IllegalArgumentException.class, () -> libro.setValutazione(6));

        libro.setValutazione(0);
        assertTrue(libro.isValidValutazione());
    }

    @Test
    public void testIsValidStatoLettura() {
        assertTrue(libro.isValidStatoLettura());

        assertThrows(IllegalArgumentException.class, () -> libro.setStatoLettura(null));

        libro.setStatoLettura(StatoLettura.DA_LEGGERE);
        assertTrue(libro.isValidStatoLettura());
    }

    @Test
    public void testCostruttoreConCampiNonValidi() {
        assertThrows(IllegalArgumentException.class, () ->
                new Libro("", "Autore", "123-456-789", "Genere", 3, StatoLettura.LETTO)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Libro("Titolo", null, "123-456-789", "Genere", 3, StatoLettura.LETTO)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Libro("Titolo", "Autore", "ABC-123", "Genere", 3, StatoLettura.LETTO)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Libro("Titolo", "Autore", "123-456-789", "", 3, StatoLettura.LETTO)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Libro("Titolo", "Autore", "123-456-789", "Genere", -1, StatoLettura.LETTO)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Libro("Titolo", "Autore", "123-456-789", "Genere", 6, StatoLettura.LETTO)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Libro("Titolo", "Autore", "123-456-789", "Genere", 3, null)
        );
    }

    @Test
    public void testSettersConValoriValidi() {
        assertDoesNotThrow(() -> libro.setTitolo("Nuovo titolo"));
        assertDoesNotThrow(() -> libro.setAutore("Nuovo autore"));
        assertDoesNotThrow(() -> libro.setIsbn("978-3-16-148410-0"));
        assertDoesNotThrow(() -> libro.setGenere("Narrativa"));
        assertDoesNotThrow(() -> libro.setValutazione(5));
        assertDoesNotThrow(() -> libro.setValutazione(0)); // accettabile come "da valutare"
        assertDoesNotThrow(() -> libro.setStatoLettura(StatoLettura.IN_LETTURA));
    }

    @Test
    public void testEquals() {
        Libro libro2 = new Libro("Altro Titolo", "Altro Autore", libro.getIsbn(), "Altro Genere", 2, StatoLettura.DA_LEGGERE);
        assertEquals(libro, libro2); // Stessi ISBN -> libri uguali

        libro2.setIsbn("543-3454-323-12");
        assertNotEquals(libro, libro2); // ISBN diversi -> libri diversi

        assertNotEquals(libro, null);
        assertNotEquals(libro, "una stringa");
    }

    @Test
    public void testToString() {
        String expected = "Libro{titolo='Il Nome della Rosa', autore='Umberto Eco', isbn='978-88-452-6445-5', genere='Storico', valutazione=5, statoLettura=LETTO}";
        assertEquals(expected, libro.toString());
    }

}
