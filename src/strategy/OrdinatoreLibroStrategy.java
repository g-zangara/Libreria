package strategy;

import model.Libro;
import java.util.List;

/**
 * Interfaccia che definisce la strategia di ordinamento dei libri.
 * Implementa il pattern Strategy per l'ordinamento flessibile.
 */
public interface OrdinatoreLibroStrategy {
    
    /**
     * Ordina una lista di libri secondo una specifica strategia.
     * 
     * @param libri Lista di libri da ordinare
     */
    void ordina(List<Libro> libri);
}
