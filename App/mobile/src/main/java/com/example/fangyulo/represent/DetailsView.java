package com.example.fangyulo.represent;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailsView extends Activity {

    private LinearLayout commView;
    private ArrayAdapter<String> comm_adapter;

    private LinearLayout billsView;
    private ListAdapter bills_adapter;

    ArrayList<String> commArr = new ArrayList<String>();
    ArrayList<String> billsArr = new ArrayList<String>();
    ArrayList<String> datesArr = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        Window window = DetailsView.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(DetailsView.this.getResources().getColor(R.color.blue));

        RepsData rd = (RepsData) getApplicationContext();
        String passed_name = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            passed_name = extras.getString("rep_name");
        }

        ImageView image = (ImageView) findViewById(R.id.det_photo);
        TextView name = (TextView) findViewById(R.id.det_name);
        TextView party = (TextView) findViewById(R.id.det_party);
        TextView end_date = (TextView) findViewById(R.id.end_date);
        name.setText(passed_name);
        Button back_button = (Button) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RepsInfo rep = rd.getRepByName(passed_name);
        image.setImageDrawable(rep.get_photo());
        commArr.addAll(Arrays.asList(rep.get_comms()));
        billsArr.addAll(Arrays.asList(rep.get_bills()));
        datesArr.addAll(Arrays.asList(rep.get_dates()));
        end_date.setText(rep.get_end_date());
        party.setText(rep.get_party());

        commView = (LinearLayout) findViewById( R.id.committee_list );
        comm_adapter = new ArrayAdapter<String>(this, R.layout.list_row, commArr);
        addItemsToView(comm_adapter, commView);

        billsView = (LinearLayout) findViewById( R.id.bills_list );
        bills_adapter = new ListAdapter(getApplicationContext(), R.layout.list_row2);
        addToAdapter(billsArr, datesArr);
        addItemsToView(bills_adapter, billsView);

    }

    private void addToAdapter(ArrayList<String> Bills, ArrayList<String> Dates) {
        for (int i = 0; i < Bills.size(); i++) {
            Bill obj = new Bill(Bills.get(i), Dates.get(i));
            bills_adapter.add(obj);
        }
    }

    public static void addItemsToView(Adapter adapter, LinearLayout view) {
        final int item_count = adapter.getCount();
        for (int i = 0; i < item_count; i++) {
            View item = adapter.getView(i, null, null);
            view.addView(item);
        }
    }

}
