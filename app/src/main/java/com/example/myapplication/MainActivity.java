package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnAddAthlete, btnListAthletes, btnSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddAthlete = findViewById(R.id.btnAddAthlete);
        btnListAthletes = findViewById(R.id.btnListAthletes);
        btnSubscriptions = findViewById(R.id.btnSubscriptions);

        // 1. الانتقال لشاشة إضافة رياضي
        btnAddAthlete.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, add_new.class);
            startActivity(intent);
        });

        // 2. الانتقال لشاشة قائمة الرياضيين
        btnListAthletes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });

        // 3. برمجة زر الاشتراكات (حسب الحاجة)
        btnSubscriptions.setOnClickListener(v -> {
             Intent intent = new Intent(MainActivity.this, SubscriptionActivity.class);
             startActivity(intent);
        });
    }
}