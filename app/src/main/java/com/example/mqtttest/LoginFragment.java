package com.example.mqtttest;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    EditText useranme, password;
    Button btnConnexion, btAjouterCompte;
    public static ClientMQTT clientMQTT;
    MonViewModel monViewModel;
    String TAG = "LoginFragment";

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        useranme = view.findViewById(R.id.ed_username);
        password = view.findViewById(R.id.ed_password);
        btnConnexion = view.findViewById(R.id.btn_connexion);
        btAjouterCompte = view.findViewById(R.id.btAjouterCompte);
        monViewModel = new ViewModelProvider(requireActivity()).get(MonViewModel.class);

        mqttInfo();

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientMQTT.publishMessage("connexion " + useranme.getText().toString() + " " + password.getText().toString());
            }
        });

        btAjouterCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientMQTT.deconnecter();
                try {
                    Thread.sleep(2000);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CreationCompteFragment()).commit();
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        });
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
                    monViewModel.getAccounts().getValue().add(new Account(msg.substring(result.get(1), result.get(2)).trim(), msg.substring(result.get(2)).trim()));
                    /*
                    monViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> {
                        //Log.d(TAG, accounts.get(0).toString());
                    });
                    */
                    clientMQTT.deconnecter();
                    Thread.sleep(2000);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MenuFragment()).commit();
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