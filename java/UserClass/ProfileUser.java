package com.example.smartapp.UserClass;

public class ProfileUser {
    private String Name;
    private String userUID;
    private String EmailID;
    private String Institute;
    private String TorS;
    private String Password;
    private String photoUrl;
    /*private String urlPDF;
    private String namePDF;*/




    public ProfileUser(String Name, String EmailID,String userUID,
                       String Institute, String Password,String photoUrl/*,String urlPDF,String namePDF*/) {

        this.Name = Name;
        this.userUID=userUID;
        this.EmailID = EmailID;
        this.Institute = Institute;
        this.Password = Password;
        this.photoUrl=photoUrl;
        /*this.urlPDF=urlPDF;
        this.namePDF=namePDF;*/
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
/*

    public ProfileUser(String urlPDF,String namePDF) {
        this.urlPDF=urlPDF;
        this.namePDF=namePDF;
    }
*/

   /* public String getNamePDF() {
        return namePDF;
    }

    public void setNamePDF(String namePDF) {
        this.namePDF = namePDF;
    }

    public String getUrlPDF() {
        return urlPDF;
    }

    public void setUrlPDF(String urlPDF) {
        this.urlPDF = urlPDF;
    }
*/
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getInstitute() {
        return Institute;
    }

    public void setInstitute(String institute) {
        Institute = institute;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public ProfileUser() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}