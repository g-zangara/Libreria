package strategy;

import model.Libro;
import java.util.Comparator;
import java.util.List;

/**
 * Strategia di ordinamento dei libri per titolo in ordine alfabetico inverso (Z-A).
 * Implementa l'interfaccia OrdinatoreLibroStrategy utilizzando il Comparator.
 */
public class OrdinaTitoloZAStrategy implements OrdinatoreLibroStrategy {
    
    /**
     * Ordina una lista di libri per titolo in ordine alfabetico inverso (Z-A).
     * 
     * @param libri Lista di libri da ordinare
     */
    @Override
    public void ordina(List<Libro> libri) {
        libri.sort(Comparator.comparing(Libro::getTitolo, String.CASE_INSENSITIVE_ORDER).reversed());
    }
}
