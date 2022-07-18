package com.example.e_wallet.nav.gallery.slideshow;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.displaymoney.DisplayMoney;
import com.example.e_wallet.R;
import com.example.e_wallet.data.TransactionData;

public class DisplayTransaction extends View {
    private int transactionID;
    private String transactionType;
    private int transactionAmount;
    private String transactionDate;
    private View view;
    private View mainView;
    private TextView amountTextView;
    private TextView typeTextView;
    private TextView dateTextView;

    public DisplayTransaction(Fragment fragment, TransactionData transactionData) {
        super(fragment.getContext());

        // Setting transaction values
        transactionID = transactionData.getTransactionID();
        transactionAmount = transactionData.getTransactionAmount();
        transactionType = transactionData.getTransactionType();
        transactionDate = transactionData.getTransactionDate();

        // settings views
        mainView = fragment.getLayoutInflater().inflate(R.layout.transaction_view, null);
        view = mainView.findViewById(R.id.TransactionCardView);
        view.setId(transactionID);
        amountTextView = view.findViewById(R.id.transactionAmount);
        amountTextView.setText(String.valueOf(transactionAmount));
        typeTextView = view.findViewById(R.id.TransactionType);
        typeTextView.setText(transactionType);
        dateTextView = view.findViewById(R.id.transactionDate);
        dateTextView.setText(transactionDate);

    }

    public View getView() {
        return mainView;
    }
}
