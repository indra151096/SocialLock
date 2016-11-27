package sociallockinvite.anything.com.sociallock.integrated;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by indra on 11/26/2016.
 */

public class scan extends Service {

    int counter = 0;
    String packageName="";
    Boolean checkFinish;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    public String getForegroundApp() {
        checkFinish = false;
        String currentApp = "NULL";
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        Log.d("Package"+counter, currentApp);
        counter++;
        return currentApp;
    }

    @Override
    public void onCreate() {
        new Thread(new Runnable() {
            public void run() {
                while(true)
                {
                    packageName = getForegroundApp();
                    if (!(packageName.equals("com.android.dialer") || packageName.equals("sociallockinvite.anything.com.sociallock")
                            || packageName.equals("com.android.systemui") || packageName.equals("com.google.android.apps.nexuslauncher")
                            || packageName.equals("com.miui.home") || packageName.equals("com.android.contacts")
                            || packageName.equals("com.sonyericsson.android.socialphonebook")))
                    {
                        showHomeScreen();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (checkFinish == true)
                        break;
                }
            }
        }).start();
    }

    public boolean showHomeScreen(){
        Intent i = new Intent(this, ok.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        return true;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        checkFinish = true;
        Toast.makeText(this, "STOP", Toast.LENGTH_SHORT).show();
    }

}
