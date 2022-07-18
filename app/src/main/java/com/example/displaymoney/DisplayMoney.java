package com.example.displaymoney;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.e_wallet.R;

public class DisplayMoney extends View {

    public static final String TAG = "DisplayMoney.TAG";

    private View view;
    private View mainView;
    private int value;
    private View root;
    private TextView walletNameTV;
    private TextView walletAmountTV;
    private ImageView walletImage;
    private DisplayMoneyViewModel displayMoneyViewModel;

    public DisplayMoney(String value, Fragment fragment) {
        super(fragment.getContext());
        mainView = fragment.getLayoutInflater().inflate(R.layout.money_view, null);
        System.out.println("DisplayMoney: " + value);
        System.out.println("DisplayMoney: " + fragment);
        view = mainView.findViewById(R.id.moneyCardView);
//        view = view.getRootView();
        view.setId(Integer.parseInt(value));
        this.value = Integer.parseInt(value);
        // initialize views
        walletNameTV = view.findViewById(R.id.TransactionType);
        walletAmountTV = view.findViewById(R.id.transactionDate);
        walletImage = view.findViewById(R.id.moneyImage);

        // setting views
        walletNameTV.setText(value);

        int image = getMipmap(value);
        if (image == -1) {
            walletImage.setImageResource(R.drawable.ic_launcher_background);
            System.out.println("DisplayMoney: image not found");
        } else {
            walletImage.setImageResource(image);
        }

        System.out.println(getInfo());
    }

    public String getInfo() {
        String info = "DisplayMoney: " + value
                + " View ID:" + view.getId()
                + " View root: " + view.getRootView()
                + " Wallet Value: " + walletNameTV.getText()
                + " Wallet Amount: " + walletAmountTV.getText()
                + " Wallet Image: " + walletImage.getDrawable();
        return info;
    }
    public String toString() {
        return "DisplayMoney: " + value;
    }
    public void setAmount(String amount) {
        walletAmountTV.setText(amount);
        //        displayMoneyViewModel.setText(amount);
//        System.out.println("Set amount for " + value + " to " + amount);
    }


    public void addToAmount() {
        String amount = walletAmountTV.getText().toString();
        int newAmount = Integer.parseInt(amount) + 1;
        setAmount(String.valueOf(newAmount));
    }

    public int getValue() {
        return value;
    }

    public View getView() {
        return mainView;
    }


    private int getMipmap(String value) {
        if (value.equals("15")) {
            return R.mipmap.money15;
        } else if (value.equals("20")) {
            return R.mipmap.money20;
        } else if (value.equals("30")) {
            return R.mipmap.money30;
        } else if (value.equals("50")) {
            return R.mipmap.money50;
        } else if (value.equals("80")) {
            return R.mipmap.money80;
        } else if (value.equals("100")) {
            return R.mipmap.money100;
        } else if (value.equals("120")) {
            return R.mipmap.money120;
        }
        return -1;
    }

    public int getAmount() {
        return Integer.parseInt(walletAmountTV.getText().toString());
    }
}
