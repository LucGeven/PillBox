package io.geven.pillbox.util;

import android.app.Application;

public class Globals extends Application {

    private String boxID = "";
    private static Globals globals;

    /**
     * Singleton creates a new instance of Globals if it hasn't yet been created,
     * returns an instance of Globals if one already exists.
     *
     * @return instance of Globals
     */
    public static Globals getInstance() {
        if (globals == null) {
            globals = new Globals();
        }
        return globals;
    }

    /**
     * Set the current box ID to a new box ID
     *
     * @param boxID the Id that the current box ID must be changed to
     * @return this instance of Globals
     */
    public Globals setBoxID(String boxID) {
        this.boxID = boxID;

        return this;
    }

    /**
     * Return the current box ID
     *
     * @return a string representing the ID of the current box
     */
    public String getBoxID() {
        return this.boxID;
    }

}
