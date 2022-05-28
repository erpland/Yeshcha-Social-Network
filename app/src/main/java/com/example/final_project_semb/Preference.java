package com.example.final_project_semb;

public class Preference {
    Boolean active;
    int code;
    String name;

    public Preference() {
    }

    public Preference(Boolean active, int code, String name) {
        this.active = active;
        this.code = code;
        this.name = name;
    }
    public Preference(Object obj){
        Preference temp = (Preference) obj;
        this.active = temp.active;
        this.code = temp.code;
        this.name = temp.name;
    }
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
