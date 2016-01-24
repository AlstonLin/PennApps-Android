package io.alstonlin.pennapps_android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SellFragment extends Fragment {

    private GoogleMap googleMap;
    private PopupWindow pw;

    public SellFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
        v = inflater.inflate(R.layout.sell_fragment, container, false);
        setupList(v);
        return v;
    }

    private void setupList(View v){
        final ListView list = (ListView) v.findViewById(R.id.sell_list);
        // TODO: Edit this so it will get only tasks

        DAO.getInstance().getRequests(new JSONRunnable() {
            @Override
            public void run(JSONObject json) {
                try {
                    ArrayList<Request> requests = getRequests(json);
                    list.setAdapter(new SellListAdapter(requests));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button refresh = (Button) getActivity().findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellListAdapter adapter = (SellListAdapter) ((ListView)getActivity().findViewById(R.id.sell_list)).getAdapter();
                refresh(adapter);
            }
        });

    }

    private void refresh(final SellListAdapter adapter){
        DAO.getInstance().getRequests(new JSONRunnable() {
            @Override
            public void run(JSONObject json) {
                try {
                    ArrayList<Request> requests = getRequests(json);
                    adapter.setRequests(requests);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ArrayList<Request> getRequests(JSONObject json) throws JSONException{
        JSONArray array = json.getJSONArray("result");
        ArrayList<Request> requests = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = (JSONObject) array.get(i);
            Request r = new Request(obj.getString("_id"), obj.getString("owner"), obj.getString("name"), obj.getString("location"), obj.getDouble("fee"));
            requests.add(r);
        }
        return requests;
    }

    private ArrayList<Chat> getChats(Request request, JSONObject json) throws JSONException{
        JSONArray array = json.getJSONArray("result");
        ArrayList<Chat> chats = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = (JSONObject) array.get(i);
            JSONArray messages = obj.getJSONArray("messages");
            ArrayList<Message> messageList = new ArrayList<>();
            Chat chat = new Chat(obj.getString("_id"), null, obj.getString("poster"), obj.getString("responder"), obj.getString("poster_name"), obj.getString("responder_name"), request);
            for (int j = 0; j < messages.length(); j++){
                JSONObject o = messages.getJSONObject(j);
                messageList.add(new Message(o.getString("from"), o.getString("content"), chat));
            }
            chats.add(chat);
        }
        return chats;
    }

    private ArrayList<Message> getMessages(JSONObject json) throws JSONException {
        ArrayList<Message> messages = new ArrayList<Message>();
        JSONArray array = json.getJSONArray("result");
        for (int i = 0; i < array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            messages.add(new Message(obj.getString("from"), obj.getString("content"), null));
        }
        return messages;
    }

    private void refreshRequest(Request req, final MessageListAdapter adapter){
        DAO.getInstance().getRequestMessages(req, new JSONRunnable() {
            @Override
            public void run(JSONObject json) {
                try {
                    ArrayList<Message> messages = getMessages(json);
                    adapter.setMessages(messages);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private class SellListAdapter extends BaseAdapter {

        public ArrayList<Request> requests;

        public SellListAdapter(ArrayList<Request> posts){
            super();
            this.requests = posts;
        }

        @Override
        public int getCount() {
            return requests.size();
        }

        @Override
        public Object getItem(int i) {
            return requests.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public void setRequests(ArrayList<Request> requests){
            this.requests = requests;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.request_list_item, viewGroup, false);
            }
            ((TextView)view.findViewById(R.id.name)).setText(requests.get(i).getName());
            ((TextView)view.findViewById(R.id.location)).setText(requests.get(i).getLocation());
            ((TextView)view.findViewById(R.id.fee)).setText("$" + Double.toString(requests.get(i).getFee()));
            final Request req = requests.get(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DAO.getInstance().getRequestMessages(req, new JSONRunnable() {
                        @Override
                        public void run(JSONObject json) {
                            try {
                                ArrayList<Message> messages = getMessages(json);
                                final MessageListAdapter adapter = new MessageListAdapter(req, messages);
                                getActivity().setContentView(R.layout.buy_message);

                                ListView lv = (ListView) getActivity().findViewById(R.id.list);
                                lv.setAdapter(adapter);

                                Button send = (Button) getActivity().findViewById(R.id.send);
                                Button refresh = (Button) getActivity().findViewById(R.id.refresh);
                                final EditText content = (EditText) getActivity().findViewById(R.id.content);
                                send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DAO.getInstance().newMessage(req.getId(), content.getText().toString(), new JSONRunnable() {
                                            @Override
                                            public void run(JSONObject json) {
                                                content.setText("");
                                                refreshRequest(req, adapter);
                                            }
                                        });
                                    }
                                });
                                refresh.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        refreshRequest(req, adapter);
                                    }
                                });
                                Button back = (Button) getActivity().findViewById(R.id.back);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), AppActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });
            return view;
        }
    }

    private class MessageListAdapter extends BaseAdapter {
        private Request request;
        private ArrayList<Message> messages;

        public MessageListAdapter(Request request, ArrayList<Message> posts){
            super();
            this.request = request;
            this.messages = posts;
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int i) {
            return messages.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.message_list_item, viewGroup, false);
            }
            ((TextView)view.findViewById(R.id.name)).setText(messages.get(i).getFrom());
            ((TextView)view.findViewById(R.id.content)).setText(messages.get(i).getContent());
            MapsInitializer.initialize(getActivity());
            String location = request.getLocation();
            double latitude = 0;
            double longitude = 0;
            location = location.trim();
            int firstNumberEndIndex = location.indexOf(' ');
            latitude = Double.parseDouble(location.substring(0, firstNumberEndIndex));
            longitude = Double.parseDouble(location.substring(firstNumberEndIndex + 1));
                try {
                if (googleMap == null) {
                    googleMap = ((MapFragment) getActivity().getFragmentManager().
                            findFragmentById(R.id.map)).getMap();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
             Marker TP = googleMap.addMarker(new MarkerOptions().
                    position(new LatLng(latitude, longitude)).title("Location"));
            return view;
        }
        public void setMessages(ArrayList<Message> messages){
            this.messages = messages;
        }
    }
}
