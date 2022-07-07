package com.example.final_project_semb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, CallBackInterface {

    TextView toLogin_btn;

    String emailTxt, passwordTxt, nameTxt, phoneNumberTxt;
    private final static int GALLERY_PHOTO = 99;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri imagePath,imageUri;
    FragmentManager fragmentManager;
    int imageViewId=0;
    ImageView profilePicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initValues();
        toLogin_btn.setOnClickListener(this);
    }

    private void initValues() {
        toLogin_btn = findViewById(R.id.tv_toLogin);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Users");


    }

    //    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_toLogin:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
        }
    }

    public void callBackReg1(int viewId, String email, String password, String confirmPass) {
        emailTxt = email;
        passwordTxt = password;
        switch (viewId) {
            case R.id.btn_register1:
                if (email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(this, "אחד או יותר מן השדות ריקים ", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(confirmPass)) {
                    Toast.makeText(this, "סיסמאות אינן תואמות!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!fieldValidation(email, Patterns.EMAIL_ADDRESS.toString())) {
                    Toast.makeText(this, "אימייל לא תקין", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!fieldValidation(password, "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!-_$%^&+=])(?=\\S+$).{4,}$")) {
                    Toast.makeText(this, "סיסמא נדרשת להכיל לפחות אות אחת גדולה,אות קטנה, מספר,סימן מיוחד", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_to_right, R.anim.slide_to_left, R.anim.slide_from_right, R.anim.slide_from_left)
                            .replace(R.id.frgCntr_fregments, RegisterPage2Fragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("f1") // name can be null
                            .commit();

                    break;
                }
        }
    }

    @Override
    public void callBackReg2(int viewId, String name, String phoneNumber) {
        nameTxt = name;
        phoneNumberTxt = phoneNumber;
        switch (viewId) {
            case R.id.btn_registerFinish:
                if (name.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(this, "אחד או יותר מן השדות ריקים ", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!fieldValidation(name, "^[a-zA-Z\\u0590-\\u05FF\\u200f\\u200e ]+$")) {
                    Toast.makeText(this, "שם יכול להכין אותיות בעברית/אנגלית בלבד", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!fieldValidation(phoneNumber, "^(?:(?:(\\+?972|\\(\\+?972\\)|\\+?\\(972\\))(?:\\s|\\.|-)?([1-9]\\d?))|(0[23489]{1})|(0[57]{1}[0-9]))(?:\\s|\\.|-)?([^0\\D]{1}\\d{2}(?:\\s|\\.|-)?\\d{4})$")) {
                    Toast.makeText(this, "מספר טלפון לא תקין", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }

        User user = new User(emailTxt, null, nameTxt, phoneNumberTxt);
        Map<String, Object> preferences = new HashMap<>();
        Reply reply = new Reply();
        Requests requests = new Requests();
        createPreferencesList(preferences);

        if (imagePath == null){ // אם לא העלו תמנו נעלה תמונה דיפולטיבית
            Resources resources = this.getResources();
            int resId = R.drawable.default_avatar;
            imagePath = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId) );

        }
        SignUpToFireBase(emailTxt, passwordTxt, user, preferences, reply, requests,imagePath);

    }

    @Override
    public void callBackImageMethod(int viewId,int imageView) {
        imageViewId=imageView;
        takeGalleryAction();
    }

    private void createPreferencesList(@NonNull Map<String, Object> preferences) {
        Preference basicEqt = new Preference(true, 0, "Basics");
        Preference computerMobilEqt = new Preference(true, 1, "Computers and mobile");
        Preference officeEqt = new Preference(true, 2, "Office");
        Preference othersEqt = new Preference(true, 3, "Others");
        Preference personalHygieneEqt = new Preference(true, 4, "Personal hygiene");
        Preference petEqt = new Preference(true, 5, "Pets");
        preferences.put("basicEqt", basicEqt);
        preferences.put("computerMobilEqt", computerMobilEqt);
        preferences.put("officeEqt", officeEqt);
        preferences.put("othersEqt", othersEqt);
        preferences.put("personalHygieneEqt", personalHygieneEqt);
        preferences.put("petEqt", petEqt);
    }

    private void SignUpToFireBase(String email, String password, User user, Map<String, Object> preferences, Reply reply, Requests requests, Uri image) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //save user to the database
                            final StorageReference ref = storageReference.child(mAuth.getUid()).child("Profile_" + System.currentTimeMillis() + ".jpg");
                            UploadTask imageUpload = ref.putFile(image);

                            Task<Uri> urlTask = imageUpload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return ref.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        imageUri = task.getResult();
                                        user.setImage(imageUri.toString());
                                        addUserToFireStore(user, preferences, reply, requests);
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(RegisterActivity.this, "Authentication Success.",
                                                Toast.LENGTH_SHORT).show();


                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Image Could Not Be Uploaded...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean fieldValidation(String field, String regex) {
        if (!field.matches(regex)) {
            return false;
        }
        return true;
    }

    private void addUserToFireStore(User user, Map<String, Object> preferences, Reply reply, Requests requests) {
        // Add a new document with a generated ID
        db.collection("users").document(mAuth.getUid())
                .set(user);
        db.collection("Preferences").document(mAuth.getUid()).set(preferences);
        db.collection("Replies").document(mAuth.getUid()).set(reply);
        db.collection("Requests").document(mAuth.getUid()).set(requests);


    }

    //upload image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GALLERY_PHOTO:
                if (resultCode == RESULT_OK) {
                    imagePath=data.getData();
                    profilePicture=findViewById(imageViewId);
                    profilePicture.setImageURI(imagePath);

                } else {
                    Toast.makeText(this, "Operation failed", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    //open gallery
    private void takeGalleryAction() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GALLERY_PHOTO);
    }



}