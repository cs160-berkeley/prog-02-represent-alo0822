package com.example.fangyulo.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.*;
import io.fabric.sdk.android.Fabric;
import retrofit.http.GET;
import retrofit.http.Query;


import com.twitter.sdk.android.core.TwitterAuthConfig;

/**
 * Created by fangyulo on 2/28/16.
 */
public class RepsAdapter extends RecyclerView.Adapter<RepsAdapter.RepsViewHolder> {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Cj767iFb3srP466ollqifRedk";
    private static final String TWITTER_SECRET = "lQvaWyV8roX2TGvXxjXHDy5EUpV05cWPfExteMWsY0HIrDvEeM";

    Context context;
    private List<RepsInfo> repsList;


    public RepsAdapter(List<RepsInfo> repsList, Context context) {
        this.repsList = repsList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return repsList.size();
    }

    @Override
    public void onBindViewHolder(final RepsViewHolder repsViewHolder, int i) {
        RepsInfo rep_i = repsList.get(i);
        repsViewHolder.rep_photo.setImageDrawable(rep_i.get_photo());
        repsViewHolder.rep_name.setText(rep_i.get_name());
        repsViewHolder.rep_party.setText(rep_i.get_party());
        final TextView name = repsViewHolder.rep_name;
        final String website = rep_i.get_website();
        final String email = rep_i.get_email();
        ViewGroup layout = repsViewHolder.tweet_layout;
        setTwitter(repsViewHolder, rep_i, layout);

        repsViewHolder.rep_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showPopup(v.getContext()).goJoe(v, email);
            }
        });

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
        itemView.setBackgroundColor(Color.WHITE);

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
//        protected TextView rep_tweet;
        protected Button rep_website;
        protected Button rep_email;
        protected TextView rep_learn_more;
        final ViewGroup tweet_layout;


        public RepsViewHolder(View v) {
            super(v);
            rep_photo = (ImageView) v.findViewById(R.id.rep_photo);
            rep_name = (TextView) v.findViewById(R.id.rep_name);
            rep_party = (TextView) v.findViewById(R.id.rep_party);
//            rep_tweet = (TextView) v.findViewById(R.id.rep_tweet);
            rep_website = (Button) v.findViewById(R.id.web_button);
            rep_email = (Button) v.findViewById(R.id.email_button);
            rep_learn_more = (TextView) v.findViewById(R.id.learn_more);
            tweet_layout = (ViewGroup) v.findViewById(R.id.tweet_layout);

        }
    }


    public void setTwitter(final RepsViewHolder repsViewHolder, final RepsInfo rep, final ViewGroup layout) {
        System.out.println("ADAPTER - in setTwitter");
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        System.out.println("ADAPTER - Set authConfig");
        Fabric.with(context, new Twitter(authConfig));
        System.out.println("ADAPTER - Set Fabric.with()");
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                Log.i("TWEET", "ADAPTER - in logInGuest callback");
                AppSession session = appSessionResult.data;
                MyTwitterApiClient twitterApiClient = new MyTwitterApiClient(session);
                twitterApiClient.getCustomService().show(null, rep.get_twitter_id(), true, new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        Log.i("TWEET", "ADAPTER - " + result.data.name);
                                rep.set_img(result.data.profileImageUrlHttps);
                        Log.i("TWEET", "ADAPTER - set photo");

                        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
                        Fabric.with(context, new Twitter(authConfig));
                        TweetUtils.loadTweet(result.data.status.id, new Callback<Tweet>() {
                            @Override
                            public void success(Result<Tweet> result) {
                                layout.addView(new CompactTweetView(context, result.data));
                                Log.i("TWEET", "ADAPTER - set tweet");
                            }

                            @Override
                            public void failure(TwitterException e) {
                                Log.i("TWITTER", "Twitter not working");
                            }
                        });
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.i("TWITTER", "Twitter not working");
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

    }

    class MyTwitterApiClient extends TwitterApiClient {
        public MyTwitterApiClient(Session session) {
            super(session);
        }
        /**
         * Provide CustomService with defined endpoints
         */
        public CustomService getCustomService() {
            return getService(CustomService.class);
        }
    }
    // example users/show service endpoint
    interface CustomService {
        @GET("/1.1/users/show.json")
        void show(@Query("user_id") Long userId,
                  @Query("screen_name") String screenName,
                  @Query("include_entities") Boolean includeEntities,
                  Callback<User> cb);
    }


}
