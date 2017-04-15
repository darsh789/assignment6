package rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.json.Json;
import javax.json.JsonObject;
import static javax.json.JsonValue.ValueType.NULL;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author c0683339
 */
public class Message {

    /**
     * initialization
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss.SSSXXX");
    private int id;
    private String title;
    private String contents;
    private String author;
    private Date senttime;
    SimpleDateFormat dt = new SimpleDateFormat("");

    /**
     * empty constructor
     */
    public Message() {

    }

    /**
     * json object create
     *
     * @param json
     */
    public Message(JsonObject json) {
        id = json.getInt("id", 0);
        title = json.getString("title", "");
        contents = json.getString("contents", "");
        author = json.getString("author", "");
        String timeStr = json.getString("senttime", "");

    }

    /**
     * getter and setter
     *
     * @return
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getSenttime() {
        return senttime;
    }

    public void setSenttime(Date senttime) {
        this.senttime = senttime;
    }

    /**
     * JSONObject with Builder
     *
     * @return
     */
    public JsonObject toJSON() {
        String timeStr = sdf.format(senttime);
        return Json.createObjectBuilder()
                .add("Id", id)
                .add("title", title)
                .add("contents", contents)
                .add("author", author)
                .add("senttime", timeStr)
                .build();
    }

}
