package com.example.easypark.classes;

public class Avis {
    private int id;
    private String content;
    private String date;
    private String user;

    public Avis() {

    }
    public Avis(int id, String content, String user,  String date) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.user = user;
    }

    public Avis(String content, String user, String date) {
        this.content = content;
        this.date = date;
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Avis{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
