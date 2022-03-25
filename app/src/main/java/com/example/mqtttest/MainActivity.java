package com.example.mqtttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.InterfaceLogin, MenuFragment.InterfaceMenu, CreationCompteFragment.InterfaceCreationCompte {

    public static ClientMQTT clientMQTT = null;
    private static final String TAG = "MainActivity";
    FragmentManager fragmentManager;
    LoginFragment loginFragment;
    MenuFragment menuFragment;
    CreationCompteFragment creationCompteFragment;
    Context c = this;
    Account account;
    List<Character> characterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        loginFragment  = LoginFragment.newInstance();
        menuFragment = MenuFragment.newInstance();
        creationCompteFragment = CreationCompteFragment.newInstance();

        characterList = new ArrayList<>();

        clientMQTT = new ClientMQTT(getApplicationContext());

        mqttInfo();
        showFragment(loginFragment);
    }

    @Override
    public void sendMessage(String msg) {
        clientMQTT.publishMessage(msg);
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
                //Log.w(TAG, "SubString : " + msg.substring(0, result.get(1)));

                if(msg.substring(0, result.get(1)).equals("tryco valide"))
                {
                    account = new Account(msg.substring(result.get(1), result.get(2)).trim(), msg.substring(result.get(2)).trim());
                    clientMQTT.publishMessage("getcharacter " + account.username + " " + account.password);
                    Toast.makeText(c, "connexion réussie", Toast.LENGTH_LONG).show();
                }
                else if(msg.substring(0, result.get(0)).trim().equals("getCharacter")){
                    List<Character> c = new ArrayList();
                    c.add(new Character(msg.substring(result.get(2) + 2, result.get(3) - 2), Integer.parseInt(msg.substring(result.get(3) + 1, result.get(4) -1)), Integer.parseInt(msg.substring(result.get(4) + 1, result.get(5) -1)), Integer.parseInt(msg.substring(result.get(5) + 1, result.get(6) -1)), Integer.parseInt(msg.substring(result.get(6) + 1, result.get(7) -1)), msg.substring(result.get(7) + 2, result.get(8) - 3)));
                    characterList = c;
                    showFragment(menuFragment);
                }
                else if(msg.equals("addUser : Success")) {
                    showFragment(loginFragment);
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

    @Override
    public void showFragment(Fragment f) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void setInfoMenu(TextView username, TextView chaName, TextView chaLevel, TextView chaHP, TextView chaMP) {
        username.setText(account.username);
        chaName.setText(characterList.get(0).name);
        chaLevel.setText(String.valueOf(characterList.get(0).level));
        chaHP.setText(String.valueOf(characterList.get(0).hp));
        chaMP.setText(String.valueOf(characterList.get(0).mp));
    }
}