package io.alstonlin.pennapps_android;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BuyFragment extends Fragment {

    private PopupWindow connectionListPopup;

    public BuyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.buy_fragment, container, false);
        setupButtons(v);
        setupList(v);
        return v;
    }


    private void setupButtons(View v){
        Button newReq = (Button) v.findViewById(R.id.new_request);
        Button refresh = (Button) v.findViewById(R.id.refresh);
        newReq.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupLayout = layoutInflater.inflate(R.layout.request_pop_up, null, false);
                connectionListPopup = new PopupWindow(popupLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                connectionListPopup.showAtLocation(getActivity().findViewById(R.id.frame), Gravity.CENTER, 0, 0);
                connectionListPopup.update(0, 0, connectionListPopup.getWidth(), connectionListPopup.getHeight());
                setupPopupButtons(popupLayout);
            }
        });
        refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final BuyListAdapter adapter = (BuyListAdapter)((ListView) getActivity().findViewById(R.id.list)).getAdapter();
                DAO.getInstance().getPosts(new JSONRunnable() {
                    @Override
                    public void run(JSONObject json) {
                        try {
                            ArrayList<Request> requests = getRequests(json);
                            adapter.setPosts(requests);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void setupPopupButtons(final View v){
        Button cancel = (Button) v.findViewById(R.id.cancel);
        Button request = (Button) v.findViewById(R.id.request_item);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionListPopup.dismiss();
            }
        });
        request.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View btn) {
                String name = ((EditText)v.findViewById(R.id.name)).getText().toString();
                String location = ((EditText)v.findViewById(R.id.location)).getText().toString();
                String fee = ((EditText)v.findViewById(R.id.fee)).getText().toString();
                DAO.getInstance().newRequest(name, location, Double.parseDouble(fee), new JSONRunnable() {
                    @Override
                    public void run(JSONObject json) {
                        try {
                            if (json.getBoolean("res")) {
                                connectionListPopup.dismiss();
                            }
                            Toast.makeText(getActivity(), json.getString("response"), Toast.LENGTH_LONG);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void setupList(View v){
        final ListView list = (ListView) v.findViewById(R.id.list);
        DAO.getInstance().getPosts(new JSONRunnable() {
            @Override
            public void run(JSONObject json) {
                try {
                    ArrayList<Request> requests = getRequests(json);
                    list.setAdapter(new BuyListAdapter(requests));
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
            Request r = new Request(obj.getString("name"), obj.getString("location"), obj.getDouble("fee"));
            requests.add(r);
        }
        return requests;
    }
    private class BuyListAdapter extends BaseAdapter {

        public ArrayList<Request> posts;

        public BuyListAdapter(ArrayList<Request> posts){
            super();
            this.posts = posts;
        }

        @Override
        public int getCount() {
            return posts.size();
        }

        @Override
        public Object getItem(int i) {
            return posts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, final ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.request_list_item, viewGroup, false);
            }
            ((TextView)view.findViewById(R.id.name)).setText(posts.get(i).getName());
            ((TextView)view.findViewById(R.id.location)).setText(posts.get(i).getLocation());
            ((TextView)view.findViewById(R.id.fee)).setText(Double.toString(posts.get(i).getFee()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().setContentView(R.layout.buy_message);
                }
            });
            return view;
        }

        public void setPosts(ArrayList<Request> posts) {
            this.posts = posts;
        }
    }
}

