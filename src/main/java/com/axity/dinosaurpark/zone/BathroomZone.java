package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class BathroomZone implements ParkZone {
    private final String name = "Baños y SPA";
    private final int maxCapacity;
    private final int useDurationSteps;
    private final double spaPrice;
    private final double spaPurchaseProbability;
    private long spaCounter = 1;
    
    private final Map<Tourist, Integer> occupants = new HashMap<>();

    public BathroomZone() {
        ParkConfig config = ParkConfig.getInstance();
        this.maxCapacity = config.getInt("bathroom.maxCapacity", 10);
        this.useDurationSteps = config.getInt("bathroom.useDurationSteps", 3);
        this.spaPrice = config.getDouble("bathroom.spaPrice", 20.0);
        this.spaPurchaseProbability = config.getDouble("bathroom.spaPurchaseProbability", 0.2);
    }

    public void tryEnter(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (hasCapacity() && !occupants.containsKey(tourist)) {
            enter(tourist);
            tourist.recordVisit(name);
            
            if (rng.nextDouble() < spaPurchaseProbability) {
                tourist.spend(spaPrice);
                csvWriter.appendRevenue(new RevenueRecord(
                    spaCounter++, "SPA_SERVICE", spaPrice, tourist.getId(), name, LocalDateTime.now()
                ));
            }
        }
    }

    public void tick() {
        Iterator<Map.Entry<Tourist, Integer>> it = occupants.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Tourist, Integer> entry = it.next();
            int timeRemaining = entry.getValue() - 1;
            if (timeRemaining <= 0) {
                it.remove(); 
            } else {
                entry.setValue(timeRemaining);
            }
        }
    }

    @Override public String getName() { return name; }
    @Override public boolean hasCapacity() { return occupants.size() < maxCapacity; }
    @Override public int getCurrentOccupancy() { return occupants.size(); }
    @Override public int getMaxCapacity() { return maxCapacity; }
    
    @Override public void enter(Tourist tourist) { occupants.put(tourist, useDurationSteps); }
    @Override public void exit(Tourist tourist) { occupants.remove(tourist); }
}