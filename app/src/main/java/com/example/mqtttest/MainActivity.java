package com.example.mqtttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class MainActivity extends AppCompatActivity {

    public static ClientMQTT clientMQTT = null;
    FragmentManager fragmentManager;
    LoginFragment loginFragment;
    MenuFragment menuFragment;
    Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        loginFragment  = LoginFragment.newInstance();
        menuFragment = MenuFragment.newInstance();

        clientMQTT = new ClientMQTT(getApplicationContext());
        loginFragment.clientMQTT = clientMQTT;

        showFragment(loginFragment);
    }

    public void showFragment(Fragment f) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}