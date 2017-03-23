package rest;


import java.text.SimpleDateFormat;
import javax.json.Json;
import javax.json.JsonObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author c0683339
 */
public class getClass {
        private int id;
    private String title;
    private String contents;
    private String author;
    SimpleDateFormat dt=new  SimpleDateFormat("");

    public getClass() {
   
    }
      public getClass(JsonObject json){ 
      id = json.getInt("productId"); 
         title = json.getString("name"); 
         contents = json.getString("contents"); 
          author= json.getString("author"); 
     } 

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

   

   public JsonObject toJSON() { 
        return Json.createObjectBuilder() 
                 .add("tId",id ) 
                 .add("name", title) 
                 .add("contents", contents) 
                 .add("author",author) 
                 .build(); 
     } 

    
    
}
