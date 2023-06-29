package com.example.gymproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClickForRpeRequest extends AppCompatActivity {

    private Button buttonLogout;
    private FirebaseAuth mAuth;

    private TextView textViewNotification;
    private Button buttonNext;

    private FloatingActionButton fabNext;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rpeRequestsRef = database.getReference("rpe_requests"); // Replace with your Firebase database reference path

    // Get the current date
    Calendar calendar = Calendar.getInstance();
    Date currentDate = calendar.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = dateFormat.format(currentDate);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_for_rpe_request);

        mAuth = FirebaseAuth.getInstance();

        buttonLogout = findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log out the user
                mAuth.signOut();

                // Navigate back to the login activity
                Intent intent = new Intent(ClickForRpeRequest.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        textViewNotification = findViewById(R.id.rpeRequestNotification);
        fabNext = findViewById(R.id.floatingActionButton);

        // Check if there is an RPE request
        boolean hasRpeRequest = checkRpeRequest();

        // Display appropriate message based on the request status
        if (hasRpeRequest) {
            textViewNotification.setText("You have an RPE request.");

            textViewNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Perform action when the TextView is clicked
                    Intent intent = new Intent(ClickForRpeRequest.this, ReportTypes.class);
                    startActivity(intent);
                }
            });
        } else {
            textViewNotification.setText("You have no requests.");
        }

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the FAB click event
                startActivity(new Intent(ClickForRpeRequest.this, SessionActivity.class));
            }
        });
    }

    private boolean checkRpeRequest() {
        // Retrieve the last updated date from your database or any other data source
        String lastUpdatedDate = ""; // Replace with your code to retrieve the last updated date

        // Compare the last updated date with the current date
        if (lastUpdatedDate.equals(formattedDate)) {
            // The request has been updated today, return false
            return false;
        } else {
            // The request has not been updated today, return true
            return true;
        }
    }
}
