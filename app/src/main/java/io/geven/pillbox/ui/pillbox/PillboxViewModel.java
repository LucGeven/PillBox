package io.geven.pillbox.ui.pillbox;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.geven.pillbox.util.FirebaseAdapter;
import io.geven.pillbox.util.FirebaseCompartmentsAdapter;
import io.geven.pillbox.util.FirebaseObserver;

public class PillboxViewModel extends ViewModel implements FirebaseObserver {


    private FirebaseCompartmentsAdapter firebaseCompartmentsAdapter;

    public PillboxViewModel() {
        firebaseCompartmentsAdapter = new FirebaseCompartmentsAdapter();
        firebaseCompartmentsAdapter.attachObserver(this);
    }

    @Override
    public void firebaseUpdate(FirebaseAdapter adapter, Object arg) {

    }

    public boolean compartmentIsAvailable() {
        return firebaseCompartmentsAdapter.compartmentIsAvailable();
    }

}