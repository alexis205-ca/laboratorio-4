package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DinosaurEscapeEvent implements SimulationEvent {
    @Override public String getName() { return "ESCAPE_DINOSAURIO"; }
    @Override public String getDescription() { return "Un dinosaurio ha escapado de su recinto."; }

    @Override
    public void execute(ParkState state, Random rng) {
        List<Dinosaur> inEnclosure = state.getDinosaurs().stream()
                .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
                .collect(Collectors.toList());

        if (inEnclosure.isEmpty()) return;

        Dinosaur escapee = inEnclosure.get(rng.nextInt(inEnclosure.size()));
        escapee.escape();
        String affected = "Dinosaurio: " + escapee.getName();

        if (rng.nextDouble() < escapee.getDangerLevel()) {
            List<Tourist> inPark = state.getActiveTourists();
            if (!inPark.isEmpty()) {
                Tourist victim = inPark.get(rng.nextInt(inPark.size()));
                victim.setStatus(TouristStatus.ATTACKED);
                affected += " | Turista atacado: " + victim.getName();
            }
        }
        state.getCsvWriter().appendEvent(toRecord(state.getCurrentStep()));
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(step, getName(), getDescription(), "Múltiples", LocalDateTime.now());
    }
}