package com.example.final_project_semb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class LoaderFragment extends Fragment {
    ViewGroup root;
    View[] dots;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_loader, container, false);
        initViews();
        try {
            animateDots();
        } catch (Exception e) {
            Log.d("testanim", e + "");
        }
        return root;
    }

    private void animateDots() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            int index = 0;
            @Override
            public void run(){
                int offset = 0;
                Animation sgAnimation = AnimationUtils.loadAnimation(root.getContext().getApplicationContext(), R.anim.anim_scale);
                sgAnimation.setStartOffset(offset);
                dots[index].startAnimation(sgAnimation);
                index++;
                offset += 100;
                if(index % dots.length == 0)
                    timer.cancel();
            }
        },0,600);


    }


    private void initViews() {
        dots = new View[4];
        for (int i = 0; i < 4; i++) {
            int resID = getResources().getIdentifier("dot" + (i + 1), "id", root.getContext().getPackageName());
            dots[i] = root.findViewById(resID);
        }
    }
}