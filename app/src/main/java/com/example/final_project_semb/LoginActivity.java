package com.example.final_project_semb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login_btn;
    TextView toRegister_tv;
    EditText email_et,password_et;
    FirebaseAuth mAuth;
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
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                validateLogin();

                break;
            case R.id.tv_toRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    private void validateLogin() {
        String pass=password_et.getText().toString();
        String email=email_et.getText().toString();
        if (pass.isEmpty()||email.isEmpty()){
            Toast.makeText(this,"Cant leave empty fields!!",Toast.LENGTH_LONG).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();


                            startActivity(new Intent(LoginActivity.this, MainActivity.class));


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }
                });

    }
}