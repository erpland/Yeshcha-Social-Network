package com.example.final_project_semb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.FirestoreClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,CallBackInterface {

    TextView toLogin_btn;

    String emailTxt,passwordTxt,nameTxt,phoneNumberTxt;
    Bitmap bitmapImage;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

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
            mAuth=FirebaseAuth.getInstance();
            db=FirebaseFirestore.getInstance();

    }

//    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_toLogin:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;
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
                if (!fieldValidation(password,"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!-_$%^&+=])(?=\\S+$).{4,}$")) {
                    Toast.makeText(this,"סיסמא נדרשת להכיל לפחות אות אחת גדולה,אות קטנה, מספר,סימן מיוחד",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_to_right,R.anim.slide_to_left,R.anim.slide_from_right,R.anim.slide_from_left)
                            .replace(R.id.frgCntr_fregments, RegisterPage2Fragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack(null) // name can be null
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
        User user=new User(emailTxt,bitmapImage,nameTxt,phoneNumberTxt);
        Map<String,Object>preferences=new HashMap<>();
        Reply reply=new Reply();
        Requests requests=new Requests();
        createPreferencesList(preferences);
        SignUpToFireBase(emailTxt,passwordTxt,user,preferences,reply,requests);



    }

    private void createPreferencesList(@NonNull Map<String,Object> preferences) {
        BasicProductsEqt basicEqt=new BasicProductsEqt(true,1,"Basics");
        ComputerAndMobileEqt computerMobilEqt=new ComputerAndMobileEqt(true,2,"Computers and mobile");
        OfficeEqt officeEqt=new OfficeEqt(true,3,"Office");
        OthersEqt othersEqt=new OthersEqt(true,4,"Others");
        PersonalHygieneEqt personalHygieneEqt=new PersonalHygieneEqt(true,5,"Personal hygiene");
        PetEqt petEqt=new PetEqt(true,6,"Pets");
        preferences.put("1",basicEqt);
        preferences.put("2",computerMobilEqt);
        preferences.put("3",officeEqt);
        preferences.put("4",othersEqt);
        preferences.put("5",personalHygieneEqt);
        preferences.put("6",petEqt);
    }

    private void SignUpToFireBase(String email,String password,User user,Map<String,Object> preferences,Reply reply,Requests requests){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //save user to the database
                            addUserToFireStore(user,preferences,reply,requests);
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private boolean fieldValidation(String field,String regex){
            if (!field.matches(regex)){
                return false;
            }
        return true;
    }

    private void addUserToFireStore(User user,Map<String,Object> preferences,Reply reply,Requests requests){
        // Add a new document with a generated ID
        db.collection("users").document(mAuth.getUid())
                .set(user);
        db.collection("Preferences").document(mAuth.getUid()).set(preferences);
        db.collection("Replies").document(mAuth.getUid()).set(reply);
        db.collection("Requests").document(mAuth.getUid()).set(requests);




    }
}