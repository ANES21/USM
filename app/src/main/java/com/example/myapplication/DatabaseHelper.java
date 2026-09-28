package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // اسم قاعدة البيانات والإصدار
    private static final String DATABASE_NAME = "AthleteManager.db";
    private static final int DATABASE_VERSION = 1;

    // 1. جدول الرياضيين (Student / Athlete)
    public static final String TABLE_STUDENT = "students";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOM = "nom";
    public static final String COLUMN_PRE = "pre";
    public static final String COLUMN_DTA = "dta";

    // 2. جدول الاشتراكات (Subscriptions)
    public static final String TABLE_SUBSCRIPTION = "subscriptions";
    public static final String COLUMN_SUB_ID = "sub_id";
    public static final String COLUMN_SUB_STUDENT_ID = "student_id";
    public static final String COLUMN_SUB_DTA_D = "dta_d";
    public static final String COLUMN_SUB_MOU = "mou";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // إنشاء جدول الرياضيين
        String CREATE_STUDENT_TABLE = "CREATE TABLE " + TABLE_STUDENT + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOM + " TEXT, "
                + COLUMN_PRE + " TEXT, "
                + COLUMN_DTA + " TEXT)";
        db.execSQL(CREATE_STUDENT_TABLE);

        // إنشاء جدول الاشتراكات
        String CREATE_SUBSCRIPTION_TABLE = "CREATE TABLE " + TABLE_SUBSCRIPTION + " ("
                + COLUMN_SUB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SUB_STUDENT_ID + " INTEGER, "
                + COLUMN_SUB_DTA_D + " TEXT, "
                + COLUMN_SUB_MOU + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_SUB_STUDENT_ID + ") REFERENCES " + TABLE_STUDENT + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_SUBSCRIPTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBSCRIPTION);
        onCreate(db);
    }

    // --- 1. إضافة رياضي جديد (ترجع long للمعرف الجديد) ---
    public long addStudent(String nom, String pre, String dta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, nom);
        values.put(COLUMN_PRE, pre);
        values.put(COLUMN_DTA, dta);

        long id = db.insert(TABLE_STUDENT, null, values);
        db.close();
        return id; // ترجع -1 في حال الفشل
    }

    // --- 2. إضافة اشتراك (تحتوي على الترتيبين لتجنب أخطاء Incompatible Types) ---

    // الترتيب الأول: (int studentId, String dtaD, int mou)
    public boolean addSubscription(int studentId, String dtaD, int mou) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUB_STUDENT_ID, studentId);
        values.put(COLUMN_SUB_DTA_D, dtaD);
        values.put(COLUMN_SUB_MOU, mou);

        long result = db.insert(TABLE_SUBSCRIPTION, null, values);
        db.close();
        return result != -1;
    }

    // Overload الترتيب الثاني: (String dtaD, int mou, int studentId) لضمان عدم حدوث خطأ عند تغيير الترتيب
    public boolean addSubscription(String dtaD, int mou, int studentId) {
        return addSubscription(studentId, dtaD, mou);
    }

    // دالة حذف رياضي بجميع اشتراكاته
    public boolean deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // حذف الاشتراكات المرتبطة بالرياضي أولاً
        db.delete(TABLE_SUBSCRIPTION, COLUMN_SUB_STUDENT_ID + " = ?", new String[]{String.valueOf(id)});

        // حذف الرياضي نفسه
        int rowsDeleted = db.delete(TABLE_STUDENT, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        return rowsDeleted > 0;
    }
}