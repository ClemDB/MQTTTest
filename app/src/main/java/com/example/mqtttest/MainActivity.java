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
    Context c = this;
    MonViewModel monViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        loginFragment  = LoginFragment.newInstance();
        menuFragment = MenuFragment.newInstance();

        clientMQTT = new ClientMQTT(getApplicationContext());
        loginFragment.clientMQTT = clientMQTT;
        mqttInfo();

        showFragment(loginFragment);
    }

    public void showFragment(Fragment f) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void showMenu() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, menuFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void mqttInfo()
    {
        clientMQTT.reconnecter();

        clientMQTT.mqttAndroidClient.setCallback(new MqttCallbackExtended()
        {
            @Override
            public void connectComplete(boolean b, String s)
            {
                Log.w(TAG,"connectComplete");
            }

            @Override
            public void connectionLost(Throwable throwable)
            {
                Log.w(TAG,"connectionLost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
            {
                Log.w(TAG, "messageArrived : " + mqttMessage.toString());
                String msg = mqttMessage.toString();
                ArrayList<Integer> result = findPositions(msg,' ');
                Log.w(TAG, "SubString : " + msg.substring(0, result.get(1)));
                if(msg.substring(0, result.get(1)).equals("tryco valide"))
                {
                    Log.d("substring", "connexion ok");
                    //monViewModel.setAccount(new Account("test", "test"));
                    SharedPreferences prefs = getSharedPreferences("test", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("test", msg.substring(result.get(1), result.get(2)));
                    editor.putBoolean("connecte", true);
                    editor.commit();

                    showFragment(menuFragment);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
            {
                Log.w(TAG, "deliveryComplete");
            }
        });
    }

    public ArrayList<Integer> findPositions(String string, char character) {
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < string.length(); i++){
            if (string.charAt(i) == character) {
                positions.add(i);
            }
        }
        return positions;
    }

}