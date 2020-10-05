package io.geven.pillbox.ui.compartment_connection;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.geven.pillbox.util.Compartment;
import io.geven.pillbox.util.FirebaseAdapter;
import io.geven.pillbox.util.FirebaseObserver;
import io.geven.pillbox.util.FirebaseOneCompartmentAdapter;

public class CompartmentConnectionViewModel extends ViewModel implements FirebaseObserver {

    // add compartment key
    private MutableLiveData<String> cKey;
    private MutableLiveData<Boolean> isConnected;
    private FirebaseOneCompartmentAdapter firebaseOneCompartmentAdapter;

    public CompartmentConnectionViewModel() {
        cKey = new MutableLiveData<>();
        isConnected = new MutableLiveData<>();
        isConnected.setValue(false);

    }

    public MutableLiveData<String> getcKey() {
        return cKey;
    }

    public void setcKey(String cKey) {
        this.cKey.setValue(cKey);

        Log.d("FILLED", cKey);

        // this class is only an observer if a cKey is known
        firebaseOneCompartmentAdapter = new FirebaseOneCompartmentAdapter(this.cKey.getValue());
        firebaseOneCompartmentAdapter.attachObserver(this);
    }

    @Override
    public void firebaseUpdate(FirebaseAdapter adapter, Object arg) {
        if (adapter instanceof FirebaseOneCompartmentAdapter) {
            Compartment c = (Compartment) arg;
            // notify the ui if the compartment is changed to filled
            if (c.getState().equals("filled")) {
                Log.d("FILLED", "HAPPENS");
                isConnected.setValue(true);
            }
        }
    }

    public MutableLiveData<Boolean> isConnected() {
        return isConnected;
    }
}
