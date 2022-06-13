package com.example.e_wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_wallet.data.UserData;
import com.example.e_wallet.ui.login.LoginActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.example.e_wallet.databinding.ActivityScrollingBinding;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class HomeScreenScroll extends AppCompatActivity {

    private ActivityScrollingBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;

        CardView cardView = (CardView) findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(HomeScreenScroll.this, "Test click", Toast.LENGTH_SHORT).show();
                finish();
                Intent OpenWallet = new Intent(HomeScreenScroll.this, Display_EWallet.class);
                startActivity(OpenWallet);
            }
        });

        toolBarLayout.setTitle("Dashboard");
        toolbar.setTitle("Dashboard");

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenScroll.this);
                builder.setTitle("What should be call it?");
                builder.setMessage("What is the name of your new wallet?");

                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint("Wallet name");

                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createWallet(linearLayout);
                        dialogInterface.dismiss();
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

    }

    private void logOut() {
        Intent startUp = new Intent(this, LoginActivity.class);
        startActivity(startUp);
        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();

    }

    private void createWallet(LinearLayout linearLayout) {
        Snackbar.make(linearLayout, "Feature currently under development", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
//        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
//        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
//        rv.setLayoutManager(llm);
//        linearLayout.addView(rv);
    }
}