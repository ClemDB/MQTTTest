package com.example.mqtttest;

import androidx.annotation.NonNull;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    List<Mur> listeMur = new ArrayList<>();

    List<Piege>listeP = new ArrayList<>();
    List<Monstre> listeM = new ArrayList();
    List<Flag> listeCheckPoint = new ArrayList();
    JSONArray j;
    View view;
    boolean co;
    int ctr = 0;
    int ctrPieges = 0;
    boolean hasCha = true;

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
        showFragment(loginFragment);
    }

    @Override
    public void sendMessage(String msg) {
        clientMQTT.publishMessage(msg);
    }

    @Override
    public void addCha(String chaName) {
        clientMQTT.publishMessage("addcharacter " + account.username + " " + account.password + " " + chaName);
    }

    @Override
    public void getCharacters(AdapterList adapterList, RecyclerView rvlist) {
        adapterList = new AdapterList(characterList);
        rvlist.setAdapter(adapterList);
    }

    @Override
    public boolean getHasCha() {
        return hasCha;
    }

    @Override
    public void changeRotation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        showFragment(gameFragment);
    }

    @Override
    public void setUsername(TextView tvUsername) {
        tvUsername.setText(account.username);
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
                        hasCha = false;
                    }

                    try {
                        if(msg.substring(0, result.get(0)).trim().equals("getLeaderboardM")) {

                            Log.d(TAG, " subJson: " + msg.substring(16));
                            j =  new JSONArray(msg.substring(16));
                            showFragment(menuFragment);
                        }

                    } catch (Exception e) {}

                    try {
                        if(msg.substring(0, result.get(0)).trim().equals("getLeaderboardFinal")) {

                            Log.d(TAG, " subJson: " + msg.substring(20));
                            JSONArray arrayj =  new JSONArray(msg.substring(20));
                            Fragment f = fragmentManager.findFragmentById(R.id.fragmentContainerView);
                            if(f instanceof GameFragment) {
                                ((GameFragment) f).workerThread(arrayj);
                            } else {
                                Log.d(TAG, "else sortie");
                            }
                        }

                    } catch (Exception e) {}




                    if(msg.substring(0, result.get(0)).trim().equals("posSortie")) {
                        //Log.d(TAG, "indddd");
                        ArrayList<Integer> tiret = findPositions(msg,'-');

                        Fragment f = fragmentManager.findFragmentById(R.id.fragmentContainerView);
                        if(f instanceof GameFragment) {
                            //Log.d(TAG, "pos x:" + msg.substring(result.get(0) + 1, tiret.get(0)) + " y:" + msg.substring(tiret.get(0) + 1));
                            int x = Integer.parseInt(msg.substring(result.get(0) + 1, tiret.get(0)));
                            int y = Integer.parseInt(msg.substring(tiret.get(0) + 1));
                            //Log.d(TAG, "pos x:" + x + " y:" + y);
                            ((GameFragment) f).gameView.setSortie(x,y);
                            //Log.d(TAG, "sortie effectue");
                        } else {
                            Log.d(TAG, "else sortie");
                        }
                    }



                    if(msg.substring(0, result.get(0)).trim().equals("monstre")) {
                        ArrayList<Integer> vir = findPositions(msg,'-');

                        int x = Integer.parseInt(msg.substring(result.get(0) + 1, vir.get(0)));
                        int y = Integer.parseInt(msg.substring(vir.get(0) + 1, vir.get(1)));
                        int  sens = Integer.parseInt(msg.substring(vir.get(1) + 1, vir.get(2)));
                        int  distance =Integer.parseInt(msg.substring(vir.get(2) + 1));

                        listeM.add(new Monstre(x,y,sens,distance));
                        //Log.d(TAG,listeM.get(0).toString());


                        Fragment f = fragmentManager.findFragmentById(R.id.fragmentContainerView);
                        if(f instanceof GameFragment) {
                            ((GameFragment) f).gameView.setMonstres(listeM);
                        } else {
                            Log.d(TAG, "error instance of submur");
                        }
                    }

                    if(msg.substring(0, result.get(0)).trim().equals("mur")) {
                        ArrayList<Integer> vir = findPositions(msg,',');
                        int x = Integer.parseInt(msg.substring(result.get(0) + 1, vir.get(0)));
                        int y = Integer.parseInt( msg.substring(vir.get(0) + 1));
                        //Log.d(TAG, "submur x:" + x);
                        //Log.d(TAG, "submur y:" + y);
                        Mur m = new Mur(x,y);
                        listeMur.add(m);

                        Fragment f = fragmentManager.findFragmentById(R.id.fragmentContainerView);
                        if(f instanceof GameFragment) {
                            ((GameFragment) f).gameView.setMur(listeMur);
                        } else {
                            Log.d(TAG, "error instance of submur");
                        }
                    }

                    if(msg.substring(0, result.get(0)).trim().equals("posSortie")) {
                        ArrayList<Integer> vir = findPositions(msg,'-');
                        int x = Integer.parseInt(msg.substring(result.get(0) + 1, vir.get(0)));
                        int y = Integer.parseInt( msg.substring(vir.get(0) + 1));
                        //Log.d(TAG, "subSortie x:" + x);
                        //Log.d(TAG, "subSortie y:" + y);



                        Fragment f = fragmentManager.findFragmentById(R.id.fragmentContainerView);
                        if(f instanceof GameFragment) {
                            ((GameFragment) f).gameView.setSortie(x,y);
                        } else {
                            Log.d(TAG, "error instance of submur");
                        }
                    }

                    if(msg.substring(0, result.get(0)).trim().equals("piege")) {
                        ArrayList<Integer> vir = findPositions(msg,',');
                        int x = Integer.parseInt(msg.substring(result.get(0) + 1, vir.get(0)));
                        int y = Integer.parseInt( msg.substring(vir.get(0) + 1));
                        Piege p = new Piege(x,y);
                        //Log.d(TAG, "subpiege x:" + x);
                        //Log.d(TAG, "subpiege y:" + y);
                        listeP.add(p);


                        Fragment f = fragmentManager.findFragmentById(R.id.fragmentContainerView);
                        if(f instanceof GameFragment) {
                            ((GameFragment) f).gameView.setPieges(listeP);
                        } else {
                            Log.d(TAG, "error instance of subPieges");
                        }
                    }

                    if(msg.substring(0, result.get(0)).trim().equals("checkpoint")) {
                        ArrayList<Integer> vir = findPositions(msg,',');
                        int x = Integer.parseInt(msg.substring(result.get(0) + 1, vir.get(0)));
                        int y = Integer.parseInt( msg.substring(vir.get(0) + 1));
                        //Log.d(TAG, "subCheckpoint x:" + x);
                        //Log.d(TAG, "subCheckpoint y:" + y);

                        listeCheckPoint.add(new Flag(x, y));

                        Fragment f = fragmentManager.findFragmentById(R.id.fragmentContainerView);
                        if(f instanceof GameFragment) {
                            ((GameFragment) f).gameView.setCheckPoint(listeCheckPoint);
                        } else {
                            Log.d(TAG, "error instance of subPieges");
                        }
                    }

                    if(msg.substring(0, result.get(0)).trim().equals("curPos")) {
                        ArrayList<Integer> tir = findPositions(msg,'-');
                        ArrayList<Integer> par = findPositions(msg,'(');
                        ArrayList<Integer> par2 = findPositions(msg,')');
                        //Log.d(TAG, "x:" + msg.substring(par.get(0) +2, tir.get(0)) + " y:" + msg.substring(tir.get(0) +1, par2.get(0) -2));
                        Fragment f = fragmentManager.findFragmentById(R.id.fragmentContainerView);
                        if(f instanceof GameFragment) {
                            int x = Integer.parseInt(msg.substring(par.get(0) +2, tir.get(0)));
                            int y = Integer.parseInt(msg.substring(tir.get(0) +1, par2.get(0) -2));
                            ((GameFragment) f).gameView.setPos(x,y);
                            //Log.d(TAG, "mouvement effectue");
                        } else {
                            Log.d(TAG, "Le mode landscape essaie de te faire couler ta session");
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
                            ((GameFragment) f).gameView.nbCoup++;
                            //Log.d(TAG, "mouvement effectue");
                        } else {
                            Log.d(TAG, "Le mode landscape essaie de te faire couler ta session");
                        }

                    }

                    if(msg.substring(0, result.get(1)).equals("tryco valide"))
                    {
                        account = new Account(msg.substring(result.get(1), result.get(2)).trim(), msg.substring(result.get(2)).trim());
                        clientMQTT.publishMessage("getcharacter " + account.username + " " + account.password);
                        clientMQTT.publishMessage("getLeaderboardMenu");
                        Toast.makeText(c, "connexion en cours", Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.substring(0, result.get(0)).trim().equals("getCharacter") && msg.length() > 25){
                        List<Character> c = new ArrayList();
                        c.add(new Character(msg.substring(result.get(2) + 2, result.get(3) - 2), Integer.parseInt(msg.substring(result.get(3) + 1, result.get(4) - 1)), Integer.parseInt(msg.substring(result.get(4) + 1, result.get(5) - 1)), Integer.parseInt(msg.substring(result.get(5) + 1, result.get(6) - 1)), Integer.parseInt(msg.substring(result.get(6) + 1, result.get(7) - 1)), msg.substring(result.get(7) + 2, result.get(8) - 3)));
                        characterList = c;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.ajouterCha) {

        }

        return true;
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
    public void setStart(GameView gv, int level) {
        //clientMQTT.publishMessage("setChaName " + characterList.get(0).name);
        reset();
        gv.isStart = true;
        clientMQTT.publishMessage("startGame " + level);
        clientMQTT.publishMessage("setChaName test");

    }

    @Override
    public void showPopup(View v, GameView gv,JSONArray j) throws JSONException {
        showP(v, gv,j);
    }

    @Override
    public void nextLevel(View v, GameView gv) {
        reset();
        gv.reset();
        setStart(gv, gv.currentLevel + 1);
    }

    @Override
    public void showPopupD(View view, GameView gv) {
        showD(view, gv);
    }

    public void showP(View v, GameView gv, JSONArray j) throws JSONException {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_layout, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        TextView tvnom1 = popupView.findViewById(R.id.tvName1);
        TextView tvcoup1 = popupView.findViewById(R.id.tvNbrCoups1);
        TextView tvtemps1 = popupView.findViewById(R.id.tvTemps1);
        TextView tvnom2 = popupView.findViewById(R.id.tvName2);
        TextView tvcoup2 = popupView.findViewById(R.id.tvNbrCoups2);
        TextView tvtemps2 = popupView.findViewById(R.id.tvTemps2);
        TextView tvnom3 = popupView.findViewById(R.id.tvName3);
        TextView tvcoup3 = popupView.findViewById(R.id.tvNbrCoups3);
        TextView tvtemps3 = popupView.findViewById(R.id.tvTemps3);
        int n = j.length();
        for(int i=0;i<n;i++){
            JSONObject score = j.getJSONObject(i);
            switch(i){
                case 0:
                    tvnom1.setText(score.getString("name"));
                    tvcoup1.setText(""+score.getInt("nbrCout"));
                    tvtemps1.setText(""+score.getInt("temps"));
                    break;
                case 1:
                    tvnom2.setText(score.getString("name"));
                    tvcoup2.setText(""+score.getInt("nbrCout"));
                    tvtemps2.setText(""+score.getInt("temps"));
                    break;

                case 2:
                    tvnom3.setText(score.getString("name"));
                    tvcoup3.setText(""+score.getInt("nbrCout"));
                    tvtemps3.setText(""+score.getInt("temps"));
                    break;

            }

        }

        Button btnRecommencer = popupView.findViewById(R.id.btnPopup_Retry);
        Button btnNiveauSuivant = popupView.findViewById(R.id.btnPopup_Suivant);
        btnRecommencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                gv.reset();
                setStart(gv, gv.currentLevel);
                popupWindow.dismiss();
            }
        });


        btnNiveauSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                gv.reset();
                gv.currentLevel++;
                setStart(gv, gv.currentLevel);
                popupWindow.dismiss();
            }
        });
    }

    private void reset() {
        listeCheckPoint = new ArrayList<>();
        listeP = new ArrayList<>();
        listeM = new ArrayList<>();
        listeMur = new ArrayList<>();
    }

    public void showD(View v, GameView gv) {
        Log.d(TAG,"inShowD");
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


        TextView titre = popupView.findViewById(R.id.tvPopup_Titre);
        Button btnRecommencer = popupView.findViewById(R.id.btnPopup_Retry);
        Button btnNiveauSuivant = popupView.findViewById(R.id.btnPopup_Suivant);
        LinearLayout linearTab = popupView.findViewById(R.id.linearTab);
        btnRecommencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                gv.reset();
                setStart(gv, gv.currentLevel);
                popupWindow.dismiss();
            }
        });

        titre.setText("Defaite");
        btnNiveauSuivant.setVisibility(View.GONE);
        linearTab.setVisibility(View.GONE);

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

    @Override
    public void getLeaderBoard(AdapterListLeader adapterList, RecyclerView rvList) throws JSONException {
        List<Leaderboard> lLeader= new ArrayList<>();

        int n = j.length();
        for(int i=0;i<n;i++){
            JSONObject score = j.getJSONObject(i);
            lLeader.add(new Leaderboard(score.getString("name"),score.getInt("nbrCout"),score.getInt("temps")));
        }

        adapterList = new AdapterListLeader(lLeader);
        rvList.setAdapter(adapterList);
    }
}