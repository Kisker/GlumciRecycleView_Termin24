package net.learn2develop.glumcirecycleview19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.learn2develop.glumcirecycleview19.adapter.GlumciRecyclerAdapter;
import net.learn2develop.glumcirecycleview19.fragments.DetailsFragment;
import net.learn2develop.glumcirecycleview19.fragments.GlumciFragment;
import net.learn2develop.glumcirecycleview19.fragments.PreferenceFragment;
import net.learn2develop.glumcirecycleview19.model.Glumac;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GlumciRecyclerAdapter.OnElementClickListener {

    private Toolbar toolbar;
    private List<String> drawerItems;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private AlertDialog dialog_about;

    public static final int NOTIF_ID = 101;
    public static final String NOTIF_CHANNEL_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showNotification();
      //  createNotificationChannel();
        setupToolbar();
        fillData();
        setupDrawer();

    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }
    }

    private void fillData() {
        drawerItems = new ArrayList<>();
        drawerItems.add("Glumci");
        drawerItems.add("Podesavanja");
        drawerItems.add("O samom app-u");
    }

    private void setupDrawer() {
        drawerList = findViewById(R.id.leftDrawer);
        drawerLayout = findViewById(R.id.drawer_layout);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItems);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = "";
                if (position == 0) {
                    title = "Glumci";
                    showGlumciFragment();
                } else if (position == 1) {
                    title = "Podesavanja";
                    showPreferences();
                } else if (position == 2) {
                    title = "O samom App-u";
                    showDialog();
                }
                setTitle(title);
                drawerLayout.closeDrawer(drawerList);
            }
        });
        //This class ActionBarDrawerToggle provides a handy way to tie together the functionality of DrawerLayout (ceo ekran)
        // and the framework ActionBar to implement the recommended design for navigation drawers.
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle("");
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("");
                super.onDrawerOpened(drawerView);
            }
        };

    }

    public void showGlumciFragment() {
        GlumciFragment listfragment = new GlumciFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.root, listfragment).commit();
    }

    private void showDetails(Glumac glumac) {
        DetailsFragment detailsFragment = new DetailsFragment();
        //bez unapred odredjenog .setJelo App bi pukao.Obvezno pozvati ovu metodu iz DetailsFragment-a.
        detailsFragment.setGlumac(glumac);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root, detailsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showPreferences() {
        FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
        PreferenceFragment preferenceFragment = new PreferenceFragment();
        fragment.replace(R.id.root, preferenceFragment);
        fragment.commit();
    }

    private void showDialog() {
        if (dialog_about == null)
            dialog_about = new DialogAbout(this).prepareDialog();
        else if (dialog_about.isShowing())
            dialog_about.dismiss();
        dialog_about.show();
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIF_CHANNEL_ID);
        builder.setContentTitle("IMBD Obvestilo")
                .setContentText("Imate nove unose filmova sa Google-a")
                .setSmallIcon(R.drawable.ic_cake_black_24dp);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIF_ID, builder.build());
    }

//        private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, "Nas Notif Kanal", importance);
//            channel.setDescription("Opis naseg kanala");
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

    @Override
    public void onElementClicked(Glumac glumac) {
        showDetails(glumac);
    }

    public static class DialogAbout extends AlertDialog.Builder {


        protected DialogAbout(@NonNull final Context context) {
            super(context);
            setTitle("Kisker");
            setMessage("By Sveto");
            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        public AlertDialog prepareDialog() {
            AlertDialog dialog = create();
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }
    }
}