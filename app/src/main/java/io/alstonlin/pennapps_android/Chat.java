package io.alstonlin.pennapps_android;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Chat {


    private String id;
    private ArrayList<Message> messages;
    private String posterId;
    private String responderId;
    private String posterName;
    private String responderName;
    private Request request;

    public Chat(String id, ArrayList<Message> messages, String posterId, String responderId, String posterName, String responderName, Request request) {
        this.id = id;
        this.messages = messages;
        this.posterId = posterId;
        this.responderId = responderId;
        this.request = request;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public String getPosterId() {
        return posterId;
    }

    public String getResponderId() {
        return responderId;
    }

    public String getPosterName() {
        return posterName;
    }

    public String getResponderName() {
        return responderName;
    }

    public Request getRequest() {
        return request;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
