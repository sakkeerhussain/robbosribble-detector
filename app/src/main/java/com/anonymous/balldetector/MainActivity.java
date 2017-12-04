package com.anonymous.balldetector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anonymous.balldetector.server.MyServer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MyServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            server = new MyServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(server != null) {
            server.stop();
        }
    }
}
