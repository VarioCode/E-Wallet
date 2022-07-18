package com.example.e_wallet.data;

public class TransactionData {
    private int transactionID;
    private String transactionType;
    private int transactionAmount;
    private String transactionDate;
    public TransactionData(int transactionID, int transactionAmount, String transactionType, String transactionDate) {
        this.transactionID = transactionID;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public int getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }
}
