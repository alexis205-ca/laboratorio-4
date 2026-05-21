package com.axity.dinosaurpark.simulation;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.*;
import com.axity.dinosaurpark.monitoring.ParkMonitor;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.zone.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class SimulationEngine {
    private final ParkState state;
    private final EventScheduler scheduler;
    private final int totalSteps;
    private final int batchSize;

    // Zonas
    private final ArrivalZone arrivalZone;
    private final CentralHub centralHub;
    private final BathroomZone bathroomZone;
    private final ObservationEnclosure basicEnclosure;
    private final ObservationEnclosure premiumEnclosure;
    private final ObservationEnclosure vipEnclosure;

    public SimulationEngine(ParkConfig config) {
        this.totalSteps = config.getTotalSteps();
        this.batchSize = config.getInt("simulation.arrivalBatchSize", 5);
        long seed = config.getSeed();

        Random rng = new Random(seed);
        CsvWriter csvWriter = new CsvWriter(config.getString("output.directory", "output"));
        PowerPlant powerPlant = new PowerPlant();

        this.state = new ParkState(powerPlant, csvWriter, rng);
        this.scheduler = new EventScheduler(seed, totalSteps);

        // Inicializar Zonas
        this.arrivalZone = new ArrivalZone();
        this.centralHub = new CentralHub();
        this.bathroomZone = new BathroomZone();
        this.basicEnclosure = new ObservationEnclosure("Recinto Básico", ExperienceType.BASIC);
        this.premiumEnclosure = new ObservationEnclosure("Recinto Premium", ExperienceType.PREMIUM);
        this.vipEnclosure = new ObservationEnclosure("Recinto VIP", ExperienceType.VIP);

        int guardsCount = config.getInt("workers.guards", 3);
        int techCount = config.getInt("workers.technicians", 2);
        double salary = config.getDouble("workers.dailySalary", 150.0);
        
        for (int i = 0; i < guardsCount; i++) {
            state.getWorkers().add(new Guard(i + 1, "Guardia " + (i + 1), salary));
        }
        for (int i = 0; i < techCount; i++) {
            state.getWorkers().add(new Technician(i + 1 + guardsCount, "Técnico " + (i + 1), salary));
        }


        int carnivores = config.getInt("dinosaurs.carnivores", 5);
        int herbivores = config.getInt("dinosaurs.herbivores", 15);
        
        for (int i = 0; i < carnivores; i++) {
            state.getDinosaurs().add(new CarnivoreDinosaur(i + 1, "T-Rex " + (i + 1), "Tyrannosaurus"));
        }
        for (int i = 0; i < herbivores; i++) {
            state.getDinosaurs().add(new HerbivoreDinosaur(carnivores + i + 1, "Triceratops " + (i + 1), "Triceratops"));
        }

        int touristsCount = config.getInt("tourists", 50);
        for (int i = 0; i < touristsCount; i++) {
            Tourist t = new Tourist(i + 1, "Turista " + (i + 1));
            arrivalZone.enter(t);
        }
    }

    public void run() {
        Random rng = state.getRng();
        CsvWriter csvWriter = state.getCsvWriter();

        for (int step = 1; step <= totalSteps; step++) {
            state.incrementStep();

            List<Tourist> arrived = arrivalZone.processBatch(batchSize, csvWriter);
            state.getActiveTourists().addAll(arrived);

            for (Tourist t : state.getActiveTourists()) {
                if (t.getStatus() == TouristStatus.IN_PARK) {
                    centralHub.visit(t, rng, csvWriter);
                    bathroomZone.tryEnter(t, rng, csvWriter);
                    
                    double rand = rng.nextDouble();
                    if (rand < 0.33) basicEnclosure.visit(t, rng, csvWriter);
                    else if (rand < 0.66) premiumEnclosure.visit(t, rng, csvWriter);
                    else vipEnclosure.visit(t, rng, csvWriter);
                }
            }


            bathroomZone.tick();
            state.getPowerPlant().tick(rng);

            scheduler.checkForEvent(step).ifPresent(e -> e.execute(state, rng));

            for (Worker w : state.getWorkers()) {
                if (w instanceof Guard) {
                    ((Guard) w).recaptureEscapedDinosaurs(state.getDinosaurs());
                } else if (w instanceof Technician) {
                    ((Technician) w).repairIfNeeded(state.getPowerPlant());
                }
                
                if (step == totalSteps) {
                    csvWriter.appendExpense(new ExpenseRecord(System.currentTimeMillis(), "SALARY", w.getDailySalary(), "Salario: " + w.getName(), LocalDateTime.now()));
                }
            }

            ParkMonitor.displaySnapshot(state);
        }
        
        System.out.println("¡Simulación terminada! Revisa la carpeta output/ para ver los archivos CSV.");
    }
}