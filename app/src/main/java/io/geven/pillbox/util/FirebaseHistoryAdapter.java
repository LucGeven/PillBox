package io.geven.pillbox.util;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;

public class FirebaseHistoryAdapter extends FirebaseAdapter {

    private DatabaseReference historyReference;
    private HashMap<String, HistoryCompartment> historyCompartments;

    public FirebaseHistoryAdapter() {
        super();

        historyReference = getBoxReference().child("history");
        historyCompartments = new HashMap<>();

        syncHistoryCompartments();



    }

    private void syncHistoryCompartments() {
        historyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // data will not be overwritten, therefore we need to clean the compartments list
                historyCompartments.clear();

                // for each history compartment that exists
                for (DataSnapshot compartment : snapshot.getChildren()) {

                    List<String> medicines = new LinkedList<>();

                    for (DataSnapshot medicine : compartment.child("medicines").getChildren()) {
                        medicines.add(medicine.getValue(String.class));
                    }

                    HistoryCompartment c = new HistoryCompartment(compartment.child("date").getValue(String.class),
                            compartment.child("time_taken").getValue(String.class),
                            medicines);

                    historyCompartments.put(compartment.getKey(), c);

                }

                notifyObservers(FirebaseHistoryAdapter.this, historyCompartments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
