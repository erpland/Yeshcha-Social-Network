package com.example.final_project_semb;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class RegisterPage1Fragment extends Fragment implements View.OnClickListener {
    Button next_btn;
    EditText email_et,password_et,passwordConfirm_et;
    ViewGroup root;
    RegistrationCallbacks registrationCallbacks;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        registrationCallbacks = (RegistrationCallbacks) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_register_page1,null);
        initViews();
        next_btn.setOnClickListener(this);
        return root;
    }

    private void initViews() {
        next_btn = root.findViewById(R.id.btn_register1);
        email_et = root.findViewById(R.id.et_emailRegister);
        password_et = root.findViewById(R.id.et_passwordRegister);
        passwordConfirm_et =root.findViewById(R.id.et_passwordAgain);
    }

    @Override
    public void onClick(View view) {
        registrationCallbacks.callBackReg1(view.getId(),email_et.getText().toString(),password_et.getText().toString(),passwordConfirm_et.getText().toString());
    }
}