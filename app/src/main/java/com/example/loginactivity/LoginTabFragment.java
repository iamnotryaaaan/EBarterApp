package com.example.loginactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class LoginTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, attachToRoot: false);

        user = root.findViewById(R.id.user);
        pass = root.findViewById(R.id.pass);
        forgetPass = root.findViewById(R.id.forget_pass);
        login = root.findViewById(R.id.button);
        adminLogin = root.findViewById(R.id.adminLogin);
        createAcc = root.findViewById(R.id.createAcc);

        user.setTranslationX(800);
        pass.setTranslationX(800);
        forgetPass.setTranslation(800);
        login.setTranslation(800);
        adminLogin.setTranslation(800);
        createAcc.setTranslation(800);

        user.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        forgetPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        adminLogin.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        createAcc.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();


        return root;
    }
}

