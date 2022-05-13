package com.example.final_project_semb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    Button toLogin_btn, toRegister_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        toLogin_btn = findViewById(R.id.btn_toLogin);
        toRegister_btn = findViewById(R.id.btn_toSignup);

        toLogin_btn.setOnClickListener(this);
        toRegister_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_toLogin:
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                break;
            case R.id.btn_toSignup:
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                break;
        }
    }
}