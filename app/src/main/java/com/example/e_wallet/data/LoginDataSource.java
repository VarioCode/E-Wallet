package com.example.e_wallet.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_wallet.qrscanner.model.LoggedInUser;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            if (username.equals("admin") && password.equals("password")) {
                LoggedInUser admin = new LoggedInUser("123456789", "admin","admin@example.com");

                return new Result.Success<>(admin);
            } else {
                throw new IOException("Incorrect login");
            }
//            LoggedInUser fakeUser =
//                    new LoggedInUser(
//                            java.util.UUID.randomUUID().toString(),
//                            "Jane Doe");
//            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));

        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}