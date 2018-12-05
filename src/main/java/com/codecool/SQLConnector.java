package com.codecool;

import java.sql.*;

public class SQLConnector {
    private static Connection connection = null;

    private static void createConnection() throws SQLException {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/guestbook", "guestbook", "admin123");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public static Connection getConnection(){
        if(connection == null){
            try {
                createConnection();
            } catch (SQLException e) {
                System.out.println("Couldn't createConnection to database");
            }
        } return connection;
    }
}