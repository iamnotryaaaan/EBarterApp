package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePage extends AppCompatActivity {

    Button logout, transact;
    FirebaseAuth auth;
    FirebaseFirestore fs;
    String email;
    TextView dispName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        logout = findViewById(R.id.logout);
        dispName = findViewById(R.id.display_name);
        transact = findViewById(R.id.transactions);

        Intent passedValues = getIntent();
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Intent intent = new Intent(this, EBarterApp.class);
        displayUser();
        transact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(email);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(intent);
                finish();
            }
        });

    }
    private void displayUser(){
        fs = FirebaseFirestore.getInstance();
        fs.collection("users").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String displayName = documentSnapshot.getString("Name");
                            dispName.setText(displayName);
                        }
                    }
                });


    }

}
