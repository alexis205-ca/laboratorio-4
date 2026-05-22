package com.axity.dinosaurpark.zone;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PowerPlantTest {

    @Test
    public void testTriggerFailureAndRepair() {
        PowerPlant plant = new PowerPlant();
        
        assertTrue(plant.isOperational());
        
        plant.triggerFailure();
        assertFalse(plant.isOperational());
        assertEquals(0.0, plant.getCurrentEnergy());
        
        plant.repair();
        assertTrue(plant.isOperational());
    }
}