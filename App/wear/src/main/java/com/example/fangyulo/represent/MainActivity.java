package com.example.fangyulo.represent;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.Intent;
import android.widget.Toast;


public class MainActivity extends WearableActivity {

    TextView title;
    TextView text;

    GridViewPager pager;
    MyGridViewPagerAdapter adapter;

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        title = (TextView) findViewById(R.id.main_title);
        Typeface playfair = Typeface.createFromAsset(getAssets(), "fonts/PlayfairDisplaySC-Bold.ttf");
        title.setTypeface(playfair);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        System.out.println("--------getting intent extras--------");

        if (extras != null) {
            String data = extras.getString("reps_data");
            System.out.println("--------Intent extra: " + data + "--------");
            updateContentView(R.layout.activity_main, data);
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        System.out.println("------- set sensor manager ---------");

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                System.out.println("---------- Detected shake! -----------");
                int rand = (int) Math.floor(Math.random()*90000) + 10000;
                String shake_loc = Integer.toString(rand);
                Intent sendIntent = new Intent(MainActivity.this, WatchToPhoneService.class);
                sendIntent.putExtra("shake_loc", shake_loc);
                startService(sendIntent);
                Toast toast = Toast.makeText(MainActivity.this, "Shake!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    public void updateContentView(int layout, String data) {
        setContentView(R.layout.activity_main);
        pager = (GridViewPager) findViewById(R.id.pager);
        adapter = new MyGridViewPagerAdapter(3, 1, data);
        pager.setAdapter(adapter);

    }


}
