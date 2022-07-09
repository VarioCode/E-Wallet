package com.example.e_wallet.data;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_wallet.qrscanner.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource extends AppCompatActivity {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            if (username.equals("admin") && password.equals("adminpassword")) {
                LoggedInUser admin = new LoggedInUser(java.util.UUID.randomUUID().toString(), "admin");

                DataStore.setUserName("admin");
                DataStore.setEmail("admin@admin.nl");

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
            return new Result.Error(new IOException("Error logging in", e));

        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}