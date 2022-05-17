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

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterPage2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterPage2Fragment extends Fragment implements View.OnClickListener {
    Button register,goBack;
    ViewGroup root;
    CallBackInterface callBackInterface;
    EditText full_name,phone_number;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterPage2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterPage2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterPage2Fragment newInstance(String param1, String param2) {
        RegisterPage2Fragment fragment = new RegisterPage2Fragment();
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
        root = (ViewGroup) inflater.inflate(R.layout.fragment_register_page2,null);
        initViews();
        register.setOnClickListener(this);
        goBack.setOnClickListener(this);
        return root;
//        return inflater.inflate(R.layout.fragment_register_page2, container, false);
    }

    private void initViews() {
        register = root.findViewById(R.id.btn_registerFinish);
        goBack = root.findViewById(R.id.btn_registerBack);
        full_name = root.findViewById(R.id.et_nameRegister);
        phone_number =root.findViewById(R.id.et_phoneRegister);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_registerBack:
                getParentFragmentManager().popBackStackImmediate();
                break;
            case R.id.btn_registerFinish:
                callBackInterface.callBackReg2(view.getId(),full_name.getText().toString(),phone_number.getText().toString(),null);

        }
    }
}