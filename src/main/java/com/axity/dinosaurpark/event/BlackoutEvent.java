package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.Random;

public class BlackoutEvent implements SimulationEvent {
    @Override public String getName() { return "APAGON_MASIVO"; }
    @Override public String getDescription() { return "Falla total en la planta de energía."; }

    @Override
    public void execute(ParkState state, Random rng) {
        state.getPowerPlant().triggerFailure();
        
        state.getCsvWriter().appendExpense(new ExpenseRecord(
            System.currentTimeMillis(), "EMERGENCY_REPAIR", 2000.0, "Reparación por apagón", LocalDateTime.now()
        ));
        
        state.getCsvWriter().appendEvent(toRecord(state.getCurrentStep()));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Planta de Energía", LocalDateTime.now());
    }
}