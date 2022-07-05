package com.example.final_project_semb;

import android.content.Context;

import androidx.fragment.app.Fragment;

public interface FragmentHandler {
    public void closeAllFragment(Fragment f);
    public void popNavBackStack();
}
