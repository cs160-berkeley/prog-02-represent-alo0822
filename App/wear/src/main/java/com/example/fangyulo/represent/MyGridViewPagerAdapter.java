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

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by fangyulo on 3/2/16.
 */
public class MyGridViewPagerAdapter extends GridPagerAdapter {

    private int row;
    private int col;
    private int vote_row;
    String[] data;
    private String zip;
    View gridView;

    public MyGridViewPagerAdapter (int row, int col, String data) {
        super();
        this.row = row;
        this.col = col;
        this.zip = data;
        parseData(data);
    }

    private void parseData(String data) {
        String[] str = data.split(", ");
        this.zip = str[str.length-1];
        System.out.println("this.zip: " + this.zip);
        this.data = new String[3];
        for (int i = 0; i < str.length-1; i++) {
            this.data[i] = str[i];
            if (!str[i].equals("Barbara Boxer") && !str[i].equals("Dianne Feinstein")){
                vote_row = i;
            }
        }
    }

    @Override
    public int getColumnCount(int row) {
        if (row == vote_row)
            return this.col + 1;
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
            final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.grid_view_pager_item, container, false);
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
        else if (row == vote_row) {
            final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.vote_view_layout, container, false);
            setUpReps(view, container, row, col);
            container.addView(view);
            return view;
        }
        return null;
    }

    private void setUpReps(View view, ViewGroup container, int row, int col) {

        if (col == 0) {
            ImageView img = (ImageView) view.findViewById(R.id.rep_photo);
            TextView name = (TextView) view.findViewById(R.id.rep_name);
            TextView party = (TextView) view.findViewById(R.id.rep_party);

            name.setText(this.data[row]);
            party.setText("Democrat");

            if (this.data[row].equals("Barbara Lee")) {
                img.setImageResource(R.drawable.barbaralee_resize);
            } else if (this.data[row].equals("Barbara Boxer")) {
                img.setImageResource(R.drawable.barbaraboxer_resize);
            } else if (this.data[row].equals("Dianne Feinstein")) {
                img.setImageResource(R.drawable.dianefeinstein_resize);
            } else if (this.data[row].equals("Nancy Pelosi")) {
                img.setImageResource(R.drawable.nancypelosi_resize);
            } else if (this.data[row].equals("Loretta Sanchez")) {
                img.setImageResource(R.drawable.lorettasanches_resize);
            }

        } else if (col == 1) {
//        }
//        if (col == 1) {
            TextView county = (TextView) view.findViewById(R.id.county);
            TextView obama = (TextView) view.findViewById(R.id.obama_votes);
            TextView romney = (TextView) view.findViewById(R.id.romney_votes);

            if (this.zip.equals("94704")) {
                county.setText("Alameda, CA");
                obama.setText("Obama: 78.7%");
                romney.setText("Romney: 18.4%");
            } else if (this.zip.equals("94103")) {
                county.setText("San Francisco, CA");
                obama.setText("Obama: 83.5%");
                romney.setText("Romney: 13.0%");
            } else {
                county.setText("Anaheim, CA");
                obama.setText("Obama: 85.5%");
                romney.setText("Romney: 12.5%");
            }

        }

    }

    private void setUpVoteView(View view, int row, int col) {

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