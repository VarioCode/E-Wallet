package com.example.e_wallet.ui.login;

import com.example.e_wallet.qrscanner.model.LoggedInUser;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private LoggedInUser user;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(LoggedInUser user) {
        this.user = user;
    }

    public LoggedInUser getUserData() {
        return user;
    }
}