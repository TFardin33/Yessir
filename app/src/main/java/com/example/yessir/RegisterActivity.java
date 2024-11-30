package com.example.yessir;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnTeacher = findViewById(R.id.btn_teacher);
        Button btnStudent = findViewById(R.id.btn_student);

        btnTeacher.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, TeacherRegisterActivity.class);   //Assuming AdminHomeActivity is the activity after login as admin
            startActivity(intent);

        });
        btnStudent.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, StudentRegisterActivity.class);   //Assuming AdminHomeActivity is the activity after login as admin
            startActivity(intent);
        });
    }
}

