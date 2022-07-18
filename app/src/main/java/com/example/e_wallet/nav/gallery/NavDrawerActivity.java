package com.example.e_wallet.nav.gallery;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.displaymoney.DisplayMoney;
import com.example.e_wallet.R;
import com.example.e_wallet.data.Database;
import com.example.e_wallet.databinding.ActivityNavDrawerBinding;
import com.example.e_wallet.nav.gallery.slideshow.SlideshowFragment;
import com.example.e_wallet.nav.gallery.slideshow.SlideshowViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class NavDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavDrawerBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Database db;
    private ArrayList<String> results;
    private ConstraintLayout constraintLayout;
    private GalleryViewModel galleryViewModel;
    private SlideshowViewModel slideshowViewModel;
    private int usedButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View root = binding.getRoot();

        db = new Database(NavDrawerActivity.this);
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);

//        results = db.getWalletContents();

        sharedPreferences = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        Toolbar toolbar = binding.appBarNavDrawer.toolbar;
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setCollapseIcon(getResources().getDrawable(R.drawable.ic_menu_white_24dp));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_white_24dp));
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_baseline_more_vert_24));

        setSupportActionBar(toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);
        
        TextView username = (TextView) headerView.findViewById(R.id.UserNameDisplay);
        username.setText(sharedPreferences.getString("username", ""));

        TextView email = (TextView) headerView.findViewById(R.id.UserEmailDisplay);
        email.setText(sharedPreferences.getString("email", ""));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.content_nav_drawer_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.content_nav_drawer_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Toast.makeText(NavDrawerActivity.this, "getting results", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(NavDrawerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            scanQR();
        } else {
            Snackbar.make(binding.getRoot(), "Camera permission is needed to scan QR code!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Snackbar.make(binding.getRoot(), "Cancelled", Snackbar.LENGTH_SHORT).show();
            } else {
//                Snackbar.make(binding.getRoot(), "Scanned: " + result.getContents(), Snackbar.LENGTH_SHORT).show();
                String qr_value = result.getContents();
                setQRvalue(qr_value);
            }
        }
    }

    private void setQRvalue(@NotNull String qr_value) {
        GalleryFragment galfrag = galleryFragment();
//        GalleryFragment galfrag = null;
        ArrayList<String> list = new ArrayList<>();
        String transactionType = "";

        switch (usedButton) {
            case 1:
                transactionType = "Deposit";

            case 2:
                transactionType = "Received";
                break;
        }

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
                db.addTransaction(Integer.parseInt(list.get(1)), transactionType);
                if (usedButton == 2) {
                    SlideshowFragment f = slideshowViewModel.getFragment();
                    f.addToLayout(db.getLastTransaction());
                    throw new Exception("added to layout");
                }
                ArrayList<DisplayMoney> contents = galfrag.getContents();
                ArrayList<String> test = db.getWalletContents();
                System.out.println("test: " + test.size());
                for (DisplayMoney displayMoney : contents) {
                    for (int i = 0; i < test.size(); i = i + 2) {
                        System.out.println("displayMoney Value: " + displayMoney.getValue());
                        System.out.println("test Value: " + test.get(i + 1));
                        System.out.println("test n: " + test.get(i));
                        if (displayMoney.getValue() == Integer.parseInt(test.get(i))){
                            System.out.println("updating");

                            if (displayMoney.getValue() == Integer.parseInt(list.get(1))) {
                                displayMoney.addToAmount();
                                galfrag.setAmount(displayMoney.getValue(), displayMoney.getAmount());
                                Toast.makeText(this, "updated value", Toast.LENGTH_LONG).show();
                                updated = true;
                            }
                        }
                    }
                }

               if (!updated) {
                   System.out.println("adding");
                   DisplayMoney displayMoney = new DisplayMoney(list.get(1), galfrag);
                   displayMoney.setAmount("1");
                   galfrag.addFragment(displayMoney);
                   Toast.makeText(this, "added a new value", Toast.LENGTH_LONG).show();
               }

            } catch (Exception e) {
                switch (Objects.requireNonNull(e.getMessage())) {
                    case "added to layout":
                        break;
                    case "SQLiteException: PRIMARY KEY must be unique (code 19)":
                        Snackbar.make(binding.getRoot(), "This coin was already added", Snackbar.LENGTH_SHORT).show();
                        break;
                    case "SQLiteException: no such table: wallet_contents":
                        db.getWalletContents();
                        break;
                    default:
                        AlertDialog.Builder builder = new AlertDialog.Builder(NavDrawerActivity.this);
                        builder.setTitle("Error");
                        builder.setMessage(e.getMessage());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                        e.printStackTrace();
                        break;
                }
            }
        }
    }

    private GalleryFragment galleryFragment() {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.somethingtolookat);
        GalleryFragment fragment = galleryViewModel.getFragment().getValue();
        System.out.println("fragment: " + fragment);
        if (fragment != null) {
            System.out.println("gallery fragment is not null");
            return fragment;
        } else {
            if (usedButton == 2) {
                return null;
            }
            System.out.println("gallery fragment is null");
            throw new NullPointerException("GalleryFragment is null");
        }
    }

    private void scanQR() {
        Toast.makeText(getApplicationContext(), "This is a experimental feature!", Toast.LENGTH_SHORT).show();
        IntentIntegrator integrator = new IntentIntegrator(NavDrawerActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setTimeout(10000);
        integrator.initiateScan();
    }

    public void setUsedButton(int usedButton) {
        this.usedButton = usedButton;
    }
}