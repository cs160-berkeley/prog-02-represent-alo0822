package com.example.fangyulo.represent;

/**
 * Created by fangyulo on 2/28/16.
 */
public class RepsInfo {
    protected int img;
    protected String name;
    protected String party;
    protected String tweet;
    protected String website;
    protected String email;
    protected String[] Committees;
    protected String[] Bills;
    protected String[] Dates;
    protected String end_date;
    protected String zip_code;

    public RepsInfo(int img, String name, String party, String tweet, String website, String email) {
        super();
        this.set_img(img);
        this.set_name(name);
        this.set_party(party);
        this.set_tweet(tweet);
        this.set_website(website);
        this.set_email(email);
    }

    public int get_img() {
        return img;
    }
    public void set_img(int img) {
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

    public String get_tweet() {
        return tweet;
    }
    public void set_tweet(String tweet) {
        this.tweet = tweet;
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
}
