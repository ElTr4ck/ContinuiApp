package com.example.continuiapp.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.continuiapp.R;
import com.example.continuiapp.models.CalculationResult;
import com.example.continuiapp.utils.TimeUtils;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final Context context;
    private List<CalculationResult> calculations;
    private OnHistoryItemClickListener listener;
    private DecimalFormat decimalFormat = new DecimalFormat("#.####");

    public interface OnHistoryItemClickListener {
        void onReuseCalculation(CalculationResult calculation);
        void onShareCalculation(CalculationResult calculation);
        void onDeleteCalculation(CalculationResult calculation, int position);
    }

    public HistoryAdapter(Context context, List<CalculationResult> calculations) {
        this.context = context;
        this.calculations = calculations;
    }

    public void setOnHistoryItemClickListener(OnHistoryItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calculation_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        CalculationResult calculation = calculations.get(position);
        holder.bind(calculation, position);
    }

    @Override
    public int getItemCount() {
        return calculations.size();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < calculations.size()) {
            calculations.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, calculations.size());
        }
    }

    public void updateData(List<CalculationResult> newCalculations) {
        this.calculations = newCalculations;
        notifyDataSetChanged();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCalculationType, tvTimestamp, tvResultValue;
        private TextView tvInputA1, tvInputV1, tvInputA2;
        private ImageButton btnMore;
        private MaterialButton btnReuse, btnShare, btnDelete;
        private ImageView ivCalculationIcon;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCalculationType = itemView.findViewById(R.id.tvCalculationType);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvResultValue = itemView.findViewById(R.id.tvResultValue);
            tvInputA1 = itemView.findViewById(R.id.tvInputA1);
            tvInputV1 = itemView.findViewById(R.id.tvInputV1);
            tvInputA2 = itemView.findViewById(R.id.tvInputA2);
            btnMore = itemView.findViewById(R.id.btnMore);
            btnReuse = itemView.findViewById(R.id.btnReuse);
            btnShare = itemView.findViewById(R.id.btnShare);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            ivCalculationIcon = itemView.findViewById(R.id.ivCalculationIcon);
        }

        public void bind(CalculationResult calculation, int position) {
            // Set calculation type
            tvCalculationType.setText(calculation.getCalculationDescription());

            // Set timestamp
            tvTimestamp.setText(TimeUtils.getRelativeTime(calculation.getTimestamp()));

            // Set result value
            tvResultValue.setText(calculation.getFormattedResult());

            // Set input values
            setInputValues(calculation);

            // Set click listeners
            setupClickListeners(calculation, position);
        }

        private void setInputValues(CalculationResult calculation) {
            // Area 1
            if (calculation.getArea1() != null) {
                tvInputA1.setText(String.format("A₁: %s %s",
                        decimalFormat.format(calculation.getArea1()),
                        calculation.getArea1Unit() != null ? calculation.getArea1Unit() : "m²"));
                tvInputA1.setAlpha(1.0f);
            } else {
                tvInputA1.setText("A₁: ---");
                tvInputA1.setAlpha(0.5f);
            }

            // Velocity 1
            if (calculation.getVelocity1() != null) {
                tvInputV1.setText(String.format("V₁: %s %s",
                        decimalFormat.format(calculation.getVelocity1()),
                        calculation.getVelocity1Unit() != null ? calculation.getVelocity1Unit() : "m/s"));
                tvInputV1.setAlpha(1.0f);
            } else {
                tvInputV1.setText("V₁: ---");
                tvInputV1.setAlpha(0.5f);
            }

            // Area 2
            if (calculation.getArea2() != null) {
                tvInputA2.setText(String.format("A₂: %s %s",
                        decimalFormat.format(calculation.getArea2()),
                        calculation.getArea2Unit() != null ? calculation.getArea2Unit() : "m²"));
                tvInputA2.setAlpha(1.0f);
            } else {
                tvInputA2.setText("A₂: ---");
                tvInputA2.setAlpha(0.5f);
            }
        }

        private void setupClickListeners(CalculationResult calculation, int position) {
            // More options menu
            btnMore.setOnClickListener(v -> showMoreOptionsMenu(v, calculation, position));

            // Reuse calculation
            btnReuse.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onReuseCalculation(calculation);
                }
            });

            // Share calculation
            btnShare.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShareCalculation(calculation);
                }
            });

            // Delete calculation
            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteCalculation(calculation, position);
                }
            });
        }

        private void showMoreOptionsMenu(View view, CalculationResult calculation, int position) {
            PopupMenu popup = new PopupMenu(context, view);
            popup.getMenuInflater().inflate(R.menu.history_item_menu, popup.getMenu());

            // Set icons
            popup.getMenu().findItem(R.id.menu_reuse).setIcon(R.drawable.ic_refresh);
            popup.getMenu().findItem(R.id.menu_share).setIcon(R.drawable.ic_share);
            popup.getMenu().findItem(R.id.menu_delete).setIcon(R.drawable.ic_delete);

            // Convertir los valores a constantes
            int reuseId = R.id.menu_reuse;
            int shareId = R.id.menu_share;
            int deleteId = R.id.menu_delete;

            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == reuseId) {
                    if (listener != null) {
                        listener.onReuseCalculation(calculation);
                    }
                    return true;
                } else if (itemId == shareId) {
                    if (listener != null) {
                        listener.onShareCalculation(calculation);
                    }
                    return true;
                } else if (itemId == deleteId) {
                    if (listener != null) {
                        listener.onDeleteCalculation(calculation, position);
                    }
                    return true;
                }
                return false;
            });

            popup.show();
        }
    }
}
