package com.example.ex4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DisplayJoystick extends AppCompatActivity implements Joystick.JoystickListener{

    private String ip;
    private int port;
    private Joystick joystick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joystick);
        Intent JoystickIntent=getIntent();
        Bundle extras=JoystickIntent.getExtras();
        ip=extras.getString("IP");
        port=extras.getInt("Port");
        joystick=new Joystick(this);
        setContentView(joystick);

    }

    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        SingeltonServer.getInstance().Send(xPercent,yPercent);
        Log.d("Main Method", "X percent: " + xPercent + " Y percent: " + yPercent);
    }
}
