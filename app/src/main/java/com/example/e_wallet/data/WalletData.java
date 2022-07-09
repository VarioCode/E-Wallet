package com.example.e_wallet.data;

import android.app.Application;

import java.util.ArrayList;

public class WalletData extends Application {
//    private ArrayList<Integer> walletContents = new ArrayList<Integer>();
    private int[] walletContents = new int[150];
    private String walletName;

    public WalletData (String walletName) {
        this.walletName = walletName;
    }

    public String getwalletName() {
        return walletName;
    }

    public void add (int value, int amount) {
        walletContents[value] += amount;
    }

    public int[] getWalletContents() {
        return walletContents;
    }

    public void update (int value) {
        walletContents[value]++;
    }


    public int totalWalletValue() {
        int totalValue = 0;
        for (int i = 1; i < walletContents.length; i++) {
            totalValue =+ walletContents[i];
        }
        return totalValue;
    }

}
