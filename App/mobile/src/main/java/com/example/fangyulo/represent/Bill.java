package com.example.fangyulo.represent;

/**
 * Created by fangyulo on 3/1/16.
 */

public class Bill {

    private String name;
    private String date;

    public Bill(String name, String date) {
        super();
        this.set_name(name);
        this.set_date(date);
    }

    public String get_name() {
        return name;
    }
    public void set_name(String name) {
        this.name = name;
    }

    public String get_date() {
        return date;
    }
    public void set_date(String date) {
        this.date = date;
    }

}
