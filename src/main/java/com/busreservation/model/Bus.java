package com.busreservation.model;

public class Bus {
    private final int busId;
    private final String busName;
    private final int totalSeats;

    public Bus(int busId, String busName, int totalSeats) {
        this.busId = busId;
        this.busName = busName;
        this.totalSeats = totalSeats;
    }

    public int getBusId() { return busId; }
    public String getBusName() { return busName; }
    public int getTotalSeats() { return totalSeats; }

    public String getBusType() { return "GENERAL"; }

    @Override
    public String toString() {
        return busName + " (" + getBusType() + ")";
    }
}
