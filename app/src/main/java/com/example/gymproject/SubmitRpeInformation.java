package com.example.gymproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SubmitRpeInformation extends AppCompatActivity {

    private EditText editTextTime;
    private Button sendbtn;
    private EditText editTextDate;
    private EditText editTextDuration;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_rpe_information);

        editTextTime = findViewById(R.id.editTextTime);
        sendbtn = findViewById(R.id.sendbtn);
        editTextDate = findViewById(R.id.editTextDate);
        editTextDuration = findViewById(R.id.editTextDuration);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the authenticated user from Firebase Authentication
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    String firstName = user.getDisplayName();
                    String email = user.getEmail();

                    // Retrieve additional details from the "PlayerDetails" collection
                    DocumentReference playerDetailsRef = db.collection("PlayerDetails").document(user.getUid());
                    playerDetailsRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String secondName = documentSnapshot.getString("secondName");

                                String time = editTextTime.getText().toString().trim();
                                String date = editTextDate.getText().toString().trim();
                                String duration = editTextDuration.getText().toString().trim();

                                // Perform the logic to send the information to the coach
                                if (!time.isEmpty() && !date.isEmpty() && !duration.isEmpty()) {
                                    // Create a document with an automatically generated ID
                                    RpeInformation rpeInformation = new RpeInformation(firstName, secondName, email, time, date, duration);
                                    db.collection("RpeInformation")
                                            .add(rpeInformation)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(SubmitRpeInformation.this, "Information sent to the coach", Toast.LENGTH_SHORT).show();
                                                    finish(); // Optionally, navigate back to the previous activity
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SubmitRpeInformation.this, "Failed to send information", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(SubmitRpeInformation.this, "Please enter all the information", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SubmitRpeInformation.this, "Player details not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SubmitRpeInformation.this, "Failed to retrieve player details", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // User is not authenticated, handle this case accordingly
                }
            }
        });
    }
}
