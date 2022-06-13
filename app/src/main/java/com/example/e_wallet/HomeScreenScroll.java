package com.example.e_wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
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

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;

        toolBarLayout.setTitle("Dashboard");
        toolbar.setTitle("Dashboard");

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "This function is under development", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenScroll.this);
                builder.setTitle("Requesting new account");
                builder.setMessage("This function is under development");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Snackbar.make(view, "Requested a new account", Snackbar.LENGTH_LONG)
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
}