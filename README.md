# USM - Application de Gestion des Abonnements Sportifs 🏋️‍♂️⚽

تطبيق أندرويد لحديثي العهد والنوادي الرياضية لإدارة معلومات الرياضيين والاشتراكات الشهرية بسهولة وسلاسة.

---

## 📱 مميزات التطبيق (Features)

- **إدارة الرياضيين:** إضافة رياضيين جدد مع تسجيل بياناتهم الأساسية (الاسم، اللقب، تاريخ الميلاد).
- **إدارة الاشتراكات:** تسديد واختيار اشتراكات للرياضيين وتحديد تاريخ بداية الاشتراك والمدة بالأشهر.
- **عرض البيانات:** قائمة تفاعلية للرياضيين مع تفاصيل مدة الاشتراكات.
- **حذف البيانات:** إمكانية حذف بيانات الرياضي واشتراكه مباشرة.
- **تصفح مرن:** استخدام القوائم المنسدلة (`AutoCompleteTextView`) لتوفير تجربة إدخال سريعة.

---

## 🛠️ التقنيات المستخدمة (Tech Stack)

* **لغة البرمجة:** Java
* **بيئة التطوير:** Android Studio
* **واجهة المستخدم:** XML, Material Design Components
* **قاعدة البيانات المحلية:** SQLite (`SQLiteOpenHelper`)
* **إدارة المستودع:** Git & GitHub

---

## 📁 هيكل المشروع (Project Architecture)

```text
com.example.myapplication/
│
├── models/
│   └── AthleteModel.java       # نموذج البيانات الخاص بالرياضي والاشتراك
│
├── database/
│   ├── DatabaseHelper.java     # التعامل المباشر مع جداول SQLite
│   └── AthleteRepository.java # مستودع البيانات لتوسيط العرض والبيانات
│
├── adapters/
│   └── AthleteAdapter.java    # ربط البيانات بـ RecyclerView
│
└── views/
    ├── MainActivity.java         # الواجهة الرئيسية
    ├── ListActivity.java         # شاشة عرض القائمة
    ├── AddAthleteActivity.java   # شاشة إضافة رياضي جديد
    └── SubscriptionActivity.java # شاشة إضافة اشتراك
