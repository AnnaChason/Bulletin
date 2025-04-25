package com.csci335.bulletin.StudentClasses;

import androidx.annotation.NonNull;

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

    private List<Event> events;

    // unneeded?
    public Student(String ID, ArrayList<SpecialStatus> status){
        this.ID = ID;
        this.specialStatus = status;

        events = new ArrayList<Event>();
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
    public void setSpecialStatus(List<SpecialStatus> specialStatus) {this.specialStatus = specialStatus; }
    public void addEvent(Event event) {
        this.events.add(event);
    }
    public void removeEvent(Event event) {
        this.events.remove(event);
    }
    public List<Event> getEvents() {
        return events;
    }
    @NonNull
    public String toString() {
        String result = "";
        for(int i = 0; i < events.toArray().length; i++) {
            result += events.get(i).getTitle() + " ";
        }
        return result;
    }
}
