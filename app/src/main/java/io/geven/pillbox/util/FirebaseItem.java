package io.geven.pillbox.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FirebaseItem <T extends Serializable> implements Serializable {

    private String id;
    public T item;

    /**
     * Constructs a new FirebaseItem.
     *
     * @param id The id or name of the item
     * @param item the item itself
     */
    public FirebaseItem(String id, T item) {
        this.id = id;
        this.item = item;
    }

    /**
     * Returns the id of the FirebaseItem.
     *
     * @return a string representing the id of the FirebaseItem
     */
    public String getId() {
        return id;
    }
}
