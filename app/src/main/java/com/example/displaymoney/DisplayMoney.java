package com.example.displaymoney;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.e_wallet.R;
import com.example.e_wallet.databinding.FragmentHomeBinding;
import com.example.e_wallet.nav.gallery.NavDrawerActivity;

public class DisplayMoney extends Fragment {
    private View view;
    private int value;
    private TextView walletNameTV;
    private TextView walletAmountTV;
    private DisplayMoneyViewModel displayMoneyViewModel;

    public DisplayMoney(String value, Fragment fragment) {

        View view = fragment.getLayoutInflater().inflate(R.layout.money_layout, null);
        view.setId(Integer.parseInt(value));
        this.value = Integer.parseInt(value);
        displayMoneyViewModel = new ViewModelProvider(fragment).get(DisplayMoneyViewModel.class);

        walletNameTV = view.findViewById(R.id.moneyValue);
        String moneyValue = value + " Aqua Coin";
        walletNameTV.setText(moneyValue);

        walletAmountTV = view.findViewById(R.id.moneyAmount);
        displayMoneyViewModel.getText().observe(fragment.getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                walletAmountTV.setText(s);
            }
        });

    }

    public void setAmount(String amount) {
        displayMoneyViewModel.setText(amount);
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
        return view;
    }
}
