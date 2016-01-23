package io.alstonlin.pennapps_android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


public class RequestPopUp extends Activity{
        PopupWindow popUp;
        LinearLayout layout;
        TextView tv;
        LinearLayout.LayoutParams params;
        LinearLayout mainLayout;
        Button but;
        boolean click = true;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }
}
