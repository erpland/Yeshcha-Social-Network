package com.example.final_project_semb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login_btn;
    TextView toRegister_tv;
    EditText email_et,password_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initValues();

        login_btn.setOnClickListener(this);
        toRegister_tv.setOnClickListener(this);
    }

    private void initValues() {
        login_btn = findViewById(R.id.btn_login);
        toRegister_tv = findViewById(R.id.tv_toRegister);
        email_et = findViewById(R.id.et_emailLogin);
        password_et = findViewById(R.id.et_passwordLogin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            case R.id.tv_toRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }
}