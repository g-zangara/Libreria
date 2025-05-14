package dao;

import model.Libro;
import java.util.List;
import java.io.IOException;

/**
 * Interfaccia DAO (Data Access Object) per la gestione della persistenza dei libri.
 * Definisce le operazioni di lettura/scrittura per salvare e caricare libri.
 */
public interface LibroDAO {

    /**
     * Salva una lista di libri su un file.
     *
     * @param libri Lista di libri da salvare
     * @param percorsoFile Percorso del file in cui salvare i dati
     * @throws IOException In caso di errori durante la scrittura del file
     */
    void salvaLibri(List<Libro> libri, String percorsoFile) throws IOException;

    /**
     * Carica una lista di libri da un file.
     *
     * @param percorsoFile Percorso del file da cui caricare i dati
     * @return Lista di libri caricati dal file
     * @throws IOException In caso di errori durante la lettura del file
     */
    List<Libro> caricaLibri(String percorsoFile) throws IOException;
}
