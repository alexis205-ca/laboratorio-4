package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.Random;

public class CentralHub implements ParkZone {
    private final String name = "Recinto Central";
    private final double souvenirPrice;
    private final double souvenirPurchaseProbability;
    private long souvenirCounter = 1;

    public CentralHub() {
        ParkConfig config = ParkConfig.getInstance();
        this.souvenirPrice = config.getDouble("hub.souvenirPrice", 15.0);
        this.souvenirPurchaseProbability = config.getDouble("hub.souvenirPurchaseProbability", 0.4);
    }

    public void visit(Tourist tourist, Random rng, CsvWriter csvWriter) {
        tourist.recordVisit(name);
        if (rng.nextDouble() < souvenirPurchaseProbability) {
            tourist.spend(souvenirPrice);
            csvWriter.appendRevenue(new RevenueRecord(
                souvenirCounter++, "SOUVENIR", souvenirPrice, tourist.getId(), name, LocalDateTime.now()
            ));
        }
    }

    @Override public String getName() { return name; }
    @Override public boolean hasCapacity() { return true; }
    @Override public int getCurrentOccupancy() { return 0; }
    @Override public int getMaxCapacity() { return Integer.MAX_VALUE; }
    @Override public void enter(Tourist tourist) {}
    @Override public void exit(Tourist tourist) {}
}