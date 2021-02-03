package com.example.smartapp.UserClass;

public class ClassArticles {
    private String namePDF;
    private String urlPDF;

    public ClassArticles(String namePDF, String urlPDF) {
        this.namePDF = namePDF;
        this.urlPDF = urlPDF;
    }

    public ClassArticles() {
    }

    public String getNamePDF() {
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
}
