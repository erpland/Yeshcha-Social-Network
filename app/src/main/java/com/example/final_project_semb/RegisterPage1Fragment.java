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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterPage1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterPage1Fragment extends Fragment implements View.OnClickListener {
    Button next_btn;
    EditText email_et,password_et,passwordConfirm_et;
    ViewGroup root;
    CallBackInterface callBackInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterPage1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterPage1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterPage1Fragment newInstance(String param1, String param2) {
        RegisterPage1Fragment fragment = new RegisterPage1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callBackInterface = (CallBackInterface) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_register_page1,null);
        initViews();
        next_btn.setOnClickListener(this);
        return root;
//        return inflater.inflate(R.layout.fragment_register_page1, container, false);
    }

    private void initViews() {
        next_btn = root.findViewById(R.id.btn_register1);
        email_et = root.findViewById(R.id.et_emailRegister);
        password_et = root.findViewById(R.id.et_passwordRegister);
        passwordConfirm_et =root.findViewById(R.id.et_passwordAgain);
    }

    @Override
    public void onClick(View view) {
        callBackInterface.callBackReg1(view.getId(),email_et.getText().toString(),password_et.getText().toString(),passwordConfirm_et.getText().toString());
    }
}