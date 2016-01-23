package io.alstonlin.pennapps_android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * This class handles the communication between the Server and the App.
 */
public class DAO {
    private static String SITE_URL = "http://45.33.80.138:8080";
    private static String LOGIN_URL = "/login";
    private static String REGISTER_URL = "/register";
    private static String REQUESTS_URL = "/requests";
    private static String NEW_REQUEST_URL = "/new_request";
    private static String NEW_CHAT_URL = "/new_chat";
    private static String POST_CHATS_URL = "/post_chats";
    private static String REQUEST_CHATS_URL = "/request_chats";
    private static String POSTS_URL = "/posts";
    private static String NEW_MESSAGE_URL = "/new_msg";
    private static DAO instance;

    private HttpURLConnection conn;
    private boolean internet = true;
    private Activity activity;
    private int eventId;
    private String deviceId;
    private String token;
    private String email;
    private DAO(){}


    /**
     * Use this to get the Singleton of DAO; instantiate() must be called first before calling this!
     *
     * @return The Singleton DAO Object
     */
    public static DAO getInstance(){
        if (instance == null){
            instance = new DAO();
        }
        return instance;
    }



    public void signUp(String name, String email, String password, JSONRunnable after) throws JSONException, IOException {
        PostTask task = new PostTask(REGISTER_URL, after);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        task.execute(nameValuePairs);
    }

    public void login(String email, String password, final JSONRunnable after){
        JSONRunnable getToken = new JSONRunnable() {
            @Override
            public void run(JSONObject json) {
                try {
                    DAO.this.token = json.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                after.run(json);
            }
        };
        PostTask task = new PostTask(LOGIN_URL, getToken);
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        this.email = email;
        task.execute(nameValuePairs);
    }

    public void getPosts(JSONRunnable after){
        PostTask task = new PostTask(POSTS_URL, after);
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("token", token));
        task.execute(nameValuePairs);
    }

    /*
       ------------------- INTERNET METHODS -------------------------------------
     */
    private void sendImage(Bitmap item) throws JSONException, UnsupportedEncodingException {
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        Random rand = new Random();
        FileOutputStream out = null;
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WildHacks";
        File dir = new File(file_path);

        if(!dir.exists())
            dir.mkdirs();
        try {
            File file = new File(dir, "sketchpad" + rand.nextInt(999) + ".jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            item.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file_path = file.getAbsolutePath();
            uploadImage u = new uploadImage();
            u.execute(file_path);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        File file = new File(file_path);
        ContentBody cb = new FileBody(file, "image/jpeg");

        entity.addPart("photo", cb);
        entity.addPart("eventID", new StringBody(Integer.toString(eventId)));
        entity.addPart("userID",  new StringBody(deviceId));
    }

    private class uploadImage extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... strings) {
            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder
                    .create();
            multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            ContentType contentType = ContentType.create("image/jpg");
            multipartEntity.addPart("photo", new FileBody(new File(strings[0]), contentType, "image.jpg"));
            HttpPost post = new HttpPost("http://polarfeed.mybluemix.net/fileupload");
            try {
                post.addHeader("eventID", String.valueOf(new StringBody(Integer.toString(eventId))));//id is anything as you may need
                multipartEntity.addPart("eventID", new StringBody(Integer.toString(eventId)));
                multipartEntity.addPart("userID", new StringBody(deviceId));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            post.setEntity(multipartEntity.build());
            HttpClient client = new DefaultHttpClient();

            try {
                HttpResponse response = client.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class PostTask extends AsyncTask<List<NameValuePair>, Void, JSONObject> {
        private String path;
        private JSONRunnable after;

        public PostTask(String path, JSONRunnable after){
            this.path = path;
            this.after = after;
        }


        @Override
        protected JSONObject doInBackground(List<NameValuePair>... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(SITE_URL + path);
            try {
                httppost.setEntity(new UrlEncodedFormEntity(params[0]));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                JSONObject output = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
                return output;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject value) {
            super.onPostExecute(value);
            after.run(value);
        }
    }

}
