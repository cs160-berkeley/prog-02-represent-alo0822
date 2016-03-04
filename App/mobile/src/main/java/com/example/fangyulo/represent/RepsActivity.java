package com.example.fangyulo.represent;
//https://www.binpress.com/tutorial/android-l-recyclerview-and-cardview-tutorial/156

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.ArrayList;
import android.view.View.OnKeyListener;

public class RepsActivity extends Activity {

    RepsData rd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reps);

        Window window = RepsActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(RepsActivity.this.getResources().getColor(R.color.blue));

        rd = (RepsData) getApplicationContext();

        EditText search_text = (EditText) findViewById(R.id.search_text);

        String zip_code = search_text.getText().toString();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            zip_code = extras.getString("zip_code");
            search_text.setText(zip_code);
        }

        if (!zip_code.equals("94704") && !zip_code.equals("94103")) {

            Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
            String data = rd.getWatchInfo(zip_code);
            sendIntent.putExtra("reps_data", data);
            startService(sendIntent);
            System.out.println("--------Started service--------");

        }

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        RepsAdapter repsAdapter = new RepsAdapter(rd.getRepsByZip(search_text.getText().toString()));
        recList.setAdapter(repsAdapter);

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

                    String data = rd.getWatchInfo(search_zip.getText().toString());
                    Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                    sendIntent.putExtra("reps_data", data);
                    startService(sendIntent);
                    System.out.println("--------Started service--------");

                }
                return false;
            }
        });

    }

//    public static List<RepsInfo> createList(int size, int zip_code) {
//
//        int [] Images = {R.drawable.barbaralee_resize, R.drawable.dianefeinstein_resize, R.drawable.barbaraboxer_resize};
//        String [] Reps = {"Barbara Lee", "Barbara Boxer", "Dianne Feinstein", "Nancy Pelosi"};
//        String [] Parties = {"Democrat", "Democrat", "Democrat", "Democrat"};
//        String [] Tweets = {"ICYMI: The @mlkfreedomctr will live stream the Barbara Lee & Elihu Harris lecture w/ @bobbyseale tonight at 6:45pm. Don’t miss it!",
//                            ".@SenateDems stood united at the Supreme Court today to tell @Senate_GOPs: #DoYourJob pic.twitter.com/idPYXovA2w",
//                            "(2/2) Kalamazoo and Hesston shootings provide yet another call for Congress to act to #StopGunViolence.",
//                            "20 million previously uninsured Americans have new health coverage thanks to the Affordable Care Act! #ACAworks http://goo.gl/jihkdg"
//                            };
//        String [] Websites = {"https://lee.house.gov", "https://www.boxer.senate.gov", "http://www.feinstein.senate.gov/public/", "https://pelosi.house.gov"};
//        String [] Emails = {"barbaralee@us.gov", "barbaraboxer@us.gov", "diannefeinstein@us.gov", "nancypelosi@us.gov"};
//
//        List<RepsInfo> result = new ArrayList<RepsInfo>();
//
//        for (int i=0; i <= size; i++) {
//            if (zip_code == 94103)
//                i = 1;
//            if (zip_code == 94704 && i == 3)
//                break;
//            int img = Images[i];
//            String name = Reps[i];
//            String party = Parties[i];
//            String tweet = Tweets[i];
//            String website = Websites[i];
//            String email = Emails[i];
//            RepsInfo repsInfo = new RepsInfo(img, name, party, tweet, website, email);
//            result.add(repsInfo);
//        }
//
//        return result;
//    }

}
