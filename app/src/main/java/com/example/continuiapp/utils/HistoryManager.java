package com.example.continuiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.continuiapp.models.CalculationResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryManager {
    private static final String PREFS_NAME = "FlowCalcHistory";
    private static final String KEY_CALCULATIONS = "calculations";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public HistoryManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveCalculation(CalculationResult calculation) {
        List<CalculationResult> calculations = getAllCalculations();
        calculations.add(calculation);

        // Sort by timestamp (newest first)
        Collections.sort(calculations, (a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));

        // Limit to last 100 calculations
        if (calculations.size() > 100) {
            calculations = calculations.subList(0, 100);
        }

        saveCalculations(calculations);
    }

    public List<CalculationResult> getAllCalculations() {
        String json = sharedPreferences.getString(KEY_CALCULATIONS, "[]");
        Type listType = new TypeToken<List<CalculationResult>>(){}.getType();
        List<CalculationResult> calculations = gson.fromJson(json, listType);
        return calculations != null ? calculations : new ArrayList<>();
    }

    public CalculationResult getCalculationById(long id) {
        List<CalculationResult> calculations = getAllCalculations();
        for (CalculationResult calc : calculations) {
            if (calc.getId() == id) {
                return calc;
            }
        }
        return null;
    }

    public void deleteCalculation(long id) {
        List<CalculationResult> calculations = getAllCalculations();
        calculations.removeIf(calc -> calc.getId() == id);
        saveCalculations(calculations);
    }

    public void clearAllHistory() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_CALCULATIONS);
        editor.apply();
    }

    private void saveCalculations(List<CalculationResult> calculations) {
        String json = gson.toJson(calculations);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CALCULATIONS, json);
        editor.apply();
    }
}
