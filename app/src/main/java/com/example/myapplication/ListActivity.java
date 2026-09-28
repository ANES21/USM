package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AthleteAdapter adapter;
    private List<AthleteModel> athleteList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerViewAthletes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        athleteList = new ArrayList<>();

        // تهيئة الـ Adapter وتمريره لـ RecyclerView
        adapter = new AthleteAdapter(athleteList, (athlete, position) -> showDeleteConfirmationDialog(athlete, position));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // إعادة قراءة البيانات وتحديث الشاشة تلقائياً عند العودة إليها
        loadAthletesFromDatabase();
    }

    private void loadAthletesFromDatabase() {
        athleteList.clear(); // تنظيف القائمة قبل الإضافة
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // استعلام يجلب بيانات الرياضي مع آخر اشتراك مُسجل له دون تكرار
        String query = "SELECT s." + DatabaseHelper.COLUMN_ID + ", " +
                "s." + DatabaseHelper.COLUMN_NOM + ", " +
                "s." + DatabaseHelper.COLUMN_PRE + ", " +
                "s." + DatabaseHelper.COLUMN_DTA + ", " +
                "sub." + DatabaseHelper.COLUMN_SUB_DTA_D + ", " +
                "sub." + DatabaseHelper.COLUMN_SUB_MOU + " " +
                "FROM " + DatabaseHelper.TABLE_STUDENT + " s " +
                "LEFT JOIN " + DatabaseHelper.TABLE_SUBSCRIPTION + " sub " +
                "ON s." + DatabaseHelper.COLUMN_ID + " = sub." + DatabaseHelper.COLUMN_SUB_STUDENT_ID + " " +
                "GROUP BY s." + DatabaseHelper.COLUMN_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String nom = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOM));
                String pre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRE));
                String dta = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DTA));

                int dtaDIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUB_DTA_D);
                int mouIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUB_MOU);

                String dtaD = (dtaDIndex != -1 && !cursor.isNull(dtaDIndex)) ? cursor.getString(dtaDIndex) : "";
                int mou = (mouIndex != -1 && !cursor.isNull(mouIndex)) ? cursor.getInt(mouIndex) : 0;

                athleteList.add(new AthleteModel(id, nom, pre, dta, dtaD, mou));
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "لا يوجد رياضيين مسجلين حالياً", Toast.LENGTH_SHORT).show();
        }
        cursor.close();

        // إعلام الـ Adapter بوجود تحديث للبيانات
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    // نافذة تأكيد الحذف
    private void showDeleteConfirmationDialog(AthleteModel athlete, int position) {
        new AlertDialog.Builder(this)
                .setTitle("تأكيد الحذف")
                .setMessage("هل أنت تأكد من حذف الرياضي " + athlete.getNom() + " " + athlete.getPre() + "؟")
                .setPositiveButton("حذف", (dialog, which) -> {
                    // حذف من SQLite باستخدام id الرياضي
                    boolean isDeleted = dbHelper.deleteStudent(athlete.getId());
                    if (isDeleted) {
                        // حذف من القائمة المحلية وتحديث الـ RecyclerView
                        athleteList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, athleteList.size());
                        Toast.makeText(ListActivity.this, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ListActivity.this, "فشل الحذف من قاعدة البيانات", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }
}