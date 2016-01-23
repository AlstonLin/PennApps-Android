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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BuyFragment extends Fragment implements OnClickListener{

    public BuyFragment() {
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
        View v = inflater.inflate(R.layout.buy_fragment, container, false);

        Button b = (Button) v.findViewById(R.id.new_request);
        b.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupLayout = layoutInflater.inflate(R.layout.request_pop_up, null, false);
        PopupWindow connectionListPopup = new PopupWindow(popupLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        connectionListPopup.showAtLocation(getActivity().findViewById(R.id.frame), Gravity.CENTER, 0, 0);
        connectionListPopup.update(0, 0, connectionListPopup.getWidth(), connectionListPopup.getHeight());
    }
}