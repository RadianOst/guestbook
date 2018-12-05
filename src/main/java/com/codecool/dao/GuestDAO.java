package com.codecool.dao;

import com.codecool.model.Guest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {
    private Connection connection;

    public GuestDAO(Connection connection) {
        this.connection = connection;
    }

    public void addGuest(Guest guest){
        String sql = "INSERT INTO guests (name, message, date) VALUES (?, ?, ?);";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, guest.getName());
            preparedStatement.setString(2, guest.getMessage());
            preparedStatement.setDate(3, guest.getDate());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Guest> getAllGuests(){
        List<Guest> guestList = new ArrayList<>();
        String sql = "SELECT * FROM guests;";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                guestList.add(parseGuestFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guestList;
    }

    private Guest parseGuestFromResultSet(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        String message = resultSet.getString("message");
        Date date = resultSet.getDate("date");

        return new Guest(name, message, date);
    }
}
