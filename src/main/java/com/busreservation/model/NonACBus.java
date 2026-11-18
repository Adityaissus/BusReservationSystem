package com.busreservation.model;

public class NonACBus extends Bus {
    public NonACBus(int id, String name, int seats) { super(id, name, seats); }
    @Override public String getBusType() { return "NON_AC"; }
}
