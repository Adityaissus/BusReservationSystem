package com.busreservation.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Ticket {
    private final long ticketId;
    private final int busId;
    private final int routeId;
    private final String passengerName;
    private final int age;
    private final String gender;
    private final String phone;
    private final int seatNumber;
    private final LocalDate journeyDate;
    private final BigDecimal price;
    private final String status;

    public Ticket(long ticketId, int busId, int routeId, String passengerName, int age,
                  String gender, String phone, int seatNumber, LocalDate journeyDate,
                  BigDecimal price, String status) {
        this.ticketId = ticketId;
        this.busId = busId;
        this.routeId = routeId;
        this.passengerName = passengerName;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.seatNumber = seatNumber;
        this.journeyDate = journeyDate;
        this.price = price;
        this.status = status;
    }

    public long getTicketId() { return ticketId; }
    public int getBusId() { return busId; }
    public int getRouteId() { return routeId; }
    public String getPassengerName() { return passengerName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public int getSeatNumber() { return seatNumber; }
    public LocalDate getJourneyDate() { return journeyDate; }
    public BigDecimal getPrice() { return price; }
    public String getStatus() { return status; }
}
