package com.example.myapplication;

public class AthleteModel {
    private int id;
    private String nom;
    private String pre;
    private String dta;  // تاريخ الميلاد
    private String dtaD; // تاريخ بداية الاشتراك
    private int mou;     // مدة الاشتراك بالأشهر

    // 1. منشئ جديد يستقبل 4 معلمات (يحل المشكلة في SubscriptionActivity)
    public AthleteModel(int id, String nom, String pre, String dta) {
        this.id = id;
        this.nom = nom;
        this.pre = pre;
        this.dta = dta;
    }

    // 2. منشئ يستقبل جميع البيانات مع الـ ID (للاستخدام في ListActivity)
    public AthleteModel(int id, String nom, String pre, String dta, String dtaD, int mou) {
        this.id = id;
        this.nom = nom;
        this.pre = pre;
        this.dta = dta;
        this.dtaD = dtaD;
        this.mou = mou;
    }

    // 3. منشئ يستقبل البيانات بدون ID
    public AthleteModel(String nom, String pre, String dta, String dtaD, int mou) {
        this.nom = nom;
        this.pre = pre;
        this.dta = dta;
        this.dtaD = dtaD;
        this.mou = mou;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPre() { return pre; }
    public String getDta() { return dta; }
    public String getDtaD() { return dtaD; }
    public int getMou() { return mou; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPre(String pre) { this.pre = pre; }
    public void setDta(String dta) { this.dta = dta; }
    public void setDtaD(String dtaD) { this.dtaD = dtaD; }
    public void setMou(int mou) { this.mou = mou; }
}