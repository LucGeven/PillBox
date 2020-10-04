package io.geven.pillbox.util;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class FirebaseCompartmentsAdapter extends FirebaseAdapter {

    private DatabaseReference compartmentsReference;
    private HashMap<String, Compartment> compartments;

    public FirebaseCompartmentsAdapter() {
        super();

        compartmentsReference = getBoxReference().child("compartments");
        compartments = new HashMap<>();

        syncCompartments();
    }

    private void syncCompartments() {
        compartmentsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // data will not be overwritten, therefore we need to clean the compartments list
                compartments.clear();

                // for each compartment that exists
                for (DataSnapshot compartment : snapshot.getChildren()) {

                    List<String> medicines = new LinkedList<>();

                    for (DataSnapshot medicine : compartment.child("medicines").getChildren()) {
                        medicines.add(medicine.getValue(String.class));
                    }

                    Compartment c = new Compartment(compartment.child("date").getValue(String.class),
                            compartment.child("state").getValue(String.class),
                            medicines);

                    compartments.put(compartment.getKey(), c);
                }

                notifyObservers(FirebaseCompartmentsAdapter.this, compartments);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * Add medicines if there is a compartment that is empty
     * @param date the date and time the medicines need to be taken
     * @param medicines the medicines that need to be taken
     * @return unique key if there exist a compertment that is empty, and null otherwise
     */
    public String addMedicines(String date, List<String> medicines) {
        for (Map.Entry<String, Compartment> entry : compartments.entrySet()) {
            Compartment compartment = entry.getValue();
            String key = entry.getKey();

            // an empty compartment exists!
            if (compartment.getState().equals("empty")) {

                // create a new compartment to replace this compartment
                Compartment c = new Compartment(date, "action", medicines);

                compartmentsReference.child(key).setValue(c);

                return key;
            }

        }

        return null;
    }


    public boolean compartmentIsAvailable() {
        for (Map.Entry<String, Compartment> entry : compartments.entrySet()) {
            Compartment compartment = entry.getValue();

            // an empty compartment exists!
            if (compartment.getState().equals("empty")) {
                return true;
            }
        }
        return false;
    }


}
