package io.geven.pillbox.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryCompartment implements Serializable {

    private String date;
    private String timeTaken;
    private List<String> medicines;

    public HistoryCompartment(String date, String timeTaken, List<String> medicines) {
        this.date = date;
        this.timeTaken = timeTaken;
        this.medicines = medicines;
    }

    public String getDate() {
        return date;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public List<String> getMedicines() {
        return medicines;
    }

    // TODO
    public String getDuration() {
        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return "0 minutes";

    }

}
