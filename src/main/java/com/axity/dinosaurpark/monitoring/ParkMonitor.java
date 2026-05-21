package com.axity.dinosaurpark.monitoring;

import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.model.DinosaurStatus;

public class ParkMonitor {
    public static void displaySnapshot(ParkState state) {
        int activeTourists = state.getActiveTourists().size();
        long dinosInEnclosure = state.getDinosaurs().stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
                .count();
        double energy = state.getPowerPlant().getCurrentEnergy();

        System.out.println("--- Paso de simulación: " + state.getCurrentStep() + " ---");
        System.out.println("Turistas en el parque: " + activeTourists);
        System.out.println("Dinosaurios en recinto: " + dinosInEnclosure + "/" + state.getDinosaurs().size());
        System.out.println("Energía de la planta: " + String.format("%.2f", energy) + "%");
        System.out.println("----------------------------------\n");
    }
}