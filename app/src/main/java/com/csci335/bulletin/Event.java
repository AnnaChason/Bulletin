package com.csci335.bulletin;
/*
class to hold data for each event post
 */
public class Event {
    private String name;
    private String date;
    private String location;
    private String description;
    private int attending;
    private int posterImg;

    public Event(String location, String name, String date, String description, int posterImg) {
        this.location = location;
        this.name = name;
        this.date = date;
        this.description = description;
        this.posterImg = posterImg;
        this.attending = 0;
    }

    /*
    Getters and Setters
     */
    public String getEventName() {
        return name;
    }
    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public int getAttending() {
        return attending;
    }

    //adds num to number of attending (just use negative number to take away attendees)
    public void updateAttending(int num){
        attending += num;
    }
    public int getPosterImg() {
        return posterImg;
    }
}
