package com.example.yessir;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InsertStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_student);

        EditText etName = findViewById(R.id.et_name);
        EditText etUsername = findViewById(R.id.et_username);
        Button btnInsert = findViewById(R.id.btn_insert);

        btnInsert.setOnClickListener(view -> {
            String Name = etName.getText().toString().trim();
            String Username = etUsername.getText().toString().trim();

            if (Name.isEmpty() || Username.isEmpty()) {
                Toast.makeText(InsertStudentActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseHelper dbHelper = new DatabaseHelper(InsertStudentActivity.this);
                boolean isInserted = dbHelper.insertStudent(Name, Username);

                if (isInserted) {
                    Toast.makeText(InsertStudentActivity.this, "Student Inserted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(InsertStudentActivity.this, TeacherActivity.class); // Assuming TeacherActivity is the next activity
                    startActivity(intent);
                } else {
                    Toast.makeText(InsertStudentActivity.this, "Insertion failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
