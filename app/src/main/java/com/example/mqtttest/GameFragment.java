package com.example.mqtttest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class GameFragment extends Fragment {

    private InterfaceGame interfaceGame;
    GameView gameView;
    Button btnStart, btnDroite;

    public GameFragment() {
        // Required empty public constructor
    }

    public interface InterfaceGame {
        void sendMessage(String msg);
        void changeRotation();
        void showFragment(Fragment f);
        void setChaName();
    }


    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
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
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //interfaceGame.showFragment(this);
        gameView = view.findViewById(R.id.gv);
        btnStart = view.findViewById(R.id.btnStart);
        btnDroite = view.findViewById(R.id.buttonDroite);
        //gameView.setPos(0, 0);
        //interfaceGame.setGameView(gameView);
        btnDroite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.setPos(gameView.x + 1 , gameView.y);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceGame.setChaName();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        interfaceGame = (GameFragment.InterfaceGame)context;
    }
}