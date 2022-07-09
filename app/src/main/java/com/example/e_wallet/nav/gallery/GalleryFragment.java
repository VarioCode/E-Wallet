package com.example.e_wallet.nav.gallery;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.displaymoney.DisplayMoney;
import com.example.e_wallet.Display_EWallet;
import com.example.e_wallet.R;
import com.example.e_wallet.data.DataStore;
import com.example.e_wallet.data.Database;
import com.example.e_wallet.databinding.FragmentGalleryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GalleryFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private Database db;
    private ConstraintLayout constraintLayout;
    private ArrayList<DisplayMoney> contents = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        db = new Database(getActivity());
//        ArrayList<String> results = db.getWalletContents();

//        galleryViewModel =
//                new ViewModelProvider(this).get(GalleryViewModel.class);


        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        constraintLayout = root.findViewById(R.id.constraint_layoutGallery);

//        StringBuilder testString = new StringBuilder();
//        for (String result : results) {
//            testString.append(result);
////
//        }
//        for (int i = 0; i < results.size(); i++) {
//            System.out.println("size: " + results.size());
//            System.out.println(results.get(i));
//        }
//
//        for (int i = 0; i < results.size(); i = i + 2) {
//            if (results.get(i) == null || results.get(i + 1) == null) {
//                System.out.println("result is null...VALUE: " + results.get(i));
//                continue;
//            }
//            System.out.println(results.get(i));
//            System.out.println(results.get(i + 1));
//            DisplayMoney displayMoney = new DisplayMoney(results.get(i), this);
//            displayMoney.setAmount(results.get(i + 1));
////            constraintLayout.addView(displayMoney.getView());
////            contents.add(displayMoney);
//        }

//        galleryViewModel.setText(testString.toString());
//
//        TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        FloatingActionButton fab = binding.fabGallary;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Scanning QR code", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
//                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
            }
        });

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MyUSerPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        return root;
    }

    public void addToView(View view) {
        constraintLayout.addView(view);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Toast.makeText(getActivity(), "getting results", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            scanQR();
        } else {
            Snackbar.make(binding.getRoot(), "Camera permission is needed to scan QR code!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Snackbar.make(binding.getRoot(), "Cancelled", Snackbar.LENGTH_SHORT).show();
            } else {
                String qr_value = result.getContents();
                setQRvalue(qr_value);
            }
        }
    }

    private void setQRvalue(String qr_value) {
        ArrayList<String> list = new ArrayList<>();
        for (String sa : qr_value.split("-")) {
            list.add(sa);
            System.out.println(sa);
        }
        if (list.size() == 2) {
            for (int i = 0; i < 1; i++) {
                String sa = list.get(i);
                sa = sa.replaceAll("\\r", "");
                sa = sa.replaceAll("\\n", "");
                sa = sa.replaceAll("\\t", "");
                sa = sa.replaceAll(" ", "");
                list.set(i, sa);
            }
            try {
                boolean updated = false;
                db.addWalletContents(list.get(0), Integer.parseInt(list.get(1)));
                ArrayList<String> test = db.getWalletContents();
                for (int i = 0; i < test.size(); i = i + 2) {
                    for (DisplayMoney displayMoney : contents) {
                        if (displayMoney.getValue() == Integer.parseInt(test.get(i))) {
                            displayMoney.setAmount(test.get(i + 1));
                            updated = true;
                            break;
                        }
                    }
                    if (updated == false) {
//                        DisplayMoney displayMoney = new DisplayMoney(test.get(i), this);
//                        displayMoney.setAmount(test.get(i + 1));
//                        contents.add(displayMoney);
                    }
                }

            } catch (Exception e) {
                if (e.getMessage().equals("PRIMARY KEY must be unique (code 19)")) {
                    if (getView() == null) {
                        Toast.makeText(getActivity(), "QR code already exists", Toast.LENGTH_SHORT).show();
                    }
                    Snackbar.make(getView(), "This coin was already added", Snackbar.LENGTH_SHORT).show();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Error");
                builder.setMessage("Error: " + e.getMessage());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void scanQR() {
        Toast.makeText(getContext(), "This is a experimental feature!", Toast.LENGTH_SHORT).show();
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setTimeout(10000);
        integrator.initiateScan();
    }

    private void displayMoney(String value, String amount, boolean update) {
        if (update) {

            GalleryViewModel moneyValueView = new ViewModelProvider(this).get(GalleryViewModel.class);
            GalleryViewModel moneyAmountView = new ViewModelProvider(this).get(GalleryViewModel.class);


            View view = constraintLayout.getViewById(Integer.parseInt(value));
            TextView moneyValue = view.findViewById(R.id.moneyValue);
            TextView moneyAmount = view.findViewById(R.id.moneyAmount);

            moneyValueView.setText(amount);

            moneyValueView.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    moneyValue.setText(s);
                }
            });

            moneyAmountView.setText(amount);

            moneyAmountView.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    moneyAmount.setText(s);
                }
            });

        } else {
            View view = getLayoutInflater().inflate(R.layout.money_layout, null);
            view.setId(Integer.parseInt(value));
            TextView walletNameTV = view.findViewById(R.id.moneyValue);
            String moneyValue = value + " Aqua Coin";
            walletNameTV.setText(moneyValue);
            TextView walletAmountTV = view.findViewById(R.id.moneyAmount);
            walletAmountTV.setText(String.valueOf(amount));
            constraintLayout.addView(view);
        }

    }
}