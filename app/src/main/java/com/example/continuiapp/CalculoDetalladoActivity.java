package com.example.continuiapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.continuiapp.models.CalculationResult;
import com.example.continuiapp.utils.CalculationUtils;
import com.example.continuiapp.utils.HistoryManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

public class CalculoDetalladoActivity extends AppCompatActivity {

    // UI Components
    private ImageButton btnBack, btnInfo;
    private TextInputEditText etArea1, etVelocity1, etArea2, etVelocity2;
    private TextInputLayout tilArea1, tilVelocity1, tilArea2, tilVelocity2;
    private Spinner spinnerArea1Units, spinnerVelocity1Units, spinnerArea2Units, spinnerVelocity2Units;
    private MaterialButton btnCalculate, btnClear, btnSaveResult;
    private CardView cvResults;
    private TextView tvCalculatedParameter, tvResultValue, tvSystemFlowRate;
    private ScrollView scrollView;

    private DecimalFormat decimalFormat = new DecimalFormat("#.####");
    private CalculationResult currentResult;
    private HistoryManager historyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_calculo_detallado);

        initializeViews();
        setupSpinners();
        setupListeners();

        // Initialize HistoryManager
        historyManager = new HistoryManager(this);

        // Check if we're reusing a calculation
        handleReuseCalculation();
    }

    private void initializeViews() {
        // Header buttons
        btnBack = findViewById(R.id.btnBack);
        btnInfo = findViewById(R.id.btnInfo);

        // Input fields
        etArea1 = findViewById(R.id.etArea1);
        etVelocity1 = findViewById(R.id.etVelocity1);
        etArea2 = findViewById(R.id.etArea2);
        etVelocity2 = findViewById(R.id.etVelocity2);

        // Input layouts
        tilArea1 = findViewById(R.id.tilArea1);
        tilVelocity1 = findViewById(R.id.tilVelocity1);
        tilArea2 = findViewById(R.id.tilArea2);
        tilVelocity2 = findViewById(R.id.tilVelocity2);

        // Spinners
        spinnerArea1Units = findViewById(R.id.spinnerArea1Units);
        spinnerVelocity1Units = findViewById(R.id.spinnerVelocity1Units);
        spinnerArea2Units = findViewById(R.id.spinnerArea2Units);
        spinnerVelocity2Units = findViewById(R.id.spinnerVelocity2Units);

        // Buttons
        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);
        btnSaveResult = findViewById(R.id.btnSaveResult);

        // Results
        cvResults = findViewById(R.id.cvResults);
        tvCalculatedParameter = findViewById(R.id.tvCalculatedParameter);
        tvResultValue = findViewById(R.id.tvResultValue);
        tvSystemFlowRate = findViewById(R.id.tvSystemFlowRate);

        // ScrollView
        scrollView = findViewById(R.id.scrollView);
    }

    private void setupSpinners() {
        // Area units spinners
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, CalculationUtils.AREA_UNITS);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerArea1Units.setAdapter(areaAdapter);
        spinnerArea2Units.setAdapter(areaAdapter);

        // Velocity units spinners
        ArrayAdapter<String> velocityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, CalculationUtils.VELOCITY_UNITS);
        velocityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVelocity1Units.setAdapter(velocityAdapter);
        spinnerVelocity2Units.setAdapter(velocityAdapter);

        // Set default selections
        spinnerArea1Units.setSelection(0); // m²
        spinnerArea2Units.setSelection(0); // m²
        spinnerVelocity1Units.setSelection(0); // m/s
        spinnerVelocity2Units.setSelection(0); // m/s
    }

    private void setupListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Info button
        btnInfo.setOnClickListener(v -> showFormulaInfo());

        // Calculate button
        btnCalculate.setOnClickListener(v -> performCalculation());

        // Clear button
        btnClear.setOnClickListener(v -> clearAllFields());

        // Save result button
        btnSaveResult.setOnClickListener(v -> saveCurrentResult());

        // Text change listeners for validation
        setupTextWatchers();

        // Spinner listeners for unit changes
        setupSpinnerListeners();
    }

    private void handleReuseCalculation() {
        Intent intent = getIntent();
        if (intent.getBooleanExtra("reuse_calculation", false)) {
            long calculationId = intent.getLongExtra("calculation_id", -1);
            if (calculationId != -1) {
                CalculationResult calculation = historyManager.getCalculationById(calculationId);
                if (calculation != null) {
                    loadCalculationData(calculation);
                    Toast.makeText(this, "Cálculo cargado desde el historial", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void loadCalculationData(CalculationResult calculation) {
        // Load Area 1
        if (calculation.getArea1() != null) {
            etArea1.setText(String.valueOf(calculation.getArea1()));
            if (calculation.getArea1Unit() != null) {
                int unitIndex = getUnitIndex(CalculationUtils.AREA_UNITS, calculation.getArea1Unit());
                if (unitIndex != -1) {
                    spinnerArea1Units.setSelection(unitIndex);
                }
            }
        }

        // Load Velocity 1
        if (calculation.getVelocity1() != null) {
            etVelocity1.setText(String.valueOf(calculation.getVelocity1()));
            if (calculation.getVelocity1Unit() != null) {
                int unitIndex = getUnitIndex(CalculationUtils.VELOCITY_UNITS, calculation.getVelocity1Unit());
                if (unitIndex != -1) {
                    spinnerVelocity1Units.setSelection(unitIndex);
                }
            }
        }

        // Load Area 2
        if (calculation.getArea2() != null) {
            etArea2.setText(String.valueOf(calculation.getArea2()));
            if (calculation.getArea2Unit() != null) {
                int unitIndex = getUnitIndex(CalculationUtils.AREA_UNITS, calculation.getArea2Unit());
                if (unitIndex != -1) {
                    spinnerArea2Units.setSelection(unitIndex);
                }
            }
        }

        // Load Velocity 2 (only if it wasn't the calculated parameter)
        if (calculation.getVelocity2() != null && !calculation.getCalculatedParameter().equals("V₂")) {
            etVelocity2.setText(String.valueOf(calculation.getVelocity2()));
            if (calculation.getVelocity2Unit() != null) {
                int unitIndex = getUnitIndex(CalculationUtils.VELOCITY_UNITS, calculation.getVelocity2Unit());
                if (unitIndex != -1) {
                    spinnerVelocity2Units.setSelection(unitIndex);
                }
            }
        }
    }

    private int getUnitIndex(String[] units, String unit) {
        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(unit)) {
                return i;
            }
        }
        return -1;
    }

    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
                hideResults();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etArea1.addTextChangedListener(textWatcher);
        etVelocity1.addTextChangedListener(textWatcher);
        etArea2.addTextChangedListener(textWatcher);
        etVelocity2.addTextChangedListener(textWatcher);
    }

    private void setupSpinnerListeners() {
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideResults();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerArea1Units.setOnItemSelectedListener(spinnerListener);
        spinnerVelocity1Units.setOnItemSelectedListener(spinnerListener);
        spinnerArea2Units.setOnItemSelectedListener(spinnerListener);
        spinnerVelocity2Units.setOnItemSelectedListener(spinnerListener);
    }

    private void validateInputs() {
        int filledFields = 0;

        if (!etArea1.getText().toString().trim().isEmpty()) filledFields++;
        if (!etVelocity1.getText().toString().trim().isEmpty()) filledFields++;
        if (!etArea2.getText().toString().trim().isEmpty()) filledFields++;
        if (!etVelocity2.getText().toString().trim().isEmpty()) filledFields++;

        // Enable calculate button only if exactly 3 fields are filled
        btnCalculate.setEnabled(filledFields == 3);

        // Clear any previous error messages
        clearErrorMessages();
    }

    private void clearErrorMessages() {
        tilArea1.setError(null);
        tilVelocity1.setError(null);
        tilArea2.setError(null);
        tilVelocity2.setError(null);
    }

    private void performCalculation() {
        try {
            // Get input values
            String area1Str = etArea1.getText().toString().trim();
            String velocity1Str = etVelocity1.getText().toString().trim();
            String area2Str = etArea2.getText().toString().trim();
            String velocity2Str = etVelocity2.getText().toString().trim();

            // Convert to standard units (m² and m/s)
            Double area1 = area1Str.isEmpty() ? null : convertAreaToStandard(
                    Double.parseDouble(area1Str), spinnerArea1Units.getSelectedItemPosition());
            Double velocity1 = velocity1Str.isEmpty() ? null : convertVelocityToStandard(
                    Double.parseDouble(velocity1Str), spinnerVelocity1Units.getSelectedItemPosition());
            Double area2 = area2Str.isEmpty() ? null : convertAreaToStandard(
                    Double.parseDouble(area2Str), spinnerArea2Units.getSelectedItemPosition());
            Double velocity2 = velocity2Str.isEmpty() ? null : convertVelocityToStandard(
                    Double.parseDouble(velocity2Str), spinnerVelocity2Units.getSelectedItemPosition());

            // Perform calculation using continuity equation: A1*V1 = A2*V2
            CalculationResult result = calculateMissingValue(area1, velocity1, area2, velocity2);

            if (result != null) {
                displayResult(result);

            } else {
                showError("Error en el cálculo. Verifica los valores ingresados.");
            }

        } catch (NumberFormatException e) {
            showError("Por favor ingresa valores numéricos válidos.");
        } catch (Exception e) {
            showError("Error inesperado durante el cálculo.");
        }
    }

    private double convertAreaToStandard(double value, int unitIndex) {
        return value * CalculationUtils.AREA_FACTORS[unitIndex];
    }

    private double convertVelocityToStandard(double value, int unitIndex) {
        return value * CalculationUtils.VELOCITY_FACTORS[unitIndex];
    }

    private double convertAreaFromStandard(double value, int unitIndex) {
        return value / CalculationUtils.AREA_FACTORS[unitIndex];
    }

    private double convertVelocityFromStandard(double value, int unitIndex) {
        return value / CalculationUtils.VELOCITY_FACTORS[unitIndex];
    }

    private CalculationResult calculateMissingValue(Double area1, Double velocity1,
                                                    Double area2, Double velocity2) {
        CalculationResult result = new CalculationResult();

        // Store input values in standard units
        result.setArea1(area1);
        result.setVelocity1(velocity1);
        result.setArea2(area2);
        result.setVelocity2(velocity2);

        // Store original input values with units
        storeOriginalValues(result);

        // Calculate missing value using A1*V1 = A2*V2
        if (area1 == null) {
            // Calculate A1 = (A2 * V2) / V1
            if (velocity1 != null && area2 != null && velocity2 != null && velocity1 != 0) {
                result.setCalculatedValue((area2 * velocity2) / velocity1);
                result.setCalculatedParameter("A₁");
                result.setCalculatedUnit(CalculationUtils.AREA_UNITS[spinnerArea1Units.getSelectedItemPosition()]);
                result.setDisplayValue(convertAreaFromStandard(result.getCalculatedValue(),
                        spinnerArea1Units.getSelectedItemPosition()));
                return result;
            }
        } else if (velocity1 == null) {
            // Calculate V1 = (A2 * V2) / A1
            if (area1 != null && area2 != null && velocity2 != null && area1 != 0) {
                result.setCalculatedValue((area2 * velocity2) / area1);
                result.setCalculatedParameter("V₁");
                result.setCalculatedUnit(CalculationUtils.VELOCITY_UNITS[spinnerVelocity1Units.getSelectedItemPosition()]);
                result.setDisplayValue(convertVelocityFromStandard(result.getCalculatedValue(),
                        spinnerVelocity1Units.getSelectedItemPosition()));
                return result;
            }
        } else if (area2 == null) {
            // Calculate A2 = (A1 * V1) / V2
            if (area1 != null && velocity1 != null && velocity2 != null && velocity2 != 0) {
                result.setCalculatedValue((area1 * velocity1) / velocity2);
                result.setCalculatedParameter("A₂");
                result.setCalculatedUnit(CalculationUtils.AREA_UNITS[spinnerArea2Units.getSelectedItemPosition()]);
                result.setDisplayValue(convertAreaFromStandard(result.getCalculatedValue(),
                        spinnerArea2Units.getSelectedItemPosition()));
                return result;
            }
        } else if (velocity2 == null) {
            // Calculate V2 = (A1 * V1) / A2
            if (area1 != null && velocity1 != null && area2 != null && area2 != 0) {
                result.setCalculatedValue((area1 * velocity1) / area2);
                result.setCalculatedParameter("V₂");
                result.setCalculatedUnit(CalculationUtils.VELOCITY_UNITS[spinnerVelocity2Units.getSelectedItemPosition()]);
                result.setDisplayValue(convertVelocityFromStandard(result.getCalculatedValue(),
                        spinnerVelocity2Units.getSelectedItemPosition()));
                return result;
            }
        }

        return null;
    }

    private void storeOriginalValues(CalculationResult result) {
        // Store original input values with their units
        String area1Str = etArea1.getText().toString().trim();
        String velocity1Str = etVelocity1.getText().toString().trim();
        String area2Str = etArea2.getText().toString().trim();
        String velocity2Str = etVelocity2.getText().toString().trim();

        if (!area1Str.isEmpty()) {
            result.setArea1(Double.parseDouble(area1Str));
            result.setArea1Unit(CalculationUtils.AREA_UNITS[spinnerArea1Units.getSelectedItemPosition()]);
        }
        if (!velocity1Str.isEmpty()) {
            result.setVelocity1(Double.parseDouble(velocity1Str));
            result.setVelocity1Unit(CalculationUtils.VELOCITY_UNITS[spinnerVelocity1Units.getSelectedItemPosition()]);
        }
        if (!area2Str.isEmpty()) {
            result.setArea2(Double.parseDouble(area2Str));
            result.setArea2Unit(CalculationUtils.AREA_UNITS[spinnerArea2Units.getSelectedItemPosition()]);
        }
        if (!velocity2Str.isEmpty()) {
            result.setVelocity2(Double.parseDouble(velocity2Str));
            result.setVelocity2Unit(CalculationUtils.VELOCITY_UNITS[spinnerVelocity2Units.getSelectedItemPosition()]);
        }
    }

    private void displayResult(CalculationResult result) {
        tvCalculatedParameter.setText(result.getCalculatedParameter() + " =");
        tvResultValue.setText(decimalFormat.format(result.getDisplayValue()) + " " + result.getCalculatedUnit());

        // Si el parametro a calcular es A1, calculamos el gasto del sistema con el resultado obtenido
        if (result.getCalculatedParameter().equals("A₁")) {
            // Calcular el gasto del sistema (flujo volumétrico)
            double flujo = result.getDisplayValue() * result.getVelocity1(); // área1 * velocidad1 (ya convertidos a unidades estándar)
            String flujoFormateado = decimalFormat.format(flujo) + " m³/s"; // Formato estándar
            tvSystemFlowRate.setText("Gasto del sistema: " + flujoFormateado);
        }
        // Si el parametro a calcular es V1, calculamos el gasto con el resultado
        else if (result.getCalculatedParameter().equals("V₁")){
            // Calcular el gasto del sistema (flujo volumétrico)
            double flujo = result.getArea1() * result.getDisplayValue(); // área1 * velocidad1 (ya convertidos a unidades estándar)
            String flujoFormateado = decimalFormat.format(flujo) + " m³/s"; // Formato estándar
            tvSystemFlowRate.setText("Gasto del sistema: " + flujoFormateado);
        }
        else{
            // Calcular el gasto del sistema (flujo volumétrico)
            double flujo = result.getArea1() * result.getVelocity1(); // área1 * velocidad1 (ya convertidos a unidades estándar)
            String flujoFormateado = decimalFormat.format(flujo) + " m³/s"; // Formato estándar
            tvSystemFlowRate.setText("Gasto del sistema: " + flujoFormateado);
        }
        cvResults.setVisibility(View.VISIBLE);

        // Store current result for saving
        currentResult = result;

        // Scroll to show results
        scrollToResults();
    }

    private void scrollToResults() {
        cvResults.post(() -> {
            if (scrollView != null) {
                scrollView.smoothScrollTo(0, cvResults.getBottom());
            }
        });
    }

    private void hideResults() {
        cvResults.setVisibility(View.GONE);
        currentResult = null;
    }

    private void clearAllFields() {
        new AlertDialog.Builder(this)
                .setTitle("Limpiar campos")
                .setMessage("¿Estás seguro de que quieres limpiar todos los campos?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    etArea1.setText("");
                    etVelocity1.setText("");
                    etArea2.setText("");
                    etVelocity2.setText("");
                    hideResults();
                    clearErrorMessages();

                    // Reset spinners to default
                    spinnerArea1Units.setSelection(0);
                    spinnerVelocity1Units.setSelection(0);
                    spinnerArea2Units.setSelection(0);
                    spinnerVelocity2Units.setSelection(0);

                    Toast.makeText(this, "Campos limpiados", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void saveCurrentResult() {
        if (currentResult == null) {
            Toast.makeText(this, "No hay resultado para guardar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to history using HistoryManager
        historyManager.saveCalculation(currentResult);

        Toast.makeText(this, "Resultado guardado en el historial", Toast.LENGTH_SHORT).show();

        // Animate save button
        animateSaveButton();
    }

    private void animateSaveButton() {
        btnSaveResult.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction(() -> {
                    btnSaveResult.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(100);
                });
    }

    private void showFormulaInfo() {
        new AlertDialog.Builder(this)
                .setTitle("Ecuación de Continuidad")
                .setMessage("La ecuación de continuidad establece que:\n\n" +
                        "A₁ × V₁ = A₂ × V₂\n\n" +
                        "Donde:\n" +
                        "• A₁ = Área inicial\n" +
                        "• V₁ = Velocidad inicial\n" +
                        "• A₂ = Área final\n" +
                        "• V₂ = Velocidad final\n\n" +
                        "Esta ecuación se basa en el principio de conservación de masa " +
                        "para fluidos incompresibles.")
                .setPositiveButton("Entendido", null)
                .setIcon(R.drawable.ic_info)
                .show();
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}