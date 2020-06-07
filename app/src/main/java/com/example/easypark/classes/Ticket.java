package com.example.easypark.classes;

import java.io.Serializable;

public class Ticket implements Serializable {
    private int id;
    private String date, heureDebut, heureFin, duree;
    double longitude, latitude;
    int dureeInHour, dureeInMin, dureeSec;



    public Ticket() {
        date = Time.getTodayDate();
        heureDebut = Time.getTimeNow();
    }

    public Ticket(int id, String heureDebut, String heureFin, String duree, double longitude, double latitude, String date) {
        this.id = id;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.duree = duree;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getDate() {
        return date;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public int getDureeInHour() {
        return dureeInHour;
    }

    public void setDureeInHour(int dureeInHour) {
        this.dureeInHour = dureeInHour;
    }

    public int getDureeInMin() {
        return dureeInMin;
    }

    public void setDureeInMin(int dureeInMin) {
        this.dureeInMin = dureeInMin;
    }

    public int getDureeSec() {
        return dureeSec;
    }

    public void setDureeSec(int dureeSec) {
        this.dureeSec = dureeSec;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", heureDebut='" + heureDebut + '\'' +
                ", heureFin='" + heureFin + '\'' +
                ", duree='" + duree + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
