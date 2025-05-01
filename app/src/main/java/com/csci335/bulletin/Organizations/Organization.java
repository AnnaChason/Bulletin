package com.csci335.bulletin.Organizations;

import com.csci335.bulletin.Main.Notif;

import java.util.ArrayList;
import java.util.List;

/*
Not connected to an activity.
Class for keeping track of each organization's data
 */
public class Organization {
    private String name;
    private String description;
    private String ID;
    private ArrayList<Notif> notifications;
    private List<String> followers;

    public Organization(){}
    public Organization(String name, String description, String ID){
        this.name = name;
        this.description = description;
        this.ID = ID;
        notifications = new ArrayList<Notif>();
    }
    public Organization(String name, String description, String ID, ArrayList<Notif> notifications){
        this.name = name;
        this.description = description;
        this.ID = ID;
        this.notifications = notifications;
    }
    /*
    getters and setters
     */
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}

    public String getId(){return ID;}
    public void setId(String ID){this.ID = ID;}

    public ArrayList<Notif> getNotifications() {return notifications;}
    public void setNotifications(ArrayList<Notif> notifications) {this.notifications = notifications;}
    public void addNotification(Notif n){notifications.add(n);}

    public List<String> getFollowers() { return followers; }
    public void setFollowers(List<String> followers) { this.followers = followers; }
    public void addFollower(String follower) {
        if(followers == null) {
            followers = new ArrayList<String>();
        }
        this.followers.add(follower);
    }
    public void removeFollower(String follower) {
        followers.remove(follower);
    }
    public boolean containsFollower(String follower) { return followers.contains(follower); }
}
