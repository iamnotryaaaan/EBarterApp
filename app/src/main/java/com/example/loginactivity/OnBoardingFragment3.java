package com.example.loginactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class OnBoardingFragment3 extends Fragment{

    FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_on_boarding3, container, attachToRoot: false);

        fab = root.findViewById(R.id.fab)
        fab.setOnClickListener(new View.OnClickListener()){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        }

        return root;

    }
}
