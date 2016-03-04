package com.example.fangyulo.represent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fangyulo on 2/28/16.
 */
public class RepsAdapter extends RecyclerView.Adapter<RepsAdapter.RepsViewHolder> {

    private List<RepsInfo> repsList;

    public RepsAdapter(List<RepsInfo> repsList) {
        this.repsList = repsList;
    }

    @Override
    public int getItemCount() {
        return repsList.size();
    }

    @Override
    public void onBindViewHolder(RepsViewHolder repsViewHolder, int i) {
        RepsInfo rep_i = repsList.get(i);
        repsViewHolder.rep_photo.setImageResource(rep_i.img);
        repsViewHolder.rep_name.setText(rep_i.name);
        repsViewHolder.rep_party.setText(rep_i.party);
        repsViewHolder.rep_tweet.setText(rep_i.tweet);

        final TextView name = repsViewHolder.rep_name;
        final String website = rep_i.website;

        repsViewHolder.rep_learn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailsView.class);
                intent.putExtra("rep_name", name.getText().toString());
                context.startActivity(intent);
            }
        });

        repsViewHolder.rep_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public RepsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_cardview, viewGroup, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("Test", "Parent clicked");
            }
        });

        return new RepsViewHolder(itemView);
    }

    public static class RepsViewHolder extends RecyclerView.ViewHolder {

        protected ImageView rep_photo;
        protected TextView rep_name;
        protected TextView rep_party;
        protected TextView rep_tweet;
        protected Button rep_website;
        protected Button rep_email;
        protected TextView rep_learn_more;

        public RepsViewHolder(View v) {
            super(v);
            rep_photo = (ImageView) v.findViewById(R.id.rep_photo);
            rep_name = (TextView) v.findViewById(R.id.rep_name);
            rep_party = (TextView) v.findViewById(R.id.rep_party);
            rep_tweet = (TextView) v.findViewById(R.id.rep_tweet);
            rep_website = (Button) v.findViewById(R.id.web_button);
            rep_email = (Button) v.findViewById(R.id.email_button);
            rep_learn_more = (TextView) v.findViewById(R.id.learn_more);
        }
    }

}
