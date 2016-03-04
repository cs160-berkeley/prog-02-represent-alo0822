package com.example.fangyulo.represent;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    TextView title;
    Typeface playfair;
    RepsData rd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Window window = MainActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.blue));

        rd = (RepsData) getApplicationContext();
        rd.setHash();

        title = (TextView)findViewById(R.id.main_title);
        playfair = Typeface.createFromAsset(getAssets(), "fonts/PlayfairDisplaySC-Bold.ttf");
        title.setTypeface(playfair);

        final EditText zip_code_entry = (EditText) findViewById(R.id.zipcode_entry);
        System.out.println("zip_code_entry: " + zip_code_entry.getText().toString());

        final String current_zip = "94704";

        TextView current_loc = (TextView) findViewById(R.id.current_loc);
        current_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                String data = rd.getWatchInfo(current_zip);
                sendIntent.putExtra("reps_data", data);
                startService(sendIntent);
                System.out.println("--------Started service--------");

                Intent intent = new Intent(MainActivity.this, RepsActivity.class);
                intent.putExtra("zip_code", current_zip);
                startActivity(intent);
            }
        });

        Button search_btn = (Button) findViewById(R.id.search_button);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                String data = rd.getWatchInfo(zip_code_entry.getText().toString());
                sendIntent.putExtra("reps_data", data);
                startService(sendIntent);
                System.out.println("--------Started service--------");

                Intent intent = new Intent(MainActivity.this, RepsActivity.class);
                System.out.println("zip_code_entry: " + zip_code_entry.getText().toString());
                intent.putExtra("zip_code", zip_code_entry.getText().toString());
                startActivity(intent);
            }
        });
    }



}
