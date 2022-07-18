package com.example.e_wallet.nav.gallery.slideshow;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.displaymoney.DisplayMoney;
import com.example.e_wallet.R;
import com.example.e_wallet.data.Database;
import com.example.e_wallet.data.TransactionData;
import com.example.e_wallet.databinding.FragmentSlideshowBinding;
import com.example.e_wallet.nav.gallery.NavDrawerActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private List<TransactionData> contents = new ArrayList<>();
    private View view;
    private ConstraintLayout constraintLayout;
    private TextView send_text, receive_text;
    private FloatingActionButton fab_send, fab_recieve;
    private ExtendedFloatingActionButton fab_start;
    private Boolean allFabsVisable;
    private LinearLayout linearLayout;
    private Database db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Setting up view
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        constraintLayout = root.findViewById(R.id.transactionFragmentConstraint);
        db = new Database(getActivity());
        db.getAllTransactions();
        slideshowViewModel = new ViewModelProvider(requireActivity()).get(SlideshowViewModel.class);
        slideshowViewModel.setmFragment(this);

        // Setting up view
        view = getLayoutInflater().inflate(R.layout.transaction_scroll, container, false);
        linearLayout = view.findViewById(R.id.linearTransactionLayout);
        fab_send = root.findViewById(R.id.send_fab);
        fab_recieve = root.findViewById(R.id.recieve_fab);
        fab_start = root.findViewById(R.id.fabStartTransaction);
        send_text = root.findViewById(R.id.send_fab_text);
        receive_text = root.findViewById(R.id.recieve_fab_text);

        // Setting up views visibility
        fab_send.setVisibility(View.GONE);
        fab_recieve.setVisibility(View.GONE);
        receive_text.setVisibility(View.GONE);
        send_text.setVisibility(View.GONE);

        for (int i = 0; i < db.getAllTransactions().size(); i++) {
            TransactionData transactionData = db.getAllTransactions().get(i);
            DisplayTransaction displayTransaction = new DisplayTransaction(this, transactionData);
            linearLayout.addView(displayTransaction.getView());
        }

        allFabsVisable = false;

        fab_start.shrink();
        fab_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!allFabsVisable){
                    fab_send.show();
                    fab_recieve.show();
                    receive_text.setVisibility(View.VISIBLE);
                    send_text.setVisibility(View.VISIBLE);
                    fab_start.extend();
                    allFabsVisable = true;
                } else {
                    fab_send.hide();
                    fab_recieve.hide();
                    receive_text.setVisibility(View.GONE);
                    send_text.setVisibility(View.GONE);
                    fab_start.shrink();
                    allFabsVisable = false;
                }
            }
        });

        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] selected = {""};

                Snackbar.make(constraintLayout, "Send money", Snackbar.LENGTH_LONG).show();
                NavDrawerActivity nav = (NavDrawerActivity) getActivity();
                ArrayList<String> walletcontents = db.getWalletContents();
                ArrayList<String> spinnercontents = new ArrayList<>();
                for (int i = 0; i < walletcontents.size(); i = i+2) {
                    spinnercontents.add(walletcontents.get(i));
                }
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spinnercontents.remove(0);
                    }
                });
                Spinner spinner = (Spinner) mView.findViewById(R.id.dialog_spinner_element);
                spinner.setPrompt("Select a coin");
                spinnercontents.add(0, "Select a coin");

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnercontents);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            selected[0] = spinnercontents.get(position);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(nav);
                builder.setTitle("Send money");
                builder.setMessage("Enter the amount you want to send");
                builder.setView(mView);
                builder.setAdapter(spinnerAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        spinnercontents.remove(0);

                    }
                });

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (spinnercontents.get(0) != selected[0]) {
                            dialog.dismiss();
                            DisplayQrFragment.setupQrMessage(selected[0]);
                            Intent intent = new Intent(getActivity(), DisplayQrFragment.class);
                            startActivity(intent);
                        } else {
                            Snackbar.make(constraintLayout, "Please select a coin, to continue", Snackbar.LENGTH_LONG).show();
                            dialog.dismiss();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        fab_recieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(constraintLayout, "Received money", Snackbar.LENGTH_LONG).show();
                NavDrawerActivity nav = (NavDrawerActivity) getActivity();
                nav.setUsedButton(2);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            }
        });

        constraintLayout.addView(view);
        return root;
    }

    public void addToLayout(TransactionData transactionData) {
        DisplayTransaction displayTransaction = new DisplayTransaction(this, transactionData);
        linearLayout.addView(displayTransaction.getView());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}