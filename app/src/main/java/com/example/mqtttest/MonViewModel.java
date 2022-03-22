package com.example.mqtttest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonViewModel extends ViewModel {

    private MutableLiveData<Account> account;

    public LiveData<Account> getAccount() {
        return account;
    }

    public void setAccount(MutableLiveData<Account> account) {
        this.account = account;
    }
}
