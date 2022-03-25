package com.example.mqtttest;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class CreationCompteFragment extends Fragment {

    EditText edNom, edMdp, edMdp2;
    Button btAjouter;
    String nom, mdp, mdp2;
    String TAG = "creationCompteFragment";
    private InterfaceCreationCompte interfaceCreationCompte;

    public CreationCompteFragment() {
        // Required empty public constructor
    }

    public interface InterfaceCreationCompte {
        void sendMessage(String msg);
    }

    // TODO: Rename and change types and number of parameters
    public static CreationCompteFragment newInstance() {
        CreationCompteFragment fragment = new CreationCompteFragment();
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
        return inflater.inflate(R.layout.fragment_creation_compte, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        btAjouter = view.findViewById(R.id.btnAjouterUser);
        edNom = view.findViewById(R.id.edNom);
        edMdp = view.findViewById(R.id.edMdp);
        edMdp2 = view.findViewById(R.id.edMdp2);


        btAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edNom.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Le nom est obligatoire.", Toast.LENGTH_LONG).show();
                    return;
                } else
                    nom = edNom.getText().toString();

                if (edMdp.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Le mot de passe est obligatoire.", Toast.LENGTH_LONG).show();
                    return;
                } else
                    mdp = edMdp.getText().toString();

                if (edMdp2.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "La confirmation du mot de passe est obligatoire.", Toast.LENGTH_LONG).show();
                    return;
                } else
                    mdp2 = edMdp2.getText().toString();

                if (!edMdp.getText().toString().equals(edMdp2.getText().toString())) {
                    Toast.makeText(getActivity(), "Erreur, les 2 mots de passe ne sont pas identique.", Toast.LENGTH_LONG).show();
                    return;

                }

                interfaceCreationCompte.sendMessage("adduser " + nom + " " + mdp);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        interfaceCreationCompte = (CreationCompteFragment.InterfaceCreationCompte)context;
    }
}