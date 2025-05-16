package test.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import controller.GestoreLibreria;
import model.Libro;
import model.StatoLettura;
import strategy.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test unitari per la classe GestoreLibreria.
 * Verifica il corretto funzionamento del Singleton e dei metodi di gestione dei libri.
 */
public class GestoreLibreriaTest {

    private GestoreLibreria gestore;
    private Libro libro1, libro2, libro3;

    @BeforeEach
    public void setUp() {
        gestore = GestoreLibreria.getInstance();
        gestore.pulisciLibreria();

        // Crea alcuni libri di test
        libro1 = new Libro("Il Nome della Rosa", "Umberto Eco", "978-88-452-6445-5", "Storico", 5, StatoLettura.LETTO);
        libro2 = new Libro("1984", "George Orwell", "978-0-452-28423-4", "Distopico", 4, StatoLettura.LETTO);
        libro3 = new Libro("La Divina Commedia", "Dante Alighieri", "978-88-04-59401-6", "Poesia", 0, StatoLettura.DA_LEGGERE);

        // Aggiungi i libri al gestore
        gestore.aggiungiLibro(libro1);
        gestore.aggiungiLibro(libro2);
        gestore.aggiungiLibro(libro3);
    }

    @AfterEach
    public void tearDown() {
        // Pulisce il gestore dopo ogni test
        gestore.pulisciLibreria();
    }

    @Test
    public void testGetInstance() {
        // Verifica che l'istanza ottenuta sia un singleton
        GestoreLibreria gestore1 = GestoreLibreria.getInstance();
        GestoreLibreria gestore2 = GestoreLibreria.getInstance();

        assertSame(gestore1, gestore2);
        assertSame(gestore, gestore1);
    }

    @Test
    public void testAggiungiLibro() {
        // Svuota il gestore
        gestore.pulisciLibreria();
        assertTrue(gestore.getLibri().isEmpty());

        // Aggiungi un libro e verifica che sia stato aggiunto
        assertTrue(gestore.aggiungiLibro(libro1));
        assertEquals(1, gestore.getLibri().size());
        assertTrue(gestore.getLibri().contains(libro1));

        // Prova ad aggiungere lo stesso libro (con lo stesso ISBN) e verifica che non sia stato aggiunto
        assertFalse(gestore.aggiungiLibro(libro1));
        assertEquals(1, gestore.getLibri().size());

        // Prova ad aggiungere null e verifica che non sia stato aggiunto
        assertFalse(gestore.aggiungiLibro(null));
        assertEquals(1, gestore.getLibri().size());
    }

    @Test
    public void testModificaLibro() {
        // Crea un nuovo libro con lo stesso ISBN di libro1 ma dati diversi
        Libro libroModificato = new Libro("Titolo Modificato", "Autore Modificato", libro1.getIsbn(), "Genere Modificato", 3, StatoLettura.IN_LETTURA);

        // Modifica il libro e verifica che sia stato modificato
        assertTrue(gestore.modificaLibro(libro1, libroModificato));

        // Verifica che i dati siano stati aggiornati
        List<Libro> libri = gestore.getLibri();
        Libro libroAggiornato = libri.stream()
                                     .filter(libro -> libro.getIsbn().equals(libro1.getIsbn()))
                                     .findFirst()
                                     .orElse(null);

        assertNotNull(libroAggiornato, "Il libro modificato dovrebbe essere presente nella libreria");
        assertEquals("Titolo Modificato", libroAggiornato.getTitolo());
        assertEquals("Autore Modificato", libroAggiornato.getAutore());
        assertEquals("Genere Modificato", libroAggiornato.getGenere());
        assertEquals(3, libroAggiornato.getValutazione());
        assertEquals(StatoLettura.IN_LETTURA, libroAggiornato.getStatoLettura());
    }

    @Test
    public void testModificaLibroInesistente() {
        // Crea un nuovo gestore e pulisci la libreria
        GestoreLibreria gestoreTest = GestoreLibreria.getInstance();
        gestoreTest.pulisciLibreria();

        // Aggiungi un libro conosciuto per assicurarci che la libreria non sia vuota
        Libro libroEsistente = new Libro("Libro Test", "Autore Test", "ISBN-TEST-123", "Genere Test", 3, StatoLettura.LETTO);
        gestoreTest.aggiungiLibro(libroEsistente);

        // Verifica che la libreria contenga esattamente un libro
        assertEquals(1, gestoreTest.getLibri().size(), "La libreria dovrebbe contenere esattamente un libro");

        // Crea un libro con ISBN diverso che sicuramente non esiste nella libreria
        Libro libroInesistente = new Libro("Non Esiste", "Autore", "ISBN-SICURO-NON-ESISTE-456", "Genere", 1, StatoLettura.DA_LEGGERE);
        Libro nuovoLibro = new Libro("Nuovo", "Autore Nuovo", "ISBN-nuovo-789", "Genere Nuovo", 2, StatoLettura.LETTO);

        // Verifica che il libro non esista prima del tentativo di modifica
        List<Libro> libriPrima = gestoreTest.getLibri();
        assertFalse(libriPrima.contains(libroInesistente), "Il libro inesistente non dovrebbe essere nella libreria");

        // Prova a modificare il libro inesistente
        boolean result = gestoreTest.modificaLibro(libroInesistente, nuovoLibro);

        // Verifica che la libreria contenga ancora solo il libro originale
        List<Libro> libriDopo = gestoreTest.getLibri();
        assertEquals(1, libriDopo.size(), "La libreria dovrebbe contenere ancora solo un libro");
        assertTrue(libriDopo.contains(libroEsistente), "La libreria dovrebbe contenere ancora il libro originale");
        assertFalse(libriDopo.contains(nuovoLibro), "Il nuovo libro non dovrebbe essere stato aggiunto");
    }

    @Test
    public void testEliminaLibro() {
        // Verifica il numero iniziale di libri
        assertEquals(3, gestore.getLibri().size());

        // Elimina un libro e verifica che sia stato eliminato
        assertTrue(gestore.eliminaLibro(libro1));
        assertEquals(2, gestore.getLibri().size());
        assertFalse(gestore.getLibri().contains(libro1));

        // Prova a eliminare un libro già eliminato
        assertFalse(gestore.eliminaLibro(libro1));
        assertEquals(2, gestore.getLibri().size());
    }

    @Test
    public void testGetLibri() {
        // Verifica che getLibri restituisca tutti i libri aggiunti
        List<Libro> libri = gestore.getLibri();
        assertEquals(3, libri.size());
        assertTrue(libri.contains(libro1));
        assertTrue(libri.contains(libro2));
        assertTrue(libri.contains(libro3));

        // Verifica che la lista restituita sia una copia (modificare la lista non deve modificare il gestore)
        libri.remove(0);
        assertEquals(2, libri.size());
        assertEquals(3, gestore.getLibri().size()); // Il gestore ha ancora 3 libri
    }

    @Test
    public void testCercaPerTitolo() {
        // Cerca per titolo esatto
        List<Libro> risultato = gestore.cercaPerTitolo("Il Nome della Rosa");
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(libro1));

        // Cerca per titolo parziale
        risultato = gestore.cercaPerTitolo("Rosa");
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(libro1));

        // Cerca per titolo case-insensitive
        risultato = gestore.cercaPerTitolo("nome");
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(libro1));

        // Cerca titolo inesistente
        risultato = gestore.cercaPerTitolo("Titolo Inesistente");
        assertTrue(risultato.isEmpty());

        // Cerca con stringa vuota (deve restituire tutti i libri)
        risultato = gestore.cercaPerTitolo("");
        assertEquals(3, risultato.size());

        // Cerca con null (deve restituire tutti i libri)
        risultato = gestore.cercaPerTitolo(null);
        assertEquals(3, risultato.size());
    }

    @Test
    public void testCercaPerAutore() {
        // Cerca per autore esatto
        List<Libro> risultato = gestore.cercaPerAutore("Umberto Eco");
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(libro1));

        // Cerca per autore parziale
        risultato = gestore.cercaPerAutore("Eco");
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(libro1));

        // Cerca per autore case-insensitive
        risultato = gestore.cercaPerAutore("umberto");
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(libro1));

        // Cerca autore inesistente
        risultato = gestore.cercaPerAutore("Autore Inesistente");
        assertTrue(risultato.isEmpty());

        // Cerca con stringa vuota (deve restituire tutti i libri)
        risultato = gestore.cercaPerAutore("");
        assertEquals(3, risultato.size());

        // Cerca con null (deve restituire tutti i libri)
        risultato = gestore.cercaPerAutore(null);
        assertEquals(3, risultato.size());
    }

    @Test
    public void testCercaPerIsbn() {
        // Cerca per ISBN esatto
        List<Libro> risultato = gestore.cercaPerIsbn(libro1.getIsbn());
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(libro1));

        // Cerca per ISBN parziale
        risultato = gestore.cercaPerIsbn("978-88");
        assertEquals(2, risultato.size()); // Dovrebbe trovare libro1 e libro3

        // Cerca ISBN inesistente
        risultato = gestore.cercaPerIsbn("ISBN-inesistente");
        assertTrue(risultato.isEmpty());

        // Cerca con stringa vuota (deve restituire tutti i libri)
        risultato = gestore.cercaPerIsbn("");
        assertEquals(3, risultato.size());

        // Cerca con null (deve restituire tutti i libri)
        risultato = gestore.cercaPerIsbn(null);
        assertEquals(3, risultato.size());
    }

    @Test
    public void testOrdinaLibri() {
        List<Libro> libri = new ArrayList<>();
        // Aggiungo i libri in un ordine specifico per il test
        libri.add(libro1); // "Il Nome della Rosa", "Umberto Eco"
        libri.add(libro2); // "1984", "George Orwell"
        libri.add(libro3); // "La Divina Commedia", "Dante Alighieri"

        // Test ordinamento per titolo A-Z
        List<Libro> ordinati = gestore.ordinaLibri(libri, new OrdinaTitoloAZStrategy());
        // Verifica che i titoli siano in ordine alfabetico
        assertTrue(ordinati.get(0).getTitolo().compareTo(ordinati.get(1).getTitolo()) <= 0);
        assertTrue(ordinati.get(1).getTitolo().compareTo(ordinati.get(2).getTitolo()) <= 0);

        // Test ordinamento per titolo Z-A
        ordinati = gestore.ordinaLibri(libri, new OrdinaTitoloZAStrategy());
        // Verifica che i titoli siano in ordine alfabetico inverso
        assertTrue(ordinati.get(0).getTitolo().compareTo(ordinati.get(1).getTitolo()) >= 0);
        assertTrue(ordinati.get(1).getTitolo().compareTo(ordinati.get(2).getTitolo()) >= 0);

        // Test ordinamento per autore A-Z
        ordinati = gestore.ordinaLibri(libri, new OrdinaAutoreAZStrategy());
        // Verifica che gli autori siano in ordine alfabetico
        assertTrue(ordinati.get(0).getAutore().compareTo(ordinati.get(1).getAutore()) <= 0);
        assertTrue(ordinati.get(1).getAutore().compareTo(ordinati.get(2).getAutore()) <= 0);

        // Test ordinamento per autore Z-A
        ordinati = gestore.ordinaLibri(libri, new OrdinaAutoreZAStrategy());
        // Verifica che gli autori siano in ordine alfabetico inverso
        assertTrue(ordinati.get(0).getAutore().compareTo(ordinati.get(1).getAutore()) >= 0);
        assertTrue(ordinati.get(1).getAutore().compareTo(ordinati.get(2).getAutore()) >= 0);

        // Test ordinamento per valutazione Ascendente
        ordinati = gestore.ordinaLibri(libri, new OrdinaValutazioneAscStrategy());
        // Verifica che le valutazioni siano in ordine crescente
        assertTrue(ordinati.get(0).getValutazione() <= ordinati.get(1).getValutazione());
        assertTrue(ordinati.get(1).getValutazione() <= ordinati.get(2).getValutazione());

        // Test ordinamento per valutazione Discendente
        ordinati = gestore.ordinaLibri(libri, new OrdinaValutazioneDescStrategy());
        // Verifica che le valutazioni siano in ordine decrescente
        assertTrue(ordinati.get(0).getValutazione() >= ordinati.get(1).getValutazione());
        assertTrue(ordinati.get(1).getValutazione() >= ordinati.get(2).getValutazione());

        // Test ordinamento con strategia null
        ordinati = gestore.ordinaLibri(libri, null);
        assertEquals(3, ordinati.size());
        // Verifica che tutti i libri originali siano presenti
        assertTrue(ordinati.contains(libro1));
        assertTrue(ordinati.contains(libro2));
        assertTrue(ordinati.contains(libro3));
    }

    @Test
    public void testGetGeneriUnici() {
        // Aggiungi un libro con un genere duplicato
        Libro libro4 = new Libro("Altro libro", "Altro Autore", "ISBN-altro", "Storico", 3, StatoLettura.DA_LEGGERE);
        gestore.aggiungiLibro(libro4);

        // Verifica i generi unici
        List<String> generi = gestore.getGeneriUnici();
        assertEquals(3, generi.size());
        assertTrue(generi.contains("Distopico"));
        assertTrue(generi.contains("Poesia"));
        assertTrue(generi.contains("Storico"));
    }

    @Test
    public void testGetAutoriUnici() {
        // Aggiungi un libro con un autore duplicato
        Libro libro4 = new Libro("Altro libro di Eco", "Umberto Eco", "ISBN-altro", "Romanzo", 3, StatoLettura.DA_LEGGERE);
        gestore.aggiungiLibro(libro4);

        // Verifica gli autori unici
        List<String> autori = gestore.getAutoriUnici();
        assertEquals(3, autori.size());
        assertTrue(autori.contains("Dante Alighieri"));
        assertTrue(autori.contains("George Orwell"));
        assertTrue(autori.contains("Umberto Eco"));
    }

    @Test
    public void testPulisciLibreria() {
        // Verifica che ci siano libri prima di pulire
        assertFalse(gestore.getLibri().isEmpty());

        // Pulisci la libreria
        gestore.pulisciLibreria();

        // Verifica che la libreria sia vuota dopo aver pulito
        assertTrue(gestore.getLibri().isEmpty());
    }
}