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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoaderFragment extends Fragment {
    ViewGroup root;
    View[] dots;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoaderFragment newInstance(String param1, String param2) {
        LoaderFragment fragment = new LoaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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