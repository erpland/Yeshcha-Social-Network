package com.example.final_project_semb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button register_btn;
    TextView toLogin_btn;
    EditText email_et, password_et, passwordConfirm_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initValues();
        toLogin_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);
    }

    private void initValues() {
        toLogin_btn = findViewById(R.id.tv_toLogin);
        register_btn = findViewById(R.id.btn_register);
        email_et = findViewById(R.id.et_emailRegister);
        password_et = findViewById(R.id.et_passwordRegister);
        passwordConfirm_et = findViewById(R.id.et_passwordAgain);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_toLogin:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;
            case R.id.btn_register:
                break;
        }
    }
}