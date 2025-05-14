package model;

/**
 * Enumerazione che rappresenta i possibili stati di lettura di un libro.
 * Definisce i tre stati possibili: letto, in lettura, da leggere.
 */
public enum StatoLettura {
    DA_LEGGERE("Da leggere"),
    IN_LETTURA("In lettura"),
    LETTO("Letto");

    private final String descrizione;

    /**
     * Costruttore dell'enumerazione.
     *
     * @param descrizione Descrizione testuale dello stato di lettura
     */
    StatoLettura(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Ottiene la descrizione testuale dello stato di lettura.
     *
     * @return Stringa rappresentante lo stato di lettura
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Converte una stringa nella corrispondente enumerazione StatoLettura.
     *
     * @param descrizione Stringa da convertire
     * @return StatoLettura corrispondente
     * @throws IllegalArgumentException se la stringa non corrisponde a nessuno stato valido
     */
    public static StatoLettura fromString(String descrizione) {
        for (StatoLettura stato : StatoLettura.values()) {
            if (stato.descrizione.equalsIgnoreCase(descrizione)) {
                return stato;
            }
        }
        throw new IllegalArgumentException("Stato di lettura non valido: " + descrizione);
    }
}
