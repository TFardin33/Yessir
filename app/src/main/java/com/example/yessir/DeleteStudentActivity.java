package com.example.yessir;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

        // Initialize UI elements and DatabaseHelper
        etUsername = findViewById(R.id.et_username);
        tvStudentInfo = findViewById(R.id.tv_student_info);
        btnSearch = findViewById(R.id.btn_search);
        btnDelete = findViewById(R.id.btn_delete);
        dbHelper = new DatabaseHelper(this);

        // Set button click listeners
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
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_REGISTER_STUDENT +
                        " WHERE " + DatabaseHelper.COL_S_USERNAME + "=?",
                new String[]{username}
        );

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

        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Delete from TABLE_REGISTER_STUDENT
        int rowsDeletedFromRegister = db.delete(
                DatabaseHelper.TABLE_REGISTER_STUDENT,
                DatabaseHelper.COL_S_USERNAME + "=?",
                new String[]{username}
        );

        // Delete from TABLE_INSERT_STUDENT
        int rowsDeletedFromInsert = db.delete(
                DatabaseHelper.TABLE_INSERT_STUDENT,
                DatabaseHelper.COL_STUDENT_USERNAME + "=?",
                new String[]{username}
        );


        Log.d("DeleteStudent", "Rows deleted from Register: " + rowsDeletedFromRegister);
        Log.d("DeleteStudent", "Rows deleted from Insert: " + rowsDeletedFromInsert);

        // Check deletion successful
        if (rowsDeletedFromRegister > 0 || rowsDeletedFromInsert > 0) {
            Toast.makeText(this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
            tvStudentInfo.setText("");
            tvStudentInfo.setVisibility(TextView.GONE);
            btnDelete.setVisibility(Button.GONE);
        } else {
            Toast.makeText(this, "Failed to delete student. Student may not exist.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
