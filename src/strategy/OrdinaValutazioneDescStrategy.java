package strategy;

import model.Libro;
import java.util.Comparator;
import java.util.List;

/**
 * Strategia di ordinamento dei libri per valutazione in ordine decrescente (da 5 a 1).
 * Implementa l'interfaccia OrdinatoreLibroStrategy utilizzando il Comparator.
 */
public class OrdinaValutazioneDescStrategy implements OrdinatoreLibroStrategy {
    
    /**
     * Ordina una lista di libri per valutazione in ordine decrescente (da 5 a 1).
     * 
     * @param libri Lista di libri da ordinare
     */
    @Override
    public void ordina(List<Libro> libri) {
        libri.sort(Comparator.comparingInt(Libro::getValutazione).reversed());
    }
}
