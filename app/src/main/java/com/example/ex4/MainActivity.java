package com.example.ex4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SingeltonServer singeltonServer;
    private Button myButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        singeltonServer=SingeltonServer.getInstance();

    }

    public void Connect(View view){
        EditText myIp=(EditText)findViewById(R.id.ip);
        EditText myPort=(EditText)findViewById(R.id.port);

        String parsedIp=myIp.getText().toString();
        int parsedPort=Integer.parseInt(myPort.getText().toString());
        singeltonServer.ConnectToServer(parsedIp,parsedPort);
        Intent joyStickIntent =new Intent(this,DisplayJoystick.class);
        Bundle extras=new Bundle();
        extras.putString("IP", parsedIp);
        extras.putInt("Port", parsedPort);
        joyStickIntent.putExtras(extras);
        startActivity(joyStickIntent);
    }
}
