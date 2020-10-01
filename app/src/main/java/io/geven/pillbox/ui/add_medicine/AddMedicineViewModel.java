package io.geven.pillbox.ui.add_medicine;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.geven.pillbox.util.FirebaseAdapter;
import io.geven.pillbox.util.FirebaseItem;
import io.geven.pillbox.util.FirebaseMedicinesAdapter;
import io.geven.pillbox.util.FirebaseObserver;

public class AddMedicineViewModel extends ViewModel implements FirebaseObserver {
    private MutableLiveData<String> mText;

    private MutableLiveData<LinkedList<FirebaseItem<String>>> mMedicines;

    FirebaseAdapter firebaseMedicinesAdapter;

    public AddMedicineViewModel() {

        mMedicines = new MutableLiveData<>();

        firebaseMedicinesAdapter = new FirebaseMedicinesAdapter();
        firebaseMedicinesAdapter.attachObserver(this);
    }

    @Override
    public void firebaseUpdate(FirebaseAdapter adapter, Object arg) {
        if (adapter instanceof FirebaseMedicinesAdapter) {
            mMedicines.setValue((LinkedList<FirebaseItem<String>>) arg);
        }
    }


    public MutableLiveData<LinkedList<FirebaseItem<String>>> getmMedicines() {
        return mMedicines;
    }
}
