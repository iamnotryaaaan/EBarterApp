package com.example.loginactivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton Facebook;
    float v=0;
    EditText user;
    EditText pass;
    TextView forgetPass;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        Facebook = findViewById(R.id.fab_facebook);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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
                forgetPass = findViewById(R.id.forget_pass);
                login = findViewById(R.id.button);

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
