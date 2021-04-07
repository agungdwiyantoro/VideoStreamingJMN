package com.example.mycomputer.videostreamingjmn;

/**
 * Created by My Computer on 1/8/2017.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import LoginClasses.SaveSharedPreference;

public class Service extends android.app.Service{

    private Timer time;
    private int f;
    Context ctx;
    int x = 0;

    googlelogin.countRowsx xf;
    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;

        googlelogin.countRowsx xf= new googlelogin.countRowsx(ctx);

        theTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptheTask();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void theTask(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
/*
                v = (vipStatus) new vipStatus(ServiceClass.this, new vipStatus.delegate() {
                    @Override
                    public void processFinish(int g) {
                        f = g;
                        Log.d("Message is here","Mowfucking nigga " + f);
                        if(f==1){
                            NotificationMethod("MSI","test notification number " + x,"ununderstandable ticker",1,ServiceClass.this);
                            x++;
                            saveSharedPreferences.count_matkul_list(ServiceClass.this,x);
                        }
                    }
                }).execute("agung.dwiyantoro@gmail.com");
*/
               // saveSharedPreferences.count_matkul_list(ServiceClass.this,saveSharedPreferences.getTempCountTopic(ServiceClass.this));
           //     SaveSharedPreference.setCountRows(ctx, SaveSharedPreference.getCountRows(ctx));
            //    Log.d("count matkul list","value " + SaveSharedPreference.getCountRows(ctx));
/*
                new getCountTopic(ServiceClass.this, new getCountTopic.delegate() {
                    @Override
                    public void saveCountValue(int x) {
                        saveSharedPreferences.TempSaveCountTopic(ServiceClass.this, x);
                        Log.d("IN IN IN IN","value " + x);
                        Log.d("TemSaveCountTopic","value " + saveSharedPreferences.getTempCountTopic(ServiceClass.this));

                        if(saveSharedPreferences.getTempCountTopic(ServiceClass.this) >   saveSharedPreferences.getCount_matkul_list(ServiceClass.this)){
                            NotificationMethod("MSI","You have a notification","ununderstandable ticker",1,ServiceClass.this);
                            //  saveSharedPreferences.count_matkul_list(ServiceClass.this,saveSharedPreferences.getTempCountTopic(ServiceClass.this));

                        }
                        else {

                            Log.d("fuck", "nggk ada update");
                        }
                    }
                }).execute("1");

*/
                //x++;
                SaveSharedPreference.setTempCountRows(ctx, SaveSharedPreference.getCountRows(ctx));
                googlelogin.countRowsx x = new googlelogin.countRowsx(ctx);
                x.execute();
//                xf.execute();
                //SaveSharedPreference.setCountRows(ctx, SaveSharedPreference.getCountRows(ctx));
                System.out.println("COUNT ROWS LAMA " + SaveSharedPreference.getTempCountRows(ctx));
                System.out.println("COUNT ROWS Baru " + SaveSharedPreference.getCountRows(ctx));
                if(SaveSharedPreference.getTempCountRows(ctx) <  SaveSharedPreference.getCountRows(ctx)){
                    NotificationMethod("PAPMO","You have a notification","ununderstandable ticker",1,Service.this);
                    //  saveSharedPreferences.count_matkul_list(ServiceClass.this,saveSharedPreferences.getTempCountTopic(ServiceClass.this));

                }
                else {

                    Log.d("fuck", "nggk ada update");
                }
            }

        };


        time = new Timer(true);
        time.schedule(timerTask,1000,5000);
    }

    public void stoptheTask(){
        if(time!=null){
            time.cancel();
        }
    }

    public void NotificationMethod(CharSequence title, CharSequence contentText, CharSequence ticker,int ID,Context context){
        Intent notificationIntent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,flag);
        //Notification notification = new Notification.Builder(this).setContentTitle(title).setTicker(ticker).setContentText(contentText).setAutoCancel(true).setContentIntent(pendingIntent).setSmallIcon(R.mipmap.ic_launcher).build();
        Notification notification = new NotificationCompat.Builder(this).setContentTitle(title).setTicker(ticker).setContentText(contentText).setAutoCancel(true).setContentIntent(pendingIntent).setSmallIcon(R.mipmap.ic_launcher).build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(ID, notification);
    }
}