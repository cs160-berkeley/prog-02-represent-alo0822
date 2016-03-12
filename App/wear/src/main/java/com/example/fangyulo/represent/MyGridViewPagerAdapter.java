package com.example.fangyulo.represent;

import android.content.Context;
import android.content.Intent;
import android.support.wearable.view.GridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fangyulo on 3/2/16.
 */
public class MyGridViewPagerAdapter extends GridPagerAdapter {

    private int row;
    private int col;
    private ArrayList<Integer> vote_row = new ArrayList<>();
    ArrayList<String[]> data;
    private String zip;
    private String county;
    private String state;
    private String obama;
    private String romney;
    View gridView;

    public MyGridViewPagerAdapter (int row, int col, String[] data) {
        super();
        this.row = row;
        this.col = col;
        parseData(data);
    }

    private void parseData(String[] str) {
//        String[] str = data.split(", ");

        String vote_info = str[str.length - 1];
        String [] vote = vote_info.split("/");
        if (vote.length > 2) {
            this.county = vote[0];
            this.state = vote[1];
            this.obama = "Obama: " + vote[2] + "%";
            this.romney = "Romney: " + vote[3] + "%";
        } else {
            this.state = "Currently unavailable.";
        }

        this.zip = str[str.length - 2];
        System.out.println("this.zip: " + this.zip);
        this.data = new ArrayList<>();
        for (int i = 0; i < str.length-2; i+=4) {
            String [] rep = new String[4];
            rep[0] = str[i]; //name
            rep[1] = str[i+1]; //party
            rep[2] = str[i+2]; //chamber
            rep[3] = str[i+3]; //img
            System.out.println(rep[0] + ", " + rep[1] + ", " + rep[2] + ", " + rep[3]);
            this.data.add(rep);

            if (str[i+2].equals("house")){
                vote_row.add((int)i/3);
            }
        }

    }

    @Override
    public int getColumnCount(int row) {
        if (vote_row.contains(row)) {
            System.out.println("getting vote_row col");
            return this.col + 1;
        }
        else
            return this.col;
    }

    @Override
    public int getRowCount() {
        return this.row;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int row, int col) {
        if (col == 0) {
//            final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.grid_view_pager_item, container, false);
            final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.grid_view_pager_item_noimg, container, false);
            setUpReps(view, container, row, col);
            container.addView(view);
            gridView = view;

            RelativeLayout reps_view = (RelativeLayout) view.findViewById(R.id.reps_layout);
            reps_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    TextView rep = (TextView) v.findViewById(R.id.rep_name);
                    String current_rep = rep.getText().toString();
                    System.out.println("--------- Rep: " + current_rep + "---------");
                    Intent sendIntent = new Intent(context, WatchToPhoneService.class);
                    sendIntent.putExtra("rep_name", current_rep);
                    System.out.println("--------- set intent ---------");
                    context.startService(sendIntent);
                    System.out.println("--------- start service ---------");
                }
            });

            return view;
        }
        else if (vote_row.contains(row)) {
            final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.vote_view_layout, container, false);
            setUpReps(view, container, row, col);
            container.addView(view);
            return view;
        }
        return null;
    }

    private void setUpReps(View view, ViewGroup container, int row, int col) {

        System.out.println("Setting up: " + row + ", " + col);
        if (col == 0) {
            TextView title = (TextView) view.findViewById(R.id.rep_title);
            TextView name = (TextView) view.findViewById(R.id.rep_name);
            TextView party = (TextView) view.findViewById(R.id.rep_party);

            System.out.println("Setting up rep " + this.data.get(row)[0]);
            String chamber = this.data.get(row)[2];
            if (chamber.equals("senate"))
                title.setText("Senator");
            else
                title.setText("Representative");
            name.setText(this.data.get(row)[0]);
            party.setText(this.data.get(row)[1]);

        } else if (col == 1) {
            System.out.println("col == 1");

            TextView county = (TextView) view.findViewById(R.id.county);
            TextView state = (TextView) view.findViewById(R.id.state);
            TextView obama = (TextView) view.findViewById(R.id.obama_votes);
            TextView romney = (TextView) view.findViewById(R.id.romney_votes);

            county.setText(this.county);
            state.setText(this.state);
            obama.setText(this.obama);
            romney.setText(this.romney);

        }

    }

    @Override
    public void destroyItem(ViewGroup container, int row, int col, Object view) {
        container.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}