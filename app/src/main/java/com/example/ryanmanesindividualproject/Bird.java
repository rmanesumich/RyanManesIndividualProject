package com.example.ryanmanesindividualproject;

public class Bird {
    public String email;
    public String birdName;
    public String zip;
    public Integer importance;

    //a blank constructor
    public Bird(){

    }

    public Bird(String email, String birdName, String zip, Integer importance) {
        this.email = email;
        this.birdName = birdName;
        this.zip = zip;
        this.importance = importance;
    }
}
