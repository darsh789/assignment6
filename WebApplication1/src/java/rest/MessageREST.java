/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.sun.istack.internal.logging.Logger;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Session;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.websocket.OnMessage;
import javax.websocket.server.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author c0683339
 */
@ApplicationScoped

public class MessageREST {
/**
 * declaration
 */
    @Inject
    private MessageController messageController;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss.SSSXXX");
    private String message;
/**
 * GET method call
 * @return 
 */
    @GET
    @Produces("application/json")
    public Response getAll() {
        return Response.ok(messageController.getAllJson()).build();

    }
  
    @Path("{id}")
    @Produces("application/json")
    public Response getById(@PathParam("id") int id) {

        JsonObject json = messageController.getByIdJson(id);
        if (json != null) {
            return Response.ok(json).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }
/**
 * for date
 * @param fromStr
 * @param toStr
 * @return 
 */
    @GET
    @Path("{from}/{to}")
    @Produces("application/json")
    public Response getByDate(@PathParam("from") String fromStr, @PathParam("to") String toStr) {
        try {
            return Response.ok(messageController.getByDateJson(sdf.parse(fromStr), sdf.parse(toStr)))
            
        } catch (ParseException ex) {
            Logger.getLogger(MessageREST.class.getName()).log(Level.SEVERE, null, ex);

            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return null;
    }
/**
 * post method
 * @param json
 * @return 
 */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response add(JsonObject json) {
        return Response.ok(messageController.addJson(json)).build();
    }
/**
 * put method
 * @param id
 * @param json
 * @return 
 */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response edit(@PathParam("id") int id, JsonObject json) {

        JsonObject jsonWithId = messageController.editJson(id, json);
        if (jsonWithId != null) {
            return Response.ok(jsonWithId).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
/**
 * to delete
 * @param id
 * @return 
 */
    @DELETE
    @Path("{id}")
    public Response del(@PathParam("id") int id) {

        if (messageController.deleteByid(id)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();

        }
    }
/**
 * to fetch Json with all method
 * @param mesage
 * @param session 
 */
    @OnMessage
    public void onMessage(String mesage, Session session) {
        String output = "";
        JsonObject json = Json.createReader(new StringReader(message)).readObject();
        if (json.containsKey("getAll")) {
            output = messageController.getAllJson().toString();
        } else if (json.containsKey("getById")) {

            output = messageController.getByIdJson(json.getInt("getById")).toString();

        } else if (json.containsKey("getFromTo")) {
            JsonArray dates = json.getJsonArray("getFromTo");

            try {
                Date from = sdf.parse(dates.getString(0));
                Date to = sdf.parse(dates.getString(1));
                output = messageController.getByDateJson(from, to).toString();
            } catch (ParseException ex) {

                output = Json.createObjectBuilder().add("error", "error parsing dates:" + dates.toString()).build().toString();
            }
        } else if (json.containsKey("post")) {
            output = messageController.addJson(json.getJsonObject("post")).toString();
        } else if (json.containsKey("put")) {
            int id = json.getJsonObject("put").getInt("id");
            output = messageController.editJson(id, json.getJsonObject("put")).toString();
        } else if (json.containsKey("delete")) {
            output = Json.createObjectBuilder().add("error", "Invalid Request").add("origional", json).build().toString();

        }
        session.getBasicRemote().sendText(output);

    }

}
