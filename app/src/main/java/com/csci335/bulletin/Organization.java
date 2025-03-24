package com.csci335.bulletin;
/*
Not connected to an activity.
Class for keeping track of each organization's data
 */
public class Organization {
    private String name;
    private String description;
    private String ID;

    public Organization(){}
    public Organization(String name, String description, String ID){
        this.name = name;
        this.description = description;
        this.ID = ID;
    }
    /*
    getters and setters
     */
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}

    public String getID(){return ID;}
    public void setID(String ID){this.ID = ID;}


}
