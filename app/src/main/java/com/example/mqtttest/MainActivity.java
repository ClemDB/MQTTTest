package com.example.mqtttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static ClientMQTT clientMQTT = null;
    private static final String TAG = "MainActivity"; //!< le TAG de la classe pour les logs
    FragmentManager fragmentManager;
    LoginFragment loginFragment;
    MenuFragment menuFragment;
    CreationCompteFragment creationCompteFragment;
    Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        loginFragment  = LoginFragment.newInstance();
        menuFragment = MenuFragment.newInstance();
        creationCompteFragment = CreationCompteFragment.newInstance();

        clientMQTT = new ClientMQTT(getApplicationContext());
        loginFragment.clientMQTT = clientMQTT;
        menuFragment.clientMQTT = clientMQTT;
        creationCompteFragment.clientMQTT = clientMQTT;


        showFragment(loginFragment);
    }

    public void showFragment(Fragment f) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}