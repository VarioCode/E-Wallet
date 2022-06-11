package com.example.e_wallet.data;

public class UserData {
    private static String userName = "Jane Doe";

    public static void setUsername(String u) {
        u = userName;
        System.out.println("This is the current username: " + userName);
    }

    public static String getUserName() {
        System.out.println("This is the current username: " + userName);
        return userName;
    }
}
