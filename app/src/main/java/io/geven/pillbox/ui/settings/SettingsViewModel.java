package io.geven.pillbox.ui.settings;

import java.util.LinkedList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.geven.pillbox.util.FirebaseAdapter;
import io.geven.pillbox.util.FirebaseItem;
import io.geven.pillbox.util.FirebaseMedicinesAdapter;
import io.geven.pillbox.util.FirebaseObserver;

public class SettingsViewModel extends ViewModel implements FirebaseObserver {

    private FirebaseMedicinesAdapter firebaseMedicinesAdapter;

    private MutableLiveData<LinkedList<FirebaseItem<String>>> mMedicines;

    public SettingsViewModel() {
        firebaseMedicinesAdapter = new FirebaseMedicinesAdapter();
        firebaseMedicinesAdapter.attachObserver(this);

        mMedicines = new MutableLiveData<>();
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

    public void removeMedicine(String key) {
        firebaseMedicinesAdapter.deleteMedicine(key);
    }

    public void addMedicine(String medicine) {
        firebaseMedicinesAdapter.addMedicine(medicine);
    }
}