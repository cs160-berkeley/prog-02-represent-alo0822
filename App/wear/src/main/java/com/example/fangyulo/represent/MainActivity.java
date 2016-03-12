package com.example.fangyulo.represent;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.GridViewPager;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class MainActivity extends WearableActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Cj767iFb3srP466ollqifRedk";
    private static final String TWITTER_SECRET = "lQvaWyV8roX2TGvXxjXHDy5EUpV05cWPfExteMWsY0HIrDvEeM";


    TextView title;

    GridViewPager pager;
    MyGridViewPagerAdapter adapter;

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    Typeface playfair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.splash_layout);

        title = (TextView) findViewById(R.id.main_title);
        playfair = Typeface.createFromAsset(getAssets(), "fonts/PlayfairDisplaySC-Bold.ttf");
        title.setTypeface(playfair);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        System.out.println("--------getting intent extras--------");

        if (extras != null) {
            String data = extras.getString("reps_data");
            System.out.println("--------Intent extra: " + data + "--------");
            if (data.equals("0")) {
                setContentView(R.layout.no_reps_layout);
            } else {
                updateContentView(R.layout.activity_main, data);
            }
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        System.out.println("------- set sensor manager ---------");

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                System.out.println("---------- Detected shake! -----------");
                setContentView(R.layout.shake_layout);
                new GetRandomZipCode().execute();
            }
        });

    }

    private class GetRandomZipCode extends AsyncTask<String, Void, String> {
        String shake_loc;

        @Override
        protected String doInBackground(String... params) {
            shake_loc = getRandZip();
            return shake_loc;
        }

        @Override
        protected void onPostExecute(String results){
            Intent sendIntent = new Intent(MainActivity.this, WatchToPhoneService.class);
            sendIntent.putExtra("shake_loc", shake_loc);
            startService(sendIntent);
        }
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
        String[] str = data.split(", ");
        int row = (int)(str.length - 2) / 4;
        adapter = new MyGridViewPagerAdapter(row, 1, str);
        pager.setAdapter(adapter);

    }

    public String getRandZip() {
        String str = "";
        AssetManager assetManager = getApplication().getAssets();
        try {
            InputStream is = assetManager.open("valid-zips.json");
            InputStreamReader isr = new InputStreamReader(is);
            char[] inputBuff = new char[100];

            int charRead;
            while (( charRead = isr.read(inputBuff)) > 0 ) {
                String read = String.copyValueOf(inputBuff, 0, charRead);
                str += read;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray zips = null;
        String rand_zip = null;
        Random rand = new Random();
        try {
            zips = new JSONArray(str);
            rand_zip = zips.getString(rand.nextInt(zips.length()));
            System.out.println("rand: " + rand_zip);
            return rand_zip;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "94704";
    }


}
