package com.example.gymproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gymproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    private EditText firstNameEditText, secondNameEditText, emailEditText, passwordEditText;
    private Button signupButton;

    private FirebaseFirestore db;

    private Button btnBack;

    FirebaseAuth mAuth;

    private String selectedRole;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        // Get references to views
        firstNameEditText = findViewById(R.id.editTextTextPersonName3);
        secondNameEditText = findViewById(R.id.editTextTextPersonName5);
        emailEditText = findViewById(R.id.editTextTextEmailAddress5);
        passwordEditText = findViewById(R.id.editTextPassword7);
        signupButton = findViewById(R.id.button3);

        Spinner spinnerRole = findViewById(R.id.spinnerRole);
//
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
//        spinnerRole.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);



        btnBack=findViewById(R.id.backlogin);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Signup.this,MainActivity.class);
                startActivity(intent);
            }
        });



        // Set click listener for signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input from EditText fields

                String firstName = firstNameEditText.getText().toString().trim();
                String secondName = secondNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String selectedRole = spinnerRole.getSelectedItem().toString();






                // Create a map with user data
                Map<String, Object> user = new HashMap<>();
                user.put("firstName", firstName);
                user.put("secondName", secondName);
                user.put("email", email);
                user.put("password", password);
                user.put("usertype",selectedRole);

                if (TextUtils.isEmpty(email)){
                    emailEditText.setError("Email cannot be empty");
                    emailEditText.requestFocus();
                }else if (TextUtils.isEmpty(password)){
                    passwordEditText.setError("Password cannot be empty");
                    passwordEditText.requestFocus();
                }else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Signup.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Signup.this, MainActivity.class));
                            } else {
                                Toast.makeText(Signup.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // Add user data to Firestore
                    db.collection("PlayerDetails")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(Signup.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                    finish(); // Finish the Signup activity
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Signup.this, "Error creating account. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
