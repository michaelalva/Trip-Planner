package com.tco.misc;


public class Person {

    String name;
    String netid;
    String hometown;
    String biography;

    private Person(){}  // prevent use of default constructor

    public Person(String name, String netid, String hometown, String biography){
        this.name = name;
        this.netid = netid;
        this.hometown = hometown;
        this.biography = biography;
    }

}