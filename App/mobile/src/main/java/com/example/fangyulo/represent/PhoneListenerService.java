package com.example.fangyulo.represent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by fangyulo on 3/3/16.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String REP = "/send_rep";
    private static final String SHAKE = "/shake_loc";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(REP) ) {

            // Value contains the String we sent over in WatchToPhoneService
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d("T", "inside case REP, message is: " + value);

            Context context = getApplicationContext();
            Intent intent = new Intent(context, DetailsView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("T", "create intent");
            intent.putExtra("rep_name", value);
            Log.d("T", "put extra");
            context.startActivity(intent);

            // so you may notice this crashes the phone because it's
            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
            // replace sending a toast with, like, starting a new activity or something.
            // who said skeleton code is untouchable? #breakCSconceptions

        } else if ( messageEvent.getPath().equalsIgnoreCase(SHAKE) ){
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Log.d("T", "inside case SHAKE, message is: " + value);

            Context context = getApplicationContext();
            Intent intent = new Intent(context, RepsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("T", "create intent");
            intent.putExtra("zip_code", value);
            Log.d("T", "put extra");
            context.startActivity(intent);

        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}
