package com.example.mqtttest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonViewModel extends ViewModel {

    private MutableLiveData<String> account;

    public LiveData<String> getAccount() {
        return account;
    }
}
