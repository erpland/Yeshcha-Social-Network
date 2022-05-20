package com.example.final_project_semb;

public class PreferencesManager {
    boolean isActive;
    int preferenceCode;
    String preferenceName;

    public PreferencesManager(boolean isActive, int preferenceCode,String preferenceName) {
        this.isActive = isActive;
        this.preferenceCode = preferenceCode;
        this.preferenceName=preferenceName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getPreferenceCode() {
        return preferenceCode;
    }

    public void setPreferenceCode(int preferenceCode) {
        this.preferenceCode = preferenceCode;
    }
}
