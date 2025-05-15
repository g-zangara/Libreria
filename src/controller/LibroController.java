package controller;

import model.Libro;
import model.StatoLettura;
import view.LibroView;
import strategy.*;
import command.*;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.*;

/**
 * Controller per gestire l'interazione tra il modello (Libro/GestoreLibreria) e la vista (LibroView).
 * Implementa il pattern MVC.
 */
public class LibroController {

    private final GestoreLibreria gestoreLibreria;
    private final LibroView view;
    private final CommandManager commandManager;

    /**
     * Costruttore che inizializza il controller con il gestore libreria e la vista.
     *
     * @param view Vista da associare al controller
     */
    public LibroController(LibroView view) {
        this.gestoreLibreria = GestoreLibreria.getInstance();
        this.view = view;
        this.commandManager = new CommandManager();
    }

    /**
     * Aggiunge un nuovo libro alla libreria usando il pattern Command.
     * Questo metodo è chiamato dalla vista e crea un comando AggiungiLibro.
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
        Command comando = new AggiungiLibroCommand(this, titolo, autore, isbn, genere,
                valutazione, statoLettura);
        commandManager.executeCommand(comando);
        aggiornaStatoPulsanti();
        return true;
    }

    /**
     * Metodo interno per aggiungere un libro senza creare un comando.
     * Questo metodo è chiamato dai comandi AggiungiLibroCommand e EliminaLibroCommand (undo).
     *
     * @param libro Libro da aggiungere
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean aggiungiLibroInterno(Libro libro) {
        boolean result = gestoreLibreria.aggiungiLibro(libro);
        if (result) {
            aggiornaTabella();
        }
        return result;
    }

    /**
     * Modifica un libro esistente nella libreria usando il pattern Command.
     * Questo metodo è chiamato dalla vista e crea un comando ModificaLibro.
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

        Command comando = new ModificaLibroCommand(this, libroSelezionato, titolo, autore, isbn,
                genere, valutazione, statoLettura);
        commandManager.executeCommand(comando);
        aggiornaStatoPulsanti();
        return true;
    }

    /**
     * Metodo interno per modificare un libro senza creare un comando.
     * Questo metodo è chiamato dal comando ModificaLibroCommand.
     *
     * @param vecchioLibro Libro da modificare
     * @param nuovoLibro Libro con i nuovi dati
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean modificaLibroInterno(Libro vecchioLibro, Libro nuovoLibro) {
        boolean result = gestoreLibreria.modificaLibro(vecchioLibro, nuovoLibro);
        if (result) {
            aggiornaTabella();
        }
        return result;
    }

    /**
     * Elimina un libro dalla libreria usando il pattern Command.
     * Questo metodo è chiamato dalla vista e crea un comando EliminaLibro.
     *
     * @param libro Libro da eliminare
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti
     */
    public boolean eliminaLibro(Libro libro) {
        if (libro == null) {
            return false;
        }

        Command comando = new EliminaLibroCommand(this, libro);
        commandManager.executeCommand(comando);
        aggiornaStatoPulsanti();
        return true;
    }

    /**
     * Metodo interno per eliminare un libro senza creare un comando.
     * Questo metodo è chiamato dal comando EliminaLibroCommand.
     *
     * @param libro Libro da eliminare
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean eliminaLibroInterno(Libro libro) {
        boolean result = gestoreLibreria.eliminaLibro(libro);
        if (result) {
            aggiornaTabella();
        }
        return result;
    }

    /**
     * Esegue l'operazione di undo (annulla l'ultima operazione).
     *
     * @return true se l'undo è stato eseguito, false se non ci sono operazioni da annullare
     */
    public boolean undo() {
        boolean result = commandManager.undo();
        aggiornaStatoPulsanti();
        return result;
    }

    /**
     * Esegue l'operazione di redo (ripristina l'ultima operazione annullata).
     *
     * @return true se il redo è stato eseguito, false se non ci sono operazioni da ripristinare
     */
    public boolean redo() {
        boolean result = commandManager.redo();
        aggiornaStatoPulsanti();
        return result;
    }

    /**
     * Verifica se è possibile eseguire un'operazione di undo.
     *
     * @return true se è possibile eseguire un undo, false altrimenti
     */
    public boolean canUndo() {
        return commandManager.canUndo();
    }

    /**
     * Verifica se è possibile eseguire un'operazione di redo.
     *
     * @return true se è possibile eseguire un redo, false altrimenti
     */
    public boolean canRedo() {
        return commandManager.canRedo();
    }

    /**
     * Ottiene la descrizione dell'ultima operazione eseguita (per tooltip undo).
     *
     * @return Descrizione dell'ultima operazione o null se non ci sono operazioni
     */
    public String getUndoDescription() {
        return commandManager.getUndoDescription();
    }

    /**
     * Ottiene la descrizione dell'ultima operazione annullata (per tooltip redo).
     *
     * @return Descrizione dell'ultima operazione annullata o null se non ci sono operazioni
     */
    public String getRedoDescription() {
        return commandManager.getRedoDescription();
    }

    /**
     * Aggiorna lo stato dei pulsanti undo/redo nella vista.
     */
    private void aggiornaStatoPulsanti() {
        view.aggiornaStatoPulsantiUndoRedo(canUndo(), canRedo(),
                getUndoDescription(), getRedoDescription());
    }

    /**
     * Carica la lista completa dei libri e aggiorna la vista.
     */
    public void caricaLibri() {
        List<Libro> libri = gestoreLibreria.getLibri();
        view.aggiornaTabella(libri);
        view.aggiornaComboBoxGeneri(gestoreLibreria.getGeneriUnici());
        view.aggiornaComboBoxAutori(gestoreLibreria.getAutoriUnici());
        // Inizializza lo stato dei pulsanti undo/redo
        aggiornaStatoPulsanti();
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
                    .filter(libro -> libro.getGenere().equalsIgnoreCase(genereSelezionato))
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
                System.err.println("Filtro non valido: " + statoLetturaSelezionato);
            }
        }

        // Filtro per valutazione
        int valutazioneSelezionata = view.getValutazioneSelezionata();
        if (valutazioneSelezionata >= 0) {
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
            }else {
                //IN TEORIA QUI NON DOVREBBE MAI ARRIVARCI
                System.err.println("Formato non supportato: " + formato);
                return;
            }

            JOptionPane.showMessageDialog(view, "Libreria caricata con successo dal file: " + percorsoFile,
                    "Caricamento completato", JOptionPane.INFORMATION_MESSAGE);
            aggiornaTabella();

            // Quando si carica una nuova libreria, si svuotano gli stack undo/redo
            commandManager.clearStacks();
            aggiornaStatoPulsanti();
        } catch (IOException e) {
            mostraErroreConScrollSeNecessario("Errore durante il caricamento della libreria: " + e.getMessage());
        }
    }

    /**
     * Mostra un messaggio di errore in un JOptionPane con scroll se il messaggio è lungo.
     *
     * @param messaggio Messaggio di errore da mostrare
     */
    private void mostraErroreConScrollSeNecessario(String messaggio) {
        if (messaggio != null && messaggio.length() > 200) {
            JTextArea textArea = new JTextArea(messaggio);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(view, scrollPane, "Errore di caricamento", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, messaggio, "Errore di caricamento", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Pulisce la libreria, rimuovendo tutti i libri presenti.
     * Svuota anche gli stack di undo e redo.
     */
    public void pulisciLibreria() {
        gestoreLibreria.pulisciLibreria();
        aggiornaTabella();

        // Svuota gli stack undo/redo quando si pulisce la libreria
        commandManager.clearStacks();
        aggiornaStatoPulsanti();
    }

}
