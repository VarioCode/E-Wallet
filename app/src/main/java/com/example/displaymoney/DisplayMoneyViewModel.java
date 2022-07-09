package com.example.displaymoney;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DisplayMoneyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DisplayMoneyViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String text) {
        mText.setValue(text);
    }

}
