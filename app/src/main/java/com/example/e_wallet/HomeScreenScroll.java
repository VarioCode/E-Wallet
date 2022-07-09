package com.example.e_wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import com.example.e_wallet.data.DataStore;
import com.example.e_wallet.data.WalletData;
import com.example.e_wallet.ui.login.LoginActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.example.e_wallet.databinding.ActivityScrollingBinding;

import java.util.ArrayList;

public class HomeScreenScroll extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    private LinearLayout layout;
    private String user_name;
    private ArrayList<WalletData> walletList = DataStore.getWallets();
//    private DataStore dataStore = ((DataStore)getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_name = DataStore.getUserName();

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NestedScrollView scrollView = findViewById(R.id.nestedScrollView);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;

        layout = findViewById(R.id.linearLayout);
//        toolBarLayout.setTitle("Dashboard");
        toolBarLayout.setTitle("Welcome " + user_name);
        scrollView.setOnScrollChangeListener(new CollapsingToolbarLayout.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // Scrolling down
                    System.out.println("Scrolling down");
                    toolBarLayout.setTitle("Dashboard");
                    System.out.println(toolBarLayout.getTitle());
                } else {
                    // Scrolling up
                    System.out.println("Scrolling up");
                }

                if (scrollY == 0) {
                    toolBarLayout.setTitle("Welcome " + user_name);
                }
            }
        });

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenScroll.this);
                builder.setTitle("What should we call it?");
                builder.setMessage("What is the name of your new wallet?");

                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint("Wallet name");

                builder.setView(input);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        createWallet(layout, (String) input.getText().toString());
                        Snackbar.make(view, "Added new wallet", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

        FloatingActionButton fab2 = binding.fab2;
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
                finish();
            }
        });

        if (walletList.size() > 0) {
            buildWalletList(layout, walletList);
        }

    }

    private void logOut() {
        Intent startUp = new Intent(this, LoginActivity.class);
        startActivity(startUp);
        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();

    }

    public void createWallet(LinearLayout linearLayout, String walletName) {
        Snackbar.make(binding.getRoot(), "Experiemental feature", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        WalletData walletData = new WalletData(walletName);

        DataStore.addWallet(walletData);

        View view = getLayoutInflater().inflate(R.layout.wallet, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                Intent OpenWallet = new Intent(HomeScreenScroll.this, Display_EWallet.class);
                DataStore.setCurrentWallet(walletData);
                startActivity(OpenWallet);

            }
        });
        System.out.println(layout);

        TextView textView = view.findViewById(R.id.walletName);
        System.out.println(textView);
        System.out.println(walletName);
        textView.setText(walletName);

        Button delete = view.findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeScreenScroll.this, "Deleted wallet: " + walletName, Toast.LENGTH_SHORT).show();
                DataStore.removeWallet(walletData);
                layout.removeView(view);
            }
        });

        layout.addView(view);
    }

    private void buildWalletList(LinearLayout linearLayout, ArrayList<WalletData> walletList) {
        for (WalletData walletData : walletList) {

            View view = getLayoutInflater().inflate(R.layout.wallet, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                finish();
                    Intent OpenWallet = new Intent(HomeScreenScroll.this, Display_EWallet.class);
                    DataStore.setCurrentWallet(walletData);
                    startActivity(OpenWallet);

                }
            });

            TextView textView = view.findViewById(R.id.walletName);
            textView.setText(walletData.getwalletName());

            Button delete = view.findViewById(R.id.deleteButton);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HomeScreenScroll.this, "Deleted wallet: " + walletData.getwalletName(), Toast.LENGTH_SHORT).show();
                    DataStore.removeWallet(walletData);
                    layout.removeView(view);
                }
            });

            layout.addView(view);
        }

    }

}