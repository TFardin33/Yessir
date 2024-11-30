package com.example.yessir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "YesSir_DB";
    public static final int DATABASE_VERSION = 1;
    // Teacher
    public static final String TABLE_REGISTER = "register";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_DOB = "DOB";
    public static final String COL_PASSWORD = "password";

    // Student
    public static final String TABLE_INSERT_STUDENT = "Inserted_student";
    public static final String COL_STUDENT_NAME = "student_name";
    public static final String COL_STUDENT_USERNAME = "student_username";
    public static final String TABLE_REGISTER_STUDENT = "registered_student";
    public static final String COL_S_ID = "s_id";
    public static final String COL_S_NAME = "s_name";
    public static final String COL_S_USERNAME = "s_username";
    public static final String COL_S_EMAIL = "s_email";
    public static final String COL_S_DOB = "s_DOB";
    public static final String COL_S_PASSWORD = "s_password";

    // class
    public static final String TABLE_CLASS = "class";
    public static final String COL_CLASS_ID = "class_id";
    public static final String COL_CLASS_NAME = "class_name";
    public static final String COL_CLASS_CODE = "class_code";

    //add student to class
    public static final String TABLE_CLASS_STUDENTS = "class_students";
    public static final String COL_SCLASS_CODE = "class_code";
    public static final String COL_SUSERNAME = "username";

    // attendance
    public static final String TABLE_ATTENDANCE = "attendance_table";
    public static final String COL_ATTENDANCE_ID = "attendance_id";
    public static final String COL_ATTENDANCE_DATE = "attendance_date";
    public static final String COL_ATTENDANCE_STATUS = "attendance_status";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_REGISTER + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_USERNAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_DOB + " TEXT, " +
                COL_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_INSERT_STUDENT + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_NAME + " TEXT, " +
                COL_STUDENT_USERNAME + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_REGISTER_STUDENT + " (" +
                COL_S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_S_NAME + " TEXT, " +
                COL_S_USERNAME + " TEXT, " +
                COL_S_EMAIL + " TEXT, " +
                COL_S_DOB + " TEXT, " +
                COL_S_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_CLASS + " (" +
                COL_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CLASS_NAME + " TEXT, " +
                COL_CLASS_CODE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_CLASS_STUDENTS + " (" +
                COL_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SCLASS_CODE + " TEXT, " +
                COL_SUSERNAME + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                COL_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CLASS_ID + " INTEGER, " +
                COL_S_ID + " INTEGER, " +
                COL_ATTENDANCE_DATE + " TEXT, " +
                COL_ATTENDANCE_STATUS + " INTEGER, " +
                "FOREIGN KEY(" + COL_CLASS_ID + ") REFERENCES " + TABLE_CLASS + "(" + COL_CLASS_ID + "), " +
                "FOREIGN KEY(" + COL_S_ID + ") REFERENCES " + TABLE_REGISTER_STUDENT + "(" + COL_S_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db ,int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSERT_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS_STUDENTS);


        onCreate(db);
    }

    public boolean insertTeacher(String name, String username, String email, String dob, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_DOB, dob);
        contentValues.put(COL_PASSWORD, password);

        long result =  db.insert(TABLE_REGISTER, null, contentValues);
        //result value, if inserted, then "row number"
        //result value, if not inserted, then -1

        return result != -1;
    }

    public boolean insertStudent(String name, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STUDENT_NAME, name);
        contentValues.put(COL_STUDENT_USERNAME, username);

        long result =  db.insert(TABLE_INSERT_STUDENT, null, contentValues);
        //result value, if inserted, then "row number"
        //result value, if not inserted, then -1

        return result != -1;
    }

    public boolean registerStudent(String name, String username, String email, String dob, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_S_NAME, name);
        contentValues.put(COL_S_USERNAME, username);
        contentValues.put(COL_S_EMAIL, email);
        contentValues.put(COL_S_DOB, dob);
        contentValues.put(COL_S_PASSWORD, password);

        long result =  db.insert(TABLE_REGISTER_STUDENT, null, contentValues);
        //result value, if inserted, then "row number"
        //result value, if not inserted, then -1

        return result != -1;
    }

    public boolean createClass(String className, String classCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_CLASS_NAME, className);
        contentValues.put(COL_CLASS_CODE, classCode);

        long result =  db.insert(TABLE_CLASS, null, contentValues);
        return result != -1;
    }

    public void addStudentToClass(String classCode, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_CLASS_NAME, classCode);
        contentValues.put(COL_CLASS_CODE, username);

        db.insert(TABLE_CLASS_STUDENTS, null, contentValues);
        db.close();
    }

    public ArrayList<String> getAllStudents() {
        ArrayList<String> students = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT s_username FROM " + TABLE_REGISTER_STUDENT, null);
        if (cursor.moveToFirst()) {
            do {
                students.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return students;
    }

    public void saveAttendance(HashMap<String, Boolean> attendanceMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Map.Entry<String, Boolean> entry : attendanceMap.entrySet()) {
            ContentValues values = new ContentValues();
            values.put("attendance_id", entry.getKey());
            values.put("attendance_status", entry.getValue() ? 1 : 0); // 1 for present, 0 for absent
            db.insert("attendance_table", null, values);
        }
        db.close();
    }

    public ArrayList<String> getAttendanceRecords() {
        ArrayList<String> attendanceRecords = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT attendance_id, attendance_status FROM attendance_table", null);
        if (cursor.moveToFirst()) {
            do {
                String record = cursor.getString(0) + ": " + (cursor.getInt(1) == 1 ? "Present" : "Absent");
                attendanceRecords.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return attendanceRecords;
    }
}
