package com.example.mqtttest;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class ClientMQTT extends AppCompatActivity
{
    private static final String TAG = "ClientMQTT";
    Context context;
    public MqttAndroidClient mqttAndroidClient = null;
    String serverUri = "tcp://192.168.2.62:1883";
    int portTTN = 1883;
    String clientId = "android";
    String subscriptionTopic = "raspi";
    String publishTopic = "raspi";
    String username = "android";
    String password = "";


    public ClientMQTT(Context context)
    {
        this.context = context;

        creer();
       // connecter();
    }

    public ClientMQTT(Context context, String serverTTN, int portTTN, String applicationId, String deviceId, String password)
    {
        this.context = context;
        this.serverUri = "tcp://" + serverTTN + ":" + portTTN;
        this.portTTN = portTTN;
        this.clientId = applicationId;
        this.subscriptionTopic = applicationId + "/devices/" + deviceId + "/up";
        this.publishTopic = applicationId + "/devices/" + deviceId + "/down";
        this.username = applicationId;
        this.password = password;
        Log.w(TAG, "MqttAndroidClient : serverUri -> " + serverUri);
        Log.w(TAG, "MqttAndroidClient : applicationId -> " + applicationId);
        Log.w(TAG, "MqttAndroidClient : deviceId -> " + deviceId);
        Log.w(TAG, "MqttAndroidClient : subscriptionTopic -> " + subscriptionTopic);
        Log.w(TAG, "MqttAndroidClient : publishTopic -> " + publishTopic);

        creer();
        //connecter();
    }

    public void creer()
    {
        Log.w(TAG, "MqttAndroidClient.creer : serverUri -> " + serverUri);
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended()
        {
            @Override
            public void connectComplete(boolean b, String s)
            {
                Log.w(TAG, "MqttAndroidClient : connectComplete -> " + s + " (" + b + ")");
            }

            @Override
            public void connectionLost(Throwable throwable)
            {
                Log.w(TAG, "MqttAndroidClient : connectionLost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
            {
                Log.w(TAG, "MqttAndroidClient : messageArrived -> " + mqttMessage.toString());

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
            {
                Log.w(TAG, "MqttAndroidClient : deliveryComplete");
            }
        });
    }

    public void publishMessage(String payload) {
        try {
            if (mqttAndroidClient.isConnected() == false) {
                mqttAndroidClient.connect();
            }

            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            message.setQos(0);
            mqttAndroidClient.publish("raspi", message,null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "publish succeed!") ;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "publish failed!") ;
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    public void setCallback(MqttCallbackExtended callback)
    {
        mqttAndroidClient.setCallback(callback);
    }

    public void connecter()
    {
        Log.w(TAG, "MqttAndroidClient.connecter : username -> " + username);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        //mqttConnectOptions.setUserName(username);
        //mqttConnectOptions.setPassword(password.toCharArray());

        try {
            mqttAndroidClient.connect(mqttConnectOptions, context, new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    Log.w(TAG, "connecter : onSuccess");
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    souscrire();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    Log.w(TAG, "connecter : onFailure -> " + serverUri + exception.toString());
                }
            });
        }
        catch (MqttException ex)
        {
            ex.printStackTrace();
            Log.e(TAG, "connecter : exception");
        }
    }

    public void reconnecter()
    {
        Log.w(TAG, "MqttAndroidClient : reconnecter");
        if(estConnecte())
            deconnecter();
        connecter();
    }

    public void deconnecter()
    {
        Log.w(TAG, "MqttAndroidClient : deconnecter");
        Thread deconnexion = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    mqttAndroidClient.disconnect();
                }
                catch (MqttException e)
                {
                    e.printStackTrace();
                    Log.e(TAG, "MqttAndroidClient : deconnecter -> exception");
                }
            }
        });
        // Démarrage
        deconnexion.start();
    }

    public boolean estConnecte()
    {
        Log.w(TAG, "MqttAndroidClient : estConnecte -> " + mqttAndroidClient.isConnected());
        return mqttAndroidClient.isConnected();
    }

    public boolean souscrire(String topic)
    {
        Log.w(TAG, "MqttAndroidClient : souscrire -> " + topic);
        try
        {
            final boolean[] retour = {false};
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    Log.w(TAG,"souscrire : onSuccess");
                    retour[0] = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    Log.w(TAG, "souscrire : onFailure");
                    retour[0] = false;
                }
            });
            return retour[0];
        }
        catch (MqttException ex)
        {
            Log.e(TAG, "souscrire : exception");
            ex.printStackTrace();
            return false;
        }
    }

    private void souscrire()
    {
        try
        {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    Log.w(TAG,"souscrire : onSuccess");
                    //publishMessage("Bonjour tout le monde");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    Log.w(TAG, "souscrire : onFailure");
                }
            });

        }
        catch (MqttException ex)
        {
            Log.e(TAG, "souscrire : exception");
            ex.printStackTrace();
        }
    }

    public void setParametres(String serverTTN, int portTTN, String applicationId, String deviceId, String password)
    {
        this.serverUri = "tcp://" + serverTTN + ":" + portTTN;
        this.portTTN = portTTN;
        this.clientId = applicationId;
        this.subscriptionTopic = applicationId + "/devices/" + deviceId + "/up";
        this.publishTopic = applicationId + "/devices/" + deviceId + "/down";
        this.username = applicationId;
        this.password = password;
        Log.w(TAG, "MqttAndroidClient : serverUri -> " + serverUri);
        Log.w(TAG, "MqttAndroidClient : applicationId -> " + applicationId);
        Log.w(TAG, "MqttAndroidClient : deviceId -> " + deviceId);
        Log.w(TAG, "MqttAndroidClient : subscriptionTopic -> " + subscriptionTopic);
        Log.w(TAG, "MqttAndroidClient : publishTopic -> " + publishTopic);
    }
}