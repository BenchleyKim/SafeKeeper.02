package com.example.safekeeper;

public class UserMarker {
    private String profile;
    private String id;
    private int pw;
    private String userName;
    private  String userAge;

    public UserMarker(String profile, String id, String userName, String userAge) {
        this.profile = profile;
        this.id = id;
        this.userName = userName;
        this.userAge = userAge;
    }

    public UserMarker(){}

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPw() {
        return pw;
    }

    public void setPw(int pw) {
        this.pw = pw;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }
}
