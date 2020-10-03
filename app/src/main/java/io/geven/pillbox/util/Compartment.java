package io.geven.pillbox.util;

import java.io.Serializable;
import java.util.List;

public class Compartment implements Serializable {

    private String date;
    private String state;
    private List<String> medicines;

    public Compartment(String date, String state, List<String> medicines) {
        this.date = date;
        this.state = state;
        this.medicines = medicines;
    }

    public String getDate() {
        return date;
    }

    public String getState() {
        return state;
    }

    public List<String> getMedicines() {
        return medicines;
    }
}
