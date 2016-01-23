package io.alstonlin.pennapps_android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SellFragment extends Fragment{

    public SellFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sell_fragment, container, false);
    }

    private class SellListAdapter extends BaseAdapter {

        public ArrayList<Request> posts;

        public SellListAdapter(ArrayList<Request> posts){
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
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
                    // TODO: Make this change
                    Toast.makeText(getActivity(), "TODO: FINISH THIS", Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }
    }
}
