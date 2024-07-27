package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;


public class MainService extends Service {
    Thread processThread;
    private static final String TAG = "log";
    MutableLiveData<Double> mutableLiveData = new MutableLiveData<>();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ThreadId mainserv",String.valueOf(Thread.currentThread().getId()));
        Log.d(TAG, "onCreate: ");
        processThread = new Thread(){
            @Override
            public void run() {
                Log.d("ThreadId thread",String.valueOf(Thread.currentThread().getId()));
                while(true){
                    try{
                        sleep(1000);
                        mutableLiveData.postValue(Math.random());
                    }
                    catch(Exception e){
                        break;
                    }
                }
            }
        };
        processThread.start();
    }

    double randomNumber;
    private IBinder mbinder = new MyService();

    class MyService extends Binder {
        public MainService getBinder(){
            return MainService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand:");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mbinder;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"des");
        super.onDestroy();
        processThread.interrupt();
    }

    public MutableLiveData<Double> getRandomNumber(){
        return mutableLiveData;
    }
}
