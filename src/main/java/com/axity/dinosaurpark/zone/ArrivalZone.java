package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArrivalZone implements ParkZone {
    private final String name = "Lugar de Arribo";
    private final int maxCapacity;
    private final double ticketPrice;
    private int currentOccupancy = 0;
    private final Queue<Tourist> waitingQueue = new LinkedList<>();
    private long ticketCounter = 1;

    public ArrivalZone() {
        ParkConfig config = ParkConfig.getInstance();
        this.maxCapacity = config.getInt("arrival.maxCapacity", 30);
        this.ticketPrice = config.getDouble("arrival.ticketPrice", 25.0);
    }

    public List<Tourist> processBatch(int batchSize, CsvWriter csvWriter) {
        List<Tourist> processed = new ArrayList<>();
        int count = 0;
        while (!waitingQueue.isEmpty() && count < batchSize && hasCapacity()) {
            Tourist t = waitingQueue.poll();
            t.setStatus(TouristStatus.IN_PARK);
            t.spend(ticketPrice);
            currentOccupancy++;
            
            csvWriter.appendRevenue(new RevenueRecord(
                ticketCounter++, "TICKET", ticketPrice, t.getId(), name, LocalDateTime.now()
            ));
            
            processed.add(t);
            count++;
        }
        return processed;
    }

    @Override public String getName() { return name; }
    @Override public boolean hasCapacity() { return currentOccupancy < maxCapacity; }
    @Override public int getCurrentOccupancy() { return currentOccupancy; }
    @Override public int getMaxCapacity() { return maxCapacity; }
    
    @Override public void enter(Tourist tourist) { waitingQueue.offer(tourist); }
    @Override public void exit(Tourist tourist) { currentOccupancy--; }
}