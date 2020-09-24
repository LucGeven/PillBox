package io.geven.pillbox.util;

/**
 * Interface for a FirebaseObserver object
 */
public interface FirebaseObserver {

    void firebaseUpdate(FirebaseAdapter adapter, Object arg);

}
