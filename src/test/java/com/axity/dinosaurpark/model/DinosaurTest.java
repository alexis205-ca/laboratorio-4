package com.axity.dinosaurpark.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DinosaurTest {

    @Test
    public void testDinosaurEscapeAndRecapture() {
        CarnivoreDinosaur rex = new CarnivoreDinosaur(1, "Rex", "T-Rex");
        
        assertEquals(DinosaurStatus.IN_ENCLOSURE, rex.getStatus());
        
        rex.escape();
        assertEquals(DinosaurStatus.ESCAPED, rex.getStatus());
        
        rex.returnToEnclosure();
        assertEquals(DinosaurStatus.IN_ENCLOSURE, rex.getStatus());
    }
}