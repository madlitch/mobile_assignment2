package com.massimo.lab2;
public class Note {
    long ID;
    String title;
    String content;
    int color;

    Note(String title, String content, int color) {
        this.title = title;
        this.content = content;
        this.color = color;
    }

    Note(long ID, String title, String content, int color) {
        this.ID = ID;
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
