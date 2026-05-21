package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import java.util.Random;

public class PowerPlant implements ParkZone {

    private final String name = "Planta de Energía";
    private double currentEnergy;
    private final double consumptionPerStep;
    private final double failureProbability;
    private boolean isOperational;

    public PowerPlant() {
        ParkConfig config = ParkConfig.getInstance();
        this.currentEnergy = config.getDouble("powerplant.initialEnergy", 100.0);
        this.consumptionPerStep = config.getDouble("powerplant.consumptionPerStep", 1.5);
        this.failureProbability = config.getDouble("powerplant.failureProbability", 0.05);
        this.isOperational = true;
    }

    public void tick(Random rng) {
        if (!isOperational) return;

        currentEnergy -= consumptionPerStep;
        if (currentEnergy < 0) currentEnergy = 0;

        if (rng.nextDouble() < failureProbability) {
            triggerFailure();
        }
    }

    public void triggerFailure() {
        this.isOperational = false;
        this.currentEnergy = 0.0;
    }

    public void repair() {
        this.isOperational = true;
        this.currentEnergy = ParkConfig.getInstance().getDouble("powerplant.initialEnergy", 100.0);
    }

    public boolean isOperational() { return isOperational; }
    public double getCurrentEnergy() { return currentEnergy; }

    @Override public String getName() { return name; }
    @Override public boolean hasCapacity() { return false; }
    @Override public int getCurrentOccupancy() { return 0; }
    @Override public int getMaxCapacity() { return 0; }
    @Override public void enter(Tourist tourist) {}
    @Override public void exit(Tourist tourist) {}
}