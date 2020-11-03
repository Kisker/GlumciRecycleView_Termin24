package net.learn2develop.glumcirecycleview19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.learn2develop.glumcirecycleview19.adapter.GlumciRecyclerAdapter;
import net.learn2develop.glumcirecycleview19.async.SimpleReceiver;
import net.learn2develop.glumcirecycleview19.async.SimpleService;
import net.learn2develop.glumcirecycleview19.fragments.DetailsFragment;
import net.learn2develop.glumcirecycleview19.fragments.GlumciFragment;
import net.learn2develop.glumcirecycleview19.fragments.PreferenceFragment;
import net.learn2develop.glumcirecycleview19.model.Glumac;
import net.learn2develop.glumcirecycleview19.tools.ReviewerTools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GlumciRecyclerAdapter.OnElementClickListener {

    private Toolbar toolbar;
    private List<String> drawerItems;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private AlertDialog dialog_about;
    private AlertDialog dialog_choose;

    public static final int NOTIF_ID = 101;
    public static final String NOTIF_CHANNEL_ID = "";
    private static final String TAG = "PERMISSIONS";
   //pozivom na klase i njihove promenljive, mi cemo definisati da nam se na ekranu prikaze Alarm set,
    //kada na primer stisnemo na Glumac 1 ili na Film 2
    private SimpleReceiver sync;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private SharedPreferences sharedPreferences;
    private String synctime;
    private boolean allowSync;

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

    private void showDialogChoose() {
        if (dialog_choose == null)
            dialog_choose = new DialogChoose(this).prepareDialogChoose();
        else if (dialog_choose.isShowing())
            dialog_choose.dismiss();
        dialog_choose.show();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //Kada navedemo action_refresh i info, obvezno u Android Manifestu ispisati users permisions kako bi nam dalo
        //informacije/datoteke natrag, odnosno kako bih ih ocitao. Uvek idemo na add - writeToFile, refresh - readFromFile, i na kraju isFileExists!
        switch (item.getItemId()){
            //Pre nego sto krenemo sa pozivanjem, kreirati activity_item_details.xml u res folderu pod imenom Menu
            case R.id.action_add:
                if (isStoragePermissionGranted()) {
                    ReviewerTools.writeToFile(Calendar.getInstance().getTime().toString() + "\n", this, "myfile.txt");
                }
                showDialogChoose();
            case R.id.action_refresh:
                if (isStoragePermissionGranted()) {
                    String text = ReviewerTools.readFromFile(this, "myfile.txt");
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.info:
                if (isStoragePermissionGranted()) {
                    if (ReviewerTools.isFileExists(MainActivity.this, "myfile.txt")) {
                        Toast.makeText(this, "Fajl postoji", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Fajl ne postoji", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_item_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIF_CHANNEL_ID);
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);
        builder.setContentTitle("IMBD Obvestilo")
                .setContentText("Imate nove unose filmova sa Google-a")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_email_24);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIF_ID, builder.build());

    }

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

    public class DialogChoose extends AlertDialog.Builder {

        protected DialogChoose(@NonNull final Context context) {
            super(context);
            setTitle("Izaberite");
            setMessage("Sta zelite da dodate/promenite/obrisete?");
            // which znaci pozicija u nizu. Mozemo izabrati opcije, itd, klikom na DialogBox
            setPositiveButton("DA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            setNegativeButton("NE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        public AlertDialog prepareDialogChoose() {
            AlertDialog dialog = create();
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }
    }
    /**
     * Od verzije Marshmallow Android uvodi pojam dinamickih permisija
     * Sto korisnicima olaksva rad, a programerima uvodi dodadan posao.
     * Cela ideja ja u tome, da se permisije ili prava da aplikcija
     * nesto uradi, ne zahtevaju prilikom instalacije, nego prilikom
     * prve upotrebe te funkcionalnosti. To za posledicu ima da mi
     * svaki put moramo da proverimo da li je odredjeno pravo dopustneo
     * ili ne. Iako nije da ponovo trazimo da korisnik dopusti, u protivnom
     * tu funkcionalnost necemo obaviti uopste.
     * */
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    /**
     *
     * Ako odredjena funkcija nije dopustena, saljemo zahtev android
     * sistemu da zahteva odredjene permisije. Korisniku seprikazuje
     * diloag u kom on zeli ili ne da dopusti odedjene permisije.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    //Ove metode ispod su veoma bitne, jer jednom kada kliklnemo na Alarm (10 minuta), postavlja se setUpManager
    //a kada izvrsimo tu akciju, mi zelimo da se taj podatak sacuva, zato koristimo setUpReceiver, odnosno sharedPreferences
    //zajedno sa pozivanjem na metodu consultPreferences.
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        setUpReceiver();
        setUpManager();

    }

    private void setUpReceiver(){
        sync = new SimpleReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction("SYNC_DATA");
        registerReceiver(sync, filter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        consultPreferences();
    }

    private void setUpManager(){
        Intent intent = new Intent(this, SimpleService.class);
        int status = ReviewerTools.getConnectivityStatus(getApplicationContext());
        intent.putExtra("STATUS", status);

        if (allowSync) {
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),1000* 60, pendingIntent);

            Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        }
    }

    private void consultPreferences(){
        synctime = sharedPreferences.getString(getString(R.string.pref_sync_list), "Glumac1");//1min
        synctime = sharedPreferences.getString(getString(R.string.pref_sync_list1), "Film1");//1min
        allowSync = sharedPreferences.getBoolean(getString(R.string.pref_sync), false);
        allowSync = sharedPreferences.getBoolean(getString(R.string.pref_sync1), false);
    }

    @Override
    protected void onPause() {
        if (manager != null) {
            manager.cancel(pendingIntent);
            manager = null;
        }

        //osloboditi resurse
        if(sync != null){
            unregisterReceiver(sync);
            sync = null;
        }

        super.onPause();

    }

}