package com.axity.dinosaurpark.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TouristTest {

    @Test
    public void testTouristSpendsMoney() {
        Tourist tourist = new Tourist(1, "Turista de prueba");
        assertEquals(0.0, tourist.getMoneySpent());
        
        tourist.spend(50.5);
        assertEquals(50.5, tourist.getMoneySpent());
    }

    @Test
    public void testRecordVisit() {
        Tourist tourist = new Tourist(2, "Turista explorador");
        tourist.recordVisit("Zona Central");
        
        assertTrue(tourist.getVisitedZones().contains("Zona Central"));
    }
}