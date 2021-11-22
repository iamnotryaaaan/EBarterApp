package com.example.loginactivity;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Post extends AppCompatActivity {

    ImageView viewPhoto;
    Button uploadPhoto, createItem;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri imageUri;
    TextInputLayout inputName, inputDescription;
    FirebaseFirestore db;
    FirebaseAuth auth;
    SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM dd, yyyy, HH:mm:ss");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        viewPhoto = findViewById(R.id.imageButton2);
        uploadPhoto = findViewById(R.id.upload_button);
        inputName = findViewById(R.id.i_name);
        createItem = findViewById(R.id.create_item);
        inputDescription = findViewById(R.id.i_description);

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });

        createItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadPhoto();
                createItem();

                inputName.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        inputName.setError(null);
                    }
                });
                inputDescription.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        inputDescription.setError(null);
                    }
                });
            }
        });

    }

    private void createItem() {

        if(!validateName() | !validateDesc()) {
            return;
        }

        String name = inputName.getEditText().getText().toString().trim();
        String description = inputDescription.getEditText().getText().toString().trim();
        String user;

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser().getEmail();
        System.out.println(user);
        db.collection("users").document(user).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String userEmail = documentSnapshot.getString("Email");
                        String userLocation = documentSnapshot.getString("Address");

                        Calendar calendar = Calendar.getInstance();
                        String currDate = format.format(calendar.getTime());

                        Map<String, Object> items = new HashMap<>();
                        items.put("userEmail", userEmail);
                        items.put("itemLocation", userLocation);
                        items.put("itemDescription", description);
                        items.put("itemName", name);
                        items.put("itemDate", getDateFromString(currDate));

                        System.out.println(getDateFromString(currDate));
                        System.out.println(currDate);

                        db.collection("items").document(name+userEmail).set(items)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Post.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Boolean validateName() {
        String val = inputName.getEditText().getText().toString();

        if (val.isEmpty()) {
            inputName.setError("Field cannot be Empty");
            return false;
        } else {
            inputName.setError(null);
            inputName.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateDesc() {
        String val = inputDescription.getEditText().getText().toString();

        if (val.isEmpty()) {
            inputDescription.setError("Field cannot be Empty");
            return false;
        } else {
            inputDescription.setError(null);
            inputDescription.setErrorEnabled(false);
            return true;
        }

    }

    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {

            imageUri = data.getData();
            viewPhoto.setImageURI(imageUri);
        }

    }

    public Date getDateFromString(String datetoSaved){

        try {
            Date date = format.parse(datetoSaved);
            return date;
        } catch (ParseException e){
            return null;
        }

    }

    private void uploadPhoto() {

        if(!validateName() | !validateDesc()) {
            return;
        }

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image");
        pd.show();
        Intent getValues = getIntent();
        Intent home = new Intent(this, Feed.class);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String name = inputName.getEditText().getText().toString().trim();

        StorageReference mountainsRef = storageReference.child("items/"+email+"/"+name);

        if (imageUri == null) {
            pd.dismiss();
            System.out.println(name);
            Toast.makeText(this, "Please Upload a Photo", Toast.LENGTH_SHORT).show();
        }
        else {
            mountainsRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                    pd.dismiss();
                    startActivity(home);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + (int)progressPercent+"%");
                }
            });
        }

    }

}
