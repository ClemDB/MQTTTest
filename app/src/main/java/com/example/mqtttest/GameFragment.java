package com.example.mqtttest;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

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
        void setStart(GameView gv, int level);
        void showPopup(View view, GameView gv,JSONArray j) throws JSONException;
        void nextLevel(View v, GameView gv);
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
        btnStart = view.findViewById(R.id.btnLevel1);
        btnDroite = view.findViewById(R.id.btnNiveau2);
        //gameView.setPos(0, 0);
        //interfaceGame.setGameView(gameView);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceGame.setStart(gameView, 1);
            }
        });

        Timer timer = new Timer();
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                if(!gameView.isDead && gameView.win == false){

                    gameView.cyclePiege();
                    gameView.mouvementMonstre();
                    for(Monstre m :gameView.listeM)
                        if ((m.getX()== gameView.x && m.getY()==gameView.y)) {
                            gameView.isDead = true;

                        }
                    for(Piege p :gameView.listeP)
                        if ((p.getX()== gameView.x && p.getY()==gameView.y)) {
                            gameView.isDead = true;

                        }
                    if(gameView.win == false && gameView.sortie.isCheck() && (gameView.x == gameView.sortie.getX() && gameView.y == gameView.sortie.getY())){
                        gameView.win = true;
                        interfaceGame.sendMessage("playerWin" + gameView.currentLevel);
                    }


                    if(gameView.isDead){
                        interfaceGame.sendMessage("playerDead");
                    }
                }


            }
        };
        timer.scheduleAtFixedRate(t,500,500);
    }

    @WorkerThread
    void workerThread(JSONArray j) {
        ContextCompat.getMainExecutor(getContext()).execute(()  -> {
            // This is where your UI code goes.
            try {
                interfaceGame.showPopup(getView(), gameView,j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        interfaceGame = (GameFragment.InterfaceGame)context;
    }

}