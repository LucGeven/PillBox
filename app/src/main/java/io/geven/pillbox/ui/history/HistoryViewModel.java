package io.geven.pillbox.ui.history;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.geven.pillbox.util.Compartment;
import io.geven.pillbox.util.FirebaseAdapter;
import io.geven.pillbox.util.FirebaseHistoryAdapter;
import io.geven.pillbox.util.FirebaseObserver;
import io.geven.pillbox.util.HistoryCompartment;

public class HistoryViewModel extends ViewModel implements FirebaseObserver {

    private FirebaseHistoryAdapter firebaseHistoryAdapter;
    private MutableLiveData<ArrayList<HistoryCompartment>> historyCompartments;

    public HistoryViewModel() {
        firebaseHistoryAdapter = new FirebaseHistoryAdapter();
        firebaseHistoryAdapter.attachObserver(this);

        historyCompartments = new MutableLiveData<>();
    }

    @Override
    public void firebaseUpdate(FirebaseAdapter adapter, Object arg) {
        if (adapter instanceof FirebaseHistoryAdapter) {
            ArrayList<HistoryCompartment> ah = new ArrayList<>(((HashMap<String, HistoryCompartment>) arg).values());

            Collections.sort(ah, new Comparator<HistoryCompartment>() {
                @Override
                public int compare(HistoryCompartment o1, HistoryCompartment o2) {
                    try {
                        return new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(o2.getDate()).compareTo(
                                new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(o1.getDate())
                        );
                    } catch (ParseException e) {
                        return 0;
                    }
                }
            });

            historyCompartments.setValue(ah);

        }
    }

    public MutableLiveData<ArrayList<HistoryCompartment>> getHistoryCompartments() {
        return historyCompartments;
    }
}