package com.roy.uqlibrarycomputers;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by roy on 30/03/15.
 */
public class LibraryComputer {
    private String available;
    private String building;
    private LatLng location;

    public LibraryComputer(String building, String available, LatLng location){
        this.building = building;
        this.available = available;
        this.location = location;
    }

    public String getBuilding(){
        return building;
    }

    public String getAvailable(){
        return available;
    }

    public LatLng getLocation() { return location; }

    public String toString(){
        return building + ": " + available + ", at " + location.toString();
    }
}
