package com.codecool;

import com.codecool.dao.GuestDAO;
import com.codecool.handlers.GuestHandler;
import com.codecool.handlers.Static;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        App app = new App();
        try{
            app.createServer();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void createServer() throws IOException{
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        Connection connection = SQLConnector.getConnection();
        GuestDAO guestDAO = new GuestDAO(connection);

        server.createContext("/", new GuestHandler(guestDAO));
        server.createContext("/static", new Static());
        server.setExecutor(null);

        server.start();
    }
}
