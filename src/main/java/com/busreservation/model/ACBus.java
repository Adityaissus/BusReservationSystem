package com.busreservation.model;

public class ACBus extends Bus {
    public ACBus(int id, String name, int seats) { super(id, name, seats); }
    @Override public String getBusType() { return "AC"; }
}
