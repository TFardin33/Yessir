package com.example.yessir;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteStudentActivity extends AppCompatActivity {

    private EditText etUsername;
    private TextView tvStudentInfo;
    private Button btnSearch, btnDelete;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_student);

        etUsername = findViewById(R.id.et_username);
        tvStudentInfo = findViewById(R.id.tv_student_info);
        btnSearch = findViewById(R.id.btn_search);
        btnDelete = findViewById(R.id.btn_delete);
        dbHelper = new DatabaseHelper(this);

        btnSearch.setOnClickListener(view -> searchStudent());
        btnDelete.setOnClickListener(view -> deleteStudent());
    }

    private void searchStudent() {
        String username = etUsername.getText().toString().trim();
        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REGISTER_STUDENT + " WHERE " + DatabaseHelper.COL_S_USERNAME + "=?", new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_S_NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_S_EMAIL));
            String dob = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_S_DOB));

            String studentInfo = "Name: " + name + "\nUsername: " + username + "\nEmail: " + email + "\nDOB: " + dob;
            tvStudentInfo.setText(studentInfo);
            tvStudentInfo.setVisibility(TextView.VISIBLE);
            btnDelete.setVisibility(Button.VISIBLE);

            cursor.close();
        } else {
            tvStudentInfo.setText("");
            tvStudentInfo.setVisibility(TextView.GONE);
            btnDelete.setVisibility(Button.GONE);
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void deleteStudent() {
        String username = etUsername.getText().toString().trim();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeletedStudent = db.delete(DatabaseHelper.TABLE_REGISTER_STUDENT, DatabaseHelper.COL_S_USERNAME + "=?", new String[]{username});
        int rowsDeletedInsertStudent = db.delete(DatabaseHelper.TABLE_INSERT_STUDENT, DatabaseHelper.COL_STUDENT_USERNAME + "=?", new String[]{username});

        if (rowsDeletedStudent > 0 && rowsDeletedInsertStudent > 0) {
            Toast.makeText(this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
            tvStudentInfo.setText("");
            tvStudentInfo.setVisibility(TextView.GONE);
            btnDelete.setVisibility(Button.GONE);
        } else {
            Toast.makeText(this, "Failed to delete student", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
