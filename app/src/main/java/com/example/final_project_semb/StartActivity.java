package com.example.final_project_semb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    Button toLogin_btn, toRegister_btn;
    FirebaseAuth mAuth;

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(StartActivity.this,MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initView();
        mAuth = FirebaseAuth.getInstance();

        toLogin_btn.setOnClickListener(this);
        toRegister_btn.setOnClickListener(this);

    }

    private void initView() {
        toLogin_btn = findViewById(R.id.btn_toLogin);
        toRegister_btn = findViewById(R.id.btn_toSignup);
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