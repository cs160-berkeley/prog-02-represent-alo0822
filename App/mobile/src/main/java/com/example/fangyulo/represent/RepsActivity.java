package com.example.fangyulo.represent;
//https://www.binpress.com/tutorial/android-l-recyclerview-and-cardview-tutorial/156

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.TweetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import io.fabric.sdk.android.Fabric;
import retrofit.http.GET;
import retrofit.http.Query;

public class RepsActivity extends Activity {

    RepsData rd;
    String CURR_ZIP = null;
    RecyclerView recList;
    ProgressBar progress_bar;
    boolean getReps = true;
    int i;
    String COUNTY;
    String STATE;
    TextView noreps;

    static final String SUNLIGHT_API_KEY = "eb30cd67cda24c2484bbc2ada7ff3459";
    static final String SUNLIGHT_API_URL = "https://congress.api.sunlightfoundation.com";
    static StringBuilder urlString = new StringBuilder();

    private static final String TWITTER_KEY = "Cj767iFb3srP466ollqifRedk";
    private static final String TWITTER_SECRET = "lQvaWyV8roX2TGvXxjXHDy5EUpV05cWPfExteMWsY0HIrDvEeM";

    static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    static final String GOOGLE_API_KEY = "AIzaSyCHWGRDMGUrrVeFxSrttQ563uMhwU98pwE";

    static final String ELECTION_URL = "https://d1b10bmlvqabco.cloudfront.net/attach/ijddlu9pcyk1sk/hq1pd634o47ic/ilndte8ipnrj/newelectioncounty2012.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reps);

        Window window = RepsActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(RepsActivity.this.getResources().getColor(R.color.blue));

        rd = (RepsData)getApplication();
//        rd.printHash();
        System.out.println("Reps Activity");

        EditText search_text = (EditText) findViewById(R.id.search_text);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        progress_bar.getIndeterminateDrawable().setColorFilter(0xFF0D47A1, android.graphics.PorterDuff.Mode.MULTIPLY);
        noreps = (TextView) findViewById(R.id.noreps);

        String zip_code = search_text.getText().toString();

        // Get zip code that was searched and display in the search bar
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            zip_code = extras.getString("zip_code");
            search_text.setText(zip_code);
            CURR_ZIP = zip_code;
        }

        Log.i("T", "in RepsAct, got intent, message is: " + CURR_ZIP);

        callAsyncTask();

        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText search_text = (EditText) v.findViewById(R.id.search_text);
                search_text.requestFocus();
            }
        });
        search_text.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    EditText search_zip = (EditText) v.findViewById(R.id.search_text);
                    Intent intent = new Intent(RepsActivity.this, RepsActivity.class);
                    System.out.println("--------- new zip to search: " + search_zip.getText().toString());
                    intent.putExtra("zip_code", search_zip.getText().toString());
                    startActivity(intent);
                    System.out.println("--------- start activity");
                }
                return false;
            }
        });
    }


    protected void callAsyncTask() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            System.out.println("-------- calling dl webpage task execute --------");
            new DownloadWebpageTask().execute();

        } else {
            Log.i("INFO", "No network connection available.");
        }
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progress_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            urlString = new StringBuilder();
            // make API call to get reps with zip code
            urlString.append(SUNLIGHT_API_URL).append("/legislators/locate?zip=").append(CURR_ZIP).append("&apikey=").append(SUNLIGHT_API_KEY);
            String result = makeAPICall();
            // set basic info for each rep
            if(result == null) {
                Log.i("INFO", "Result is null");
            }
            try {
                System.out.println("-------- got response: + " + result + " --------");
                JSONObject jsonRootObject = new JSONObject(result);
                if (jsonRootObject.optInt("count") == 0) {
                    Log.i("INFO", "no reps found");
                    getReps = false;
                    return null;
                }
                System.out.println("-------- got json object, calling setHashWithZip --------");
                rd.setHashWithZip(jsonRootObject, CURR_ZIP);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // make API call for each rep to get images, committees, and bills
            for (String key : rd.allReps.keySet()) {
                RepsInfo rep = rd.allReps.get(key);
                System.out.println("Adding details for: " + rep.get_name());
                String bioguide_id = rep.get_bioguide();
                // get images for each rep
                urlString = new StringBuilder();
                urlString.append("https://theunitedstates.io/images/congress").append("/original").append(bioguide_id).append(".jpg");

                // get committees for each rep
                urlString = new StringBuilder();
                urlString.append(SUNLIGHT_API_URL).append("/committees?member_ids=").append(bioguide_id).append("&apikey=").append(SUNLIGHT_API_KEY);
                result = makeAPICall();
                handleResult(result, "committee", rep);
                // get bills for each rep
                urlString = new StringBuilder();
                urlString.append(SUNLIGHT_API_URL).append("/bills?sponsor_id=").append(bioguide_id).append("&apikey=").append(SUNLIGHT_API_KEY);
                result = makeAPICall();
                handleResult(result, "bills", rep);
            }

            urlString = new StringBuilder();
            Log.i("LOC", "calling geocode for county and state");
            urlString.append(GEOCODE_URL).append(CURR_ZIP).append("&key=").append(GOOGLE_API_KEY);
            result = makeAPICall();
            if(result == null) {
                Log.i("INFO", "Result is null");
            }
            try {
                System.out.println("-------- got response --------");
                JSONObject jsonRootObject = new JSONObject(result);
                System.out.println("-------- got json object,  --------");
                JSONArray jsonArrayResults = jsonRootObject.optJSONArray("results");
                JSONObject addr_components_obj = jsonArrayResults.getJSONObject(0);
                JSONArray addr_components = addr_components_obj.optJSONArray("address_components");

                getCountyState(addr_components);

                Log.i("LOC", "county is: " + COUNTY);
                Log.i("LOC", "state is: " + STATE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            urlString = new StringBuilder();
            Log.i("DATA", "getting vote view data");
            urlString.append(ELECTION_URL);
            result = makeAPICall();
            if (result == null) {
                Log.i("INFO", "Result is null");
            }
            try {
                Log.i("LOC", "setting loc in rd");
                JSONObject jsonRootObject = new JSONObject(result);
                Log.i("T", "check 2 county and state: " + COUNTY + ", " + STATE);
                rd.getVoteData(COUNTY, STATE, jsonRootObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void getCountyState(JSONArray array) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    String long_name = array.getJSONObject(i).getString("long_name");
                    String result = array.getJSONObject(i).getString("short_name");
                    JSONArray types = array.getJSONObject(i).getJSONArray("types");
                    for (int j = 0; j < types.length(); j++) {
                        if (types.get(j).equals("administrative_area_level_2")) {
                            COUNTY = result.replace(" County", "");
                        } else if (types.get(j).equals("administrative_area_level_1")){
                            STATE = result;
                            if (COUNTY == null)
                                COUNTY = long_name;
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (getReps)
                callSetTwitter();
            else {
                progress_bar.setVisibility(View.GONE);
                noreps.setVisibility(View.VISIBLE);
//                String zip = getRandZip();
//                callNewIntent(zip);
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                String data = "0";
                System.out.println("data to watch: " + data);
                sendIntent.putExtra("reps_data", data);
                startService(sendIntent);
                System.out.println("--------Started service--------");
            }
        }

        protected void handleResult(String result, String type, RepsInfo rep) {
            if(result == null) {
                Log.i("INFO", "Result is null");
            }
            try {
                System.out.println("-------- got response: + " + result);
                JSONObject jsonRootObject = new JSONObject(result);
                if (type.equals("committee")) {
                    System.out.println("-------- got json object, calling setRepCommittees --------");
                    rd.setRepCommittees(jsonRootObject, rep);
                    System.out.println("----- finish setting comms -----");
                } else if (type.equals("bills")) {
                    System.out.println("-------- got json object, calling setRepBills --------");
                    rd.setRepBills(jsonRootObject, rep);
                    System.out.println("----- finish setting bills -----");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    public void callNewIntent(String zip) {
        EditText search_zip = (EditText) findViewById(R.id.search_text);
        search_zip.setText(zip);
        Intent intent = new Intent(RepsActivity.this, RepsActivity.class);
        intent.putExtra("zip_code", search_zip.getText().toString());
        startActivity(intent);
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

    protected void callSetTwitter() {
        i = 0;
        for (String key : rd.allReps.keySet()) {
            Log.i("TWEET", "in callsettwitter, i: " + i);
            Log.i("TWEET", "in callsettwitter, size is: " + rd.allReps.keySet().size());
            Log.i("TWEET", "in callsettwitter, about to set: " + key);
            setTwitter(rd.allReps.get(key), rd.allReps.keySet().size());
        }

    }

    public void setTwitter(final RepsInfo rep, final int size) {
        System.out.println("in setTwitter");
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        System.out.println("Set authConfig");
        Fabric.with(this, new Twitter(authConfig));
        System.out.println("Set Fabric.with()");
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                Log.i("TWEET", "in logInGuest callback");
                AppSession session = appSessionResult.data;
                MyTwitterApiClient twitterApiClient = new MyTwitterApiClient(session);
                twitterApiClient.getCustomService().show(null, rep.get_twitter_id(), true, new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        //set profile image url in rep
                        rep.set_smallimg(result.data.profileImageUrlHttps);
                        rep.set_img(result.data.profileImageUrlHttps.replace("_normal", ""));
                        Log.i("TWEET", "set curr_img: " + rep.get_img());
                        Log.i("TWEET", result.data.name);

                        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
                        Fabric.with(RepsActivity.this, new Twitter(authConfig));
                        TweetUtils.loadTweet(result.data.status.id, new Callback<Tweet>() {
                            @Override
                            public void success(Result<Tweet> result) {
                                Log.i("TWEET", "set tweet");
                                //set tweet in rep
                                rep.set_tweet(result.data);
                                Log.i("TWEET", rep.get_tweet().text);
                                i++;
                                Log.i("TWEET", "increment i, i is now " + i);
                                if (i == size)
                                    new DownloadWebpageTask2().execute();
                            }
                            @Override
                            public void failure(TwitterException e) {
                                Log.i("TWITTER", "Twitter not working");
                            }
                        });
                    }
                    @Override
                    public void failure(TwitterException e) {
                        Log.i("TWITTER", "Twitter not working");
                    }
                });
            }
            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

    }

    class MyTwitterApiClient extends TwitterApiClient {
        public MyTwitterApiClient(Session session) {
            super(session);
        }
        /**
         * Provide CustomService with defined endpoints
         */
        public CustomService getCustomService() {
            return getService(CustomService.class);
        }
    }
    // example users/show service endpoint
    interface CustomService {
        @GET("/1.1/users/show.json")
        void show(@Query("user_id") Long userId,
                  @Query("screen_name") String screenName,
                  @Query("include_entities") Boolean includeEntities,
                  Callback<User> cb);
    }

    protected Drawable imageFromURL(String url) {
        try {
            InputStream inputStream = (InputStream) new URL(url).getContent();
            Drawable img = Drawable.createFromStream(inputStream, "src name");
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class DownloadWebpageTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            Log.i("TWEET", "In doInBackground for getting photos");
            for (String key : rd.allReps.keySet()) {
                RepsInfo rep = rd.allReps.get(key);
                Log.i("TWEET", "setting photo for: " + rep.get_name());
                Drawable photo = imageFromURL(rep.get_img());
                rep.set_photo(photo);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("TWEET", "In onPostExecute for getting photos");
            progress_bar.setVisibility(View.GONE);

            Log.i("TWEET", "GETTING PHOTOS - calling adapter");
            RepsAdapter repsAdapter = new RepsAdapter(rd.getRepsArrayList(), RepsActivity.this);
            recList.setAdapter(repsAdapter);

            Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
            String data = rd.getWatchInfo(CURR_ZIP);
            System.out.println("data to watch: " + data);
            sendIntent.putExtra("reps_data", data);
            startService(sendIntent);
            System.out.println("--------Started service--------");
        }
    }
}
