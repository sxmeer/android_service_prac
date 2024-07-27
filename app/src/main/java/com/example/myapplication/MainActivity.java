package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button startServiceButton;
    Button stopServiceButton;
    Button bindServiceButton;
    Button unbindServiceButton;
    Button numberButton;
    TextView serviceStatusTextView;
    Observer<Double> observer;

    BroadcastReceiver myBroadcastReceiver = new MyBroadcastReciever();
    Intent serviceIntent;

    private boolean isServiceBinded = false;
    private ServiceConnection serviceConnection;
    private MainService mainService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ThreadId mainact",String.valueOf(Thread.currentThread().getId()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent(this,MainService.class);

        serviceStatusTextView= findViewById(R.id.display_result_text_view);
        startServiceButton = findViewById(R.id.start_service_button);
        stopServiceButton = findViewById(R.id.finish_service_button);
        bindServiceButton = findViewById(R.id.bind_service_button);
        unbindServiceButton = findViewById(R.id.unbind_service_button);
        numberButton = findViewById(R.id.get_generated_number_button);

        startServiceButton.setOnClickListener(this);
        stopServiceButton.setOnClickListener(this);
        bindServiceButton.setOnClickListener(this);
        unbindServiceButton.setOnClickListener(this);
        numberButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("com.sameer.org");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver,intentFilter);


        Intent intent = new Intent();
        intent.setAction("com.sameer.org");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        sendBroadcast(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_service_button:
                startService(serviceIntent);
                break;
            case R.id.finish_service_button:
                stopService(serviceIntent);
                break;
            case R.id.bind_service_button:
                bindToService();
                break;
            case R.id.unbind_service_button:
                unbindToService();
                break;
            case R.id.get_generated_number_button:
                getNumber();
                break;
        }
    }
    public void bindToService(){
        if(serviceConnection==null){
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    Log.i("log","new service connection");
                    MainService.MyService myServiceBinder = (MainService.MyService)iBinder;
                    mainService = myServiceBinder.getBinder();
                    Log.i("log",mainService.toString());
                    isServiceBinded = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    mainService = null;
                    isServiceBinded = false;
                }
            };
        }
        bindService(serviceIntent,serviceConnection,BIND_AUTO_CREATE);
        observer = new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                serviceStatusTextView.setText(String.valueOf(aDouble));
            }
        };
    }
    public void unbindToService(){
        if(isServiceBinded){
            mainService.getRandomNumber().removeObserver(observer);
            unbindService(serviceConnection);
            isServiceBinded = false;
        }
    }
    public void getNumber(){
        if (mainService == null) {
            Toast.makeText(this,"start service",Toast.LENGTH_SHORT).show();
        }else{
            mainService.getRandomNumber().observe(this, observer
               /*     new Observer<Double>() {
                @Override
                public void onChanged(Double aDouble) {
                    if(mainService!=null)
                        serviceStatusTextView.setText(String.valueOf(aDouble));
                }
            }*/
            );
        }
    }
}
