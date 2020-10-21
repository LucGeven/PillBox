package io.geven.pillbox.ui.add_medicine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.geven.pillbox.util.FirebaseAdapter;
import io.geven.pillbox.util.FirebaseCompartmentsAdapter;
import io.geven.pillbox.util.FirebaseItem;
import io.geven.pillbox.util.FirebaseMedicinesAdapter;
import io.geven.pillbox.util.FirebaseObserver;

public class AddMedicineViewModel extends ViewModel implements FirebaseObserver {
    private MutableLiveData<String> mText;

    private MutableLiveData<LinkedList<FirebaseItem<String>>> mMedicines;

    private MutableLiveData<Date> lastDate;

    FirebaseMedicinesAdapter firebaseMedicinesAdapter;
    FirebaseCompartmentsAdapter firebaseCompartmentsAdapter;

    public AddMedicineViewModel() {

        mMedicines = new MutableLiveData<>();
        lastDate = new MutableLiveData<>();

        firebaseMedicinesAdapter = new FirebaseMedicinesAdapter();
        firebaseMedicinesAdapter.attachObserver(this);

        firebaseCompartmentsAdapter = new FirebaseCompartmentsAdapter();
        firebaseCompartmentsAdapter.attachObserver(this);
    }

    @Override
    public void firebaseUpdate(FirebaseAdapter adapter, Object arg) {
        if (adapter instanceof FirebaseMedicinesAdapter) {
            mMedicines.setValue((LinkedList<FirebaseItem<String>>) arg);
        }
        if (adapter instanceof FirebaseCompartmentsAdapter) {
            setMinDate();
        }
    }


    public MutableLiveData<LinkedList<FirebaseItem<String>>> getmMedicines() {
        return mMedicines;
    }

    public void setMinDate() {
        String s = firebaseCompartmentsAdapter.getLastDate();
        if (s == null) {
            lastDate.setValue(new Date());
            return;
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            lastDate.setValue(format.parse(s));
            return;
        } catch (ParseException ex) {
            ex.printStackTrace();
            lastDate.setValue(new Date());
            return;
        }
    }

    public String addMedicines(String date, List<String> medicines) {
        return firebaseCompartmentsAdapter.addMedicines(date, medicines);
    }

    public MutableLiveData<Date> getLastDate() {
        return lastDate;
    }
}
