package dao;

import model.Libro;
import model.StatoLettura;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'interfaccia LibroDAO per la gestione dei libri in formato CSV.
 */
public class CsvLibroDAO implements LibroDAO {

    // Costanti
    private static final String SEPARATOR = ",";
    private static final String QUOTE = "\"";
    private static final String DOUBLE_QUOTE = "\"\"";
    private static final String HEADER = "titolo,autore,isbn,genere,valutazione,statoLettura";

    /**
     * Salva una lista di libri in formato CSV.
     *
     * @param libri Lista di libri da salvare
     * @param percorsoFile Percorso del file CSV in cui salvare i dati
     * @throws IOException In caso di errori durante la scrittura del file
     */
    @Override
    public void salvaLibri(List<Libro> libri, String percorsoFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(percorsoFile))) {
            // Scrive l'intestazione
            writer.write(HEADER);
            writer.newLine();

            // Scrive i dati di ogni libro
            for (Libro libro : libri) {
                StringBuilder sb = new StringBuilder();

                // Aggiunge i campi con escape se necessario
                sb.append(escapeCsv(libro.getTitolo())).append(SEPARATOR);
                sb.append(escapeCsv(libro.getAutore())).append(SEPARATOR);
                sb.append(escapeCsv(libro.getIsbn())).append(SEPARATOR);
                sb.append(escapeCsv(libro.getGenere())).append(SEPARATOR);

                // Salva la valutazione (0 = "da valutare" o numero)
                sb.append(escapeCsv(libro.getValutazioneAsString())).append(SEPARATOR);

                sb.append(libro.getStatoLettura().name());

                writer.write(sb.toString());
                writer.newLine();
            }
        }
    }

    /**
     * Carica una lista di libri da un file CSV.
     * Se anche un solo libro non è valido, l'intera operazione fallisce.
     *
     * @param percorsoFile Percorso del file CSV da cui caricare i dati
     * @return Lista di libri caricati dal file
     * @throws IOException In caso di errori durante la lettura del file o se ci sono libri non validi
     */
    @Override
    public List<Libro> caricaLibri(String percorsoFile) throws IOException {
        List<Libro> libri = new ArrayList<>();
        File file = new File(percorsoFile);

        // Verifica che il file abbia solo una estensione e che sia .csv
        String nomeFile = file.getName();
        int ultimoPunto = nomeFile.lastIndexOf('.');
        if (ultimoPunto == -1 || !nomeFile.substring(ultimoPunto + 1).equalsIgnoreCase("csv") ||
                nomeFile.substring(0, ultimoPunto).contains(".")) {
            System.err.println("Formato file non valido: " + percorsoFile);
            throw new IOException("Formato file non valido.\n Il file deve avere solo l'estensione .csv senza estensioni multiple.");
        }

        // Se il file non esiste, restituisce una lista vuota
        if (!file.exists()) {
            System.err.println("File non trovato: " + percorsoFile);
            throw new IOException("File non trovato:\n" + percorsoFile);
        }

        List<String> errori = new ArrayList<>();
        int numeroRiga = 1; // Inizia da 1 per l'intestazione

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Salta l'intestazione
            String line = reader.readLine();
            numeroRiga++;

            // Legge le righe di dati
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Libro libro = parseLibroFromCsv(line);
                    if (libro == null) {
                        // Libro con formato CSV non valido
                        errori.add("Riga " + numeroRiga + ": formato CSV non valido");
                    } else if (!libro.isValid()) {
                        // Libro con dati incompleti o invalidi
                        errori.add("Riga " + numeroRiga + " (" +
                                (libro.getTitolo().isEmpty() ? "titolo mancante" : libro.getTitolo()) +
                                "): dati incompleti o non validi");
                    } else {
                        libri.add(libro);
                    }
                }
                numeroRiga++;
            }
        }

        // Se ci sono errori, interrompi il caricamento e segnala
        if (!errori.isEmpty()) {
            String messaggioErrore = "Impossibile caricare il file. Sono stati trovati libri non validi:" +
                    "\n" + String.join("\n", errori);
            throw new IOException(messaggioErrore);
        }

        return libri;
    }

    /**
     * Converte una riga CSV in un oggetto Libro.
     * Include validazione degli input durante il parsing.
     *
     * @param csvLine Riga CSV da convertire
     * @return Oggetto Libro costruito dai dati CSV, o null in caso di errore
     */
    private Libro parseLibroFromCsv(String csvLine) {
        // Divide la riga in campi tenendo conto delle virgolette
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;

        char[] chars = csvLine.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (c == '"') {
                // Se è un doppio apice che fa parte del contenuto
                if (inQuotes && i + 1 < chars.length && chars[i + 1] == '"') {
                    field.append('"');
                    i++; // Salta il secondo apice
                } else {
                    // Altrimenti è un delimitatore di campo con apici
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // Separatore di campo (solo se non siamo tra virgolette)
                fields.add(field.toString());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }

        // Aggiungi l'ultimo campo
        fields.add(field.toString());

        // Verifica che ci siano tutti i campi necessari
        if (fields.size() != 6) {
            System.err.println("Formato CSV non valido. Numero di campi errato: " + fields.size());
            return null;
        }

        // Estrae i valori dai campi
        String titolo = fields.get(0);
        String autore = fields.get(1);
        String isbn = fields.get(2);
        String genere = fields.get(3);

        // Gestione della valutazione, inclusa l'opzione "da valutare"
        int valutazione;
        String valutazioneStr = fields.get(4).trim();
        try {
            if (valutazioneStr.equalsIgnoreCase("Da valutare") || valutazioneStr.equals("0")) {
                valutazione = 0; // "da valutare"
            } else {
                valutazione = Integer.parseInt(valutazioneStr);
                // Validazione rigorosa del range
                if (valutazione < 0 || valutazione > 5) {
                    System.err.println("La valutazione deve essere tra 0 e 5, trovato: " + valutazione);
                    return null;
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("La valutazione deve essere un numero intero tra 0 e 5 o 'Da valutare', trovato: " + valutazioneStr);
            return null;
        }

        // Gestione dello stato di lettura con validazione rigorosa
        StatoLettura statoLettura;
        String statoLetturaStr = fields.get(5).trim();
        try {
            statoLettura = StatoLettura.valueOf(statoLetturaStr);
        } catch (IllegalArgumentException e) {
            try {
                // Prova a convertire usando la descrizione
                statoLettura = StatoLettura.fromString(statoLetturaStr);
            } catch (IllegalArgumentException ex) {
                System.err.println("Stato di lettura non valido: " + statoLetturaStr);
                return null;
            }
        }

        // Crea il libro
        Libro libro = new Libro(titolo, autore, isbn, genere, valutazione, statoLettura);

        // Verifica che tutti i campi obbligatori siano validi
        if (!libro.isValid()) {
            System.err.println("CSV libro non valido: " + csvLine);
            return null;
        }

        return libro;
    }

    /**
     * Aggiunge escape characters per campi CSV (racchiude tra virgolette e gestisce le virgolette interne).
     *
     * @param field Campo da escapare
     * @return Campo escapato per CSV
     */
    private String escapeCsv(String field) {
        if (field == null) {
            return "";
        }

        // Se il campo contiene virgole, virgolette o newline, deve essere racchiuso tra virgolette
        boolean needQuotes = field.contains(SEPARATOR) || field.contains(QUOTE) ||
                field.contains("\n") || field.contains("\r");

        if (needQuotes) {
            // Gestisce le virgolette interne raddoppiandole
            String escapedField = field.replace(QUOTE, DOUBLE_QUOTE);
            return QUOTE + escapedField + QUOTE;
        } else {
            return field;
        }
    }
}
