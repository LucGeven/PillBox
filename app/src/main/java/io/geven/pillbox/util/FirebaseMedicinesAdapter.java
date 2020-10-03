package io.geven.pillbox.util;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;

public class FirebaseMedicinesAdapter extends FirebaseAdapter {

    private List<FirebaseItem<String>> medicines;
    private DatabaseReference medicinesReference;

    public FirebaseMedicinesAdapter() {
        super();

        medicines = new LinkedList<>();
        medicinesReference = getBoxReference().child("medicines");

        syncMedicines();



    }

    private void syncMedicines() {

        // if data is changed of a chore
        medicinesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // data will not be overwritten, therefore we need to clean the medicines list
                medicines.clear();

                // for each medicine that exists in the medicines list
                for (DataSnapshot medicine : dataSnapshot.getChildren()) {

                    medicines.add(new FirebaseItem<String>(medicine.getKey(), medicine.getValue(String.class)));

                }

                notifyObservers(FirebaseMedicinesAdapter.this, medicines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }

    public void addMedicine(String medicine) {
        medicinesReference.push().setValue(medicine);
    }

    public void deleteMedicine(String id) {
        medicinesReference.child(id).removeValue();
    }
}
