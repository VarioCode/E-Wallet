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
        System.out.println("current text element: " + mText.getValue());
        return mText;
    }

    public void setText(String text) {
        System.out.println("Set text to: " + text);
        mText.setValue(text);
        System.out.println("Text set too: " + mText.getValue());
    }

}
