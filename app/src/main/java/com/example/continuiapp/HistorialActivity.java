package com.example.continuiapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.continuiapp.models.HistoryAdapter;
import com.example.continuiapp.models.CalculationResult;
import com.example.continuiapp.utils.HistoryManager;
import com.example.continuiapp.utils.TimeUtils;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistorialActivity extends AppCompatActivity implements HistoryAdapter.OnHistoryItemClickListener {

    private ImageButton btnBack, btnClearAll;
    private RecyclerView rvHistory;
    private LinearLayout llEmptyState;
    private CardView cvStatistics;
    private MaterialButton btnStartCalculating;
    private TextView tvTotalCalculations, tvTodayCalculations, tvWeekCalculations;

    private HistoryAdapter historyAdapter;
    private HistoryManager historyManager;
    private List<CalculationResult> calculations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_historial);

        initializeViews();
        setupRecyclerView();
        setupListeners();
        loadHistoryData();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnClearAll = findViewById(R.id.btnClearAll);
        rvHistory = findViewById(R.id.rvHistory);
        llEmptyState = findViewById(R.id.llEmptyState);
        cvStatistics = findViewById(R.id.cvStatistics);
        btnStartCalculating = findViewById(R.id.btnStartCalculating);
        tvTotalCalculations = findViewById(R.id.tvTotalCalculations);
        tvTodayCalculations = findViewById(R.id.tvTodayCalculations);
        tvWeekCalculations = findViewById(R.id.tvWeekCalculations);

        historyManager = new HistoryManager(this);
    }

    private void setupRecyclerView() {
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryAdapter(this, calculations);
        historyAdapter.setOnHistoryItemClickListener(this);
        rvHistory.setAdapter(historyAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnClearAll.setOnClickListener(v -> showClearAllDialog());

        btnStartCalculating.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalculoDetalladoActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadHistoryData() {
        calculations = historyManager.getAllCalculations();

        if (calculations.isEmpty()) {
            showEmptyState();
        } else {
            showHistoryList();
            updateStatistics();
        }

        historyAdapter.updateData(calculations);
    }

    private void showEmptyState() {
        llEmptyState.setVisibility(View.VISIBLE);
        rvHistory.setVisibility(View.GONE);
        cvStatistics.setVisibility(View.GONE);
        btnClearAll.setVisibility(View.GONE);
    }

    private void showHistoryList() {
        llEmptyState.setVisibility(View.GONE);
        rvHistory.setVisibility(View.VISIBLE);
        cvStatistics.setVisibility(View.VISIBLE);
        btnClearAll.setVisibility(View.VISIBLE);
    }

    private void updateStatistics() {
        int total = calculations.size();
        int today = getTodayCalculationsCount();
        int thisWeek = getThisWeekCalculationsCount();

        tvTotalCalculations.setText(String.valueOf(total));
        tvTodayCalculations.setText(String.valueOf(today));
        tvWeekCalculations.setText(String.valueOf(thisWeek));
    }

    private int getTodayCalculationsCount() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Date startOfDay = today.getTime();

        int count = 0;
        for (CalculationResult calc : calculations) {
            if (calc.getTimestamp().after(startOfDay)) {
                count++;
            }
        }
        return count;
    }

    private int getThisWeekCalculationsCount() {
        Calendar weekStart = Calendar.getInstance();
        weekStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        weekStart.set(Calendar.HOUR_OF_DAY, 0);
        weekStart.set(Calendar.MINUTE, 0);
        weekStart.set(Calendar.SECOND, 0);
        weekStart.set(Calendar.MILLISECOND, 0);
        Date startOfWeek = weekStart.getTime();

        int count = 0;
        for (CalculationResult calc : calculations) {
            if (calc.getTimestamp().after(startOfWeek)) {
                count++;
            }
        }
        return count;
    }

    private void showClearAllDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Limpiar Historial")
                .setMessage("¿Estás seguro de que quieres eliminar todo el historial de cálculos? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar Todo", (dialog, which) -> {
                    historyManager.clearAllHistory();
                    calculations.clear();
                    historyAdapter.updateData(calculations);
                    showEmptyState();
                    Toast.makeText(this, "Historial eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .setIcon(R.drawable.ic_warning)
                .show();
    }

    // HistoryAdapter.OnHistoryItemClickListener implementation
    @Override
    public void onReuseCalculation(CalculationResult calculation) {
        Intent intent = new Intent(this, CalculoDetalladoActivity.class);
        intent.putExtra("reuse_calculation", true);
        intent.putExtra("calculation_id", calculation.getId());
        startActivity(intent);
    }

    @Override
    public void onShareCalculation(CalculationResult calculation) {
        String shareText = createShareText(calculation);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Resultado de Cálculo - ContinuiApp");

        startActivity(Intent.createChooser(shareIntent, "Compartir cálculo"));
    }

    @Override
    public void onDeleteCalculation(CalculationResult calculation, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Cálculo")
                .setMessage("¿Estás seguro de que quieres eliminar este cálculo?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    historyManager.deleteCalculation(calculation.getId());
                    historyAdapter.removeItem(position);

                    if (calculations.isEmpty()) {
                        showEmptyState();
                    } else {
                        updateStatistics();
                    }

                    Toast.makeText(this, "Cálculo eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private String createShareText(CalculationResult calculation) {
        StringBuilder sb = new StringBuilder();
        sb.append("📊 Resultado de Cálculo - FlowCalc\n\n");
        sb.append("🔢 ").append(calculation.getCalculationDescription()).append("\n");
        sb.append("📈 Resultado: ").append(calculation.getFormattedResult()).append("\n\n");
        sb.append("📋 Valores utilizados:\n");

        if (calculation.getArea1() != null) {
            sb.append("• A₁: ").append(calculation.getArea1()).append(" ").append(calculation.getArea1Unit()).append("\n");
        }
        if (calculation.getVelocity1() != null) {
            sb.append("• V₁: ").append(calculation.getVelocity1()).append(" ").append(calculation.getVelocity1Unit()).append("\n");
        }
        if (calculation.getArea2() != null) {
            sb.append("• A₂: ").append(calculation.getArea2()).append(" ").append(calculation.getArea2Unit()).append("\n");
        }
        if (calculation.getVelocity2() != null) {
            sb.append("• V₂: ").append(calculation.getVelocity2()).append(" ").append(calculation.getVelocity2Unit()).append("\n");
        }

        sb.append("\n🕐 Calculado: ").append(TimeUtils.getFormattedDate(calculation.getTimestamp()));
        sb.append("\n\n📱 Generado con FlowCalc Pro");

        return sb.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        loadHistoryData();
    }
}