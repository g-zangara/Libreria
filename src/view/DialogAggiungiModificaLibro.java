package view;

import model.Libro;
import model.StatoLettura;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Finestra di dialogo per aggiungere o modificare un libro.
 */
public class DialogAggiungiModificaLibro extends JDialog {

    private JTextField campoTitolo;
    private JTextField campoAutore;
    private JTextField campoIsbn;
    private JTextField campoGenere;
    private JComboBox<String> comboValutazione;
    private JComboBox<String> comboStatoLettura;

    private boolean confermato = false;

    /**
     * Costruttore per la finestra di dialogo.
     *
     * @param parent Frame genitore
     * @param libro Libro da modificare, o null se si sta aggiungendo un nuovo libro
     */
    public DialogAggiungiModificaLibro(JFrame parent, Libro libro) {
        super(parent, libro == null ? "Aggiungi Libro" : "Modifica Libro", true);

        initUI(libro);
    }

    /**
     * Inizializza l'interfaccia utente del dialogo.
     *
     * @param libro Libro da modificare, o null se si sta aggiungendo un nuovo libro
     */
    private void initUI(Libro libro) {
        // Impostazioni base della finestra
        setSize(450, 350);
        setLocationRelativeTo(getParent());
        setResizable(false);

        // Layout principale
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // Pannello per i campi di input
        JPanel panelCampi = new JPanel(new GridLayout(6, 2, 10, 10));

        // Campi di input
        campoTitolo = new JTextField();
        campoAutore = new JTextField();
        campoIsbn = new JTextField();
        campoGenere = new JTextField();

        // Valutazione con "da valutare" come opzione
        comboValutazione = new JComboBox<>(new String[]{"Da valutare", "1", "2", "3", "4", "5"});

        comboStatoLettura = new JComboBox<>();
        for (StatoLettura stato : StatoLettura.values()) {
            comboStatoLettura.addItem(stato.getDescrizione());
        }

        // Aggiungi componenti al pannello
        panelCampi.add(new JLabel("Titolo:"));
        panelCampi.add(campoTitolo);
        panelCampi.add(new JLabel("Autore:"));
        panelCampi.add(campoAutore);
        panelCampi.add(new JLabel("ISBN:"));
        panelCampi.add(campoIsbn);
        panelCampi.add(new JLabel("Genere:"));
        panelCampi.add(campoGenere);
        panelCampi.add(new JLabel("Valutazione:"));
        panelCampi.add(comboValutazione);
        panelCampi.add(new JLabel("Stato di lettura:"));
        panelCampi.add(comboStatoLettura);

        // Pannello per i pulsanti
        JPanel panelPulsanti = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnAnnulla = new JButton("Annulla");
        btnAnnulla.addActionListener(e -> dispose());

        JButton btnConferma = new JButton(libro == null ? "Aggiungi" : "Modifica");
        btnConferma.addActionListener(e -> {
            if (validaCampi()) {
                confermato = true;
                dispose();
            }
        });

        panelPulsanti.add(btnAnnulla);
        panelPulsanti.add(btnConferma);

        // Aggiungi i pannelli al layout principale
        contentPane.add(panelCampi, BorderLayout.CENTER);
        contentPane.add(panelPulsanti, BorderLayout.SOUTH);

        // Se stiamo modificando un libro, popola i campi con i suoi dati
        if (libro != null) {
            campoTitolo.setText(libro.getTitolo());
            campoAutore.setText(libro.getAutore());
            campoIsbn.setText(libro.getIsbn());
            campoGenere.setText(libro.getGenere());

            // Imposta la valutazione corretta
            if (libro.getValutazione() == 0) {
                comboValutazione.setSelectedItem("Da valutare");
            } else {
                comboValutazione.setSelectedItem(String.valueOf(libro.getValutazione()));
            }

            comboStatoLettura.setSelectedItem(libro.getStatoLetturaAsString());
        }
    }

    /**
     * Valida i campi di input.
     *
     * @return true se tutti i campi sono validi, false altrimenti
     */
    private boolean validaCampi() {
        // Validazione titolo
        if (campoTitolo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Il titolo non può essere vuoto.",
                    "Errore di validazione",
                    JOptionPane.ERROR_MESSAGE);
            campoTitolo.requestFocus();
            return false;
        }

        // Validazione autore
        if (campoAutore.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "L'autore non può essere vuoto.",
                    "Errore di validazione",
                    JOptionPane.ERROR_MESSAGE);
            campoAutore.requestFocus();
            return false;
        }

        // Validazione ISBN
        String isbn = campoIsbn.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "L'ISBN non può essere vuoto.",
                    "Errore di validazione",
                    JOptionPane.ERROR_MESSAGE);
            campoIsbn.requestFocus();
            return false;
        }

        if (!Pattern.matches("^[0-9\\-]+$", isbn)) {
            JOptionPane.showMessageDialog(this,
                    "L'ISBN deve contenere solo numeri e trattini.",
                    "Errore di validazione",
                    JOptionPane.ERROR_MESSAGE);
            campoIsbn.requestFocus();
            return false;
        }

        // Validazione genere
        if (campoGenere.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Il genere non può essere vuoto.",
                    "Errore di validazione",
                    JOptionPane.ERROR_MESSAGE);
            campoGenere.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Verifica se l'utente ha confermato l'operazione.
     *
     * @return true se l'utente ha confermato, false altrimenti
     */
    public boolean isConfermato() {
        return confermato;
    }

    /**
     * Ottiene il titolo inserito.
     *
     * @return Titolo del libro
     */
    public String getTitolo() {
        return campoTitolo.getText().trim();
    }

    /**
     * Ottiene l'autore inserito.
     *
     * @return Autore del libro
     */
    public String getAutore() {
        return campoAutore.getText().trim();
    }

    /**
     * Ottiene l'ISBN inserito.
     *
     * @return ISBN del libro
     */
    public String getIsbn() {
        return campoIsbn.getText().trim();
    }

    /**
     * Ottiene il genere inserito.
     *
     * @return Genere del libro
     */
    public String getGenere() {
        return campoGenere.getText().trim();
    }

    /**
     * Ottiene la valutazione selezionata.
     *
     * @return Valutazione del libro (0 = da valutare, 1-5 = stelle)
     */
    public int getValutazione() {
        String val = (String) comboValutazione.getSelectedItem();
        if ("Da valutare".equals(val)) {
            return 0;
        } else {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                return 0; // Default a "da valutare" in caso di errore
            }
        }
    }

    /**
     * Ottiene lo stato di lettura selezionato.
     *
     * @return Stato di lettura del libro
     */
    public StatoLettura getStatoLettura() {
        String statoString = (String) comboStatoLettura.getSelectedItem();
        return StatoLettura.fromString(statoString);
    }
}
