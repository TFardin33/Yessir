package com.example.yessir;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etUsername = findViewById(R.id.et_username);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this);
                if (checkTeacher(dbHelper, username, password)) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, TeacherActivity.class); // Assuming TeacherActivity is the activity after login as a teacher
                    startActivity(intent);
                } else if (checkStudent(dbHelper, username, password)) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, StudentActivity.class); // Assuming StudentActivity is the activity after login as a student
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); // Assuming RegisterActivity is the registration activity
            startActivity(intent);
        });
    }

    private boolean checkStudent(DatabaseHelper dbHelper, String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REGISTER_STUDENT + " WHERE " + DatabaseHelper.COL_S_USERNAME + "=? AND " + DatabaseHelper.COL_S_PASSWORD + "=?", new String[]{username, password});
        boolean studentExists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return studentExists;
    }

    private boolean checkTeacher(DatabaseHelper dbHelper, String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REGISTER + " WHERE " + DatabaseHelper.COL_USERNAME + "=? AND " + DatabaseHelper.COL_PASSWORD + "=?", new String[]{username, password});
        boolean teacherExists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return teacherExists;
    }
}
