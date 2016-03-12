package com.example.fangyulo.represent;

import android.graphics.drawable.Drawable;

import com.twitter.sdk.android.core.models.Tweet;

/**
 * Created by fangyulo on 2/28/16.
 */
public class RepsInfo {
    protected String img;
    protected String small_img;
    protected Drawable photo;
    protected String name;
    protected String party;
    protected String twitter_id;
    protected String website;
    protected String email;
    protected String[] Committees;
    protected String[] Bills;
    protected String[] Dates;
    protected String end_date;
    protected String zip_code;
    protected String house;
    protected String bioguide_id;
    protected Tweet tweet;

    public RepsInfo(String name, String party, String website, String email) {
        super();
        this.set_name(name);
        this.set_party(party);
        this.set_website(website);
        this.set_email(email);
    }

    public String get_img() {
        return img;
    }
    public void set_img(String img) {
        this.img = img;
    }

    public String get_name() {
        return name;
    }
    public void set_name(String name) {
        this.name = name;
    }

    public String get_party() {
        return party;
    }
    public void set_party(String party) {
        this.party = party;
    }

    public String get_twitter_id() {
        return twitter_id;
    }
    public void set_twitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public String get_website() {
        return website;
    }
    public void set_website(String website) {
        this.website = website;
    }

    public String get_email() {
        return email;
    }
    public void set_email(String email) {
        this.email = email;
    }

    public String get_zip_code() {
        return zip_code;
    }
    public void set_zip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String get_end_date() {
        return end_date;
    }
    public void set_end_date(String end_date) {
        this.end_date = end_date;
    }

    public String[] get_bills() {
        return Bills;
    }
    public void set_bills(String[] Bills) {
        this.Bills = Bills;
    }

    public String[] get_comms() {
        return Committees;
    }
    public void set_comms(String[] comms) {
        this.Committees = comms;
    }

    public String[] get_dates() {
        return Dates;
    }
    public void set_dates(String[] dates) {
        this.Dates = dates;
    }

    public String get_house() { return house; }
    public void set_house(String house) { this.house = house; }

    public String get_bioguide() {
        return bioguide_id;
    }
    public void set_bioguide(String bioguide_id) {
        this.bioguide_id = bioguide_id;
    }

    public Tweet get_tweet() { return tweet; }
    public void set_tweet(Tweet tweet) { this.tweet = tweet; }

    public Drawable get_photo() { return photo; }
    public void set_photo( Drawable d ) { this.photo = d; }

    public String get_smallimg() { return this.small_img; }
    public void set_smallimg( String img) { this.small_img = img; }
}
