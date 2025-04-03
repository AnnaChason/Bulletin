package com.csci335.bulletin.StudentClasses;

import java.util.Set;

public class Student {
    private String ID;
    private String email;
    private String password;
    private String name;
    private int age;
    private Year year;
    private Gender gender;
    private Set<SpecialStatus> specialStatus;

    // unneeded?
    public Student(String ID){
        this.ID = ID;
    }

    public Student() {}

    public String getID() { return ID; }
    public void setID(String ID) { this.ID = ID; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // should check permissions?
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public Year getYear() { return year; }
    public void setYear(Year year) { this.year = year; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public Set<SpecialStatus> getSpecialStatus() { return specialStatus; }
    public void setSpecialStatus(SpecialStatus specialStatus) { this.specialStatus.add(specialStatus); }
}
