package controller;

import model.Libro;
import model.StatoLettura;
import view.LibroView;
import strategy.*;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

/**
 * Controller per gestire l'interazione tra il modello (Libro/GestoreLibreria) e la vista (LibroView).
 * Implementa il pattern MVC.
 */
public class LibroController {

    private GestoreLibreria gestoreLibreria;
    private LibroView view;

    /**
     * Costruttore che inizializza il controller con il gestore libreria e la vista.
     *
     * @param view Vista da associare al controller
     */
    public LibroController(LibroView view) {
        this.gestoreLibreria = GestoreLibreria.getInstance();
        this.view = view;
    }

    /**
     * Aggiunge un nuovo libro alla libreria.
     *
     * @param titolo Titolo del libro
     * @param autore Autore del libro
     * @param isbn ISBN del libro
     * @param genere Genere del libro
     * @param valutazione Valutazione del libro
     * @param statoLettura Stato lettura del libro
     * @return true se l'inserimento è avvenuto con successo, false altrimenti
     */
    public boolean aggiungiLibro(String titolo, String autore, String isbn, String genere,
                                 int valutazione, StatoLettura statoLettura) {
        Libro nuovoLibro = new Libro(titolo, autore, isbn, genere, valutazione, statoLettura);
        boolean result = gestoreLibreria.aggiungiLibro(nuovoLibro);
        if (result) {
            aggiornaTabella();
        }
        return result;
    }

    /**
     * Modifica un libro esistente nella libreria.
     *
     * @param libroSelezionato Libro da modificare
     * @param titolo Nuovo titolo
     * @param autore Nuovo autore
     * @param isbn Nuovo ISBN
     * @param genere Nuovo genere
     * @param valutazione Nuova valutazione
     * @param statoLettura Nuovo stato di lettura
     * @return true se la modifica è avvenuta con successo, false altrimenti
     */
    public boolean modificaLibro(Libro libroSelezionato, String titolo, String autore, String isbn,
                                 String genere, int valutazione, StatoLettura statoLettura) {
        if (libroSelezionato == null) {
            return false;
        }

        Libro nuovoLibro = new Libro(titolo, autore, isbn, genere, valutazione, statoLettura);
        boolean result = gestoreLibreria.modificaLibro(libroSelezionato, nuovoLibro);
        if (result) {
            aggiornaTabella();
        }
        return result;
    }

    /**
     * Elimina un libro dalla libreria.
     *
     * @param libro Libro da eliminare
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti
     */
    public boolean eliminaLibro(Libro libro) {
        boolean result = gestoreLibreria.eliminaLibro(libro);
        if (result) {
            aggiornaTabella();
        }
        return result;
    }

    /**
     * Carica la lista completa dei libri e aggiorna la vista.
     */
    public void caricaLibri() {
        List<Libro> libri = gestoreLibreria.getLibri();
        view.aggiornaTabella(libri);
        view.aggiornaComboBoxGeneri(gestoreLibreria.getGeneriUnici());
        view.aggiornaComboBoxAutori(gestoreLibreria.getAutoriUnici());
    }

    /**
     * Cerca libri in base ai criteri di ricerca specificati.
     *
     * @param testoCerca Testo di ricerca
     * @param tipoCerca Tipo di ricerca (titolo, autore, isbn)
     */
    public void cercaLibri(String testoCerca, String tipoCerca) {
        List<Libro> risultato;

        if (testoCerca == null || testoCerca.trim().isEmpty()) {
            risultato = gestoreLibreria.getLibri();
        } else {
            switch (tipoCerca) {
                case "Titolo":
                    risultato = gestoreLibreria.cercaPerTitolo(testoCerca);
                    break;
                case "Autore":
                    risultato = gestoreLibreria.cercaPerAutore(testoCerca);
                    break;
                case "ISBN":
                    risultato = gestoreLibreria.cercaPerIsbn(testoCerca);
                    break;
                default:
                    risultato = gestoreLibreria.getLibri();
            }
        }

        // Applica i filtri selezionati
        risultato = applicaFiltri(risultato);

        // Applica l'ordinamento selezionato
        risultato = applicaOrdinamento(risultato);

        view.aggiornaTabella(risultato);
    }

    /**
     * Applica i filtri selezionati nella vista a una lista di libri.
     * Supporta l'applicazione di filtri multipli in combinazione.
     *
     * @param libriOriginali Lista di libri da filtrare
     * @return Lista filtrata di libri
     */
    private List<Libro> applicaFiltri(List<Libro> libriOriginali) {
        List<Libro> libri = new ArrayList<>(libriOriginali); // Clona la lista per non modificare l'originale

        // Filtro per genere
        String genereSelezionato = view.getGenereSelezionato();
        if (genereSelezionato != null && !genereSelezionato.equals("Tutti")) {
            libri = libri.stream()
                    .filter(libro -> libro.getGenere().toLowerCase().equals(genereSelezionato.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filtro per autore
        String autoreSelezionato = view.getAutoreSelezionato();
        if (autoreSelezionato != null && !autoreSelezionato.equals("Tutti")) {
            libri = libri.stream()
                    .filter(libro -> libro.getAutore().toLowerCase().contains(autoreSelezionato.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filtro per stato di lettura
        String statoLetturaSelezionato = view.getStatoLetturaSelezionato();
        if (statoLetturaSelezionato != null && !statoLetturaSelezionato.equals("Tutti")) {
            try {
                StatoLettura stato = StatoLettura.fromString(statoLetturaSelezionato);
                libri = libri.stream()
                        .filter(libro -> libro.getStatoLettura() == stato)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                // Ignora filtro non valido
            }
        }

        // Filtro per valutazione
        int valutazioneSelezionata = view.getValutazioneSelezionata();
        if (valutazioneSelezionata >= 0) { // Includi anche 0 (da valutare)
            libri = libri.stream()
                    .filter(libro -> libro.getValutazione() == valutazioneSelezionata)
                    .collect(Collectors.toList());
        }

        return libri;
    }

    /**
     * Applica l'ordinamento selezionato nella vista a una lista di libri.
     *
     * @param libri Lista di libri da ordinare
     * @return Lista ordinata di libri
     */
    private List<Libro> applicaOrdinamento(List<Libro> libri) {
        String ordinamentoSelezionato = view.getOrdinamentoSelezionato();
        if (ordinamentoSelezionato != null) {
            OrdinatoreLibroStrategy strategy = null;

            switch (ordinamentoSelezionato) {
                case "Titolo (A-Z)":
                    strategy = new OrdinaTitoloAZStrategy();
                    break;
                case "Titolo (Z-A)":
                    strategy = new OrdinaTitoloZAStrategy();
                    break;
                case "Autore (A-Z)":
                    strategy = new OrdinaAutoreAZStrategy();
                    break;
                case "Autore (Z-A)":
                    strategy = new OrdinaAutoreZAStrategy();
                    break;
                case "Valutazione (1-5)":
                    strategy = new OrdinaValutazioneAscStrategy();
                    break;
                case "Valutazione (5-1)":
                    strategy = new OrdinaValutazioneDescStrategy();
                    break;
            }

            if (strategy != null) {
                libri = gestoreLibreria.ordinaLibri(libri, strategy);
            }
        }

        return libri;
    }

    /**
     * Aggiorna la tabella nella vista con la lista filtrata e ordinata.
     */
    public void aggiornaTabella() {
        // Ottiene la lista dei libri
        List<Libro> libri = gestoreLibreria.getLibri();

        // Applica i filtri
        libri = applicaFiltri(libri);

        // Applica l'ordinamento
        libri = applicaOrdinamento(libri);

        // Aggiorna la tabella nella vista
        view.aggiornaTabella(libri);

        // Aggiorna le combo box di filtro
        view.aggiornaComboBoxGeneri(gestoreLibreria.getGeneriUnici());
        view.aggiornaComboBoxAutori(gestoreLibreria.getAutoriUnici());
    }

    /**
     * Salva la libreria nel formato specificato.
     *
     * @param percorsoFile Percorso del file
     * @param formato Formato del file (JSON o CSV)
     */
    public void salvaLibreria(String percorsoFile, String formato) {
        try {
            if ("JSON".equalsIgnoreCase(formato)) {
                gestoreLibreria.salvaLibriInJson(percorsoFile);
            } else if ("CSV".equalsIgnoreCase(formato)) {
                gestoreLibreria.salvaLibriInCsv(percorsoFile);
            }
            JOptionPane.showMessageDialog(view, "Libreria salvata con successo nel file: " + percorsoFile,
                    "Salvataggio completato", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "Errore durante il salvataggio della libreria: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carica la libreria dal formato specificato.
     *
     * @param percorsoFile Percorso del file
     * @param formato Formato del file (JSON o CSV)
     */
    public void caricaLibreria(String percorsoFile, String formato) {
        try {
            if ("JSON".equalsIgnoreCase(formato)) {
                gestoreLibreria.caricaLibriDaJson(percorsoFile);
            } else if ("CSV".equalsIgnoreCase(formato)) {
                gestoreLibreria.caricaLibriDaCsv(percorsoFile);
            }
            JOptionPane.showMessageDialog(view, "Libreria caricata con successo dal file: " + percorsoFile,
                    "Caricamento completato", JOptionPane.INFORMATION_MESSAGE);
            aggiornaTabella();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "Errore durante il caricamento della libreria: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}
