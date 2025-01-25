package com.example.yessir;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TeacherRegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth; // Firebase Authentication instance
    private FirebaseFirestore firestore; // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        // Initialize FirebaseAuth, Firestore, and SQLite Helper
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Views
        EditText etName = findViewById(R.id.et_name);
        EditText etUsername = findViewById(R.id.et_username);
        EditText etEmail = findViewById(R.id.et_email);
        EditText etDOB = findViewById(R.id.et_dob);
        EditText etPassword = findViewById(R.id.et_password);
        EditText etCPassword = findViewById(R.id.et_con_password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);

        // Login button action
        btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherRegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Register button action
        btnRegister.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String dob = etDOB.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String cPassword = etCPassword.getText().toString().trim();

            // Validate inputs
            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || dob.isEmpty() || password.isEmpty() || cPassword.isEmpty()) {
                Toast.makeText(TeacherRegisterActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(cPassword)) {
                Toast.makeText(TeacherRegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            } else {
                // Register user in Firebase Authentication
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Send email verification
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(emailTask -> {
                                                if (emailTask.isSuccessful()) {
                                                    Toast.makeText(TeacherRegisterActivity.this, "Verification email sent. Please verify and log in.", Toast.LENGTH_SHORT).show();

                                                    // Save user data in Firestore and SQLite
                                                    saveTeacherDataToFirestore(name, username, email, dob);
                                                    saveTeacherDataToSQLite(name, username, email, dob, password);

                                                    // Navigate to Login Activity
                                                    Intent intent = new Intent(TeacherRegisterActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(TeacherRegisterActivity.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(TeacherRegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void saveTeacherDataToFirestore(String name, String username, String email, String dob) {
        Map<String, Object> teacherData = new HashMap<>();
        teacherData.put("name", name);
        teacherData.put("username", username);
        teacherData.put("email", email);
        teacherData.put("dob", dob);

        firestore.collection("teachers")
                .add(teacherData)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(TeacherRegisterActivity.this, "Teacher data saved to Firestore successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(TeacherRegisterActivity.this, "Failed to save teacher data to Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveTeacherDataToSQLite(String name, String username, String email, String dob, String password) {
        DatabaseHelper dbHelper = new DatabaseHelper(TeacherRegisterActivity.this);
        boolean isInserted = dbHelper.insertTeacher(name, username, email, dob, password);
        if (isInserted) {
            Toast.makeText(this, "Teacher data saved to SQLite successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save teacher data to SQLite", Toast.LENGTH_SHORT).show();
        }
    }
}
