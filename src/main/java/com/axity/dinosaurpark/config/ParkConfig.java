package com.axity.dinosaurpark.config;

import java.io.InputStream;
import java.util.Properties;

public final class ParkConfig {

    private static ParkConfig instance;
    private final Properties props;

    // Constructor PRIVADO: Nadie puede hacer "new ParkConfig()"
    private ParkConfig() {
        props = new Properties();
        // Carga park.properties con getClass().getClassLoader().getResourceAsStream()
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("park.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                System.err.println("No se pudo encontrar el archivo park.properties");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     // Punto de acceso global — crea la instancia solo si no existe
    public static ParkConfig getInstance() {
        if (instance == null) {
            instance = new ParkConfig();
        }
        return instance;
    }

    // Métodos de lectura
    public int getInt(String key, int defaultValue) {
        String value = props.getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    public double getDouble(String key, double defaultValue) {
        String value = props.getProperty(key);
        return value != null ? Double.parseDouble(value) : defaultValue;
    }

    public String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public long getSeed() {
        return Long.parseLong(props.getProperty("simulation.seed", "42"));
        // lee simulation.seed
    }

    public int getTotalSteps() {
        return getInt("simulation.totalSteps", 100);
    }

    // Solo para tests — permite resetear la instancia entre tests
    static void resetForTesting() {
        instance = null;
    }
}