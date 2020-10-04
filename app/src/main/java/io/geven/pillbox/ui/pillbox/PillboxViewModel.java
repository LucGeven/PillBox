package io.geven.pillbox.ui.pillbox;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.geven.pillbox.util.Compartment;
import io.geven.pillbox.util.FirebaseAdapter;
import io.geven.pillbox.util.FirebaseCompartmentsAdapter;
import io.geven.pillbox.util.FirebaseObserver;

public class PillboxViewModel extends ViewModel implements FirebaseObserver {


    private FirebaseCompartmentsAdapter firebaseCompartmentsAdapter;

    private MutableLiveData<ArrayList<Compartment>> compartments;


    public PillboxViewModel() {
        firebaseCompartmentsAdapter = new FirebaseCompartmentsAdapter();
        firebaseCompartmentsAdapter.attachObserver(this);

        compartments = new MutableLiveData<>();
    }

    @Override
    public void firebaseUpdate(FirebaseAdapter adapter, Object arg) {
        if (adapter instanceof FirebaseCompartmentsAdapter) {

            ArrayList<Compartment> ac = new ArrayList<>(((HashMap<String, Compartment>) arg).values());

            Iterator<Compartment> iter = ac.iterator();
            while (iter.hasNext()) {
                Compartment item = iter.next();
                if (!item.getState().equals("filled")) {
                    iter.remove();
                }
            }

            Collections.sort(ac, new Comparator<Compartment>() {
                @Override
                public int compare(Compartment o1, Compartment o2) {
                    try {
                        return new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(o1.getDate()).compareTo(
                                new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(o2.getDate())
                        );
                    } catch (ParseException e) {
                        return 0;
                    }
                }
            });

            compartments.setValue(ac);
        }
    }

    public boolean compartmentIsAvailable() {
        return firebaseCompartmentsAdapter.compartmentIsAvailable();
    }

    public MutableLiveData<ArrayList<Compartment>> getCompartments() {
        return compartments;
    }
}