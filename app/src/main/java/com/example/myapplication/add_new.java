package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class add_new extends AppCompatActivity {

    // تعريف عناصر الواجهة
    private TextInputEditText etNom, etPre, etDta;
    private Button btnSave, btnShowList, btnMain;

    // تعريف كائن قاعدة البيانات
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new); // اسم ملف الـ XML الخاص بالواجهة

        // تهيئة كائن DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // ربط المعرفات (IDs) بالمتغيرات
        etNom = findViewById(R.id.etNom);
        etPre = findViewById(R.id.etPre);
        etDta = findViewById(R.id.etDta);
        btnSave = findViewById(R.id.btnSave);
        btnShowList = findViewById(R.id.btnShowList);
        btnMain = findViewById(R.id.btnMain);

        // 1. فتح تقويم عند الضغط على حقل تاريخ الميلاد
        etDta.setOnClickListener(v -> showDatePicker());

        // 2. برمجة زر حفظ البيانات (إضافة البيانات إلى SQLite)
        btnSave.setOnClickListener(v -> saveAthleteData());

        // 3. برمجة زر عرض الرياضيين
        btnShowList.setOnClickListener(v -> {
            Intent intent = new Intent(add_new.this, ListActivity.class);
            startActivity(intent);
        });

        // 4. برمجة زر الرئيسية
        btnMain.setOnClickListener(v -> {
            Intent intent = new Intent(add_new.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // دالة عرض تقويم اختيار التاريخ (DatePicker)
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                add_new.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // تنسيق التاريخ المُختار: YYYY-MM-DD
                    String dateString = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    etDta.setText(dateString);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // دالة حفظ البيانات والتحقق من صحتها في SQLite
    private void saveAthleteData() {
        String nom = etNom.getText() != null ? etNom.getText().toString().trim() : "";
        String pre = etPre.getText() != null ? etPre.getText().toString().trim() : "";
        String dta = etDta.getText() != null ? etDta.getText().toString().trim() : "";

        // التحقق من تعبئة جميع الحقول
        if (nom.isEmpty() || pre.isEmpty() || dta.isEmpty()) {
            Toast.makeText(this, "يرجى إدخال جميع البيانات المطلوبة!", Toast.LENGTH_SHORT).show();
            return;
        }

        // إدخال البيانات في قاعدة البيانات عبر DatabaseHelper (ترجع long بالـ ID)
        long newId = dbHelper.addStudent(nom, pre, dta);

        if (newId != -1) {
            Toast.makeText(this, "تم حفظ الرياضي بنجاح: " + nom + " " + pre, Toast.LENGTH_LONG).show();

            // إفراغ الحقول بعد الحفظ بنجاح
            etNom.setText("");
            etPre.setText("");
            etDta.setText("");

            // اختياري: الانتقال مباشرة لإضافة اشتراك لهذا الرياضي عبر تمرير الـ ID
            /*
            Intent intent = new Intent(add_new.this, SubscriptionActivity.class);
            intent.putExtra("ATHLETE_ID", (int) newId);
            startActivity(intent);
            */
        } else {
            Toast.makeText(this, "حدث خطأ أثناء حفظ البيانات في قاعدة البيانات!", Toast.LENGTH_SHORT).show();
        }
    }
}