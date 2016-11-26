package sociallockinvite.anything.com.sociallock.integrated;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

/**
 * Created by indra on 11/26/2016.
 */

public class MyApplication extends Application {
    private static BeaconManager beaconManager;
    private static Region regi;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());
// add this below:

        //beaconManager.setBackgroundScanPeriod(100000000, 0);
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Toast.makeText(getApplicationContext(), "ENTER", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), scan.class);
                startService(intent);
                Log.d("TEST", "ENTERING");
                Intent i = new Intent(getApplicationContext(), ok.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                beaconStop();
            }
            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
                Toast.makeText(getApplicationContext(), "EXIT", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void beaconConnect()
    {
        Log.d("trigger", "connect");
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                regi = new Region(
                        "monitored region",
                        UUID.fromString("C7F69F9E-0346-6A33-9BAB-2AD263116307"),
                        34272, 49387);
                beaconManager.startMonitoring(regi);
            }
        });
    }

    public static void beaconStop()
    {
        beaconManager.stopMonitoring(regi);
    }
}
