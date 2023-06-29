package com.example.gymproject;

public class RpeInformation {
    private String time;
    private String date;
    private String duration;

    public RpeInformation() {
        // Default constructor required for Firestore
    }

    public RpeInformation(String time, String date, String duration) {
        this.time = time;
        this.date = date;
        this.duration = duration;
    }

//    public RpeInformation(String playerName, String email, String time, String date, String duration) {
//    }

    public RpeInformation(String firstName, String lastName, String email, String time, String date, String duration) {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
