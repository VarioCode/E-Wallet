package com.example.e_wallet.qrscanner;

import android.content.DialogInterface;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.e_wallet.Display_EWallet;
import com.example.e_wallet.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRScanner_Activity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        textView = findViewById(R.id.textView);
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null){
            if (intentResult.getContents() == null){
                AlertDialog.Builder builder = new AlertDialog.Builder(QRScanner_Activity.this);
                builder.setTitle("Something went wrong");
                builder.setMessage("Something went wrong while scanning. Please try again.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(QRScanner_Activity.this, Display_EWallet.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
            }else {
//                textView.setText(intentResult.getContents());
                Display_EWallet.setQrValue(intentResult.getContents());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}