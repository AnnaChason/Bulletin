package com.csci335.bulletin.AdminClasses;

public class AdminUser {
    private String email;
    private String uid;
    private int code = 1234;

    public AdminUser( String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public AdminUser(){
    }

    public Boolean testCode(int code){
        boolean test = false;
        if( this.code == code){
            test = true;
        }

        return test;
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
