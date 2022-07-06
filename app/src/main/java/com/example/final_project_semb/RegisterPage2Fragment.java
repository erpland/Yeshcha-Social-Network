package com.example.final_project_semb;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class RegisterPage2Fragment extends Fragment implements View.OnClickListener {
    Button register, goBack;
    ViewGroup root;
    CallBackInterface callBackInterface;
    EditText full_name, phone_number;
    RelativeLayout imageSection;
    RegisterActivity registerActivity;

    ImageView profilePic;




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callBackInterface = (CallBackInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_register_page2, null);
        initViews();
        register.setOnClickListener(this);
        goBack.setOnClickListener(this);
        imageSection.setOnClickListener(this);
        return root;

    }

    private void initViews() {
        register = root.findViewById(R.id.btn_registerFinish);
        goBack = root.findViewById(R.id.btn_registerBack);
        full_name = root.findViewById(R.id.et_nameRegister);
        phone_number = root.findViewById(R.id.et_phoneRegister);
        imageSection = root.findViewById(R.id.rly_addPhoto);
        profilePic = root.findViewById(R.id.iv_avatar);
        registerActivity = (RegisterActivity) getActivity();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_registerBack:
                getParentFragmentManager().popBackStackImmediate();
                break;
            case R.id.btn_registerFinish:
                callBackInterface.callBackReg2(view.getId(), full_name.getText().toString(), phone_number.getText().toString());
                break;
            case R.id.rly_addPhoto:
                callBackInterface.callBackImageMethod(view.getId(),profilePic.getId());
                break;

        }
    }
}