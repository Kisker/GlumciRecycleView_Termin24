package net.learn2develop.glumcirecycleview19.async;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import net.learn2develop.glumcirecycleview19.R;
import net.learn2develop.glumcirecycleview19.tools.ReviewerTools;

/**
 * Created by milossimic on 10/23/16.
 *
 * BroadcastReceiver je komonenta koja moze da reaguje na poruke drugih delova
 * samog sistema kao i korisnicki definisanih. Cesto se koristi u sprezi sa
 * servisima i asinhronim zadacima.
 *
 * Pored toga on moze da reaguje i na neke sistemske dogadjaje prispece sms poruke
 * paljenje uredjaja, novi poziv isl.
 */
public class SimpleReceiver extends BroadcastReceiver {

    private static int notificationID = 1;
    //Ako zelimo da kreiramo mogucnost odabira da li zelimo da prikazemo ili ne notifikaciju i onda ubacujemo unutar SharedPref, kako bismo
    //pozvali tu metodu.
    private boolean enabledNotifications = true;

    /**
     * Moramo pozvati unutar BroadcastReceiver-a zato sto on ima vezu ka nasoj aktivnosti
     * gde se lista zapravo nalazi.
     * */
    private void readFileAndFillList(Context context){
        // Load product names from array resource

        String[] products = ReviewerTools.readFromFile(context,"myfile.txt").split("\n");

        // Create an ArrayAdaptar from the array of Strings
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.list_item, products);
        ListView listView = (ListView) ((Activity)context).findViewById(R.id.products);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }

    @Override
    /**
     * Intent je bitan parametar za BroadcastReceiver. Kada posaljemo neku poruku,
     * ovaj Intent cuva akciju i podatke koje smo mu poslali.
     * */
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        enabledNotifications = sharedPreferences.getBoolean("notifications_show", true);

        /**
         * Posto nas BroadcastReceiver reaguje samo na jednu akciju koju smo definisali
         * dobro je da proverimo da li som dobili bas tu akciju. Ako jesmo onda mozemo
         * preuzeti i sadrzaj ako ga ima.
         *
         * Voditi racuna o tome da se naziv akcije kada korisnik salje Intent mora poklapati sa
         * nazivom akcije kada akciju proveravamo unutar BroadcastReceiver-a. Isto vazi i za podatke.
         * Dobra praksa je da se ovi nazivi izdvoje unutar neke staticke promenljive.
         * */
        if(intent.getAction().equals("SYNC_DATA")) {
            //prosirimo string.xml, kako bi nam ucitao element allow_message
            // boolean showMessage = sharedPreferences.getBoolean(context.getString(R.string.allow_message), false);


            if (intent.getAction().equals("SYNC_DATA")) {
                int resultCode = intent.getExtras().getInt("RESULT_CODE");

                ReviewerTools.readFile(context);

                //Ovde odredjujemo to pozivanje Notifications u Podesavanja. Pre toga u preference.xml odrediti Switch Preference!
                if (enabledNotifications) {
                    prepareNotification(resultCode, context);
                } else {
                    Toast.makeText(context, "Sinhronizovano", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void prepareNotification(int resultCode, Context context){
        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

        Log.i("MY_ANDROID_APP", "NOtif");

        if(resultCode == ReviewerTools.TYPE_NOT_CONNECTED){
            mBuilder.setSmallIcon(R.drawable.ic_action_settings);
            mBuilder.setContentTitle(context.getString(R.string.autosync_problem));
            mBuilder.setContentText(context.getString(R.string.no_internet));

        }else if(resultCode == ReviewerTools.TYPE_MOBILE){
            mBuilder.setSmallIcon(R.drawable.ic_stat_buy);
            mBuilder.setContentTitle(context.getString(R.string.autosync_warning));
            mBuilder.setContentText(context.getString(R.string.connect_to_wifi));
        }else{
            mBuilder.setSmallIcon(R.drawable.ic_action_refresh);
            mBuilder.setContentTitle(context.getString(R.string.autosync));
            mBuilder.setContentText(context.getString(R.string.good_news_sync));
        }


        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(notificationID, mBuilder.build());
    }
}

