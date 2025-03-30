package com.csci335.bulletin.AdminClasses;

public class AdminUser {
    private String email;
    private String uid;
    private static String adminCode = "1234";

    public AdminUser(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public static String getAdminCode(){return adminCode;}

    public AdminUser(){
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String email) {
        this.uid = uid;
    }


}
