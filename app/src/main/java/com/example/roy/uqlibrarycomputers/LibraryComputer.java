package com.example.roy.uqlibrarycomputers;

/**
 * Created by roy on 30/03/15.
 */
public class LibraryComputer {
    private String available;
    private String building;

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

}
