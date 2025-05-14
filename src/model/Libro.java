package model;

import java.util.Objects;

/**
 * Classe che rappresenta un libro nella libreria personale.
 * Contiene tutti gli attributi richiesti: titolo, autore, ISBN, genere, valutazione e stato di lettura.
 */
public class Libro {
    private String titolo;
    private String autore;
    private String isbn;
    private String genere;
    private int valutazione; // 0 = da valutare, da 1 a 5 stelle
    private StatoLettura statoLettura;

    /**
     * Costruttore che inizializza un nuovo libro con tutti i suoi attributi.
     *
     * @param titolo Titolo del libro
     * @param autore Autore del libro
     * @param isbn Codice ISBN del libro
     * @param genere Genere letterario del libro
     * @param valutazione Valutazione del libro (0 = da valutare, da 1 a 5 stelle)
     * @param statoLettura Stato di lettura del libro (letto, in lettura, da leggere)
     */
    public Libro(String titolo, String autore, String isbn, String genere, int valutazione, StatoLettura statoLettura) {
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn;
        this.genere = genere;
        this.valutazione = valutazione;
        this.statoLettura = statoLettura;
    }

    /**
     * Costruttore vuoto necessario per la serializzazione/deserializzazione.
     */
    public Libro() {
        this.titolo = "";
        this.autore = "";
        this.isbn = "";
        this.genere = "";
        this.valutazione = 0; // Da valutare come default
        this.statoLettura = StatoLettura.DA_LEGGERE;
    }

    // Getters e Setters
    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public int getValutazione() {
        return valutazione;
    }

    /**
     * Restituisce la valutazione come stringa descrittiva.
     *
     * @return Stringa rappresentante la valutazione (da valutare o numero di stelle)
     */
    public String getValutazioneAsString() {
        if (valutazione == 0) {
            return "Da valutare";
        } else {
            return String.valueOf(valutazione);
        }
    }

    public void setValutazione(int valutazione) {
        this.valutazione = valutazione;
    }

    public StatoLettura getStatoLettura() {
        return statoLettura;
    }

    public void setStatoLettura(StatoLettura statoLettura) {
        this.statoLettura = statoLettura;
    }

    /**
     * Restituisce lo stato di lettura come stringa.
     * Utile per la serializzazione in formato testuale o visualizzazione.
     *
     * @return Stringa rappresentante lo stato di lettura
     */
    public String getStatoLetturaAsString() {
        return statoLettura.getDescrizione();
    }

    /**
     * Verifica se l'ISBN è valido.
     * Un ISBN valido deve contenere solo numeri e trattini.
     *
     * @return true se l'ISBN è valido, false altrimenti
     */
    public boolean isValidIsbn() {
        return isbn != null && !isbn.trim().isEmpty() &&
                isbn.matches("^[0-9\\-]+$");
    }

    /**
     * Verifica se il titolo è valido.
     *
     * @return true se il titolo è valido, false altrimenti
     */
    public boolean isValidTitolo() {
        return titolo != null && !titolo.trim().isEmpty();
    }

    /**
     * Verifica se l'autore è valido.
     *
     * @return true se l'autore è valido, false altrimenti
     */
    public boolean isValidAutore() {
        return autore != null && !autore.trim().isEmpty();
    }

    /**
     * Verifica se il genere è valido.
     *
     * @return true se il genere è valido, false altrimenti
     */
    public boolean isValidGenere() {
        return genere != null && !genere.trim().isEmpty();
    }

    /**
     * Verifica se tutti i campi obbligatori sono validi.
     *
     * @return true se tutti i campi sono validi, false altrimenti
     */
    public boolean isValid() {
        return isValidTitolo() && isValidAutore() &&
                isValidIsbn() && isValidGenere();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        // ISBN è un identificatore univoco per un libro
        return Objects.equals(isbn, libro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titolo='" + titolo + '\'' +
                ", autore='" + autore + '\'' +
                ", isbn='" + isbn + '\'' +
                ", genere='" + genere + '\'' +
                ", valutazione=" + valutazione +
                ", statoLettura=" + statoLettura +
                '}';
    }
}