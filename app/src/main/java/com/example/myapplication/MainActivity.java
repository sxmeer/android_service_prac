package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button startServiceButton;
    Button stopServiceButton;
    Button bindServiceButton;
    Button unbindServiceButton;
    Button numberButton;
    TextView serviceStatusTextView;

    Intent serviceIntent;

    private boolean isServiceBinded = false;
    private ServiceConnection serviceConnection;
    private MainService mainService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                    isServiceBinded = false;
                }
            };
        }
        bindService(serviceIntent,serviceConnection,BIND_AUTO_CREATE);
    }
    public void unbindToService(){
        if(isServiceBinded){
            unbindService(serviceConnection);
            isServiceBinded = false;
        }
    }
    public void getNumber(){
        mainService.getRandomNumber().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                serviceStatusTextView.setText(String.valueOf(aDouble));
            }
        });
    }
}
