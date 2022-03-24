package com.example.mqtttest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MonViewModel extends ViewModel {

    private MutableLiveData<List<Account>> accounts;
    private MutableLiveData<List<Character>> characters;

    public LiveData<List<Account>> getAccounts() {
        if (accounts == null) {
            accounts = new MutableLiveData<List<Account>>();
            genererListe();
        }
        return accounts;
    }

    public LiveData<List<Character>> getCharacters() {
        if (characters == null) {
            characters = new MutableLiveData<List<Character>>();
            genererListeCharacters();
        }
        return characters;
    }

    public void genererListe() {
        List<Account> l = new ArrayList<>();
        accounts.setValue(l);
    }

    public void genererListeCharacters() {
        List<Character> l = new ArrayList<>();
        characters.setValue(l);
    }

}
