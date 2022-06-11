package com.example.e_wallet.data;

import com.example.e_wallet.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            if (username.equals("admin") && password.equals("adminpassword")) {
                LoggedInUser admin = new LoggedInUser(java.util.UUID.randomUUID().toString(), "admin");
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
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}