package com.example.safekeeper;

public class SafeMarker {
    private String markerTitle;
    private String markerSnippet;
    private double Marker_Lng;
    private double Marker_Lat;
        public SafeMarker(String markerTitle, String markerSnippet, double Marker_Lng, double Marker_Lat){
            this.markerTitle = markerTitle;
            this.markerSnippet=markerSnippet;
            this.Marker_Lng=Marker_Lng;
            this.Marker_Lat=Marker_Lat;

        }
        public SafeMarker(){}

    public String getMarkerTitle() {
        return markerTitle;
    }

    public void setMarkerTitle(String markerTitle) {
        this.markerTitle = markerTitle;
    }

    public String getMarkerSnippet() {
        return markerSnippet;
    }

    public void setMarkerSnippet(String markerSnippet) {
        this.markerSnippet = markerSnippet;
    }

    public double getMarker_Lng() {
        return Marker_Lng;
    }

    public void setMarker_Lng(float marker_Lng) {
        Marker_Lng = marker_Lng;
    }

    public double getMarker_Lat() {
        return Marker_Lat;
    }

    public void setMarker_Lat(float marker_Lat) {
        Marker_Lat = marker_Lat;
    }
}
