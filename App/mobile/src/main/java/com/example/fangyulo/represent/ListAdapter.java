package com.example.fangyulo.represent;

/**
 * Created by fangyulo on 3/1/16.
 */

import android.content.Context;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


public class ListAdapter extends ArrayAdapter {

    private List list = new ArrayList();

    static class ListHolder
    {
        TextView NAME;
        TextView DATE;
    }

    public ListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Bill obj) {
        list.add(obj);
        super.add(obj);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ListHolder holder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_row2, parent, false);
            holder = new ListHolder();
            holder.NAME = (TextView) row.findViewById(R.id.bill_name);
            System.out.println("------------" + holder.NAME + "------------");
            holder.DATE = (TextView) row.findViewById(R.id.introduced_date);
            row.setTag(holder);
        } else {
            holder = (ListHolder) row.getTag();
        }
        Bill obj = (Bill) getItem(position);
        holder.NAME.setText(obj.get_name());
        holder.DATE.setText("Introduced: " + obj.get_date());

        return row;
    }
}