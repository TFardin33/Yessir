package com.example.yessir;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class OtherActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        dbHelper = new DatabaseHelper(this);

        Button btnCreateClass = findViewById(R.id.btn_create_class);
        Button btnAddStudent = findViewById(R.id.btn_add_student);
        Button btnMarkAttendance = findViewById(R.id.btn_mark_attendance);
        Button btnViewAttendance = findViewById(R.id.btn_view_attendance);

        btnCreateClass.setOnClickListener(v -> createClass());
        btnAddStudent.setOnClickListener(v -> addStudentToClass());
        btnMarkAttendance.setOnClickListener(v -> markAttendance());
        btnViewAttendance.setOnClickListener(v -> viewAttendance());
    }

    private void createClass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Class");

        final EditText inputClassName = new EditText(this);
        inputClassName.setHint("Class Name");

        final EditText inputClassCode = new EditText(this);
        inputClassCode.setHint("Class Code");

        builder.setView(inputClassName);
        builder.setView(inputClassCode);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String className = inputClassName.getText().toString().trim();
            String classCode = inputClassCode.getText().toString().trim();

            if (!className.isEmpty() && !classCode.isEmpty()) {
                boolean insert = dbHelper.createClass(className, classCode);
                if(insert) {
                    Toast.makeText(this, "Class created successfully", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addStudentToClass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Student to Class");

        final EditText inputClassCode = new EditText(this);
        inputClassCode.setHint("Class Code");

        final EditText inputUsername = new EditText(this);
        inputUsername.setHint("Student Username");

        builder.setView(inputClassCode);
        builder.setView(inputUsername);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String classCode = inputClassCode.getText().toString().trim();
            String username = inputUsername.getText().toString().trim();

            if (!classCode.isEmpty() && !username.isEmpty()) {
                if (isStudentRegistered(username)) {
                    dbHelper.addStudentToClass(classCode, username);
                    Toast.makeText(this, "Student added to class", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private boolean isStudentRegistered(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REGISTER_STUDENT + " WHERE " + DatabaseHelper.COL_S_USERNAME + "=?", new String[]{username});
        boolean studentExists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return studentExists;
    }

    private void markAttendance() {
        Intent intent = new Intent(this, MarkAttendanceActivity.class);
        startActivity(intent);
    }

    private void viewAttendance() {
        Intent intent = new Intent(this, ViewAttendanceActivity.class);
        startActivity(intent);
    }
}
