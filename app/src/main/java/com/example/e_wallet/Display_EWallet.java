package com.example.e_wallet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.example.e_wallet.ui.main.SectionsPagerAdapter;
import com.example.e_wallet.databinding.ActivityDisplayEwalletBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.w3c.dom.Text;

public class Display_EWallet extends AppCompatActivity {

    private ActivityDisplayEwalletBinding binding;
    private String qr_value;
    private TextView textView;

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
//        TextView textView = binding.textView;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Display_EWallet.this, "This is a experimental feature!", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(Display_EWallet.this, new String[]{android.Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
                int perm = ContextCompat.checkSelfPermission(Display_EWallet.this, android.Manifest.permission.CAMERA);
                if (perm == PackageManager.PERMISSION_GRANTED) {
                    scanQR();
                } else if (perm == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(Display_EWallet.this, "Camera permission is required to scan QR code", Toast.LENGTH_LONG).show();
                }
                scanQR();
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

    private void scanQR() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setTimeout(10000);
        integrator.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                qr_value = result.getContents();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}