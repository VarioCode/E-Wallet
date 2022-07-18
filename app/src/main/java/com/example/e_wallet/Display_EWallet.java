package com.example.e_wallet;

import android.Manifest.permission;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import com.example.e_wallet.data.DataStore;
import com.example.e_wallet.data.WalletData;
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

import java.util.ArrayList;

public class Display_EWallet extends AppCompatActivity {

    private ActivityDisplayEwalletBinding binding;
    private String qr_value;
    private TextView textView;
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private TabLayout tabs;
    private TabLayout.Tab tab1;
    private TabLayout.Tab tab2;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private boolean con = false;
    private WalletData selectedWallet = DataStore.getCurrentWallet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDisplayEwalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        linearLayout = findViewById(R.id.linear45);

//        tabs = findViewById(R.id.tabs);
////        tab1 = tabs.getTabAt(0);
//        tab1 = tabs.newTab();
//        tab1.setText("Balance");
//        tab2 = tabs.newTab();
//        tab2.setText("Transactions");
//        tabs.addTab(tab1);
//        tabs.addTab(tab2);
//        tabs.selectTab(tab1);
//
//        sectionsPagerAdapter = new SectionsPagerAdapter(tab1.parent.getContext(), getSupportFragmentManager());
//        viewPager = binding.viewPager;
//        viewPager.setAdapter(sectionsPagerAdapter);

//        tabs = binding.tabs;
//        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;
        FloatingActionButton fab6 = binding.fab6;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(Display_EWallet.this, new String[]{permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(Display_EWallet.this, permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            scanQR();
        } else {
            Snackbar.make(binding.getRoot(), "Camera permission is needed to scan QR code!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Snackbar.make(binding.getRoot(), "Cancelled", Snackbar.LENGTH_SHORT).show();
            } else {
//                Snackbar.make(binding.getRoot(), "Scanned: " + getQRvalue(result.getContents()), Snackbar.LENGTH_SHORT).show();
                qr_value = result.getContents();
                String qr = getQRvalue(qr_value);
//                Snackbar.make(binding.getRoot(), "Scanned: " + qr, Snackbar.LENGTH_SHORT).show();
                if (qr == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Display_EWallet.this);
                    builder.setTitle("Error");
                    builder.setMessage("Invalid QR code");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    qr = qr.replaceAll("\\r", "");
                    qr = qr.replaceAll("\\n", "");
                    qr = qr.replaceAll("\\t", "");
                    qr = qr.replaceAll(" ", "");
                    int value = Integer.parseInt(qr);
                    DataStore.getCurrentWallet().update(value);
                    buildWalletContents(value, DataStore.getCurrentWallet().getWalletContents()[value]);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String getQRvalue(String qr_value) {
        ArrayList<String> list = new ArrayList<>();
        for (String sa : qr_value.split("-")) {
            list.add(sa);
            System.out.println(sa);
        }
        if (list.size() == 2) {
            ArrayList<String> list2 = DataStore.getScanned();
            for (int i = 0; i < list2.size(); i++) {
//                Toast.makeText(this, list.get(0), Toast.LENGTH_SHORT).show();
                String sa = list2.get(i);
                if (sa.equals(list.get(0))) {
//                    Toast.makeText(this, "Oops", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
            DataStore.addScanned(list.get(0));
            return list.get(1);
        } else {
            return null;
        }
    }

    public void scanQR() {
        Toast.makeText(Display_EWallet.this, "This is a experimental feature!", Toast.LENGTH_SHORT).show();
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setTimeout(10000);
        integrator.initiateScan();
    }

    private void buildWalletContents(int value, int amount) {
        View view = getLayoutInflater().inflate(R.layout.money_view, null);
        view.setId(value);
        TextView walletNameTV = view.findViewById(R.id.TransactionType);
        walletNameTV.setText(String.valueOf(value + " Aqua Coin"));
        TextView walletAmountTV = view.findViewById(R.id.transactionDate);
        walletAmountTV.setText(String.valueOf(amount));
        linearLayout.addView(view);
        linearLayout.getViewTreeObserver();
    }

    private void generateTab() {

    }



}