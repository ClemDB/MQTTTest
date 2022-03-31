package com.example.mqtttest;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.util.List;

public class CharactersFragment extends Fragment {

    public List<Character> characterList;
    public AdapterList adapterList;
    RecyclerView rvListe;
    private InterfaceCharacters interfaceCharacters;
    public LinearLayout divLayout;

    public CharactersFragment() {
        // Required empty public constructor
    }

    public interface InterfaceCharacters {
        void sendMessage(String msg);
        void getCharacters(AdapterList adapterList, RecyclerView rvList);
        boolean getHasCha();
    }

    public static CharactersFragment newInstance() {
        CharactersFragment fragment = new CharactersFragment();
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
        return inflater.inflate(R.layout.fragment_characters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        divLayout = view.findViewById(R.id.divAjouter);
        rvListe = view.findViewById(R.id.rvListCharacters);
        rvListe.setHasFixedSize(true);
        rvListe.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterList = new AdapterList(characterList);

        rvListe.setAdapter(adapterList);

        interfaceCharacters.getCharacters(adapterList, rvListe);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        interfaceCharacters = (CharactersFragment.InterfaceCharacters)context;
    }

}