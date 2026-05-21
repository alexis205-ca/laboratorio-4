package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.Random;

public class ObservationEnclosure implements ParkZone {
    private final String name;
    private final ExperienceType type;
    private final int maxVisitors;
    private final double entryFee;
    private int currentOccupancy = 0;
    private long feeCounter = 1;

    public ObservationEnclosure(String name, ExperienceType type) {
        this.name = name;
        this.type = type;
        ParkConfig config = ParkConfig.getInstance();
        String typeStr = type.name().toLowerCase();
        this.maxVisitors = config.getInt("enclosure." + typeStr + ".maxVisitors", 10);
        this.entryFee = config.getDouble("enclosure." + typeStr + ".entryFee", 10.0);
    }

    public void visit(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (hasCapacity()) {
            enter(tourist);
            tourist.recordVisit(name);
            tourist.spend(entryFee);
            
            csvWriter.appendRevenue(new RevenueRecord(
                feeCounter++, "ENCLOSURE_FEE_" + type.name(), entryFee, tourist.getId(), name, LocalDateTime.now()
            ));
            
            conductSurvey(tourist, rng);
            exit(tourist);
        }
    }

    private void conductSurvey(Tourist tourist, Random rng) {
        int score;
        switch (type) {
            case VIP: score = 3 + rng.nextInt(3); break; // 3-5
            case PREMIUM: score = 2 + rng.nextInt(3); break; // 2-4
            default: score = 1 + rng.nextInt(3); break; // 1-3
        }
    }

    @Override public String getName() { return name; }
    @Override public boolean hasCapacity() { return currentOccupancy < maxVisitors; }
    @Override public int getCurrentOccupancy() { return currentOccupancy; }
    @Override public int getMaxCapacity() { return maxVisitors; }
    
    @Override public void enter(Tourist tourist) { currentOccupancy++; }
    @Override public void exit(Tourist tourist) { currentOccupancy--; }
}