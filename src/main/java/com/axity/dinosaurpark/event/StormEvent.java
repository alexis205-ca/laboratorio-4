package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

public class StormEvent implements SimulationEvent {
    @Override public String getName() { return "TORMENTA_TORRENCIAL"; }
    @Override public String getDescription() { return "Una tormenta obliga a los turistas a refugiarse."; }

    @Override
    public void execute(ParkState state, Random rng) {
        for (Tourist t : state.getActiveTourists()) {
            t.recordVisit("Evacuación");
        }
        
        state.getCsvWriter().appendExpense(new ExpenseRecord(
            System.currentTimeMillis(), "STORM_DAMAGE", 500.0, "Daños por tormenta", LocalDateTime.now()
        ));
        
        state.getCsvWriter().appendEvent(toRecord(state.getCurrentStep()));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Todos los turistas activos", LocalDateTime.now());
    }
}