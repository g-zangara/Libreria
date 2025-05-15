package view;

import controller.LibroController;
import model.Libro;
import model.StatoLettura;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Vector;

/**
 * Classe che implementa l'interfaccia grafica Swing per la gestione della libreria.
 * Rappresenta la vista nel pattern MVC.
 */
public class LibroView extends JFrame {

    // Controller
    private final LibroController controller;

    // Componenti UI
    private JTable tabellaLibri;
    private DefaultTableModel modelloTabella;
    private JTextField campoCerca;
    private JComboBox<String> comboTipoCerca;
    private JComboBox<String> comboGenere;
    private JComboBox<String> comboAutore;
    private JComboBox<String> comboStatoLettura;
    private JComboBox<String> comboValutazione;
    private JComboBox<String> comboOrdinamento;
    private JButton btnAggiungi, btnModifica, btnElimina;
    private JButton btnCerca, btnResetFiltri;
    private JButton btnSalvaJSON, btnSalvaCSV, btnCaricaJSON, btnCaricaCSV;
    private JButton btnPulisciLibreria;
    private JButton btnInfo;
    private JButton btnUndo, btnRedo;

    /**
     * Costruttore che inizializza la vista.
     */
    public LibroView() {
        super("Gestione Libreria Personale");

        // Inizializza il controller
        controller = new LibroController(this);

        // Inizializza i componenti UI
        initUI();

        // Carica i libri iniziali
        controller.caricaLibri();
    }

    /**
     * Inizializza l'interfaccia utente.
     */
    private void initUI() {
        // Impostazioni base della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setMinimumSize(new Dimension(1150, 600));
        setLocationRelativeTo(null);

        // Layout principale
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Pannello superiore per ricerca e filtri con scrollbar
        JPanel panelSuperiore = creaPanelSuperiore();

        // Pannello centrale per la tabella dei libri
        JPanel panelCentrale = creaPanelCentrale();

        // Pannello inferiore per i pulsanti delle operazioni
        JPanel panelInferiore = creaPanelInferiore();

        add(panelSuperiore, BorderLayout.NORTH);
        add(panelCentrale, BorderLayout.CENTER);
        add(panelInferiore, BorderLayout.SOUTH);
    }

    private JPanel creaPanelSuperiore() {
        JPanel panelSuperiore = new JPanel(new BorderLayout(10, 10));

        // Pannello di ricerca
        JPanel panelRicerca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRicerca.setBorder(BorderFactory.createTitledBorder("Ricerca"));

        campoCerca = new JTextField(15);
        comboTipoCerca = new JComboBox<>(new String[]{"Titolo", "Autore", "ISBN"});
        btnCerca = new JButton("Cerca");
        btnCerca.addActionListener(e -> controller.cercaLibri(campoCerca.getText(), (String) comboTipoCerca.getSelectedItem()));

        panelRicerca.add(new JLabel("Cerca per:"));
        panelRicerca.add(comboTipoCerca);
        panelRicerca.add(campoCerca);
        panelRicerca.add(btnCerca);

        // Pannello dei filtri
        JPanel panelFiltri = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltri.setBorder(BorderFactory.createTitledBorder("Filtri"));

        comboGenere = new JComboBox<>();
        comboGenere.addItem("Tutti");
        comboGenere.addActionListener(e -> controller.aggiornaTabella());

        comboAutore = new JComboBox<>();
        comboAutore.addItem("Tutti");
        comboAutore.addActionListener(e -> controller.aggiornaTabella());

        comboStatoLettura = new JComboBox<>(new String[]{"Tutti", "Letto", "In lettura", "Da leggere"});
        comboStatoLettura.addActionListener(e -> controller.aggiornaTabella());

        comboValutazione = new JComboBox<>(new String[]{"Tutti", "Da valutare", "1", "2", "3", "4", "5"});
        comboValutazione.setSelectedIndex(0);
        comboValutazione.addActionListener(e -> controller.aggiornaTabella());

        btnResetFiltri = new JButton("Reset Filtri");
        btnResetFiltri.addActionListener(e -> resetFiltri());

        panelFiltri.add(new JLabel("Genere:"));
        panelFiltri.add(comboGenere);
        panelFiltri.add(new JLabel("Autore:"));
        panelFiltri.add(comboAutore);
        panelFiltri.add(new JLabel("Stato:"));
        panelFiltri.add(comboStatoLettura);
        panelFiltri.add(new JLabel("Valutazione:"));
        panelFiltri.add(comboValutazione);
        panelFiltri.add(btnResetFiltri);

        // Pannello di ordinamento
        JPanel panelOrdinamento = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOrdinamento.setBorder(BorderFactory.createTitledBorder("Ordinamento"));

        comboOrdinamento = new JComboBox<>(new String[]{
                "Titolo (A-Z)", "Titolo (Z-A)",
                "Autore (A-Z)", "Autore (Z-A)",
                "Valutazione (1-5)", "Valutazione (5-1)"
        });
        comboOrdinamento.addActionListener(e -> controller.aggiornaTabella());

        panelOrdinamento.add(new JLabel("Ordina per:"));
        panelOrdinamento.add(comboOrdinamento);

        // Pannello che contiene filtri e ordinamento con scrollbar
        JPanel panelFiltriOrdinamento = new JPanel();
        panelFiltriOrdinamento.setLayout(new BoxLayout(panelFiltriOrdinamento, BoxLayout.Y_AXIS));
        panelFiltriOrdinamento.add(panelFiltri);
        panelFiltriOrdinamento.add(panelOrdinamento);

        // Pannello superiore con scrollbar
        JScrollPane scrollPanelSuperiore = new JScrollPane(panelFiltriOrdinamento);
        scrollPanelSuperiore.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanelSuperiore.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanelSuperiore.setBorder(null);

        // Aggiungi i pannelli al pannello superiore
        panelSuperiore.add(panelRicerca, BorderLayout.NORTH);
        panelSuperiore.add(scrollPanelSuperiore, BorderLayout.CENTER);

        return panelSuperiore;
    }

    private JPanel creaPanelCentrale() {
        JPanel panelCentrale = new JPanel(new BorderLayout());

        // Crea la tabella con il modello dati
        String[] colonne = {"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Stato Lettura"};
        modelloTabella = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Non permette la modifica diretta nella tabella
            }
        };

        tabellaLibri = new JTable(modelloTabella);
        tabellaLibri.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaLibri.getTableHeader().setReorderingAllowed(false);

        // Imposta le dimensioni preferite delle colonne
        tabellaLibri.getColumnModel().getColumn(0).setPreferredWidth(200); // Titolo
        tabellaLibri.getColumnModel().getColumn(1).setPreferredWidth(150); // Autore
        tabellaLibri.getColumnModel().getColumn(2).setPreferredWidth(100); // ISBN
        tabellaLibri.getColumnModel().getColumn(3).setPreferredWidth(100); // Genere
        tabellaLibri.getColumnModel().getColumn(4).setPreferredWidth(80);  // Valutazione
        tabellaLibri.getColumnModel().getColumn(5).setPreferredWidth(100); // Stato Lettura

        JScrollPane scrollPane = new JScrollPane(tabellaLibri);
        panelCentrale.add(scrollPane, BorderLayout.CENTER);

        return panelCentrale;
    }

    private JPanel creaPanelInferiore() {
        JPanel panelInferiore = new JPanel(new BorderLayout());

        // Pulsanti per le operazioni sui libri
        JPanel panelOperazioni = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));

        btnAggiungi = new JButton("Aggiungi Libro");
        btnAggiungi.addActionListener(e -> mostraDialogAggiungiLibro());

        btnModifica = new JButton("Modifica Libro");
        btnModifica.addActionListener(e -> mostraDialogModificaLibro());

        btnElimina = new JButton("Elimina Libro");
        btnElimina.addActionListener(e -> eliminaLibroSelezionato());

        btnUndo = new JButton("Undo");
        btnUndo.setToolTipText("Annulla l'ultima operazione");
        btnUndo.addActionListener(e -> controller.undo());
        btnUndo.setEnabled(false);

        btnRedo = new JButton("Redo");
        btnRedo.setToolTipText("Ripristina l'ultima operazione annullata");
        btnRedo.addActionListener(e -> controller.redo());
        btnRedo.setEnabled(false);

        panelOperazioni.add(btnAggiungi);
        panelOperazioni.add(btnModifica);
        panelOperazioni.add(btnElimina);
        panelOperazioni.add(btnUndo);
        panelOperazioni.add(btnRedo);

        // Pulsante info
        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        btnInfo = new JButton("Info");
        btnInfo.addActionListener(e -> mostraInfoDialog());
        panelCentro.add(btnInfo);

        // Pulsanti per il salvataggio/caricamento
        JPanel panelPersistenza = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));

        btnSalvaJSON = new JButton("Salva JSON");
        btnSalvaJSON.addActionListener(e -> salvaFile("JSON"));

        btnSalvaCSV = new JButton("Salva CSV");
        btnSalvaCSV.addActionListener(e -> salvaFile("CSV"));

        btnCaricaJSON = new JButton("Carica JSON");
        btnCaricaJSON.addActionListener(e -> caricaFile("JSON"));

        btnCaricaCSV = new JButton("Carica CSV");
        btnCaricaCSV.addActionListener(e -> caricaFile("CSV"));

        //Pulsante per pulire la libreria
        btnPulisciLibreria = new JButton("Pulisci Libreria");
        btnPulisciLibreria.addActionListener(e -> pulisciLibreria());
        panelPersistenza.add(btnSalvaJSON);
        panelPersistenza.add(btnSalvaCSV);
        panelPersistenza.add(btnCaricaJSON);
        panelPersistenza.add(btnCaricaCSV);
        panelPersistenza.add(btnPulisciLibreria);

        // Contenitore per i pulsanti
        JPanel bottonePulsantiContainer = new JPanel();
        bottonePulsantiContainer.setLayout(new BoxLayout(bottonePulsantiContainer, BoxLayout.X_AXIS));
        bottonePulsantiContainer.add(panelOperazioni);
        bottonePulsantiContainer.add(Box.createHorizontalGlue());
        bottonePulsantiContainer.add(panelCentro);
        bottonePulsantiContainer.add(Box.createHorizontalGlue());
        bottonePulsantiContainer.add(panelPersistenza);
        bottonePulsantiContainer.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        // Scroll panel per i pulsanti
        JScrollPane scrollPanelInferiore = new JScrollPane(bottonePulsantiContainer,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanelInferiore.setBorder(null);
        scrollPanelInferiore.getHorizontalScrollBar().setUnitIncrement(15);

        panelInferiore.add(scrollPanelInferiore, BorderLayout.CENTER);

        return panelInferiore;
    }

    /**
     * Aggiorna lo stato dei pulsanti Undo e Redo in base alla disponibilità delle operazioni.
     *
     * @param canUndo true se l'operazione di undo è disponibile
     * @param canRedo true se l'operazione di redo è disponibile
     * @param undoDescription descrizione dell'operazione di undo
     * @param redoDescription descrizione dell'operazione di redo
     */
    public void aggiornaStatoPulsantiUndoRedo(boolean canUndo, boolean canRedo,
                                              String undoDescription, String redoDescription) {
        btnUndo.setEnabled(canUndo);
        btnRedo.setEnabled(canRedo);

        // Aggiorna i tooltip con la descrizione delle azioni
        if (canUndo) {
            btnUndo.setToolTipText("Annulla: " + undoDescription);
        } else {
            btnUndo.setToolTipText("Nessuna operazione da annullare");
        }

        if (canRedo) {
            btnRedo.setToolTipText("Ripristina: " + redoDescription);
        } else {
            btnRedo.setToolTipText("Nessuna operazione da ripristinare");
        }
    }

    /**
     * Mostra la finestra di dialogo delle informazioni sull'applicazione.
     */
    private void mostraInfoDialog() {
        String infoText = "Nome: Gestione Libreria Personale\n\n" +
                "Versione: v1.0\n\n" +
                "Descrizione:\n" +
                "- Aggiungi, modifica e elimina i tuoi libri\n" +
                "- Cerca libri per titolo, autore o ISBN\n" +
                "- Filtra per genere, autore, stato di lettura, valutazione\n" +
                "- Ordina i libri secondo diversi criteri\n" +
                "- Funzionalità Undo/Redo per annullare o ripristinare le operazioni\n" +
                "- Salva e carica la tua libreria in formato JSON o CSV\n\n" +
                "Autore: Progetto Demo Java";

        JOptionPane.showMessageDialog(this,
                infoText,
                "Informazioni sull'applicazione",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Aggiorna la tabella con i libri forniti.
     *
     * @param libri Lista di libri da visualizzare
     */
    public void aggiornaTabella(List<Libro> libri) {
        // Pulisci la tabella
        modelloTabella.setRowCount(0);

        // Aggiungi le righe per ogni libro
        for (Libro libro : libri) {
            Vector<Object> riga = new Vector<>();
            riga.add(libro.getTitolo());
            riga.add(libro.getAutore());
            riga.add(libro.getIsbn());
            riga.add(libro.getGenere());

            // Visualizza la valutazione come stringa ("da valutare" o stelle)
            riga.add(libro.getValutazioneAsString());

            riga.add(libro.getStatoLetturaAsString());

            modelloTabella.addRow(riga);
        }
    }

    /**
     * Aggiorna la combo box dei generi con i generi unici disponibili.
     *
     * @param generi Lista di generi unici
     */
    public void aggiornaComboBoxGeneri(List<String> generi) {
        String genereSelezionato = (String) comboGenere.getSelectedItem();

        comboGenere.removeAllItems();
        comboGenere.addItem("Tutti");

        for (String genere : generi) {
            comboGenere.addItem(genere);
        }

        // Ripristina la selezione precedente se possibile
        if (genereSelezionato != null) {
            comboGenere.setSelectedItem(genereSelezionato);
        } else {
            comboGenere.setSelectedIndex(0);
        }
    }

    /**
     * Aggiorna la combo box degli autori con gli autori unici disponibili.
     *
     * @param autori Lista di autori unici
     */
    public void aggiornaComboBoxAutori(List<String> autori) {
        String autoreSelezionato = (String) comboAutore.getSelectedItem();

        comboAutore.removeAllItems();
        comboAutore.addItem("Tutti");

        for (String autore : autori) {
            comboAutore.addItem(autore);
        }

        // Ripristina la selezione precedente se possibile
        if (autoreSelezionato != null) {
            comboAutore.setSelectedItem(autoreSelezionato);
        } else {
            comboAutore.setSelectedIndex(0);
        }
    }

    /**
     * Mostra la finestra di dialogo per aggiungere un nuovo libro.
     */
    private void mostraDialogAggiungiLibro() {
        DialogAggiungiModificaLibro dialog = new DialogAggiungiModificaLibro(this, null);
        dialog.setVisible(true);

        if (dialog.isConfermato()) {
            String titolo = dialog.getTitolo();
            String autore = dialog.getAutore();
            String isbn = dialog.getIsbn();
            String genere = dialog.getGenere();
            int valutazione = dialog.getValutazione();
            StatoLettura statoLettura = dialog.getStatoLettura();

            boolean successo = controller.aggiungiLibro(titolo, autore, isbn, genere, valutazione, statoLettura);

            if (successo) {
                JOptionPane.showMessageDialog(this, "Libro aggiunto con successo!", "Operazione completata",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile aggiungere il libro. L'ISBN potrebbe essere già esistente.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Mostra la finestra di dialogo per modificare un libro esistente.
     */
    private void mostraDialogModificaLibro() {
        Libro libroSelezionato = getLibroSelezionato();

        if (libroSelezionato == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da modificare.",
                    "Nessun libro selezionato", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DialogAggiungiModificaLibro dialog = new DialogAggiungiModificaLibro(this, libroSelezionato);
        dialog.setVisible(true);

        if (dialog.isConfermato()) {
            String titolo = dialog.getTitolo();
            String autore = dialog.getAutore();
            String isbn = dialog.getIsbn();
            String genere = dialog.getGenere();
            int valutazione = dialog.getValutazione();
            StatoLettura statoLettura = dialog.getStatoLettura();

            boolean successo = controller.modificaLibro(libroSelezionato, titolo, autore, isbn, genere, valutazione, statoLettura);

            if (successo) {
                JOptionPane.showMessageDialog(this, "Libro modificato con successo!", "Operazione completata",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile modificare il libro.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Elimina il libro selezionato dalla tabella.
     */
    private void eliminaLibroSelezionato() {
        Libro libroSelezionato = getLibroSelezionato();

        if (libroSelezionato == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da eliminare.",
                    "Nessun libro selezionato", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int conferma = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare il libro \"" + libroSelezionato.getTitolo() + "\"?",
                "Conferma eliminazione",
                JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) {
            boolean successo = controller.eliminaLibro(libroSelezionato);

            if (successo) {
                JOptionPane.showMessageDialog(this, "Libro eliminato con successo!", "Operazione completata",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile eliminare il libro.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Ottiene il libro selezionato nella tabella.
     *
     * @return Libro selezionato o null se nessun libro è selezionato
     */
    private Libro getLibroSelezionato() {
        int rigaSelezionata = tabellaLibri.getSelectedRow();

        if (rigaSelezionata == -1) {
            return null;
        }

        String titolo = (String) tabellaLibri.getValueAt(rigaSelezionata, 0);
        String autore = (String) tabellaLibri.getValueAt(rigaSelezionata, 1);
        String isbn = (String) tabellaLibri.getValueAt(rigaSelezionata, 2);
        String genere = (String) tabellaLibri.getValueAt(rigaSelezionata, 3);

        // Gestione della valutazione come stringa
        String valutazioneString = (String) tabellaLibri.getValueAt(rigaSelezionata, 4);
        int valutazione;
        if ("Da valutare".equalsIgnoreCase(valutazioneString)) {
            valutazione = 0;
        } else {
            try {
                valutazione = Integer.parseInt(valutazioneString);
            } catch (NumberFormatException e) {
                valutazione = 0; // Default a "da valutare" in caso di errore
            }
        }

        String statoLetturaString = (String) tabellaLibri.getValueAt(rigaSelezionata, 5);

        StatoLettura statoLettura;
        try {
            statoLettura = StatoLettura.fromString(statoLetturaString);
        } catch (IllegalArgumentException e) {
            statoLettura = StatoLettura.DA_LEGGERE;
        }

        return new Libro(titolo, autore, isbn, genere, valutazione, statoLettura);
    }

    /**
     * Resetta tutti i filtri ai valori predefiniti.
     */
    private void resetFiltri() {
        campoCerca.setText("");
        comboTipoCerca.setSelectedIndex(0);
        comboGenere.setSelectedIndex(0);
        comboAutore.setSelectedIndex(0);
        comboStatoLettura.setSelectedIndex(0);
        comboValutazione.setSelectedIndex(0);
        comboOrdinamento.setSelectedIndex(0);

        controller.caricaLibri();
    }

    /**
     * Mostra un dialogo per salvare la libreria in un file.
     *
     * @param formato Formato del file (JSON o CSV)
     */
    private void salvaFile(String formato) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salva libreria in " + formato);

        String estensione = formato.toLowerCase();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(
                formato.toUpperCase() + " Files (*." + estensione + ")", estensione);
        fileChooser.setFileFilter(filtro);

        int risultato = fileChooser.showSaveDialog(this);

        if (risultato == JFileChooser.APPROVE_OPTION) {
            File fileSelezionato = fileChooser.getSelectedFile();
            String percorso = fileSelezionato.getAbsolutePath();

            // Aggiungi l'estensione se mancante
            if (!percorso.toLowerCase().endsWith("." + estensione)) {
                percorso += "." + estensione;
            }

            controller.salvaLibreria(percorso, formato);
        }
    }

    /**
     * Mostra un dialogo per caricare la libreria da un file.
     *
     * @param formato Formato del file (JSON o CSV)
     */
    private void caricaFile(String formato) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Carica libreria da " + formato);

        String estensione = formato.toLowerCase();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(
                formato.toUpperCase() + " Files (*." + estensione + ")", estensione);
        fileChooser.setFileFilter(filtro);

        int risultato = fileChooser.showOpenDialog(this);

        if (risultato == JFileChooser.APPROVE_OPTION) {
            File fileSelezionato = fileChooser.getSelectedFile();
            controller.caricaLibreria(fileSelezionato.getAbsolutePath(), formato);
        }
    }

    /**
     * Ottiene il genere selezionato nella combo box.
     *
     * @return Genere selezionato o "Tutti" se nessuno è selezionato
     */
    public String getGenereSelezionato() {
        return (String) comboGenere.getSelectedItem();
    }

    /**
     * Ottiene l'autore selezionato nella combo box.
     *
     * @return Autore selezionato o "Tutti" se nessuno è selezionato
     */
    public String getAutoreSelezionato() {
        return (String) comboAutore.getSelectedItem();
    }

    /**
     * Ottiene lo stato di lettura selezionato nella combo box.
     *
     * @return Stato di lettura selezionato o "Tutti" se nessuno è selezionato
     */
    public String getStatoLetturaSelezionato() {
        return (String) comboStatoLettura.getSelectedItem();
    }

    /**
     * Ottiene la valutazione selezionata nella combo box.
     *
     * @return Valutazione selezionata o -1 se "Tutti" è selezionato (nessun filtro),
     * 0 se "da valutare" è selezionato, altrimenti il valore numerico (1-5)
     */
    public int getValutazioneSelezionata() {
        String valutazione = (String) comboValutazione.getSelectedItem();
        if (valutazione == null || "Tutti".equals(valutazione)) {
            return -1; // Nessun filtro
        } else if ("Da valutare".equals(valutazione)) {
            return 0; // Filtra per "da valutare"
        } else {
            try {
                return Integer.parseInt(valutazione);
            } catch (NumberFormatException e) {
                return -1; // In caso di errore, non applica filtro
            }
        }
    }

    /**
     * Ottiene l'ordinamento selezionato nella combo box.
     *
     * @return Ordinamento selezionato
     */
    public String getOrdinamentoSelezionato() {
        return (String) comboOrdinamento.getSelectedItem();
    }

    /**
     * Pulisce la libreria dopo conferma dell'utente.
     * Rimuove tutti i libri dalla libreria.
     */
    private void pulisciLibreria() {
        int conferma = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler pulire la libreria? Tutti i libri saranno rimossi.\n" +
                        "Questa operazione non può essere annullata e cancellerà anche la cronologia di Undo/Redo.",
                "Conferma pulizia libreria",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (conferma == JOptionPane.YES_OPTION) {
            controller.pulisciLibreria();
            JOptionPane.showMessageDialog(this,
                    "La libreria è stata pulita con successo.",
                    "Operazione completata",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
