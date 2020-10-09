package io.geven.pillbox.util;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class FirebaseBoxAdapter extends FirebaseAdapter {

    /**
     * Constructs a FirebaseBoxAdapter
     */
    public FirebaseBoxAdapter() {
        super();
    }

    public void setBoxID(final String id) {
        FirebaseDatabase.getInstance().getReference().child("boxes").orderByChild("id").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // id exist
                    Globals.getInstance().setBoxID(id);
                    String key = snapshot.getChildren().iterator().next().getRef().getKey();
                    Globals.getInstance().setBoxKey(key);
                    notifyObservers(FirebaseBoxAdapter.this, true);

                } else {
                    notifyObservers(FirebaseBoxAdapter.this, false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
