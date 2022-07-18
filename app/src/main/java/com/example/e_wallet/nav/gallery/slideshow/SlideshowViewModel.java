package com.example.e_wallet.nav.gallery.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<SlideshowFragment> mFragment;

    public SlideshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
        mFragment = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
    public SlideshowFragment getFragment() {
        return mFragment.getValue();
    }

    public void setmFragment(SlideshowFragment fragment) {
        mFragment.setValue(fragment);
    }
}