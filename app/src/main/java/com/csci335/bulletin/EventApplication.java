package com.csci335.bulletin;

public class EventApplication {
    public String date;
    public String title;
    public String description;
    public String location;
    public int attendance;

    public EventApplication(String date, String title, String description, String location) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.location = location;
        this.attendance = 0;
    }

    public EventApplication() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }
}
