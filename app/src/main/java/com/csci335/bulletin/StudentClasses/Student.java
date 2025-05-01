package com.csci335.bulletin.StudentClasses;

import com.csci335.bulletin.Main.Notif;
import com.csci335.bulletin.Events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Student {
    private String ID;
    private String email;
    private String password;
    private String name;
    private int age;
    private Year year;
    private Gender gender;
    private List<SpecialStatus> specialStatus;
    private List<Notif> notifications;
    // Events the student has marked as attending
    private List<String> events;
    private List<String> followedOrgs;

    // unneeded?
    public Student(String ID, ArrayList<SpecialStatus> status){
        this.ID = ID;
        this.specialStatus = status;
        notifications = new ArrayList<Notif>();
        events = new ArrayList<String>();
        followedOrgs = new ArrayList<String>();
    }
    public Student(String ID, ArrayList<SpecialStatus> status, List<Notif> notifications){
        this.ID = ID;
        this.specialStatus = status;
        this.notifications = notifications;
        events = new ArrayList<String>();
        followedOrgs = new ArrayList<String>();
    }

    public Student() {
        specialStatus = new ArrayList<SpecialStatus>();
        notifications = new ArrayList<Notif>();
        events = new ArrayList<String>();
        followedOrgs = new ArrayList<String>();
    }

    public String getID() { return ID; }
    public void setID(String ID) { this.ID = ID; }

    // should check permissions?
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public Year getYear() { return year; }
    public void setYear(Year year) { this.year = year; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public List<SpecialStatus> getSpecialStatus() { return specialStatus; }
    public void addSpecialStatus(SpecialStatus specialStatus) { this.specialStatus.add(specialStatus); }
    public void setSpecialStatus(List<SpecialStatus> specialStatus) { this.specialStatus = specialStatus; }

    public List<Notif> getNotifications() {return notifications;}
    public void setNotifications(List<Notif> notifications) {this.notifications = notifications;}
    public void addNotification(Notif n){notifications.add(n);}

    public List<String> getEvents() { return this.events; }
    public void setEvents(List<String> events) { this.events = events; }
    public void addEvent(String event) {
        if (event == null) {
            events = new ArrayList<String>();
        }
        events.add(event);
    }
    public void removeEvent(String event) { events.remove(event); }
    public List<String> getFollowedOrgs() { return followedOrgs; }
    public void setFollowedOrgs(List<String> followedOrgs) { this.followedOrgs = followedOrgs; }
    public void addFollowedOrg(String followedOrg) { followedOrgs.add(followedOrg); }
    public void removeFollowedOrg(String followedOrg) { followedOrgs.remove(followedOrg); }
    public boolean containsFollowedOrg(String followedOrg) { return followedOrgs.contains(followedOrg); }

}
