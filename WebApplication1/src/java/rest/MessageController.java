/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author c0683339
 */
@ApplicationScoped
public class MessageController {
 /**
  * date initialize
  * declare list
  */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:SS.sssxxx");
    List<Message> messages;

    public MessageController() throws SQLException {
        //messages = new ArrayList<>();
        retrieveAllMessages();

    }
  /**
   * method to connect database to select from database
   * @throws SQLException 
   */
    public void retrieveAllMessages() throws SQLException {
        try {
            if (messages == null) {
                messages = new ArrayList<>();
            }
            messages.clear();
            Connection conn = DBUtils.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM message");
            while (rs.next()) {
                Message m = new Message();
                m.setId(rs.getInt("id"));
                m.setTitle(rs.getString("title"));
                m.setContents(rs.getString("contents"));
                m.setAuthor(rs.getString("author"));
                m.setSenttime(rs.getDate("senttime"));
                messages.add(m);

            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/**
 * to enter details and update in it
 * @param m 
 */
    public void persistToDb(Message m) {

        try {
            String sql = "";
            Connection conn = DBUtils.getConnection();
            if (m.getId() <= 0) {
                sql = "INSERT INTO message (title, contents,author,senttime) VALUES (?, ?, ?, ?)";
            } else {
                sql = "UPDATE message SET title=?,contents=?,author=?,senttime=? WHERE id=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, m.getTitle());
                pstmt.setString(2, m.getContents());
                pstmt.setString(3, m.getAuthor());
                pstmt.setDate(4, new java.sql.Date(m.getSenttime().getTime()));
                if (m.getId() > 0) {
                    pstmt.setInt(5, m.getId());
                }
                pstmt.executeUpdate();
                if (m.getId() <= 0) {
                    ResultSet rs = pstmt.getGeneratedKeys();
                    rs.next();
                    int id = rs.getInt(1);
                    m.setId(id);
                }
                conn.close();

            }

        } catch (SQLException ex) {
            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * delete from database
     * @param m
     * @throws SQLException 
     */
    public void removeFromDb(Message m) throws SQLException {
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM message WHERE id= ?");
            pstmt.setInt(1, m.getId());
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 /**
  * for jsonArray
  * @return 
  */
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
/**
 * to get by id
 * @param id
 * @return 
 */
    public JsonObject getByIdJson(int id) {
        Message m = getById(id);
        if (m != null) {
            return getById(id).toJSON();

        } else {
            return null;
        }
    }

    public JsonArray getByDateJson(Date from, Date to) {
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Message m : messages) {
            if ((m.getSenttime().after(from) && m.getSenttime().before(to)) || m.getSenttime().equals(from) || m.getSenttime().equals(to)) {
                json.add(m.toJSON());
            }
        }
        return json.build();

    }
/**
 * to add jsonObject
 * @param json
 * @return 
 */
    public JsonObject addJson(JsonObject json) {
        Message m = new Message(json);
        persistToDb(m);
        messages.add(m);
        return m.toJSON();
    }
/**
 * to change Json 
 * @param id
 * @param json
 * @return 
 */
    public JsonObject editJson(int id, JsonObject json) {

        Message m = getById(id);
        m.setTitle(json.getString("title", ""));
        m.setContents(json.getString("contents", ""));
        m.setAuthor(json.getString("author", ""));
        String timeStr = json.getString("senttime", "");
        try {

            m.setSenttime(sdf.parse(timeStr));

        } catch (ParseException ex) {

            m.setSenttime(new Date());

            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, "Failed Parsing Date");
        }
        persistToDb(m);
        return m.toJSON();

    }
    /**
     * for delete call by id its delete
     * @param id
     * @return 
     */

    public boolean deleteByid(int id) {

        Message m = getById(id);
        if (m != null) {
            messages.remove(m);
            return true;

        } else {

            return false;

        }
    }

    JsonObject getAllJson(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
/**
 * for date json call
 * @param from
 * @param to
 * @return 
 */
    Object getByDateJson(java.util.Date from, java.util.Date to) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
