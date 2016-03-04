package com.example.fangyulo.represent;

import android.app.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fangyulo on 3/3/16.
 */
public class RepsData extends Application {

    Map<String, RepsInfo> allReps = new HashMap<String, RepsInfo>();

    RepsInfo barbara_lee = setBasicInfo(0);
    RepsInfo barbara_boxer = setBasicInfo(1);
    RepsInfo dianne_feinstein = setBasicInfo(2);
    RepsInfo nancy_pelosi = setBasicInfo(3);
    RepsInfo loretta_sanchez = setBasicInfo(4);

    public RepsInfo setBasicInfo(int i) {
        int [] Images = {R.drawable.barbaralee_resize, R.drawable.barbaraboxer_resize, R.drawable.dianefeinstein_resize, R.drawable.nancypelosi_resize, R.drawable.lorettasanches_resize};
        String [] Names = {"Barbara Lee", "Barbara Boxer", "Dianne Feinstein", "Nancy Pelosi", "Loretta Sanchez"};
        String [] Parties = {"Democrat", "Democrat", "Democrat", "Democrat", "Democrat"};
        String [] Zips = {"94704", null, null, "94103", "92802"};
        String [] Tweets = {"ICYMI: The @mlkfreedomctr will live stream the Barbara Lee & Elihu Harris lecture w/ @bobbyseale tonight at 6:45pm. Don’t miss it!",
                ".@SenateDems stood united at the Supreme Court today to tell @Senate_GOPs: #DoYourJob pic.twitter.com/idPYXovA2w",
                "(2/2) Kalamazoo and Hesston shootings provide yet another call for Congress to act to #StopGunViolence.",
                "20 million previously uninsured Americans have new health coverage thanks to the Affordable Care Act! #ACAworks http://goo.gl/jihkdg",
                "Let’s protect workers & consumers in an industry that continues 2 grow & become a larger part of California’s economy & prosperity #Cannabis"
        };
        String [] Websites = {"https://lee.house.gov", "https://www.boxer.senate.gov", "http://www.feinstein.senate.gov/public/", "https://pelosi.house.gov", "https://lorettasanchez.house.gov"};
        String [] Emails = {"barbaralee@us.gov", "barbaraboxer@us.gov", "diannefeinstein@us.gov", "nancypelosi@us.gov", "lorettasanchez@us.gov"};
        RepsInfo rep = new RepsInfo(Images[i], Names[i], Parties[i], Tweets[i], Websites[i], Emails[i]);
        rep.set_zip_code(Zips[i]);
        return rep;
    }

    public void setHash() {
        allReps.put(barbara_lee.get_name(), barbara_lee);
        allReps.put(barbara_boxer.get_name(), barbara_boxer);
        allReps.put(dianne_feinstein.get_name(), dianne_feinstein);
        allReps.put(nancy_pelosi.get_name(), nancy_pelosi);
        allReps.put(loretta_sanchez.get_name(), loretta_sanchez);
    }

//    public void printHash(){
//        for (String key : allReps.keySet()) {
//            System.out.println(key + ", " + allReps.get(key).get_zip_code());
//            String zip = allReps.get(key).get_zip_code();
//            if (zip == null) {
//                System.out.println("true");
//            } else if (zip.equals("94704")) {
//                System.out.println("true");
//            }
//        }
//    }

    public void setDetails() {
        String [] Committees0 = {"Senate Select Committee on Ethics", "Senate Committee on Environment and Public Works",
                "Senate Committee on Foreign Relations"};
        String [] Bills0 = {"Female Veteran Suicide Prevention Act",
                "Tule Lake National Historic Site Establishment Act of 2015",
                "End of Suffering Act of 2015",
                "SAFE DRONE Act of 2015.",
                "West Coast Ocean Protection Act of 2015",
                "Pell Grant Restoration Act of 2015",
                "Pechanga Band of Luiseno Mission Indians Water Rights Settlement Act"};
        String [] Dates0 = {"Feb 3, 2016", "Dec 17, 2015", "Oct 22, 2015", "Oct 7, 2015", "Oct 7, 2015", "Sep 16, 2015", "Aug 5, 2015"};
        barbara_boxer.set_comms(Committees0);
        barbara_boxer.set_bills(Bills0);
        barbara_boxer.set_dates(Dates0);
        barbara_boxer.set_end_date("January 3, 2017");


        String [] Committees1 = {"Senate Select Committee on Intelligence", "Senate Committee on Appropriations",
                "Senate Committee on the Judiciary", "Senate Committee on Rules and Administration", "United States Senate Caucus on International Narcotics Control"};
        String [] Bills1 = {"California Desert Conservation, Off-Road Recreation, and Renewable Energy Act",
                "Interstate Threats Clarification Act of 2016",
                "California Long-Term Provisions for Water Supply and Short-Term Provisions for Emergency Drought Relief Act",
                "A bill to authorize the use of passenger facility charges at an airport previously associated with the airport at which the charges are collected.",
                "Fiscal Year 2016 Department of Veterans Affairs Seismic Safety and Construction Authorization Act.",
                "Requiring Reporting of Online Terrorist Activity Act",
                "Visa Waiver Program Security Enhancement Act"};
        String [] Dates1 = {"Feb 23, 2016", "Feb 11, 2016", "Feb 10, 2016", "Jan 12, 2016", "Dec 18, 2015", "Dec 8, 2015", "Dec 1, 2015"};
        dianne_feinstein.set_comms(Committees1);
        dianne_feinstein.set_bills(Bills1);
        dianne_feinstein.set_dates(Dates1);
        dianne_feinstein.set_end_date("January 3, 2019");

        String [] Committees2 = {"House Committee on Appropriations", "House Committee on The Budget"};
        String [] Bills2 = {"Improving Access to Mental Health Act",
                "Recognizing the 70th anniversary of the establishment of the United Nations.",
                "Equal Access to Abortion Coverage in Health Insurance (EACH Woman) Act of 2015",
                "Pathways Out of Poverty Act of 2015",
                "Calling for Sickle Cell Trait research.",
                "Recognizing the significance of National Caribbean American Heritage Month.",
                "Student Support Act"};
        String [] Dates2 = {"Oct 8, 2015", "Sep 16, 2015", "Jul 8, 2015", "Jun 10, 2015", "Jun 3, 2015", "May 21, 2015", "May 15, 2015"};

        barbara_lee.set_comms(Committees2);
        barbara_lee.set_bills(Bills2);
        barbara_lee.set_dates(Dates2);
        barbara_lee.set_end_date("January 3, 2017");

        String [] Committees3 = {"None"};
        String [] Bills3 = {"Puerto Rico Emergency Financial Stability Act of 2015", "Raising a question of the privileges of the House.",
                "For the relief of Maria Carmen Castro Ramirez and J. Refugio Carreno Rojas.", "Panama Canal and Pan-Pacific Exhibition Centennial Celebration Act",
                "To designate the federal building currently known as Federal Office Building 8, located at 200 C Street Southwest in the District of Columbia, ...\n" +
                "... as the \"Thomas P. O’Neill, Jr. Federal Building\"."};
        String [] Dates3 = {"Dec 18, 2015", "Jul 9, 2015", "Jan 14, 2015", "Jul 19, 2013", "Nov 27, 2012"};

        nancy_pelosi.set_comms(Committees3);
        nancy_pelosi.set_bills(Bills3);
        nancy_pelosi.set_dates(Dates3);
        nancy_pelosi.set_end_date("January 3, 2017");

        String [] Committees4 = {"House Armed Services Committee", "House Committee on Homeland Security", "Joint Economic Committee"};
        String [] Bills4 = {"DHS Human Trafficking Prevention Act of 2016", "Honoring the life and accomplishments of Henry Thomas Segerstrom and expressing condolences on his passing",
        "To authorize the President to award the Medal of Honor to Special Forces Command Sergeant Major Ramon Rodriguez of the United States Army ...\n" +
                "... for acts of valor during the Vietnam War.", "To direct the Secretary of Homeland Security to designate John Wayne Airport in Orange County, California, as a U.S. Customs and Border Protection ...\n" +
                "... (CBP) port of entry, and for other purposes.", "Affordability for Constant and Continual Education to Enhance Student Success Act"};
        String [] Dates4 = {"Jan 13, 2016", "Apr 21, 2015", "Mar 24, 2015", "Feb 24, 2015", "Jan 9, 2015"};

        loretta_sanchez.set_comms(Committees4);
        loretta_sanchez.set_bills(Bills4);
        loretta_sanchez.set_dates(Dates4);
        loretta_sanchez.set_end_date("January 3, 2017");

    }

    public RepsInfo getRepByName(String name) {
        RepsInfo rep = allReps.get(name);
        setDetails();
        return allReps.get(name);
    }

    public ArrayList<RepsInfo> getRepsByZip(String zip) {
        ArrayList<RepsInfo> repsByZip = new ArrayList<RepsInfo>();
        if (zip.equals("94704")) {
            repsByZip.add(barbara_boxer);
            repsByZip.add(barbara_lee);
            repsByZip.add(dianne_feinstein);
        } else if (zip.equals("94103")) {
            repsByZip.add(barbara_boxer);
            repsByZip.add(dianne_feinstein);
            repsByZip.add(nancy_pelosi);
        } else {
            repsByZip.add(barbara_boxer);
            repsByZip.add(dianne_feinstein);
            repsByZip.add(loretta_sanchez);
        }
        return repsByZip;
    }

    public String getWatchInfo(String zip_code) {
        ArrayList<RepsInfo> reps = getRepsByZip(zip_code);
        String data = "";
        String name;
        for (int i = 0; i < reps.size(); i++) {
            name = reps.get(i).get_name();
            data += name + ", ";
        }
        data += zip_code;
        return data;
    }

}
