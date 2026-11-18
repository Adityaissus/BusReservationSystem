package com.busreservation.model;

public class SleeperBus extends Bus {
    public SleeperBus(int id, String name, int seats) { super(id, name, seats); }
    @Override public String getBusType() { return "SLEEPER"; }
}
