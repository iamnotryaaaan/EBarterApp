package com.example.loginactivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginactivity.ui.login.RVAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Feed extends AppCompatActivity {

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    ArrayList<GetItem> user;
    RVAdapter rvAdapter;
    TextView dispEmpty, logOut;
    FloatingActionButton create;
    Button rate;
    TextView profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Intent intent = new Intent(this, Post.class);
        Intent startRate = new Intent(this,Rate.class);
        Intent startProfile = new Intent(this,Profile.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        create = findViewById(R.id.create_item);
        logOut = findViewById(R.id.logout);
        rate = findViewById(R.id.ratebutton);
        profile = findViewById(R.id.profile);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        user = new ArrayList<>();

        rvAdapter = new RVAdapter(this, user);
        recyclerView.setAdapter(rvAdapter);

        EventChangeListener();

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startRate);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startProfile);
            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                finish();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(Feed.this, EBarterApp.class));
            }
        });

    }

    private void EventChangeListener() {

        dispEmpty = findViewById(R.id.disp_empty);

        db.collection("items").orderBy("itemDate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            if (progressDialog.isShowing()) progressDialog.dismiss();
                            Log.e("Database Error", error.getMessage());
                            return;
                        }

                        if (value.isEmpty()) {
                            dispEmpty.setText("Nothing To Show Here");
                            if (progressDialog.isShowing()) progressDialog.dismiss();
                        }
                        else {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    user.add(dc.getDocument().toObject(GetItem.class));
                                }
                                rvAdapter.notifyDataSetChanged();
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                            }
                        }

                    }
                });

    }

}