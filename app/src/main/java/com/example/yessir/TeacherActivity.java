package com.example.yessir;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TeacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        Button btnInsert = findViewById(R.id.btn_insert_student);
        Button btnDLT = findViewById(R.id.btn_dlt_student);
        Button btnActivity = findViewById(R.id.btn_other_activity);

        btnInsert.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherActivity.this, InsertStudentActivity.class);
            startActivity(intent);

        });
        btnDLT.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherActivity.this, DeleteStudentActivity.class);
            startActivity(intent);

        });
        btnActivity.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherActivity.this, OtherActivity.class);
            startActivity(intent);

        });
    }
}