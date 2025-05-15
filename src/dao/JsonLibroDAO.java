package dao;

import model.Libro;
import model.StatoLettura;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'interfaccia LibroDAO per la gestione dei libri in formato JSON.
 * Utilizza la codifica manuale del JSON per evitare dipendenze esterne.
 */
public class JsonLibroDAO implements LibroDAO {

    /**
     * Salva una lista di libri in formato JSON.
     *
     * @param libri Lista di libri da salvare
     * @param percorsoFile Percorso del file JSON in cui salvare i dati
     * @throws IOException In caso di errori durante la scrittura del file
     */
    @Override
    public void salvaLibri(List<Libro> libri, String percorsoFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(percorsoFile))) {
            writer.write("[\n");

            for (int i = 0; i < libri.size(); i++) {
                Libro libro = libri.get(i);
                writer.write("  {\n");
                writer.write("    \"titolo\": \"" + escapeJson(libro.getTitolo()) + "\",\n");
                writer.write("    \"autore\": \"" + escapeJson(libro.getAutore()) + "\",\n");
                writer.write("    \"isbn\": \"" + escapeJson(libro.getIsbn()) + "\",\n");
                writer.write("    \"genere\": \"" + escapeJson(libro.getGenere()) + "\",\n");

                // Valutazione come numero, per retrocompatibilità
                writer.write("    \"valutazione\": " + libro.getValutazione() + ",\n");

                writer.write("    \"statoLettura\": \"" + libro.getStatoLettura().name() + "\"\n");
                writer.write("  }");

                // Aggiungi virgola se non è l'ultimo elemento
                if (i < libri.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }

            writer.write("]");
        }
    }

    /**
     * Carica una lista di libri da un file JSON.
     * Se anche un solo libro non è valido, l'intera operazione fallisce.
     *
     * @param percorsoFile Percorso del file JSON da cui caricare i dati
     * @return Lista di libri caricati dal file
     * @throws IOException In caso di errori durante la lettura del file o se ci sono libri non validi
     */
    @Override
    public List<Libro> caricaLibri(String percorsoFile) throws IOException {
        List<Libro> libri = new ArrayList<>();
        File file = new File(percorsoFile);

        // Verifica che il file abbia solo una estensione e che sia .json
        String nomeFile = file.getName();
        int ultimoPunto = nomeFile.lastIndexOf('.');
        if (ultimoPunto == -1 || !nomeFile.substring(ultimoPunto + 1).equalsIgnoreCase("json") ||
                nomeFile.substring(0, ultimoPunto).contains(".")) {
            System.err.println("Formato file non valido: " + percorsoFile);
            throw new IOException("Formato file non valido.\n Il file deve avere solo l'estensione .json senza estensioni multiple.");
        }

        // Se il file non esiste, restituisce una lista vuota
        if (!file.exists()) {
            return libri;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line).append("\n");
            }

            // Parsing manuale del JSON
            String content = jsonContent.toString().trim();
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1).trim();

                // Suddivide gli oggetti JSON
                List<String> jsonObjects = splitJsonObjects(content);

                List<String> errori = new ArrayList<>();
                int indice = 0;

                for (String jsonObject : jsonObjects) {
                    indice++;
                    Libro libro = parseJsonLibro(jsonObject);
                    if (libro == null) {
                        // Libro non valido
                        errori.add("Libro #" + indice + ": formato JSON non valido");
                    } else if (!libro.isValid()) {
                        // Libro con dati incompleti o invalidi
                        errori.add("Libro #" + indice + " (" +
                                (libro.getTitolo().isEmpty() ? "titolo mancante" : libro.getTitolo()) +
                                "): dati incompleti o non validi");
                    } else {
                        libri.add(libro);
                    }
                }

                // Se ci sono errori, interrompi il caricamento e segnala
                if (!errori.isEmpty()) {
                    String messaggioErrore = "Impossibile caricare il file. Sono stati trovati libri non validi:" +
                            "\n" + String.join("\n", errori);
                    throw new IOException(messaggioErrore);
                }
            }
        }

        return libri;
    }

    /**
     * Suddivide una stringa contenente oggetti JSON in una lista di stringhe,
     * ciascuna rappresentante un singolo oggetto JSON.
     *
     * @param jsonContent Stringa contenente più oggetti JSON
     * @return Lista di stringhe, ciascuna rappresentante un oggetto JSON
     */
    private List<String> splitJsonObjects(String jsonContent) {
        List<String> objects = new ArrayList<>();
        int nesting = 0;
        StringBuilder currentObject = new StringBuilder();

        for (char c : jsonContent.toCharArray()) {
            if (c == '{') {
                nesting++;
            } else if (c == '}') {
                nesting--;
            }

            currentObject.append(c);

            if (nesting == 0 && !currentObject.toString().trim().isEmpty()) {
                // Rimozione di eventuali virgole finali
                String obj = currentObject.toString().trim();
                if (obj.endsWith(",")) {
                    obj = obj.substring(0, obj.length() - 1);
                }
                if (!obj.isEmpty()) {
                    objects.add(obj);
                }
                currentObject = new StringBuilder();
            }
        }

        return objects;
    }

    /**
     * Converte un oggetto JSON (come stringa) in un oggetto Libro.
     * Include validazione degli input durante il parsing.
     *
     * @param jsonObject Stringa rappresentante un oggetto JSON
     * @return Oggetto Libro costruito dai dati JSON, o null in caso di errore
     */
    private Libro parseJsonLibro(String jsonObject) {
        jsonObject = jsonObject.trim();
        if (!jsonObject.startsWith("{") || !jsonObject.endsWith("}")) {
            return null;
        }

        // Rimuove le parentesi graffe
        jsonObject = jsonObject.substring(1, jsonObject.length() - 1).trim();

        String titolo = "";
        String autore = "";
        String isbn = "";
        String genere = "";
        int valutazione = 0; // Default a "da valutare"
        StatoLettura statoLettura = StatoLettura.DA_LEGGERE; // Default a "da leggere"

        // Suddivide le coppie chiave-valore
        String[] pairs = jsonObject.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim();

                // Rimuove virgolette per stringhe
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                switch (key) {
                    case "titolo":
                        titolo = value;
                        break;
                    case "autore":
                        autore = value;
                        break;
                    case "isbn":
                        isbn = value;
                        break;
                    case "genere":
                        genere = value;
                        break;
                    case "valutazione":
                        try {
                            if (value.equalsIgnoreCase("Da valutare") || value.equals("0")) {
                                valutazione = 0;
                            } else {
                                valutazione = Integer.parseInt(value);
                                // Validazione rigorosa del range
                                if (valutazione < 0 || valutazione > 5) {
                                    System.err.println("La valutazione deve essere tra 0 e 5, trovato: " + valutazione);
                                    return null;
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("La valutazione deve essere un numero intero tra 0 e 5 o 'Da valutare', trovato: " + value);
                            return null;
                        }
                        break;
                    case "statoLettura":
                        try {
                            statoLettura = StatoLettura.valueOf(value);
                        } catch (IllegalArgumentException e) {
                            // Prova a convertire usando la descrizione
                            try {
                                statoLettura = StatoLettura.fromString(value);
                            } catch (IllegalArgumentException ex) {
                                System.err.println("Stato di lettura non valido: " + value);
                                return null;
                            }
                        }
                        break;
                }
            }
        }

        // Crea il libro
        Libro libro = new Libro(titolo, autore, isbn, genere, valutazione, statoLettura);

        // Verifica che tutti i campi obbligatori siano validi
        if (!libro.isValid()) {
            System.err.println("JSON libro non valido: " + jsonObject);
            return null;
        }

        return libro;
    }

    /**
     * Aggiunge escape characters per caratteri speciali nel JSON.
     *
     * @param text Testo da escapare
     * @return Testo escapato per JSON
     */
    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
