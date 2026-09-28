package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddNewActivity extends AppCompatActivity {

    // Vmesniški elementi
    private TextInputEditText etNom, etPre, etDta;
    private Button btnSave, btnShowList, btnMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        // Povezava elementov iz XML
        etNom = findViewById(R.id.etNom);
        etPre = findViewById(R.id.etPre);
        etDta = findViewById(R.id.etDta);
        btnSave = findViewById(R.id.btnSave);
        btnShowList = findViewById(R.id.btnShowList);
        btnMain = findViewById(R.id.btnMain);

        // 1. Odpiranje koledarja ob kliku na polje za datum
        etDta.setOnClickListener(v -> showDatePicker());

        // 2. Gumb za shranjevanje podatkov
        btnSave.setOnClickListener(v -> saveAthleteData());

        // 3. Gumb za prikaz seznama športnikov
        btnShowList.setOnClickListener(v -> {
            Intent intent = new Intent(AddNewActivity.this, ListActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Prehod na seznam športnikov", Toast.LENGTH_SHORT).show();
        });

        // 4. Gumb za vrnitev na osnovno stran
        btnMain.setOnClickListener(v -> {
            Intent intent = new Intent(AddNewActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Neobvezno: zapre trenutno aktivnost
        });
    }

    // Prikaz pogovornega okna za izbiro datuma (DatePicker)
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddNewActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Oblika datuma: YYYY-MM-DD
                    String dateString = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    etDta.setText(dateString);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Shranjevanje podatkov in preverjanje vnosov
    private void saveAthleteData() {
        String nom = etNom.getText() != null ? etNom.getText().toString().trim() : "";
        String pre = etPre.getText() != null ? etPre.getText().toString().trim() : "";
        String dta = etDta.getText() != null ? etDta.getText().toString().trim() : "";

        // Preverjanje, ali so vsa polja izpolnjena
        if (nom.isEmpty() || pre.isEmpty() || dta.isEmpty()) {
            Toast.makeText(this, "Prosimo, vnesite vse zahtevane podatke!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Uspešno shranjevanje
        String message = "Športnik shranjen: " + nom + " " + pre;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Počiščenje vnosnih polj
        etNom.setText("");
        etPre.setText("");
        etDta.setText("");
    }
}