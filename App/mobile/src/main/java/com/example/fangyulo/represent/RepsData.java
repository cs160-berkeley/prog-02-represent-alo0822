package com.example.fangyulo.represent;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by fangyulo on 3/3/16.
 */
public class RepsData extends Application {

    Map<String, RepsInfo> allReps;
    String votes_info;

    public void printHash(){
        for (String key : allReps.keySet()) {
            System.out.println(allReps.get(key).get_name());
        }
    }

    public RepsInfo getRepByName(String name) {
        return allReps.get(name);
    }

    public ArrayList<RepsInfo> getRepsArrayList() {
        ArrayList<RepsInfo> reps = new ArrayList<RepsInfo>();
        for (String key : allReps.keySet()) {
            reps.add(allReps.get(key));
        }
        return reps;
    }

    public String getWatchInfo(String zip_code) {
        Log.i("DATA", "in getWatchInfo");
        String data = "";
        String name;
        String party;
        String chamber;
        String img;
        for (String key : allReps.keySet()) {
            RepsInfo rep = allReps.get(key);
            name = rep.get_name();
            party = rep.get_party();
            chamber = rep.get_house();
            img = rep.get_smallimg();
            data += name + ", " + party + ", " + chamber + ", " + img + ", ";
        }
//        String votes = getVoteData(county, state, is);
        Log.i("DATA", "votes_info: " + this.votes_info);
        data += zip_code + ", " + this.votes_info;
        return data;
    }

    public void getVoteData(String county, String state, JSONObject jsonObject) {
        Log.i("DATA", "in getVoteData");
        Log.i("DATA", "in getVoteData, county: " + county + ", state: " + state);
        this.votes_info =  county + "/" + state + "/";
        try {
            Log.i("DATA", "in getVoteData, in try");
            JSONObject obj = jsonObject.getJSONObject(county + " County, " + state);
            String obama = obj.optString("obama");
            String romney = obj.optString("romney");
            this.votes_info += obama + "/"  +romney;
            Log.i("DATA", "votes_info is now: " + this.votes_info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setHashWithZip (JSONObject jsonRootObject, String zip_code) {
        allReps = new HashMap<String, RepsInfo>();
        JSONArray jsonArray = jsonRootObject.optJSONArray("results");

        //Iterate the jsonArray and print the info of JSONObjects
        for(int i=0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {

                jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.optString("first_name").toString() + " " + jsonObject.optString("last_name").toString();
                Log.i("INFO", "getting rep: " + name);
                String party = jsonObject.optString("party").toString();
                String website = jsonObject.optString("website").toString();
                String email = jsonObject.opt("oc_email").toString();
                String end_date = jsonObject.opt("term_end").toString();
                String bioguide_id = jsonObject.opt("bioguide_id").toString();
                String house = jsonObject.optString("chamber").toString();
                String twitter_id = jsonObject.opt("twitter_id").toString();
                int img = R.drawable.barbaraboxer_resize;


                if (party.equals("D")) {
                    party = "Democrat";
                } else if (party.equals("R")) {
                    party = "Republican";
                } else if (party.equals("I")) {
                    party = "Independent";
                }

                Log.i("INFO", "setting repInfo obj " + name);
                RepsInfo obj = new RepsInfo(name, party, website, email);
                obj.set_house(house);
                obj.set_end_date(end_date);
                obj.set_bioguide(bioguide_id);
//                obj.set_img(img);
                obj.set_twitter_id(twitter_id);

                Log.i("INFO", "putting rep in hash: " + obj.get_name());
                allReps.put(name, obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            printHash();

        }

    }

    public void setRepCommittees (JSONObject jsonRootObject, RepsInfo rep) {
        JSONArray jsonArray = jsonRootObject.optJSONArray("results");
        ArrayList<String> comms = new ArrayList<String>();
        String name = null;
        for(int i=0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
                name = jsonObject.optString("name");
                comms.add(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (comms.size() == 0) {
            System.out.println("this rep has no committees");
            comms.add("This representative has no committee assignments.");
        }
        rep.set_comms(comms.toArray(new String[comms.size()]));
    }

    public void setRepBills (JSONObject jsonRootObject, RepsInfo rep) {
        JSONArray jsonArray = jsonRootObject.optJSONArray("results");
        ArrayList<String> bills = new ArrayList<String>();
        ArrayList<String> dates = new ArrayList<String>();
        String name = null;
        String date = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
                name = jsonObject.optString("short_title");
                if (name.equals("null")) {
                    name = jsonObject.optString("official_title");
                }
                date = jsonObject.optString("introduced_on");
                bills.add(name);
                dates.add(date);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        rep.set_bills(bills.toArray(new String[bills.size()]));
        rep.set_dates(dates.toArray(new String[dates.size()]));
    }


}
