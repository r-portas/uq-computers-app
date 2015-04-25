package com.roy.uqlibrarycomputers;

import android.location.Location;

/**
 * Created by roy on 30/03/15.
 */
public class LibraryComputer {
    private String available;
    private String building;
    private Location location;

    public LibraryComputer(String building, String available){
        this.building = building;
        this.available = available;
    }

    public String getBuilding(){
        return building;
    }

    public String getAvailable(){
        return available;
    }

    public Location getLocation() { return location; }

    public String toString(){
        return building + ": " + available;
    }
}
