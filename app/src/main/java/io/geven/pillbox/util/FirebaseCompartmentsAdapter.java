package io.geven.pillbox.util;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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

                    Compartment c = new Compartment(
                            compartment.child("id").getValue(String.class),
                            compartment.child("date").getValue(String.class),
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

        ArrayList<String> ac = new ArrayList<>(compartments.keySet());
        Collections.sort(ac, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return compartments.get(o1).getId().compareTo(compartments.get(o2).getId());
            }
        });

        for (String key : ac) {
            if (compartments.get(key).getState().equals("empty")) {
                // create a new compartment to replace this compartment
                Compartment c = new Compartment(compartments.get(key).getId(), date, "action", medicines);

                compartmentsReference.child(key).setValue(c);

                return key;
            }
        }

        return null;
    }


    public boolean addCompartmentAllowed() {
        ArrayList<Compartment> ac = new ArrayList<>(((HashMap<String, Compartment>) compartments).values());
        Collections.sort(ac, new Comparator<Compartment>() {
            @Override
            public int compare(Compartment o1, Compartment o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        for (int i = 0; i < ac.size(); i++) {
            if (ac.get(i).getState().equals("empty")) {
                for (int j = i + 1; j < ac.size(); j++) {
                    if (ac.get(j).getState().equals("filled")) {
                        return false;
                    }
                }
            }
        }

        // check whether all compartments are filled
        for (Compartment c : ac) {
            if (!c.getState().equals("filled")) {
                return true;
            }
        }

        return false;
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

    public String getLastDate() {

        ArrayList<Compartment> ac = new ArrayList<>(((HashMap<String, Compartment>) compartments).values());
        Collections.sort(ac, new Comparator<Compartment>() {
            @Override
            public int compare(Compartment o1, Compartment o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });

        for (Compartment c : ac) {
            if (c.getState().equals("filled")) {
                return c.getDate();
            }
        }

        return null;
    }


}
