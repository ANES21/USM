package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AthleteAdapter extends RecyclerView.Adapter<AthleteAdapter.ViewHolder> {

    private final List<AthleteModel> athleteList;
    private final OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(AthleteModel athlete, int position);
    }

    public AthleteAdapter(List<AthleteModel> athleteList, OnDeleteClickListener listener) {
        this.athleteList = athleteList;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_athlete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AthleteModel athlete = athleteList.get(position);

        String fullName = athlete.getNom() + " " + athlete.getPre();
        holder.tvFullName.setText(fullName);
        holder.tvBirthDate.setText("تاريخ الميلاد: " + athlete.getDta());

        // حساب تاريخ نهاية الاشتراك تلقائياً
        String endDate = calculateEndDate(athlete.getDtaD(), athlete.getMou());
        holder.tvDuration.setText("تاريخ النهاية: " + endDate);

        // برمجة ضغطة زر الحذف
        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(athlete, position);
            }
        });
    }

    // دالة إضافة عدد الأشهر (MOU) إلى تاريخ بداية الاشتراك (DTA_D)
    private String calculateEndDate(String startDateStr, int months) {
        if (startDateStr == null || startDateStr.isEmpty()) {
            return "غير محدد";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date startDate = sdf.parse(startDateStr);
            Calendar calendar = Calendar.getInstance();
            if (startDate != null) {
                calendar.setTime(startDate);
                calendar.add(Calendar.MONTH, months); // إضافة عدد الأشهر
                return sdf.format(calendar.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return startDateStr; // إرجاع النص الأساسي في حال حدوث خطأ في التنسيق
    }

    @Override
    public int getItemCount() {
        return athleteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName, tvBirthDate, tvDuration;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvBirthDate = itemView.findViewById(R.id.tvBirthDate);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}