package com.example.e_wallet.nav.gallery.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.e_wallet.R;
import com.example.e_wallet.data.Database;
import com.example.e_wallet.data.TransactionData;
import com.example.e_wallet.databinding.FragmentHomeBinding;
import com.example.e_wallet.nav.gallery.slideshow.DisplayTransaction;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView acc_name, acc_number, balance_text;
    private LinearLayout transaction_history_linearLayout;
    private Database db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = new Database(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        // Setting views
        acc_name = root.findViewById(R.id.balance_nametext_textview);
        acc_number = root.findViewById(R.id.acc_number_text_textview);
        balance_text = root.findViewById(R.id.balance_text_textview);
        transaction_history_linearLayout = root.findViewById(R.id.recent_transactions_linearlayout);

        // Giving views values
        acc_name.setText(sharedPreferences.getString("username", ""));
        acc_number.setText(sharedPreferences.getString("id", ""));
        balance_text.setText(String.valueOf(db.getBalance()));

        List<TransactionData> recenttransactions = db.getRecentTransactions(3);
        for (int i = 0; i < recenttransactions.size(); i++) {
            TransactionData transactionData = recenttransactions.get(i);
            DisplayTransaction displayTransaction = new DisplayTransaction(this, transactionData);
            transaction_history_linearLayout.addView(displayTransaction.getView());
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}