package com.example.final_project_semb;

import android.graphics.Bitmap;
import android.net.Uri;

public interface RegistrationCallbacks {
    public void callBackReg1(int viewId,String email,String password,String confirmPass);
    public void callBackReg2(int viewId, String name, String phoneNumber);
    public void callBackImageMethod(int viewId,int imageView);
}
