package com.csci335.bulletin.StudentClasses;

import com.csci335.bulletin.Main.Notif;

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

    // unneeded?
    public Student(String ID, ArrayList<SpecialStatus> status){
        this.ID = ID;
        this.specialStatus = status;
        notifications = new ArrayList<Notif>();
    }
    public Student(String ID, ArrayList<SpecialStatus> status, List<Notif> notifications){
        this.ID = ID;
        this.specialStatus = status;
        this.notifications = notifications;
    }

    public Student() {
        specialStatus = new ArrayList<SpecialStatus>();
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
}
