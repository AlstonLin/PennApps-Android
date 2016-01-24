package io.alstonlin.pennapps_android;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {
    private String from;
    private String content;
    private Chat chat;

    public Message(String from, String content, Chat chat) {
        this.from = from;
        this.content = content;
        this.chat = chat;
    }

    public void publish(final Context context){
        JSONRunnable after = new JSONRunnable() {
            @Override
            public void run(JSONObject json) {
                try {
                    if (!json.getBoolean("res")) {
                        Toast.makeText(context, "There was an error while publishing this message.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DAO.getInstance().newMessage(chat.getId(), content, after);
    }

    public String getFrom() {
        return from;
    }

    public String getContent() {
        return content;
    }

    public Chat getChat() {
        return chat;
    }
}
