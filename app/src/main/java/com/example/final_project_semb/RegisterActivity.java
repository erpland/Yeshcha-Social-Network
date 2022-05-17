package com.example.final_project_semb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,CallBackInterface {

    TextView toLogin_btn;

    String emailTxt,passwordTxt,nameTxt,phoneNumberTxt;
    Bitmap bitmapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initValues();
        toLogin_btn.setOnClickListener(this);
//        register_btn.setOnClickListener(this);
    }

    private void initValues() {
        toLogin_btn = findViewById(R.id.tv_toLogin);
//        register_btn = findViewById(R.id.btn_register);
//        email_et = findViewById(R.id.et_emailRegister);
//        password_et = findViewById(R.id.et_passwordRegister);
//        passwordConfirm_et = findViewById(R.id.et_passwordAgain);
    }

//    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_toLogin:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;
//            case R.id.btn_register:
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.fc_registerFgmntMngr, RegisterPage2Fragment.class, null)
//                        .setReorderingAllowed(true)
//                        .addToBackStack("name") // name can be null
//                        .commit();
//                break;
        }
    }
    public void callBackReg1(int viewId,String email,String password,String confirmPass){
        emailTxt=email;
        passwordTxt=password;
        switch (viewId){
            case R.id.btn_register1:
                if (email.isEmpty()||password.isEmpty()||confirmPass.isEmpty())
                {
                    Toast.makeText(this,"אחד או יותר מן השדות ריקים ",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(confirmPass)){
                    Toast.makeText(this,"סיסמאות אינן תואמות!",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!fieldValidation(email,Patterns.EMAIL_ADDRESS.toString())){
                    Toast.makeText(this,"אימייל לא תקין",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!fieldValidation(password,"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")) {
                    Toast.makeText(this,"סיסמא נדרשת להכיל לפחות אות אחת גדולה,אות קטנה, מספר,סימן מיוחד",Toast.LENGTH_LONG).show();
                    return;
                }

//
                else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frgCntr_fregments, RegisterPage2Fragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name") // name can be null
                            .commit();

                    break;
                }
        }
    }

    @Override
    public void callBackReg2(int viewId, String name, String phoneNumber, Bitmap imageMap) {
        nameTxt=name;
        phoneNumberTxt=phoneNumber;
        bitmapImage=imageMap;
        switch (viewId){
            case R.id.btn_registerFinish:
                if (name.isEmpty()||phoneNumber.isEmpty())
                {
                    Toast.makeText(this,"אחד או יותר מן השדות ריקים ",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!fieldValidation(name,"^[a-zA-Z\\u0590-\\u05FF\\u200f\\u200e ]+$")){
                    Toast.makeText(this,"שם יכול להכין אותיות בעברית/אנגלית בלבד",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!fieldValidation(phoneNumber,"^(?:(?:(\\+?972|\\(\\+?972\\)|\\+?\\(972\\))(?:\\s|\\.|-)?([1-9]\\d?))|(0[23489]{1})|(0[57]{1}[0-9]))(?:\\s|\\.|-)?([^0\\D]{1}\\d{2}(?:\\s|\\.|-)?\\d{4})$")) {
                    Toast.makeText(this,"מספר טלפון לא תקין",Toast.LENGTH_LONG).show();
                    return;
                }
        }
    }

    private boolean fieldValidation(String field,String regex){
            if (!field.matches(regex)){
                return false;
            }
        return true;
    }
}