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
    private Request request;

    public Chat(String id, ArrayList<Message> messages, String posterId, String responderId, Request request) {
        this.id = id;
        this.messages = messages;
        this.posterId = posterId;
        this.responderId = responderId;
        this.request = request;
    }


    public void publish(final Context context){
        DAO.getInstance().newChat(request.getId(), responderId, posterId, new JSONRunnable() {
            @Override
            public void run(JSONObject json) {
                try {
                    if (!json.getBoolean("res")) {
                        Toast.makeText(context, "There was an error while publishing", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

    public Request getRequest() {
        return request;
    }

}
