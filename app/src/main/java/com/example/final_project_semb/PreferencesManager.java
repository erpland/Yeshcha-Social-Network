package com.example.final_project_semb;

public class PreferencesManager {
    boolean isActive;
    int preferenceCode;
    String preferenceName;

    public String getPreferenceName() {
        return preferenceName;
    }

    public PreferencesManager(boolean isActive, int preferenceCode, String preferenceName) {
        this.isActive = isActive;
        this.preferenceCode = preferenceCode;
        this.preferenceName=preferenceName;
    }
    public PreferencesManager(PreferencesManager obj){
        this.isActive=obj.isActive;
        this.preferenceCode=obj.preferenceCode;
        this.preferenceName=obj.preferenceName;
    }
    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getPreferenceCode() {
        return preferenceCode;
    }

    public void setPreferenceCode(int preferenceCode) {
        this.preferenceCode = preferenceCode;
    }


}
