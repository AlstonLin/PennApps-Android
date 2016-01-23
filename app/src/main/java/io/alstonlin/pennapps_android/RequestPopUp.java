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
            popUp = new PopupWindow(this);
            layout = new LinearLayout(this);
            mainLayout = new LinearLayout(this);
            tv = new TextView(this);
            but = new Button(this);
            but.setText("Click Me");
            but.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    if (click) {
                        popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
                        popUp.update(50, 50, 300, 80);
                        click = false;
                    } else {
                        popUp.dismiss();
                        click = true;
                    }
                }

            });
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setOrientation(LinearLayout.VERTICAL);
            tv.setText("Hi this is a sample text for popup window");
            layout.addView(tv, params);
            popUp.setContentView(layout);
            // popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
            mainLayout.addView(but, params);
            setContentView(mainLayout);
        }
}
