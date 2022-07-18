package com.example.e_wallet.nav.gallery.slideshow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.e_wallet.R;
import com.example.e_wallet.data.Database;
import com.example.e_wallet.databinding.ActivityDisplayQrBinding;
import com.example.e_wallet.databinding.ActivityNavDrawerBinding;
import com.example.e_wallet.databinding.FragmentGalleryBinding;
import com.example.e_wallet.nav.gallery.NavDrawerActivity;
import com.google.zxing.WriterException;
import org.jetbrains.annotations.NotNull;

import static android.content.Context.WINDOW_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class DisplayQrFragment extends AppCompatActivity {
    private ActivityDisplayQrBinding binding;
    private ImageView qrImage;
    private Button closeButton;
    private ConstraintLayout constraintLayout;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    private static String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View root = binding.getRoot();
        Database db = new Database(getApplicationContext());
        String serialnr = db.getCoin(Integer.parseInt(message));

        String code = serialnr + "-" + message;
        constraintLayout = root.findViewById(R.id.displayQrConstraintLayout);
        qrImage = root.findViewById(R.id.idIVQrcode);
        closeButton = root.findViewById(R.id.CloseQRButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayQrFragment.this);
                builder.setTitle("Close QR Code");
                builder.setMessage("Did you transfer your money?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //adding to the DB etc.
                        db.removeWalletContents(serialnr);
                        Intent intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = Math.min(width, height);
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(code, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }

    }
    public static void setupQrMessage(String m) {
        message = m;

    }

    private void closeQr() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
