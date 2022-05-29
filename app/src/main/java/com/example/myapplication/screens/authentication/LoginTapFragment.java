package com.example.myapplication.screens.authentication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.datastructures.chatty.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginTapFragment extends Fragment {

    EditText phone, OTP;
    Button generateOTPBtn, verifyOTPBtn;
    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);
        initWidgets(root);

        phone.setTranslationY(800);
        OTP.setTranslationY(800);
        generateOTPBtn.setTranslationY(800);
        verifyOTPBtn.setTranslationY(800);

        phone.setAlpha(v);
        OTP.setAlpha(v);
        generateOTPBtn.setAlpha(v);
        verifyOTPBtn.setAlpha(v);

        phone.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        OTP.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        generateOTPBtn.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        verifyOTPBtn.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();

        return root;
    }
    public void initWidgets(ViewGroup root) {
        phone =  root.findViewById(R.id.phoneEditText);
        OTP =  root.findViewById(R.id.OTPEditText);
        generateOTPBtn = root.findViewById(R.id.generateOTPButton);
        verifyOTPBtn =  root.findViewById(R.id.verifyOTPButton);
    }
}
