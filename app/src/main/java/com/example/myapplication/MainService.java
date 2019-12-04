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
    MutableLiveData<Double> mutableLiveData = new MutableLiveData<>();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("log",String.valueOf(Thread.currentThread().getId()));
        processThread = new Thread(){
            @Override
            public void run() {
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }

    @Override
    public void onDestroy() {
        Log.i("log","des");
        super.onDestroy();
        processThread.interrupt();
    }

    public MutableLiveData<Double> getRandomNumber(){
        return mutableLiveData;
    }
}
