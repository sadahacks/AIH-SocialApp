package com.example.smartapp.Messagesdirectory;

public class allusermodel {

    private String Name;
    private String Institute;

    public allusermodel() {
    }

    public allusermodel(String name, String institute) {
        Name = name;
        Institute = institute;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getInstitute() {
        return Institute;
    }

    public void setInstitute(String institute) {
        Institute = institute;
    }
}
