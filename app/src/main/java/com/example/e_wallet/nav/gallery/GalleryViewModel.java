package com.example.e_wallet.nav.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.e_wallet.data.Database;

import java.util.ArrayList;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<GalleryFragment> mFragment;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mFragment = new MutableLiveData<>();
//        mText.setValue("This is gallery fragment");
    }

    public void setFragment(GalleryFragment fragment) {
        mFragment.setValue(fragment);
    }
    public LiveData<GalleryFragment> getFragment() {
        return mFragment;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String text) {
        mText.setValue(text);
    }
}