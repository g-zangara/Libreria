package controller;

import model.Libro;
import model.StatoLettura;
import dao.LibroDAO;
import dao.JsonLibroDAO;
import dao.CsvLibroDAO;
import strategy.OrdinatoreLibroStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementazione del pattern Singleton per la gestione centralizzata dei libri.
 * Gestisce la collezione di libri e le operazioni di ricerca, filtro e ordinamento.
 */
public class GestoreLibreria {

    // Singleton instance
    private static GestoreLibreria instance;

    // Attributi del gestore
    private List<Libro> libri;
    private LibroDAO jsonDAO;
    private LibroDAO csvDAO;

    /**
     * Costruttore privato per il pattern Singleton.
     * Inizializza le liste e gli oggetti DAO.
     */
    private GestoreLibreria() {
        this.libri = new ArrayList<>();
        this.jsonDAO = new JsonLibroDAO();
        this.csvDAO = new CsvLibroDAO();
    }

    /**
     * Ottiene l'istanza singleton del gestore libreria.
     *
     * @return L'istanza singleton del GestoreLibreria
     */
    public static synchronized GestoreLibreria getInstance() {
        if (instance == null) {
            instance = new GestoreLibreria();
        }
        return instance;
    }

    /**
     * Aggiunge un libro alla collezione.
     *
     * @param libro Libro da aggiungere
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean aggiungiLibro(Libro libro) {
        // Verifica che il libro non sia già presente (controllo basato sull'ISBN)
        if (libro != null && !libri.contains(libro)) {
            return libri.add(libro);
        }
        return false;
    }

    /**
     * Modifica un libro esistente nella collezione.
     *
     * @param vecchioLibro Libro da modificare
     * @param nuovoLibro Libro con i nuovi dati
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean modificaLibro(Libro vecchioLibro, Libro nuovoLibro) {
        int index = libri.indexOf(vecchioLibro);
        if (index != -1) {
            libri.set(index, nuovoLibro);
            return true;
        }
        return false;
    }

    /**
     * Elimina un libro dalla collezione.
     *
     * @param libro Libro da eliminare
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean eliminaLibro(Libro libro) {
        return libri.remove(libro);
    }

    /**
     * Ottiene la lista completa dei libri.
     *
     * @return Lista dei libri
     */
    public List<Libro> getLibri() {
        return new ArrayList<>(libri); // Restituisce una copia per evitare modifiche esterne
    }

    /**
     * Cerca libri per titolo.
     *
     * @param titolo Titolo da cercare (match parziale, case-insensitive)
     * @return Lista di libri che corrispondono alla ricerca
     */
    public List<Libro> cercaPerTitolo(String titolo) {
        if (titolo == null || titolo.trim().isEmpty()) {
            return getLibri();
        }

        String titoloLower = titolo.toLowerCase();
        return libri.stream()
                .filter(libro -> libro.getTitolo().toLowerCase().contains(titoloLower))
                .collect(Collectors.toList());
    }

    /**
     * Cerca libri per autore.
     *
     * @param autore Autore da cercare (match parziale, case-insensitive)
     * @return Lista di libri che corrispondono alla ricerca
     */
    public List<Libro> cercaPerAutore(String autore) {
        if (autore == null || autore.trim().isEmpty()) {
            return getLibri();
        }

        String autoreLower = autore.toLowerCase();
        return libri.stream()
                .filter(libro -> libro.getAutore().toLowerCase().contains(autoreLower))
                .collect(Collectors.toList());
    }

    /**
     * Cerca libri per ISBN.
     *
     * @param isbn ISBN da cercare (match parziale, case-insensitive)
     * @return Lista di libri che corrispondono alla ricerca
     */
    public List<Libro> cercaPerIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return getLibri();
        }

        String isbnLower = isbn.toLowerCase();
        return libri.stream()
                .filter(libro -> libro.getIsbn().toLowerCase().contains(isbnLower))
                .collect(Collectors.toList());
    }

    /**
     * Filtra libri per genere.
     *
     * @param genere Genere da filtrare (match esatto, case-insensitive)
     * @return Lista di libri che corrispondono al filtro
     */
    public List<Libro> filtraPerGenere(String genere) {
        if (genere == null || genere.trim().isEmpty() || genere.equalsIgnoreCase("Tutti")) {
            return getLibri();
        }

        String genereLower = genere.toLowerCase();
        return libri.stream()
                .filter(libro -> libro.getGenere().toLowerCase().equals(genereLower))
                .collect(Collectors.toList());
    }

    /**
     * Filtra libri per stato di lettura.
     *
     * @param statoLettura Stato di lettura da filtrare
     * @return Lista di libri che corrispondono al filtro
     */
    public List<Libro> filtraPerStatoLettura(StatoLettura statoLettura) {
        if (statoLettura == null) {
            return getLibri();
        }

        return libri.stream()
                .filter(libro -> libro.getStatoLettura() == statoLettura)
                .collect(Collectors.toList());
    }

    /**
     * Filtra libri per valutazione.
     *
     * @param valutazione Valutazione da filtrare (0 = da valutare, 1-5 = stelle)
     * @return Lista di libri che corrispondono al filtro
     */
    public List<Libro> filtraPerValutazione(int valutazione) {
        if (valutazione < 0) {
            return getLibri(); // Nessun filtro se valutazione è -1
        }

        return libri.stream()
                .filter(libro -> libro.getValutazione() == valutazione)
                .collect(Collectors.toList());
    }

    /**
     * Ordina i libri secondo la strategia specificata.
     *
     * @param libriDaOrdinare Lista di libri da ordinare
     * @param strategy Strategia di ordinamento da applicare
     * @return Lista ordinata di libri
     */
    public List<Libro> ordinaLibri(List<Libro> libriDaOrdinare, OrdinatoreLibroStrategy strategy) {
        if (strategy == null) {
            return libriDaOrdinare;
        }

        List<Libro> result = new ArrayList<>(libriDaOrdinare);
        strategy.ordina(result);
        return result;
    }

    /**
     * Carica libri da un file JSON.
     *
     * @param percorsoFile Percorso del file JSON
     * @throws IOException In caso di errori durante la lettura del file
     */
    public void caricaLibriDaJson(String percorsoFile) throws IOException {
        libri = jsonDAO.caricaLibri(percorsoFile);
    }

    /**
     * Salva libri in un file JSON.
     *
     * @param percorsoFile Percorso del file JSON
     * @throws IOException In caso di errori durante la scrittura del file
     */
    public void salvaLibriInJson(String percorsoFile) throws IOException {
        jsonDAO.salvaLibri(libri, percorsoFile);
    }

    /**
     * Carica libri da un file CSV.
     *
     * @param percorsoFile Percorso del file CSV
     * @throws IOException In caso di errori durante la lettura del file
     */
    public void caricaLibriDaCsv(String percorsoFile) throws IOException {
        libri = csvDAO.caricaLibri(percorsoFile);
    }

    /**
     * Salva libri in un file CSV.
     *
     * @param percorsoFile Percorso del file CSV
     * @throws IOException In caso di errori durante la scrittura del file
     */
    public void salvaLibriInCsv(String percorsoFile) throws IOException {
        csvDAO.salvaLibri(libri, percorsoFile);
    }

    /**
     * Ottiene tutti i generi unici presenti nella collezione di libri.
     *
     * @return Lista di generi unici
     */
    public List<String> getGeneriUnici() {
        return libri.stream()
                .map(Libro::getGenere)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Ottiene tutti gli autori unici presenti nella collezione di libri.
     *
     * @return Lista di autori unici
     */
    public List<String> getAutoriUnici() {
        return libri.stream()
                .map(Libro::getAutore)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
