package com.codecool.handlers;

import com.codecool.dao.GuestDAO;
import com.codecool.model.Guest;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuestHandler implements HttpHandler {
    private GuestDAO guestDAO;

    public GuestHandler(GuestDAO guestDAO){
        this.guestDAO = guestDAO;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("POST")){
            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String formData = bufferedReader.readLine();

            System.out.println(formData);

            Map<String, String> inputs = parseFormData(formData);
            String name = inputs.get("name");
            String message = inputs.get("message");
            Date date = Date.valueOf(LocalDate.now());

            System.out.println(date + "____" + name + ": " + message);

            Guest guest = new Guest(name, message, date);
            guestDAO.addGuest(guest);
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/guestbook.twig");
        JtwigModel model = JtwigModel.newModel();

        List<Guest> allGuests = guestDAO.getAllGuests();
        allGuests = reverseGuestList(allGuests);
        model.with("guests", allGuests);
        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> inputs = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs){
            String[] keyValue = pair.split("=");
            String value = URLDecoder.decode(keyValue[1], "UTF-8");
            inputs.put(keyValue[0], value);
        }
        return inputs;
    }

    private List<Guest> reverseGuestList(List<Guest> guests){
        List<Guest> reversedList = new ArrayList<>();
        for (int i=guests.size()-1; i>=0; --i){
            reversedList.add(guests.get(i));
        }
        return reversedList;
    }
}
