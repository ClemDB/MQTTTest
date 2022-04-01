package com.example.mqtttest;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;

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
    public InterfaceMenu interfaceMenu;
    public AdapterListLeader adapterList;
    RecyclerView rvListe;
    public List<Leaderboard> leaderboardList;

    public MenuFragment() {
        // Required empty public constructor
    }

    public interface InterfaceMenu{
        void showFragment(Fragment f);
        void setInfoMenu(CardView cv, TextView username, TextView chaName, TextView chaLevel, TextView chaHP, TextView chaMP);
        void getLeaderBoard(AdapterListLeader adapterList, RecyclerView rvList) throws JSONException;
        void changeRotation();
        void setUsername(TextView tvUsername);
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

        rvListe = view.findViewById(R.id.rvListLeader);
        rvListe.setHasFixedSize(true);
        rvListe.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterList = new AdapterListLeader(leaderboardList);

        rvListe.setAdapter(adapterList);

        interfaceMenu.setUsername(tvUsername);


        btnPersonnages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                monViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
                    clientMQTT.publishMessage("getchajson " + accounts.get(0).username + " " + accounts.get(0).password);
                });
                 */
                interfaceMenu.showFragment(new CharactersFragment());
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new GameFragment()).commit();
                interfaceMenu.changeRotation();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        interfaceMenu = (MenuFragment.InterfaceMenu)context;
    }
}