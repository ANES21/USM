package com.example.myapplication;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SubscriptionActivity extends AppCompatActivity {

    private AutoCompleteTextView actvStudent;
    private TextInputEditText etDtaD, etMou;
    private Button btnSaveSubscription;
    private DatabaseHelper dbHelper;

    private final List<AthleteModel> athleteList = new ArrayList<>();
    private final List<String> athleteNames = new ArrayList<>();
    private int selectedStudentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        dbHelper = new DatabaseHelper(this);

        // ربط عناصر الواجهة
        actvStudent = findViewById(R.id.actvStudent);
        etDtaD = findViewById(R.id.etDtaD);
        etMou = findViewById(R.id.etMou);
        btnSaveSubscription = findViewById(R.id.btnSaveSubscription);

        // تعيين تاريخ اليوم تلقائياً كقيمة افتراضية
        setCurrentDate();

        // 1. تحميل قائمة الرياضيين داخل القائمة المنسدلة
        loadAthletesToDropdown();

        // 2. إظهار التقويم عند الضغط على حقل تاريخ بداية الاشتراك
        etDtaD.setOnClickListener(v -> showDatePicker(etDtaD));

        // 3. إظهار التقويم عند الضغط على حقل مدة/تاريخ نهاية الاشتراك
        etMou.setOnClickListener(v -> showDatePicker(etMou));

        // 4. حفظ الاشتراك
        btnSaveSubscription.setOnClickListener(v -> saveSubscription());
    }

    // دالة وضع تاريخ اليوم بتنسيق YYYY-MM-DD
    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String currentDate = sdf.format(new Date());
        etDtaD.setText(currentDate);
    }

    private void loadAthletesToDropdown() {
        athleteList.clear();
        athleteNames.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_STUDENT, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String nom = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOM));
                String pre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRE));
                String dta = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DTA));

                AthleteModel athlete = new AthleteModel(id, nom, pre, dta);
                athleteList.add(athlete);
                athleteNames.add(nom + " " + pre + " (ID: " + id + ")");
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, athleteNames);
        actvStudent.setAdapter(adapter);

        actvStudent.setOnItemClickListener((parent, view, position, id) -> {
            selectedStudentId = athleteList.get(position).getId();
        });
    }

    // دالة موحدة لإظهار التقويم وتحديث الحقل المستهدف
    private void showDatePicker(TextInputEditText targetEditText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SubscriptionActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String dateString = String.format(Locale.US, "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    targetEditText.setText(dateString);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void saveSubscription() {
        String dtaD = etDtaD.getText() != null ? etDtaD.getText().toString().trim() : "";
        String mouStr = etMou.getText() != null ? etMou.getText().toString().trim() : "";

        if (selectedStudentId == -1) {
            Toast.makeText(this, "يرجى اختيار رياضي من القائمة!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dtaD.isEmpty() || mouStr.isEmpty()) {
            Toast.makeText(this, "يرجى إدخال تاريخ بداية الاشتراك ومدته!", Toast.LENGTH_SHORT).show();
            return;
        }

        int mou;
        try {
            mou = Integer.parseInt(mouStr);
        } catch (NumberFormatException e) {
            mou = 1; // قيمة افتراضية عند الفشل أو إدخال تاريخ
        }

        boolean isInserted = dbHelper.addSubscription(dtaD, mou, selectedStudentId);

        if (isInserted) {
            Toast.makeText(this, "تم حفظ الاشتراك بنجاح!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "حدث خطأ أثناء حفظ الاشتراك", Toast.LENGTH_SHORT).show();
        }
    }
}