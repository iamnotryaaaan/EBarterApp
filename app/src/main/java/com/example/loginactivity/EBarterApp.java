package com.example.loginactivity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class EBarterApp extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton Facebook;
    float v=0;
    TextInputLayout user, pass;
    TextInputLayout email, password, number, address, name;
    Button login, signup;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser!=null) {
            startActivity(new Intent(this, Feed.class));
            finish();
        }
        else {
            setContentView(R.layout.activity_login);

            tabLayout = findViewById(R.id.tab_layouts);
            viewPager = findViewById(R.id.view_pager);
            Facebook = findViewById(R.id.fab_facebook);

            tabLayout.addTab(tabLayout.newTab().setText("Login"));
            tabLayout.addTab(tabLayout.newTab().setText("Signup"));
            tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

            final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
            viewPager.setAdapter(adapter);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            Facebook.setTranslationY(300);
            tabLayout.setTranslationY(300);

            Facebook.setAlpha(v);
            tabLayout.setAlpha(v);

            Facebook.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    user = findViewById(R.id.user);
                    pass = findViewById(R.id.pass);
                    email = findViewById(R.id.sign_username);
                    password = findViewById(R.id.sign_password);
                    number = findViewById(R.id.sign_number);
                    address = findViewById(R.id.sign_address);
                    login = findViewById(R.id.login_button);
                    signup = findViewById(R.id.signup_button);
                    name = findViewById(R.id.sign_name);

                    signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signupUser();
                        }
                    });

                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loginUser();

                            pass.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b) {
                                    pass.setError(null);
                                }
                            });

                        }
                    });

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }



    }

    private Boolean validateUser() {
        String val = user.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            user.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            user.setError("Invalid Email Address");
            return false;
        } else {
            user.setError(null);
            user.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatePass() {
        String val = pass.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            pass.setError("Field cannot be Empty");
            return false;
        } else {
            pass.setError(null);
            return true;
        }

    }

    private Boolean validateNumber() {
        String val = number.getEditText().getText().toString();
        String numberPattern = "09+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]";

        if (val.isEmpty()) {
            number.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(numberPattern)) {
            number.setError("Invalid Number");
            return false;
        } else {
            number.setError(null);
            number.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateSEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid Email Address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateSAddress() {
        String val = address.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            address.setError("Field cannot be Empty");
            return false;
        } else {
            address.setError(null);
            address.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateSPassword() {
        String val = password.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            return true;
        }

    }

    private void signupUser() {

        if(!validateSEmail() | !validateSPassword() | !validateNumber() | !validateSAddress()) {
            return;
        }

        String userEmail = email.getEditText().getText().toString().trim();
        String userNumber = number.getEditText().getText().toString().trim();
        String userAddress = address.getEditText().getText().toString().trim();
        String userPassword = password.getEditText().getText().toString().trim();
        String userName = name.getEditText().getText().toString().trim();

        Intent intent = new Intent(this, Feed.class);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://www.example.com/finishSignUp?cartId=1234")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.example.android",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();

        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_LONG).show();
                    auth.sendSignInLinkToEmail(userEmail, actionCodeSettings)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                    }
                                }
                            });
                    startActivity(intent);
                    finish();
                } else Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Map<String, Object> user = new HashMap<>();
        user.put("Email", userEmail);
        user.put("Number", userNumber);
        user.put("Address", userAddress);
        user.put("Password", userPassword);
        user.put("Name", userName);

        db.collection("users").document(userEmail).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId())
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    private void loginUser() {

        if (!validateUser() | !validatePass()) {
            return;
        }

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent intent = new Intent(this, Feed.class);

        String userEnteredEmail = user.getEditText().getText().toString().trim();
        String userEnteredPassword = pass.getEditText().getText().toString().trim();

        intent.putExtra("email", userEnteredEmail);
        System.out.println("Login Success");

        auth.signInWithEmailAndPassword(userEnteredEmail, userEnteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                    intent.putExtra("email", userEnteredEmail);
                    startActivity(intent);
                    finish();
                } else Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
