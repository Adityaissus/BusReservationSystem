package com.busreservation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {

        String url = System.getenv().getOrDefault(
                "BUS_DB_URL",
                "jdbc:mysql://localhost:3306/bus_reservation?useSSL=false&serverTimezone=UTC"
        );

        String user = System.getenv().getOrDefault("BUS_DB_USER", "busapp");

        String password = System.getenv().getOrDefault(
                "BUS_DB_PASSWORD",
                "BusProject@123"
        );

        return DriverManager.getConnection(url, user, password);
    }
}