package com.example.yessir;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MarkAttendanceActivity extends AppCompatActivity {

    private ListView lvStudents;
    private Button btnSaveAttendance;
    private ArrayList<String> students;
    private HashMap<String, Boolean> attendanceMap;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        lvStudents = findViewById(R.id.lv_students);
        btnSaveAttendance = findViewById(R.id.btn_save_attendance);
        dbHelper = new DatabaseHelper(this);

        students = dbHelper.getAllStudents(); // Method to get all students from the database
        attendanceMap = new HashMap<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, students);
        lvStudents.setAdapter(adapter);
        lvStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        btnSaveAttendance.setOnClickListener(v -> saveAttendance());
    }

    private void saveAttendance() {
        for (int i = 0; i < lvStudents.getCount(); i++) {
            String student = lvStudents.getItemAtPosition(i).toString();
            boolean isPresent = lvStudents.isItemChecked(i);
            attendanceMap.put(student, isPresent);
        }

        dbHelper.saveAttendance(attendanceMap); // Method to save attendance to the database

        Toast.makeText(this, "Attendance saved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
