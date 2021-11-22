package com.example.loginactivity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewItem extends AppCompatActivity {

    TextView name, description, location, date, item;
    ImageView vPicture;
    FirebaseFirestore db;
    ImageView returnHome;
    StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        name = findViewById(R.id.view_user);
        description = findViewById(R.id.view_description);
        location = findViewById(R.id.view_location);
        date = findViewById(R.id.view_date);
        returnHome = findViewById(R.id.return_button);
        vPicture = findViewById(R.id.view_photo);
        item = findViewById(R.id.view_item);

        Intent home = new Intent(this, Feed.class);

        displayReport();

        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(home);
                finish();
            }
        });

    }

    private void displayReport() {

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String rName = intent.getStringExtra("name");
        String user = intent.getStringExtra("user");

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy, HH:mm:ss");

        db.collection("items").document(rName+user).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String displayName = documentSnapshot.getString("itemName");
                            String displayDescription = documentSnapshot.getString("itemDescription");
                            String displayLocation = documentSnapshot.getString("itemLocation");
                            Date displayDate = documentSnapshot.getDate("itemDate");
                            item.setText(displayName);
                            description.setText(displayDescription);
                            location.setText(displayLocation);
                            date.setText(dateFormatter.format(displayDate));

                            db.collection("users").document(user).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String userName = documentSnapshot.getString("Name");
                                            name.setText(userName);
                                        }
                                    });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error fetching document", e);
            }
        });

        reference = FirebaseStorage.getInstance().getReference().child("items/"+user+"/"+rName);

        try {
            final File localFile = File.createTempFile(rName, "jpg");
            reference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            vPicture.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ViewItem.this, "Picture cannot be retrieved", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}