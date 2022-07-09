package com.example.e_wallet.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EWallet.db";

    private static final int moneyValues[] = new int[]{1, 2, 5, 10, 15, 20, 30, 50, 80, 100, 120};

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS walletData (id INTEGER PRIMARY KEY AUTOINCREMENT, balance INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS transactionData (id INTEGER PRIMARY KEY AUTOINCREMENT, amount INTEGER, type TEXT, date DATETIME)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS walletContents (id INTEGER PRIMARY KEY AUTOINCREMENT, serialnumber TEXT UNIQUE, value INTEGER)");
    }

    public void updateBalance(int balance) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE walletData SET balance = " + balance);
        db.close();
    }

    public void addTransaction(int amount, String type) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO transactionData (amount, type, date) VALUES (" + amount + ", '" + type + "', datetime('now'))");
        db.close();
    }

    public void addWalletContents(String serialnr, int v) throws Exception {

        if (checkIfExists(serialnr)) {
            throw new Exception("Serial number already exists");
        }


        ContentValues values = new ContentValues();
        values.put("serialnumber", serialnr);
        values.put("value", v);

        SQLiteDatabase db = getWritableDatabase();
        long insert = db.insert("walletContents", null, values);

        if (insert == -1) {
            throw new Exception("Failed to insert");
        }

        System.out.println("Inserted: " + insert);

        db.close();
    }

    public void removeWalletContents(String serialnr) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM walletContents WHERE serialnr = '" + serialnr + "'";
        db.execSQL(sql);
        db.close();
    }

    private boolean checkIfExists(String serialnr) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM walletContents WHERE serialnumber = '" + serialnr + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public ArrayList<String> getWalletContents() {
        System.out.println("Getting wallet contents");
        SQLiteDatabase db = getReadableDatabase();
//        db.execSQL("SELECT * FROM walletContents");
//        Cursor cursor = db.rawQuery("SELECT value, COUNT (*) AS amount FROM walletContents", null);
//        Cursor cursor = db.rawQuery("SELECT * FROM walletContents", null);
//        if (cursor.getCount() == 0) {
//            System.out.println("Wallet: No contents");
//        }
        Cursor cursor = db.query(
                "walletContents",

                new String[]{"value", "COUNT (*) AS amount"},
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<String> content = new ArrayList<>();
        while(cursor.moveToNext()) {
            System.out.println("Wallet: " + cursor.getString(0) + " " + cursor.getString(1));
            String value = cursor.getString(
                    cursor.getColumnIndexOrThrow("value")
            );
            String amount = String.valueOf(cursor.getString(
                    cursor.getColumnIndexOrThrow("amount")
            ));
            System.out.println("Get value result: " + value + " " + amount);
            content.add(value);
            content.add(amount);
        }
        cursor.close();
        db.close();

        return content;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop tables for debugging.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS walletData");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS transactionData");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS walletContents");
        onCreate(sqLiteDatabase);
    }
}

