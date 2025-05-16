package test.strategy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import model.Libro;
import model.StatoLettura;
import strategy.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Test unitari per le classi che implementano OrdinatoreLibroStrategy.
 * Verifica il corretto funzionamento delle strategie di ordinamento.
 */
public class OrdinatoreLibroStrategyTest {

    private List<Libro> libri;
    private Libro libro1, libro2, libro3, libro4;

    @BeforeEach
    public void setUp() {
        libri = new ArrayList<>();

        libro1 = new Libro("La Divina Commedia", "Dante Alighieri", "ISBN1", "Poesia", 5, StatoLettura.LETTO);
        libro2 = new Libro("1984", "George Orwell", "ISBN2", "Distopico", 4, StatoLettura.LETTO);
        libro3 = new Libro("Il Nome della Rosa", "Umberto Eco", "ISBN3", "Storico", 3, StatoLettura.IN_LETTURA);
        libro4 = new Libro("Anna Karenina", "Lev Tolstoj", "ISBN4", "Romanzo", 0, StatoLettura.DA_LEGGERE);

        // Aggiunta dei libri in ordine casuale
        libri.add(libro1); // Dante Alighieri
        libri.add(libro3); // Umberto Eco
        libri.add(libro2); // George Orwell
        libri.add(libro4); // Lev Tolstoj
    }

    @Test
    public void testOrdinaTitoloAZStrategy() {
        // Ordina per titolo A-Z
        OrdinatoreLibroStrategy strategy = new OrdinaTitoloAZStrategy();
        strategy.ordina(libri);

        // Verifica l'ordine: 1984, Anna Karenina, Il Nome della Rosa, La Divina Commedia
        assertEquals(libro2, libri.get(0)); // "1984"
        assertEquals(libro4, libri.get(1)); // "Anna Karenina"
        assertEquals(libro3, libri.get(2)); // "Il Nome della Rosa"
        assertEquals(libro1, libri.get(3)); // "La Divina Commedia"
    }

    @Test
    public void testOrdinaTitoloZAStrategy() {
        // Ordina per titolo Z-A
        OrdinatoreLibroStrategy strategy = new OrdinaTitoloZAStrategy();
        strategy.ordina(libri);

        // Verifica l'ordine: La Divina Commedia, Il Nome della Rosa, Anna Karenina, 1984
        assertEquals(libro1, libri.get(0)); // "La Divina Commedia"
        assertEquals(libro3, libri.get(1)); // "Il Nome della Rosa"
        assertEquals(libro4, libri.get(2)); // "Anna Karenina"
        assertEquals(libro2, libri.get(3)); // "1984"
    }

    @Test
    public void testOrdinaAutoreAZStrategy() {
        // Ordina per autore A-Z
        OrdinatoreLibroStrategy strategy = new OrdinaAutoreAZStrategy();
        strategy.ordina(libri);

        // Verifica l'ordine: Dante Alighieri, George Orwell, Lev Tolstoj, Umberto Eco
        assertEquals(libro1, libri.get(0)); // "Dante Alighieri"
        assertEquals(libro2, libri.get(1)); // "George Orwell"
        assertEquals(libro4, libri.get(2)); // "Lev Tolstoj"
        assertEquals(libro3, libri.get(3)); // "Umberto Eco"
    }

    @Test
    public void testOrdinaAutoreZAStrategy() {
        // Ordina per autore Z-A
        OrdinatoreLibroStrategy strategy = new OrdinaAutoreZAStrategy();
        strategy.ordina(libri);

        // Verifica l'ordine: Umberto Eco, Lev Tolstoj, George Orwell, Dante Alighieri
        assertEquals(libro3, libri.get(0)); // "Umberto Eco"
        assertEquals(libro4, libri.get(1)); // "Lev Tolstoj"
        assertEquals(libro2, libri.get(2)); // "George Orwell"
        assertEquals(libro1, libri.get(3)); // "Dante Alighieri"
    }

    @Test
    public void testOrdinaValutazioneAscStrategy() {
        // Ordina per valutazione ascendente
        OrdinatoreLibroStrategy strategy = new OrdinaValutazioneAscStrategy();
        strategy.ordina(libri);

        // Verifica l'ordine: 0, 3, 4, 5
        assertEquals(libro4, libri.get(0)); // 0 (Da valutare)
        assertEquals(libro3, libri.get(1)); // 3
        assertEquals(libro2, libri.get(2)); // 4
        assertEquals(libro1, libri.get(3)); // 5
    }

    @Test
    public void testOrdinaValutazioneDescStrategy() {
        // Ordina per valutazione discendente
        OrdinatoreLibroStrategy strategy = new OrdinaValutazioneDescStrategy();
        strategy.ordina(libri);

        // Verifica l'ordine: 5, 4, 3, 0
        assertEquals(libro1, libri.get(0)); // 5
        assertEquals(libro2, libri.get(1)); // 4
        assertEquals(libro3, libri.get(2)); // 3
        assertEquals(libro4, libri.get(3)); // 0 (Da valutare)
    }

    @Test
    public void testCaseSensitivity() {
        // Crea libri con titoli che differiscono solo per maiuscole/minuscole
        List<Libro> libriCase = new ArrayList<>();
        Libro libroA = new Libro("aaa", "Autore1", "ISBN-A", "Genere", 1, StatoLettura.LETTO);
        Libro libroB = new Libro("AAA", "Autore2", "ISBN-B", "Genere", 2, StatoLettura.LETTO);
        libriCase.add(libroA);
        libriCase.add(libroB);

        // Test ordinamento titolo A-Z (case insensitive)
        OrdinatoreLibroStrategy strategyTitolo = new OrdinaTitoloAZStrategy();
        strategyTitolo.ordina(libriCase);

        // L'ordine dovrebbe dipendere dalla posizione originale, dato che i titoli sono uguali ignorando il caso
        // Verifichiamo solo che non siano stati modificati in modo imprevisto
        assertEquals(2, libriCase.size());
        assertTrue(libriCase.contains(libroA));
        assertTrue(libriCase.contains(libroB));

        // Test ordinamento autore A-Z (case insensitive)
        libriCase.clear();
        Libro libroC = new Libro("Titolo1", "autore", "ISBN-C", "Genere", 1, StatoLettura.LETTO);
        Libro libroD = new Libro("Titolo2", "AUTORE", "ISBN-D", "Genere", 2, StatoLettura.LETTO);
        libriCase.add(libroC);
        libriCase.add(libroD);

        OrdinatoreLibroStrategy strategyAutore = new OrdinaAutoreAZStrategy();
        strategyAutore.ordina(libriCase);

        // Come sopra, verifichiamo solo che non ci siano stati problemi
        assertEquals(2, libriCase.size());
        assertTrue(libriCase.contains(libroC));
        assertTrue(libriCase.contains(libroD));
    }

}
