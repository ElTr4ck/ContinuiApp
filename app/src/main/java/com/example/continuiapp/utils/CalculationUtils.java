package com.example.continuiapp.utils;

public class CalculationUtils {

    // Area conversion factors to m²
    public static final double[] AREA_FACTORS = {
            1.0,        // m²
            0.0001,     // cm²
            0.000001,   // mm²
            0.00064516, // in²
            0.092903    // ft²
    };

    // Velocity conversion factors to m/s
    public static final double[] VELOCITY_FACTORS = {
            1.0,        // m/s
            0.01,       // cm/s
            0.001,      // mm/s
            0.277778,   // km/h
            0.3048,     // ft/s
            0.44704     // mph
    };

    public static final String[] AREA_UNITS = {"m²", "cm²", "mm²", "in²", "ft²"};
    public static final String[] VELOCITY_UNITS = {"m/s", "cm/s", "mm/s", "km/h", "ft/s", "mph"};

    public static double convertArea(double value, int fromUnit, int toUnit) {
        // Convert to standard (m²) then to target unit
        double standardValue = value * AREA_FACTORS[fromUnit];
        return standardValue / AREA_FACTORS[toUnit];
    }

    public static double convertVelocity(double value, int fromUnit, int toUnit) {
        // Convert to standard (m/s) then to target unit
        double standardValue = value * VELOCITY_FACTORS[fromUnit];
        return standardValue / VELOCITY_FACTORS[toUnit];
    }

    public static boolean isValidNumber(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        try {
            double value = Double.parseDouble(input.trim());
            return value > 0; // Must be positive for physical quantities
        } catch (NumberFormatException e) {
            return false;
        }
    }
}