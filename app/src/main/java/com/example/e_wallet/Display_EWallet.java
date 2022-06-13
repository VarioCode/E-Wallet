package com.example.e_wallet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.e_wallet.qrscanner.QRScanner_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.e_wallet.ui.main.SectionsPagerAdapter;
import com.example.e_wallet.databinding.ActivityDisplayEwalletBinding;

public class Display_EWallet extends AppCompatActivity {

    private ActivityDisplayEwalletBinding binding;
    private static String qr_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDisplayEwalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;
        FloatingActionButton fab6 = binding.fab6;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This is a experimental feature!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                ActivityCompat.requestPermissions(Display_EWallet.this, new String[]{android.Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
                int perm = ContextCompat.checkSelfPermission(Display_EWallet.this, android.Manifest.permission.CAMERA);
                if (perm == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Display_EWallet.this, QRScanner_Activity.class);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Display_EWallet.this);
                    builder.setTitle("Permission Required");
                    builder.setMessage("Camera permission is required to use this feature. Please allow it in settings.");
                    builder.setPositiveButton("OK", null);
                }

            }
        });

        fab6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(Display_EWallet.this, HomeScreenScroll.class);
                startActivity(intent);
            }
        });
    }

    public static void setQrValue(String value) {
        qr_value = value;
        Snackbar.make(null, "QR Code: " + qr_value, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}