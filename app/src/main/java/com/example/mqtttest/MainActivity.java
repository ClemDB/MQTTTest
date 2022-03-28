package com.example.mqtttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.pm.ActivityInfo;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.InterfaceLogin, MenuFragment.InterfaceMenu, CreationCompteFragment.InterfaceCreationCompte, GameFragment.InterfaceGame, CharactersFragment.InterfaceCharacters {

    public static ClientMQTT clientMQTT = null;
    private static final String TAG = "MainActivity";
    FragmentManager fragmentManager;
    LoginFragment loginFragment;
    MenuFragment menuFragment;
    GameFragment gameFragment;
    CreationCompteFragment creationCompteFragment;
    Context c = this;
    Account account;
    List<Character> characterList;
    boolean co;

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
        gameFragment = GameFragment.newInstance();
        creationCompteFragment = CreationCompteFragment.newInstance();

        characterList = new ArrayList<>();

        clientMQTT = new ClientMQTT(getApplicationContext());

        mqttInfo();
        showFragment(gameFragment);
    }

    @Override
    public void sendMessage(String msg) {
        clientMQTT.publishMessage(msg);
    }

    @Override
    public void getCharacters(AdapterList adapterList, RecyclerView rvlist) {
        adapterList = new AdapterList(characterList);
        rvlist.setAdapter(adapterList);
    }

    @Override
    public void changeRotation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        showFragment(gameFragment);
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
                co = true;
            }

            @Override
            public void connectionLost(Throwable throwable)
            {
                Log.w(TAG,"connectionLost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
            {
                if(co) {
                    Log.w(TAG, "messageArrived : " + mqttMessage.toString());
                    String msg = mqttMessage.toString();
                    ArrayList<Integer> result = findPositions(msg,' ');
                    //Log.w(TAG, "SubString : " + msg.substring(0, result.get(0)).trim());

                    if(msg.trim().equals("getchaempty")) {
                        showFragment(menuFragment);
                    }

                    if(msg.substring(0, result.get(0)).trim().equals("curPos")) {
                        ArrayList<Integer> tir = findPositions(msg,'-');
                        ArrayList<Integer> par = findPositions(msg,'(');
                        ArrayList<Integer> par2 = findPositions(msg,')');
                        Log.d(TAG, "x:" + msg.substring(par.get(0) +2, tir.get(0)) + " y:" + msg.substring(tir.get(0) +1, par2.get(0) -2));
                        Fragment f = fragmentManager.findFragmentById(R.id.fragmentContainerView);
                        if(f instanceof GameFragment) {
                            int x = Integer.parseInt(msg.substring(par.get(0) +2, tir.get(0)));
                            int y = Integer.parseInt(msg.substring(tir.get(0) +1, par2.get(0) -2));
                            ((GameFragment) f).gameView.setPos(x,y);
                            //Log.d(TAG, "mouvement effectue");
                        }
                    }

                    if(msg.substring(0, result.get(0)).trim().equals("mouvement")) {
                        ArrayList<Integer> tiret = findPositions(msg,'-');

                        Fragment f = fragmentManager.findFragmentById(R.id.fragmentContainerView);
                        if(f instanceof GameFragment) {
                            //Log.d(TAG, "pos x:" + msg.substring(result.get(0) + 1, tiret.get(0)) + " y:" + msg.substring(tiret.get(0) + 1));
                            int x = Integer.parseInt(msg.substring(result.get(0) + 1, tiret.get(0)));
                            int y = Integer.parseInt(msg.substring(tiret.get(0) + 1));
                            //Log.d(TAG, "pos x:" + x + " y:" + y);
                            ((GameFragment) f).gameView.setPos(x,y);
                            //Log.d(TAG, "mouvement effectue");
                        } else {
                            Log.d(TAG, "Le mode landscape essaie de te faire couler ta session");
                        }

                    }

                    if(msg.substring(0, result.get(1)).equals("tryco valide"))
                    {
                        account = new Account(msg.substring(result.get(1), result.get(2)).trim(), msg.substring(result.get(2)).trim());
                        clientMQTT.publishMessage("getcharacter " + account.username + " " + account.password);
                        Toast.makeText(c, "connexion en cours", Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.substring(0, result.get(0)).trim().equals("getCharacter") && msg.length() > 25){
                        List<Character> c = new ArrayList();
                        c.add(new Character(msg.substring(result.get(2) + 2, result.get(3) - 2), Integer.parseInt(msg.substring(result.get(3) + 1, result.get(4) - 1)), Integer.parseInt(msg.substring(result.get(4) + 1, result.get(5) - 1)), Integer.parseInt(msg.substring(result.get(5) + 1, result.get(6) - 1)), Integer.parseInt(msg.substring(result.get(6) + 1, result.get(7) - 1)), msg.substring(result.get(7) + 2, result.get(8) - 3)));
                        characterList = c;
                        showFragment(menuFragment);
                    }
                    else if(msg.equals("addUser : Success")) {
                        showFragment(loginFragment);
                    }
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
    public void setChaName() {
        //clientMQTT.publishMessage("setChaName " + characterList.get(0).name);
        clientMQTT.publishMessage("startGame");
        clientMQTT.publishMessage("setChaName test");

    }

    @Override
    public void setInfoMenu(CardView cv, TextView username, TextView chaName, TextView chaLevel, TextView chaHP, TextView chaMP) {
        username.setText(account.username);
        if(characterList.size() > 0) {
            chaName.setText(characterList.get(0).name);
            chaLevel.setText(String.valueOf(characterList.get(0).level));
            chaHP.setText(String.valueOf(characterList.get(0).hp));
            chaMP.setText(String.valueOf(characterList.get(0).mp));
        }
        else {
            cv.setVisibility(View.GONE);
        }
    }
}