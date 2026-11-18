package com.busreservation.interfaces;

import com.busreservation.exceptions.SeatBookedException;

public interface Reservable {
    void bookSeat(int busId, int seatNumber, java.time.LocalDate journeyDate)
            throws SeatBookedException;
    void cancelSeat(long ticketId) throws Exception;
}
