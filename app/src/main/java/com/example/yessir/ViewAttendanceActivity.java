package com.example.yessir;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewAttendanceActivity extends AppCompatActivity {

    private ListView lvAttendance;
    private DatabaseHelper dbHelper;
    private ArrayList<String> attendanceRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        lvAttendance = findViewById(R.id.lv_attendance);
        dbHelper = new DatabaseHelper(this);

        attendanceRecords = dbHelper.getAttendanceRecords(); // Method to get all attendance records from the database

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, attendanceRecords);
        lvAttendance.setAdapter(adapter);
    }
}
