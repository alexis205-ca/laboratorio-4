package com.axity.dinosaurpark.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {
    private final String outputDir;

    public CsvWriter(String outputDir) {
        this.outputDir = outputDir;
        initFile("revenues.csv", "id,type,amount,touristId,zone,timestamp");
        initFile("expenses.csv", "id,type,amount,description,timestamp");
        initFile("events.csv", "step,eventName,description,affectedEntities,timestamp");
    }

    private void initFile(String filename, String header) {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (FileWriter fw = new FileWriter(outputDir + "/" + filename, false)) {
            fw.write(header + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendRevenue(RevenueRecord r) {
        appendToFile("revenues.csv", r.toCsvLine());
    }

    public void appendExpense(ExpenseRecord e) {
        appendToFile("expenses.csv", e.toCsvLine());
    }

    public void appendEvent(EventRecord ev) {
        appendToFile("events.csv", ev.toCsvLine());
    }

    private void appendToFile(String filename, String line) {
        try (FileWriter fw = new FileWriter(outputDir + "/" + filename, true)) {
            fw.write(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}