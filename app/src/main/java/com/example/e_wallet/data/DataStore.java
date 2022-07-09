package com.example.e_wallet.data;

import android.app.Application;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DataStore extends AppCompatActivity {
    private static String userName;
    private static String email;

    public DataStore(String userName) {
        this.userName = userName;

    }

    public static void setEmail(String email) {
        DataStore.email = email;
    }

    public static String getEmail() {
        return email;
    }

    public static void setUserName(String u) {
        userName = u;
        System.out.println("Current user: " + userName);
    }

    public static String getUserName() {
        return userName;
    }

    // Wallet data
    private static ArrayList<WalletData> wallets = new ArrayList<>();
    private static WalletData currentWallet = null;
    private static ArrayList<String> scanned = new ArrayList<>();

    public static ArrayList<WalletData> getWallets() {
        return wallets;
    }

    public static void addWallet(WalletData w) {
        wallets.add(w);
    }

    public static int getWalletIndex(WalletData w) {
        return wallets.indexOf(w);
    }

    public static void removeWallet(WalletData w) {
        wallets.remove(w);
        for (int i = 0; i < wallets.size(); i++) {
            System.out.println(wallets.get(i).getwalletName());
        }
    }

    public static void setCurrentWallet(WalletData w) {
        currentWallet = w;
    };

    public static WalletData getCurrentWallet() {
        return currentWallet;
    }

    public static void addScanned(String s) {
        scanned.add(s);
    }

    public static ArrayList<String> getScanned() {
        return scanned;
    }


    private void saveData() {
//        FileOutputStream outputStream = getApplicationContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
//        outputStream.write(homeScoreBytes);
//        outputStream.close();
    }


}
