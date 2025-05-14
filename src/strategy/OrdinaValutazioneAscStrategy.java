package strategy;

import model.Libro;
import java.util.Comparator;
import java.util.List;

/**
 * Strategia di ordinamento dei libri per valutazione in ordine crescente (da 1 a 5).
 * Implementa l'interfaccia OrdinatoreLibroStrategy utilizzando il Comparator.
 */
public class OrdinaValutazioneAscStrategy implements OrdinatoreLibroStrategy {
    
    /**
     * Ordina una lista di libri per valutazione in ordine crescente (da 1 a 5).
     * 
     * @param libri Lista di libri da ordinare
     */
    @Override
    public void ordina(List<Libro> libri) {
        libri.sort(Comparator.comparingInt(Libro::getValutazione));
    }
}
