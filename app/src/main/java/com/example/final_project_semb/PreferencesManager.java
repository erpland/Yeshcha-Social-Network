package com.example.final_project_semb;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PreferencesManager implements Parcelable {
    Preference basicEqt;
    Preference computerMobilEqt;
    Preference officeEqt;
    Preference othersEqt;
    Preference personalHygieneEqt;
    Preference petEqt;

    public PreferencesManager() {
    }

    public PreferencesManager(Preference basicEqt, Preference computerMobilEqt, Preference officeEqt, Preference othersEqt, Preference personalHygieneEqt, Preference petEqt) {
        this.basicEqt = basicEqt;
        this.computerMobilEqt = computerMobilEqt;
        this.officeEqt = officeEqt;
        this.othersEqt = othersEqt;
        this.personalHygieneEqt = personalHygieneEqt;
        this.petEqt = petEqt;
    }

    protected PreferencesManager(Parcel in) {
    }

    public static final Creator<PreferencesManager> CREATOR = new Creator<PreferencesManager>() {
        @Override
        public PreferencesManager createFromParcel(Parcel in) {
            return new PreferencesManager(in);
        }

        @Override
        public PreferencesManager[] newArray(int size) {
            return new PreferencesManager[size];
        }
    };

    public Preference getBasicEqt() {
        return basicEqt;
    }

    public void setBasicEqt(Preference basicEqt) {
        this.basicEqt = basicEqt;
    }

    public Preference getComputerMobilEqt() {
        return computerMobilEqt;
    }

    public void setComputerMobilEqt(Preference computerMobilEqt) {
        this.computerMobilEqt = computerMobilEqt;
    }

    public Preference getOfficeEqt() {
        return officeEqt;
    }

    public void setOfficeEqt(Preference officeEqt) {
        this.officeEqt = officeEqt;
    }

    public Preference getOthersEqt() {
        return othersEqt;
    }

    public void setOthersEqt(Preference othersEqt) {
        this.othersEqt = othersEqt;
    }

    public Preference getPersonalHygieneEqt() {
        return personalHygieneEqt;
    }

    public void setPersonalHygieneEqt(Preference personalHygieneEqt) {
        this.personalHygieneEqt = personalHygieneEqt;
    }

    public Preference getPetEqt() {
        return petEqt;
    }

    public void setPetEqt(Preference petEqt) {
        this.petEqt = petEqt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
    public ArrayList<Boolean> GetArrayOfActiveState(){
        return new ArrayList<Boolean>(){
            {
                add(basicEqt.getActive());
                add(computerMobilEqt.getActive());
                add(officeEqt.getActive());
                add(othersEqt.getActive());
                add(personalHygieneEqt.getActive());
                add(petEqt.getActive());
            }
        };
    }
}
