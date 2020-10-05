package io.geven.pillbox.util;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;

public class FirebaseOneCompartmentAdapter extends FirebaseAdapter{

    private DatabaseReference compartmentReference;

    private Compartment compartment;

    public FirebaseOneCompartmentAdapter(String compartmentKey) {
        super();

        compartmentReference = getBoxReference().child("compartments").child(compartmentKey);

        syncCompartment();
    }

    private void syncCompartment() {
        compartmentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state = snapshot.child("state").getValue(String.class);
                String date = snapshot.child("date").getValue(String.class);

                List<String> medicines = new LinkedList<>();

                for (DataSnapshot medicine : snapshot.child("medicines").getChildren()) {
                    medicines.add(medicine.getValue(String.class));
                }

                compartment = new Compartment(date, state, medicines);

                notifyObservers(FirebaseOneCompartmentAdapter.this, compartment);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
