package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReciever extends BroadcastReceiver {
    private static final String TAG = "ThreadId";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG+" broad", "onReceive: "+Thread.currentThread().getId());
        Toast.makeText(context,"received",Toast.LENGTH_SHORT).show();
    }
}
