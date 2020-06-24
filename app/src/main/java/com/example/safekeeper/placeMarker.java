package com.example.safekeeper;

public class placeMarker {
    private String title;
    private String name;
    private  String address;

    public placeMarker() { }

    public placeMarker(String title, String name, String address) {
        this.title = title;
        this.name = name;
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
