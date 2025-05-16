package test.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import model.StatoLettura;

/**
 * Test unitari per l'enumerazione StatoLettura.
 * Verifica il corretto funzionamento dei metodi dell'enumerazione.
 */
public class StatoLetturaTest {

    @Test
    public void testGetDescrizione() {
        assertEquals("Da leggere", StatoLettura.DA_LEGGERE.getDescrizione());
        assertEquals("In lettura", StatoLettura.IN_LETTURA.getDescrizione());
        assertEquals("Letto", StatoLettura.LETTO.getDescrizione());
    }

    @Test
    public void testFromString() {
        assertEquals(StatoLettura.DA_LEGGERE, StatoLettura.fromString("Da leggere"));
        assertEquals(StatoLettura.IN_LETTURA, StatoLettura.fromString("In lettura"));
        assertEquals(StatoLettura.LETTO, StatoLettura.fromString("Letto"));

        // Test di sensibilità al maiuscolo/minuscolo
        assertEquals(StatoLettura.DA_LEGGERE, StatoLettura.fromString("da leggere"));
        assertEquals(StatoLettura.IN_LETTURA, StatoLettura.fromString("IN LETTURA"));
        assertEquals(StatoLettura.LETTO, StatoLettura.fromString("LeTTo"));
    }

    @Test
    public void testFromStringInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            StatoLettura.fromString("Stato non valido");
        });

        String expectedMessage = "Stato di lettura non valido: Stato non valido";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}