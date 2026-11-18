package com.busreservation.model;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Route {
    private final int routeId;
    private final Bus bus;
    private final String source;
    private final String destination;
    private final LocalTime departureTime;
    private final LocalTime arrivalTime;
    private final BigDecimal price;

    public Route(int routeId, Bus bus, String source, String destination,
                 LocalTime departureTime, LocalTime arrivalTime, BigDecimal price) {
        this.routeId = routeId;
        this.bus = bus;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
    }

    public int getRouteId() { return routeId; }
    public Bus getBus() { return bus; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public LocalTime getDepartureTime() { return departureTime; }
    public LocalTime getArrivalTime() { return arrivalTime; }
    public BigDecimal getPrice() { return price; }

    @Override
    public String toString() {
        return bus.getBusName() + " | " + bus.getBusType() + " | "
                + source + " -> " + destination + " | "
                + departureTime + " - " + arrivalTime + " | Rs. " + price;
    }
}
