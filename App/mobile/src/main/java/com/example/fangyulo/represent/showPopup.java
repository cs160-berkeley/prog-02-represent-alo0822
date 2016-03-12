package com.example.fangyulo.represent;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by fangyulo on 3/11/16.
 * http://stackoverflow.com/questions/27409781/how-to-define-layout-in-a-popupwindow-from-an-xml-file-when-popupwindow-method
 */
public class showPopup {
    Context ctx;
    Button popup_exit;
    TextView popup_title, popup_email;

    public showPopup(Context ctx){
        this.ctx = ctx;
    }

    public void setEmail(String email, TextView popup_email) {
        popup_email.setText(email);
    }

    public void goJoe(View parent, String email){
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.pop_up_layout, null, false);
        final PopupWindow popup = new PopupWindow(popUpView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popup.setContentView(popUpView);
        popup_exit = (Button) popUpView.findViewById(R.id.popup_exit);
        popup_title = (TextView) popUpView.findViewById(R.id.popup_title);
        popup_email = (TextView) popUpView.findViewById(R.id.popup_email);
        setEmail(email, popup_email);
        popup.showAtLocation(parent, Gravity.CENTER_HORIZONTAL, 10, 10);

        popup_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

    }
}