package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class EventScheduler {
    private final Map<Integer, SimulationEvent> scheduledEvents = new HashMap<>();

    public EventScheduler(long seed, int totalSteps) {
        Random rng = new Random(seed);
        
        scheduledEvents.put(rng.nextInt(totalSteps), new DinosaurEscapeEvent());
        scheduledEvents.put(rng.nextInt(totalSteps), new BlackoutEvent());
        scheduledEvents.put(rng.nextInt(totalSteps), new StormEvent());
    }

    public Optional<SimulationEvent> checkForEvent(int step) {
        return Optional.ofNullable(scheduledEvents.get(step));
    }
}