package com.example.mqtttest;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {


    TextView tvUsername, tvChaName, tvChaLevel, tvChaHP, tvChaMP;
    CardView cardView;
    Button btnPersonnages, btnStart;
    String TAG = "MenuFragment";
    private InterfaceMenu interfaceMenu;

    public MenuFragment() {
        // Required empty public constructor
    }

    public interface InterfaceMenu{
        void showFragment(Fragment f);
        void setInfoMenu(CardView cv, TextView username, TextView chaName, TextView chaLevel, TextView chaHP, TextView chaMP);
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cardView = view.findViewById(R.id.cardViewMenu);
        tvUsername = view.findViewById(R.id.tvUsernameMenu);
        tvChaName = view.findViewById(R.id.tvMenuChaName);
        tvChaLevel = view.findViewById(R.id.tvMenuChaLevel);
        tvChaHP = view.findViewById(R.id.tvMenuChaHP);
        tvChaMP = view.findViewById(R.id.tvMenuChaMP);
        btnPersonnages = view.findViewById(R.id.btnMenuPersonnages);
        btnStart = view.findViewById(R.id.btnMenuStart);

        //tvUsername.setText(interfaceMenu.getAccount().username);
        interfaceMenu.setInfoMenu(cardView, tvUsername, tvChaName, tvChaLevel, tvChaHP, tvChaMP);
        /*
        monViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
            tvUsername.setText(accounts.get(0).username);
        });
         */

        btnPersonnages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                monViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
                    clientMQTT.publishMessage("getchajson " + accounts.get(0).username + " " + accounts.get(0).password);
                });
                 */
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new GameFragment()).commit();
                interfaceMenu.showFragment(new GameFragment());
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        interfaceMenu = (MenuFragment.InterfaceMenu)context;
    }
/*
    private void mqttInfo()
    {
       clientMQTT.reconnecter();

        clientMQTT.mqttAndroidClient.setCallback(new MqttCallbackExtended()
        {
            @Override
            public void connectComplete(boolean b, String s)
            {
                Log.w(TAG,"connectComplete");
                try {
                    Thread.sleep(2000);

                    monViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
                        clientMQTT.publishMessage("getcharacter " + accounts.get(0).username + " " + accounts.get(0).password);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                if(msg.substring(0, result.get(0)).trim().equals("getCharacter")){
                    //monViewModel.getCharacters().getValue().add(new Character(msg.substring(result.get(1), result.get(2) - 1), Integer.parseInt(msg.substring(result.get(2), result.get(3) -1)), Integer.parseInt(msg.substring(result.get(3), result.get(4) -1)), Integer.parseInt(msg.substring(result.get(4), result.get(5) -1)), Integer.parseInt(msg.substring(result.get(5), result.get(6) -1)), msg.substring(result.get(6))));
                    monViewModel.getCharacters().getValue().add(new Character(msg.substring(result.get(2) + 2, result.get(3) - 2), Integer.parseInt(msg.substring(result.get(3) + 1, result.get(4) -1)), Integer.parseInt(msg.substring(result.get(4) + 1, result.get(5) -1)), Integer.parseInt(msg.substring(result.get(5) + 1, result.get(6) -1)), Integer.parseInt(msg.substring(result.get(6) + 1, result.get(7) -1)), msg.substring(result.get(7) + 2, result.get(8) - 3)));
                    monViewModel.getCharacters().observe(getViewLifecycleOwner(), characters -> {
                        Log.d(TAG, characters.get(0).toString());
                        tvChaName.setText(characters.get(0).name);
                        tvChaLevel.setText(String.valueOf(characters.get(0).level));
                        tvChaHP.setText(String.valueOf(characters.get(0).hp));
                        tvChaMP.setText(String.valueOf(characters.get(0).mp));
                    });
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
 */
}