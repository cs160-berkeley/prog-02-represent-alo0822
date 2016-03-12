package com.example.fangyulo.represent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends Activity {

    static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    static final String GOOGLE_API_KEY = "AIzaSyCHWGRDMGUrrVeFxSrttQ563uMhwU98pwE";

    TextView title;
    Typeface playfair;
    RepsData rd;
    String currentLocation;
    String latitude;
    String longitude;
    StringBuilder urlString;
    String current_zip;

    protected LocationManager locationManager;


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private final int REQUEST_PERMISSION = 10;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Window window = MainActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.blue));

        rd = (RepsData) getApplication();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        title = (TextView) findViewById(R.id.main_title);
        playfair = Typeface.createFromAsset(getAssets(), "fonts/PlayfairDisplaySC-Bold.ttf");
        title.setTypeface(playfair);

        final EditText zip_code_entry = (EditText) findViewById(R.id.zipcode_entry);
        System.out.println("zip_code_entry: " + zip_code_entry.getText().toString());

        TextView current_loc = (TextView) findViewById(R.id.current_loc);
        current_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCurrentLocation();
                Log.i("GEO", "click button, calling getCurrentAddress()");

            }
        });

        Button search_btn = (Button) findViewById(R.id.search_button);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, RepsActivity.class);
                intent.putExtra("zip_code", zip_code_entry.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void getLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Log.i("T", "lat: " + latitude);
            Log.i("T", "long: " + longitude);
            this.latitude = Double.toString(location.getLatitude());
            this.longitude = Double.toString(location.getLongitude());
            new DownloadWebpageTask().execute();
        }
    }

    protected void showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getLocation();
        }
        else{
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);

        } else {
            Toast toast = Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT);
            toast.show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
                return;

            } else {
                Toast toast = Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }



    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            urlString = new StringBuilder();
            urlString.append(GEOCODE_URL).append(latitude).append(",").append(longitude).append("&key=").append(GOOGLE_API_KEY);
            Log.i("LOC", "making api call");
            String result = makeAPICall();
            if (result == null) {
                Log.i("INFO", "Result is null");
            }
            try {
                System.out.println("-------- got response --------");
                JSONObject jsonRootObject = new JSONObject(result);
                System.out.println("-------- got json object,  --------");
                JSONArray jsonArrayResults = jsonRootObject.optJSONArray("results");
                JSONObject addr_components_obj = jsonArrayResults.getJSONObject(0);
                JSONArray addr_components = addr_components_obj.optJSONArray("address_components");
                String zip = null;
                for (int i = 0; i < addr_components.length(); i++) {
                    try {
                        String possible = addr_components.getJSONObject(i).getString("short_name");
                        JSONArray types = addr_components.getJSONObject(i).getJSONArray("types");
                        if (types.get(0).equals("postal_code")) {
                            zip = possible;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                current_zip = zip;
                result = zip;
                Log.i("LOC", "result is: " + result);
                currentLocation = result;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("LOC", "In onPostExecute for getting location information");
//            Toast.makeText(MainActivity.this, "Current location: " + currentLocation, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, RepsActivity.class);
            intent.putExtra("zip_code", current_zip);
            startActivity(intent);
        }

        protected String makeAPICall() {
            try {
                URL url = new URL(urlString.toString());
                System.out.println("-------- Setting connection with URL: " + url + " --------");
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                Log.e("ERROR", e.getMessage(), e);
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

    }
}
