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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.displaymoney.DisplayMoney;
import com.example.e_wallet.R;
import com.example.e_wallet.data.Database;
import com.example.e_wallet.databinding.ActivityNavDrawerBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class NavDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavDrawerBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Database db;
    ArrayList<String> results;
    private ArrayList<DisplayMoney> contents = new ArrayList<>();
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View root = binding.getRoot();

        db = new Database(NavDrawerActivity.this);


//        results = db.getWalletContents();

//        constraintLayout = findViewById(R.id.constraint_layoutGallery);
//
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
//            DisplayMoney displayMoney = new DisplayMoney(results.get(i), Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_gallery)));
//            displayMoney.setAmount(results.get(i + 1));
//            constraintLayout.addView(displayMoney.getView());
//            contents.add(displayMoney);
//        }


        sharedPreferences = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        setSupportActionBar(binding.appBarNavDrawer.toolbar);

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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_drawer);
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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav_drawer);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.nav_home) {
                    binding.navView.getMenu().findItem(R.id.nav_home).setChecked(true);
                } else if (destination.getId() == R.id.nav_gallery) {
                    binding.navView.getMenu().findItem(R.id.nav_gallery).setChecked(true);
                    setupGallery();
                } else if (destination.getId() == R.id.nav_slideshow) {
                    binding.navView.getMenu().findItem(R.id.nav_slideshow).setChecked(true);
                }
            }
        });
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setupGallery() {
        results = db.getWalletContents();

        GalleryFragment galleryFragment = null;
        System.out.println("HI THERE IM HERE");
        if (getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_gallery) == null) {
            System.out.println("ENTERED IF");
            Fragment fragment = new GalleryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_gallery, fragment).commit();
            for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
                System.out.println(fragmentManager.getFragments().get(i));
                if (fragmentManager.getFragments().get(i) != null) {
                    System.out.println("fragment is not null");
                    galleryFragment = (GalleryFragment) fragmentManager.getFragments().get(i);
                    System.out.println(fragmentManager.getFragments().get(i).getClass().getName());
                } else {
                    System.out.println("fragment is null");
                }
            }
        }
        System.out.println("HI THERE IM HERE 2");
        if (galleryFragment == null) {
            System.out.println("ENTERED IF 2");
            AlertDialog.Builder builder = new AlertDialog.Builder(NavDrawerActivity.this);
            builder.setTitle("Error");
            builder.setMessage("Gallery fragment is null");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {

            constraintLayout = findViewById(R.id.constraint_layoutGallery);

            System.out.println("ENTERED ELSE");

            for (int i = 0; i < results.size(); i++) {
                System.out.println("size: " + results.size());
                System.out.println(results.get(i));
            }

            for (int i = 0; i < results.size(); i = i + 2) {
                System.out.println("Adding stuff");
                if (results.get(i) == null || results.get(i + 1) == null) {
                    System.out.println("result is null...VALUE: " + results.get(i));
                    continue;
                }
                System.out.println(results.get(i));
                System.out.println(results.get(i + 1));
                DisplayMoney displayMoney = new DisplayMoney(results.get(i), galleryFragment);
                displayMoney.setAmount(results.get(i + 1));

//                constraintLayout.addView(displayMoney.getView());
                galleryFragment.addToView(displayMoney.getView());
                contents.add(displayMoney);
            }

        }

        System.out.println("HI THERE IM HERE 3");

    }

    // Methods
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
                Snackbar.make(binding.getRoot(), "Scanned: " + result.getContents(), Snackbar.LENGTH_SHORT).show();
                String qr_value = result.getContents();
                setQRvalue(qr_value);
            }
        }
    }

    private void setQRvalue(@NotNull String qr_value) {
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
                        DisplayMoney displayMoney = new DisplayMoney(results.get(i), (GalleryFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_gallery)));
                        displayMoney.setAmount(test.get(i + 1));
                        contents.add(displayMoney);
                        constraintLayout.addView(displayMoney.getView());
                    }
                }
            } catch (Exception e) {
                if (e.getMessage().equals("PRIMARY KEY must be unique (code 19)")) {
                    Snackbar.make(binding.getRoot(), "This coin was already added", Snackbar.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NavDrawerActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Something went wrong");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    e.printStackTrace();
                }
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
            }
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

}