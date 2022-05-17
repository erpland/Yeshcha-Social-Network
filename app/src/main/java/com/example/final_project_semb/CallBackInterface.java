package com.example.final_project_semb;

import android.graphics.Bitmap;

public interface CallBackInterface {
    public void callBackReg1(int viewId,String email,String password,String confirmPass);
    public void callBackReg2(int viewId, String name, String phoneNumber, Bitmap imageMap);

}
