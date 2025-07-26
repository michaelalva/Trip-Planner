package com.tco.misc;

import java.util.ArrayList;

public class People extends ArrayList<Person> {

  public People() {

    add(person1());
    add(person2());
    add(person3());
    add(person4());
    add(person5());
  }

  Person person1() {
    final String name = "Leonardo Rodolico";
    final String netid = "leoo";
    final String hometown = "Asti, Italy";
    final String bio = "Junior Computer Science student at CSU with a concentration in AI/ML. I'm planning on getting a PhD in BCI. I'm a big fan of all types of exercising (running, weight-lifting, etc.), video-games (League of Legends, Teamfight Tactics, and Path of Exile 2), and reading fiction books.";

    return new Person(name, netid, hometown, bio);
  }

  Person person2() {
    final String name = "Zachary Kinnaman";
    final String netid = "zack722";
    final String hometown = "Ventura, California";
    final String bio = "Born and raised in Ventura, CA. Junior computer science student at CSU. I enjoy hiking, gaming, and lifting weights.";

    return new Person(name, netid, hometown, bio);
  }

  Person person3() {
    final String name = "Cougar Fischer";
    final String netid = "cougar";
    final String hometown = "Maple Valley, Washington";
    final String bio = "Born in Renton, WA and grew up in Maple Valley, WA. I enjoy gaming, reading, woodworking, and working on my NAS.";

    return new Person(name, netid, hometown, bio);
  }

  Person person4() {
    final String name = "Michael Duy Alva";
    final String netid = "duyalva";
    final String hometown = "Storm Lake, Iowa";
    final String bio = "I'm a junior computer science student from Greeley, CO, with a concentration in software engineering. I enjoy going to the gym, playing many different sports, doing outdoor activities, and watching YouTube videos.";

    return new Person(name, netid, hometown, bio);
  }

  Person person5() {
    final String name = "Bryce Shakin";
    final String netid = "bryce24";
    final String hometown = "Castle Rock, Colorado";
    final String bio = "I was born In California but moved to Colorado when I was five and have been here ever since. I love racing cars, building cars, and playing video games.";

    return new Person(name, netid, hometown, bio);
  }

}
