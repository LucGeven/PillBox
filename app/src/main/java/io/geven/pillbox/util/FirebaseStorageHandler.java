package io.geven.pillbox.util;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseStorageHandler {

    private StorageReference storageRef;


    public FirebaseStorageHandler() {
        storageRef = FirebaseStorage.getInstance().getReference().child(Globals.getInstance().getBoxID());
    }

    public void uploadFile(Uri file) {
        storageRef.putFile(file);
    }

}
