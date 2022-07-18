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

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 5);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS walletData (id INTEGER PRIMARY KEY AUTOINCREMENT, balance INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS transactionData (id INTEGER PRIMARY KEY AUTOINCREMENT, amount INTEGER, type TEXT, date DATETIME)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS walletContents (id INTEGER PRIMARY KEY AUTOINCREMENT, serialnumber TEXT UNIQUE, value INTEGER)");
        sqLiteDatabase.execSQL("INSERT INTO walletData (balance) VALUES (0)");
    }

    public int calcBalance() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(value) FROM walletContents", null);
        int balance = 0;
        if (cursor.moveToFirst()) {
            balance = cursor.getInt(0);
        }
        cursor.close();
        updateBalance(balance);
        return balance;
    }

    public int getBalance() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT balance FROM walletData", null);
        res.moveToFirst();
        try {
            int balance = res.getInt(0);
            res.close();
            return balance;
        } catch (Exception e) {
            return 0;
        }

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

    public List<TransactionData> getAllTransactions() {
        List<TransactionData> transactions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM transactionData ORDER BY date", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            transactions.add(new TransactionData(res.getInt(0), res.getInt(1), res.getString(2), res.getString(3)));
            res.moveToNext();
        }
        res.close();
        return transactions;
    }

    public TransactionData getLastTransaction() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM transactionData ORDER BY date DESC LIMIT 1", null);
        res.moveToFirst();
        TransactionData transaction = new TransactionData(res.getInt(0), res.getInt(1), res.getString(2), res.getString(3));
        res.close();
        return transaction;
    }

    public List<TransactionData> getRecentTransactions(int numTransactions) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM transactionData ORDER BY date DESC LIMIT " + numTransactions, null);
        List<TransactionData> transactions = new ArrayList<>();
        if (res.moveToFirst()) {
            do {
                transactions.add(new TransactionData(res.getInt(0), res.getInt(1), res.getString(2), res.getString(3)));
            } while (res.moveToNext());
        }
        res.close();
        return transactions;
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
        calcBalance();
        db.close();
    }

    public void removeWalletContents(String serialnr) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM walletContents WHERE serialnumber = '" + serialnr + "'";
        db.execSQL(sql);
        calcBalance();
        db.close();
    }

    public String getCoin(int value) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("SELECT serialnumber FROM walletContents WHERE value = " + value + " LIMIT 1", null);
        String coins = null;
        if (res.moveToFirst()) {
            do {
                coins = res.getString(0);
            } while (res.moveToNext());
        }
        res.close();
        return coins;
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
                "value",
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

