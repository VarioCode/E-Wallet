package com.example.e_wallet.nav.gallery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.displaymoney.DisplayMoney;
import com.example.e_wallet.R;
import com.example.e_wallet.data.Database;
import com.example.e_wallet.databinding.FragmentGalleryBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    public static final String TAG = "GalleryFragment";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private Database db;
    private LinearLayout linearLayout;
    private ConstraintLayout constraintLayout;
    private ArrayList<DisplayMoney> contents = new ArrayList<>();
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        System.out.println("GalleryFragment: onCreateView");
        db = new Database(getActivity());
        ArrayList<String> results = db.getWalletContents();

        galleryViewModel = new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);
        galleryViewModel.setFragment(this);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        view = getLayoutInflater().inflate(R.layout.money_scroll, container, false);
        linearLayout = view.findViewById(R.id.linear45);
        constraintLayout = root.findViewById(R.id.constraint_layoutGallery);

        for (int i = 0; i < results.size(); i++) {
//            System.out.println("GalleryFragment: loop1");
//            System.out.println("size: " + results.size());
            System.out.println(results.get(i));
        }

        for (int i = 0; i < results.size(); i = i + 2) {
//            System.out.println("GalleryFragment: loop2");
            if (results.get(i) == null || results.get(i + 1) == null) {
//                System.out.println("result is null...VALUE: " + results.get(i));
                continue;
            }
            System.out.println(results.get(i));
            System.out.println(results.get(i + 1));

            DisplayMoney displayMoney = new DisplayMoney(results.get(i), this);
            displayMoney.setAmount(results.get(i + 1));
            System.out.println("Gallery Display: " + results.get(i) + " Amount " + results.get(i+1));
            if (displayMoney.getView() != null) {
                linearLayout.addView(displayMoney.getView());
                contents.add(displayMoney);
                continue;
            } else {
                System.out.println("view is null");
                throw new NullPointerException("view is null");
            }

        }
        System.out.println("GalleryFragment: adding fab");
        FloatingActionButton fab = binding.fabGallary;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Scanning QR code", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
//                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
            }
        });
        System.out.println("GalleryFragment: adding settings");
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MyUSerPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        constraintLayout.addView(view);

        System.out.println("GalleryFragment: End of onCreateView");
        return root;
    }

    public void addToView(View view) {
        constraintLayout.addView(view);
    }

    public void addFragment(DisplayMoney addedDisplay) {
        contents.add(addedDisplay);
        System.out.println("Added a framgent to the constraint layout.");
        linearLayout.addView(addedDisplay.getView());
    }

    public void setAmount(int value, int amount) {
        System.out.println("Set a new amount for " + value + " to " + amount);
        for (DisplayMoney displayMoney : contents) {
            if (displayMoney.getValue() == value) {
                displayMoney.setAmount(String.valueOf(amount));
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

    public ArrayList<DisplayMoney> getContents() {
        return contents;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }
}