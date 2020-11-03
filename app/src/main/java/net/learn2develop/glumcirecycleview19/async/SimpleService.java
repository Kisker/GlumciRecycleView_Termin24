package net.learn2develop.glumcirecycleview19.async;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import net.learn2develop.glumcirecycleview19.tools.ReviewerTools;

public class SimpleService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * Provericemo trenutnu povezanost sa mrezom.
         * Za ovo koristimo dostupne pozive android operativnog sistema
         * */
//        int status = ReviewerTools.getConnectivityStatus(getApplicationContext());
        int status = intent.getExtras().getInt("STATUS");
        /**
         * Primer poziva asinhronog zadatka ako ima veze ka mrezi
         * npr. sinhronizacija mail-ova fotografija, muzike dokumenata isl.
         * */
        if(status == ReviewerTools.TYPE_WIFI){
            new SimpleSyncTask(getApplicationContext()).execute(status);
        }

        /**
         * Zaustaviti servis nakon obavljenog pokretanja asinhronog zadatka.
         * Ovu metodu nije potrebno pozvati ako zelimo da nasa aplikacija
         * konstantno osluskuje na neke izmene (npr. novi email, viber poruka isl)
         * */
        stopSelf();

        /**
         * Ako iz nekog razloga operativni sistem ubije servis
         * ne kreirati novi.
         * */
        return START_NOT_STICKY;
    }
}
