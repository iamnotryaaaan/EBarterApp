package com.example.loginactivity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile2 extends AppCompatActivity {
    TextView viewName, viewAdress, viewEmail, viewNumber;
    FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        viewAdress = findViewById(R.id.viewAddress);
        viewName = findViewById(R.id.viewName);
        viewEmail = findViewById(R.id.viewEmail);
        viewNumber = findViewById(R.id.viewNumber);

        viewProfile();

    }

    private void viewProfile() {

        db = FirebaseFirestore.getInstance();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Intent intent = getIntent();
        String user = intent.getStringExtra("user");

        db.collection("users").document(user).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String displayName = documentSnapshot.getString("Name");
                            String displayNumber = documentSnapshot.getString("Number");
                            String displayEmail = documentSnapshot.getString("Email");
                            String displayAddress = documentSnapshot.getString("Address");
                            viewName.setText(displayName);
                            viewNumber.setText(displayNumber);
                            viewEmail.setText(displayEmail);
                            viewAdress.setText(displayAddress);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error fetching document", e);
            }
        });

    }
}
