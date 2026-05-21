package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.model.*;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.zone.PowerPlant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParkState {
    private int currentStep = 0;
    private final List<Tourist> activeTourists = new ArrayList<>();
    private final List<Dinosaur> dinosaurs = new ArrayList<>();
    private final List<Worker> workers = new ArrayList<>();
    
    private final PowerPlant powerPlant;
    private final CsvWriter csvWriter;
    private final Random rng;

    public ParkState(PowerPlant powerPlant, CsvWriter csvWriter, Random rng) {
        this.powerPlant = powerPlant;
        this.csvWriter = csvWriter;
        this.rng = rng;
    }

    public void incrementStep() { 
        currentStep++; 
    }
    
    public int getCurrentStep() { 
        return currentStep; 
    }

    public List<Tourist> getActiveTourists() { return activeTourists; }
    public List<Dinosaur> getDinosaurs() { return dinosaurs; }
    public List<Worker> getWorkers() { return workers; }

    public PowerPlant getPowerPlant() { return powerPlant; }
    public CsvWriter getCsvWriter() { return csvWriter; }
    public Random getRng() { return rng; }
}