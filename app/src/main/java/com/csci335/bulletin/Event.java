package com.csci335.bulletin;

import java.util.ArrayList;

/*
class to hold data for each event post
 */
public class Event implements Comparable<Event>{
    private String title;
    private String date;
    private String location;
    private String description;
    private int attendance;
    // posterImg is the complete database reference necessary to retrieve the image
    private String posterImg;
    private String category;
    private String organizationID;
    private String organizationName;

    public Event(String title, String date, String location, String description, String posterImg, int attendance, String category, String organizationID, String organizationName) {
        this.location = location;
        this.title = title;
        this.date = date;
        this.description = description;
        this.posterImg = posterImg;
        this.attendance = attendance;
        this.category = category;
        this.organizationID = organizationID;
        this.organizationName = organizationName;
    }
    public Event(){

    }

    public static String[] categoryOptions(){
        return new String[]{"", "Sport", "Music", "Ministry/Service", "Speaker", "Dance", "Faith", "Movie/Games", "informational"};
    }
    /*
    Getters and Setters
     */
    public String getTitle() {return title;}
    public void setTitle(String title){this.title=title;}
    public String getDate() {return date;}
    public void setDate(String date){this.date =date;}
    public String getLocation() {return location;}
    public void setLocation(String location){this.location = location;}
    public String getDescription() {return description;}
    public void setDescription(String description){this.description = description;}
    public int getAttendance() {return attendance;}
    public void setAttendance(int attendance){this.attendance = attendance;}
    //adds num to number of attendance (just use negative number to take away attendees)
    public void updateAttendance(int num){attendance += num;}
    public String getPosterImg() {return posterImg;}
    public void setPosterImg(String posterImg){this.posterImg = posterImg;}
    public String getCategory() {return category;}
    public void setCategory(String category){this.category = category;}
    public String getOrganizationID() {return organizationID;}
    public void setOrganizationID(String organizationID) {this.organizationID = organizationID;}
    public String getOrganizationName() {return organizationName;}
    public void setOrganizationName(String organizationName) {this.organizationName = organizationName;}

    /*
            Returns event date as a number that can be compared to other dates. form YYMMDD
             */
    public int dateToNum(){
        int dateNums = 0;
        try{
            int idx = date.indexOf("/");
            dateNums = Integer.parseInt(date.substring(0, idx)) * 100;
            dateNums += Integer.parseInt(date.substring(idx+1, date.indexOf("/",idx+1)));
            idx = date.indexOf("/",idx+1);
            String year = date.substring(idx+1);
            if(year.length() > 2)
                year = year.substring(year.length() - 2);
            dateNums += Integer.parseInt(year) * 10000;
    } catch (Exception e) {//date invalid
            dateNums = -999;
        }
        return dateNums;
    }

    /*
    comparing events by date earlier to later
        (could come up with other ways to sort it later like number of attendees)
    0 means equal, - number means this event is earlier, positive number means this event is later
     */
    @Override
    public int compareTo(Event e) {
        return Integer.compare(this.dateToNum(), e.dateToNum());
    }

}
