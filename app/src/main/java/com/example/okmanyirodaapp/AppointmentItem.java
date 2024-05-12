package com.example.okmanyirodaapp;


public class AppointmentItem {

    private final String name;
    private final String date;

    public AppointmentItem(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String  getDate() {
        return date;
    }

}
