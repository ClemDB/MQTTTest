package com.example.mqtttest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GameFragment extends Fragment {

    private InterfaceGame interfaceGame;

    public GameFragment() {
        // Required empty public constructor
    }
    public interface InterfaceGame {
        void sendMessage(String msg);
        void changeRotation();
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
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        interfaceGame.changeRotation();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        interfaceGame = (GameFragment.InterfaceGame)context;
    }
}