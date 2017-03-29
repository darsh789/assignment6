/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author c0683339
 */
public class MessageController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:SS.sssxxx");
    List<Message> messages;

    public MessageController() {
        messages = new ArrayList<>();

    }

    public JsonArray getAllJson() {

        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Message m : messages) {
            json.add(m.toJSON());
        }
        return json.build();
    }

    public Message getById(int id) {

        for (Message m : messages) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public JsonObject addJson(JsonObject json) {
        Message m = new Message(json);
        messages.add(m);
        return m.toJSON();
    }
    
   public boolean deleteByid(int id){
       
       Message m=getById(id);
       if(m!=null)
       {
           messages.remove(m);
           return true;
           
       }
       else{
           
           return false;
    
       }
        private final static String studentNumber = "C0683339";

    /**
     * Utility method used to create a Database Connection
     *
     * @return the Connection object
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        String server = "ipro.lambton.on.ca";
        String username = studentNumber + "-java";
        String password = studentNumber;
        String database = username;
        String jdbc = String.format("jdbc:mysql://%s/%s", server, database);
        return DriverManager.getConnection(jdbc, username, password);
    }
   }
   
   
   
   }
          
