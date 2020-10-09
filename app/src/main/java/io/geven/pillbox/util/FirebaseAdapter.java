package io.geven.pillbox.util;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public abstract class FirebaseAdapter {

    private DatabaseReference boxReference;

    // declare list that keeps track of all observers who needs to be updated if something changes
    private List<FirebaseObserver> observers;


    /**
     * Constructs a FirebaseAdapter and an ArrayList of FirebaseObservers with a reference to the box
     */
    public FirebaseAdapter() {

        observers = new ArrayList<>();
    }

    /**
     * Getter method to get the box reference
     * @return the box reference
     */
    protected DatabaseReference getBoxReference() {
        updateBoxReference();
        return boxReference;
    }

    /**
     * Attach observer to the observer list
     * @param observer the observer that needs to be added
     */
    public void attachObserver(FirebaseObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove observer from the observer list
     * @param observer the observer that needs to be removed
     */
    public void detachObserver(FirebaseObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies each observer that is in the observer list
     * @param adapter the adapter that runs this method
     * @param arg the data that needs to be transfered to the observer
     */
    public void notifyObservers(FirebaseAdapter adapter, Object arg) {
        for (FirebaseObserver observer : observers) {
            observer.firebaseUpdate(adapter, arg);
        }
    }

    public void updateBoxReference() {
        // if a box ID is known, then store the database reference that stores the data of the box in boxReference
        if (Globals.getInstance().getBoxID() != null && Globals.getInstance().getBoxKey() != null) {
            boxReference = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("boxes")
                    .child(Globals.getInstance().getBoxKey());
        }
    }

}
