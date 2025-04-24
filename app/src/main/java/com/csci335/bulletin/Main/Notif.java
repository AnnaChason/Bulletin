package com.csci335.bulletin.Main;

/*
class for notification objects
not to be confused with the notifications class which is the one connected to the ui!
 */
public class Notif {
    private String title;
    private String message;
    private boolean read;
    private String uid;//idk if we will need this

    public Notif(){}
    public Notif(String title, String message, boolean read){
        this.title = title;
        this.message = message;
        this.read = read;
    }
    public Notif(String title, String message){
        this(title,message,false);
    }


    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}

    public boolean getRead() {return read;}

    public void setRead(boolean read) {this.read = read;}
}
