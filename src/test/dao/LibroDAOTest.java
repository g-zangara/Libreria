package test.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import dao.LibroDAO;
import dao.JsonLibroDAO;
import dao.CsvLibroDAO;
import model.Libro;
import model.StatoLettura;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test unitari per le implementazioni di LibroDAO.
 * Verifica il corretto funzionamento delle operazioni di persistenza dei dati.
 */
public class LibroDAOTest {

    private LibroDAO jsonDAO;
    private LibroDAO csvDAO;
    private List<Libro> libriTest;
    private String jsonFilePath;
    private String csvFilePath;

    @BeforeEach
    public void setUp() {
        jsonDAO = new JsonLibroDAO();
        csvDAO = new CsvLibroDAO();

        // Crea una directory temporanea per i test
        File tempDir = new File("temp_test");
        tempDir.mkdir();

        // Prepara i percorsi dei file temporanei
        jsonFilePath = "temp_test/libri_test.json";
        csvFilePath = "temp_test/libri_test.csv";

        // Crea una lista di libri di test
        libriTest = new ArrayList<>();
        libriTest.add(new Libro("Il Nome della Rosa", "Umberto Eco", "978-88-452-6445-5", "Storico", 5, StatoLettura.LETTO));
        libriTest.add(new Libro("1984", "George Orwell", "978-0-452-28423-4", "Distopico", 4, StatoLettura.IN_LETTURA));
        libriTest.add(new Libro("La Divina Commedia", "Dante Alighieri", "978-88-04-59401-6", "Poesia", 0, StatoLettura.DA_LEGGERE));
    }

    @AfterEach
    public void tearDown() {
        // Elimina i file temporanei se esistono
        new File(jsonFilePath).delete();
        new File(csvFilePath).delete();
        new File("temp_test").delete();
    }

    @Test
    public void testJsonSalvaCaricaLibri() throws IOException {
        // Salva i libri in formato JSON
        jsonDAO.salvaLibri(libriTest, jsonFilePath);

        // Verifica che il file sia stato creato
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());
        assertTrue(jsonFile.length() > 0);

        // Carica i libri salvati
        List<Libro> libriCaricati = jsonDAO.caricaLibri(jsonFilePath);

        // Verifica che i libri caricati corrispondano a quelli salvati
        assertNotNull(libriCaricati);
        assertEquals(libriTest.size(), libriCaricati.size());

        // Verifica i dati di ciascun libro
        for (int i = 0; i < libriTest.size(); i++) {
            Libro libroOriginale = libriTest.get(i);
            Libro libroCaricato = libriCaricati.get(i);

            assertEquals(libroOriginale.getTitolo(), libroCaricato.getTitolo());
            assertEquals(libroOriginale.getAutore(), libroCaricato.getAutore());
            assertEquals(libroOriginale.getIsbn(), libroCaricato.getIsbn());
            assertEquals(libroOriginale.getGenere(), libroCaricato.getGenere());
            assertEquals(libroOriginale.getValutazione(), libroCaricato.getValutazione());
            assertEquals(libroOriginale.getStatoLettura(), libroCaricato.getStatoLettura());
        }
    }

    @Test
    public void testCsvSalvaCaricaLibri() throws IOException {
        // Salva i libri in formato CSV
        csvDAO.salvaLibri(libriTest, csvFilePath);

        // Verifica che il file sia stato creato
        File csvFile = new File(csvFilePath);
        assertTrue(csvFile.exists());
        assertTrue(csvFile.length() > 0);

        // Carica i libri salvati
        List<Libro> libriCaricati = csvDAO.caricaLibri(csvFilePath);

        // Verifica che i libri caricati corrispondano a quelli salvati
        assertNotNull(libriCaricati);
        assertEquals(libriTest.size(), libriCaricati.size());

        // Verifica i dati di ciascun libro
        for (int i = 0; i < libriTest.size(); i++) {
            Libro libroOriginale = libriTest.get(i);
            Libro libroCaricato = libriCaricati.get(i);

            assertEquals(libroOriginale.getTitolo(), libroCaricato.getTitolo());
            assertEquals(libroOriginale.getAutore(), libroCaricato.getAutore());
            assertEquals(libroOriginale.getIsbn(), libroCaricato.getIsbn());
            assertEquals(libroOriginale.getGenere(), libroCaricato.getGenere());
            assertEquals(libroOriginale.getValutazione(), libroCaricato.getValutazione());
            assertEquals(libroOriginale.getStatoLettura(), libroCaricato.getStatoLettura());
        }
    }

    @Test
    public void testJsonCaricaFileNonEsistente() {
        // Tenta di caricare da un file che non esiste
        String fileInesistente = "temp_test/file_inesistente.json";

        // Dovrebbe lanciare un'eccezione IOException
        assertThrows(IOException.class, () -> {
            jsonDAO.caricaLibri(fileInesistente);
        });
    }

    @Test
    public void testCsvCaricaFileNonEsistente() {
        // Tenta di caricare da un file che non esiste
        String fileInesistente = "temp_test/file_inesistente.csv";

        // Dovrebbe lanciare un'eccezione IOException
        assertThrows(IOException.class, () -> {
            csvDAO.caricaLibri(fileInesistente);
        });
    }

    @Test
    public void testJsonSalvaListaVuota() throws IOException {
        // Salva una lista vuota
        List<Libro> listaVuota = new ArrayList<>();
        jsonDAO.salvaLibri(listaVuota, jsonFilePath);

        // Verifica che il file sia stato creato
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());

        // Carica la lista vuota
        List<Libro> libriCaricati = jsonDAO.caricaLibri(jsonFilePath);

        // Verifica che la lista caricata sia vuota
        assertNotNull(libriCaricati);
        assertTrue(libriCaricati.isEmpty());
    }

    @Test
    public void testCsvSalvaListaVuota() throws IOException {
        // Salva una lista vuota
        List<Libro> listaVuota = new ArrayList<>();
        csvDAO.salvaLibri(listaVuota, csvFilePath);

        // Verifica che il file sia stato creato
        File csvFile = new File(csvFilePath);
        assertTrue(csvFile.exists());

        // Carica la lista vuota
        List<Libro> libriCaricati = csvDAO.caricaLibri(csvFilePath);

        // Verifica che la lista caricata sia vuota
        assertNotNull(libriCaricati);
        assertTrue(libriCaricati.isEmpty());
    }

    @Test
    public void testJsonCaricaLibriConStessoIsbn() throws IOException {
        // Crea una lista di libri con ISBN duplicati
        List<Libro> libriDuplicati = new ArrayList<>();
        libriDuplicati.add(new Libro("Libro 1", "Autore 1", "1234567890", "Genere 1", 5, StatoLettura.LETTO));
        libriDuplicati.add(new Libro("Libro 2", "Autore 2", "1234567890", "Genere 2", 4, StatoLettura.IN_LETTURA));

        // Salva i libri in formato JSON
        jsonDAO.salvaLibri(libriDuplicati, jsonFilePath);

        // Verifica che venga lanciata un'eccezione durante il caricamento
        IOException exception = assertThrows(IOException.class, () -> {
            jsonDAO.caricaLibri(jsonFilePath);
        });

        // Controlla che il messaggio dell'eccezione contenga informazioni sui duplicati
        String expectedMessage = "libro già presente o isbn duplicato";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void testCsvCaricaLibriConStessoIsbn() throws IOException {
        // Crea una lista di libri con ISBN duplicati
        List<Libro> libriDuplicati = new ArrayList<>();
        libriDuplicati.add(new Libro("Libro 1", "Autore 1", "1234567890", "Genere 1", 5, StatoLettura.LETTO));
        libriDuplicati.add(new Libro("Libro 2", "Autore 2", "1234567890", "Genere 2", 4, StatoLettura.IN_LETTURA));

        // Salva i libri in formato CSV
        csvDAO.salvaLibri(libriDuplicati, csvFilePath);

        // Verifica che venga lanciata un'eccezione durante il caricamento
        IOException exception = assertThrows(IOException.class, () -> {
            csvDAO.caricaLibri(csvFilePath);
        });

        // Controlla che il messaggio dell'eccezione contenga informazioni sui duplicati
        String expectedMessage = "libro già presente o isbn duplicato";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
