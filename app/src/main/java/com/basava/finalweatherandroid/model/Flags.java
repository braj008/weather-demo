package com.basava.finalweatherandroid.model;

public class Flags {
    private String neareststation;

    private String units;

    private String[] sources;

    public String getNeareststation() {
        return neareststation;
    }

    public void setNeareststation(String neareststation) {
        this.neareststation = neareststation;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String[] getSources() {
        return sources;
    }

    public void setSources(String[] sources) {
        this.sources = sources;
    }

    @Override
    public String toString() {
        return "ClassPojo [nearest-station = " + neareststation + ", units = " + units + ", sources = " + sources + "]";
    }
}
