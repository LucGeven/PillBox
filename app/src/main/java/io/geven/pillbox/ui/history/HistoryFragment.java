package io.geven.pillbox.ui.history;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import io.geven.pillbox.R;
import io.geven.pillbox.util.HistoryCompartment;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private LinearLayout scrollLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        scrollLayout = (LinearLayout) root.findViewById(R.id.scroll_history);

        historyViewModel.getHistoryCompartments().observe(this, new Observer<ArrayList<HistoryCompartment>>() {
            @Override
            public void onChanged(ArrayList<HistoryCompartment> historyCompartments) {
                scrollLayout.removeAllViews();

                for (HistoryCompartment compartment : historyCompartments) {
                    View pillboxItem = LayoutInflater.from(getContext()).inflate(R.layout.history_item, scrollLayout, false);

                    TextView date = (TextView) pillboxItem.findViewById(R.id.history_pillbox_date);
                    date.setText(compartment.getDate());

                    TextView duration = (TextView) pillboxItem.findViewById(R.id.history_duration);
                    duration.setText(compartment.getDuration());

                    GridLayout g = (GridLayout) pillboxItem.findViewById(R.id.history_pillbox_medicines);

                    for (String medicine : compartment.getMedicines()) {
                        View medicineDesign = LayoutInflater.from(getContext()).inflate(R.layout.medicine_item, g, false);
                        final ExtendedFloatingActionButton eFAB = (ExtendedFloatingActionButton) medicineDesign.findViewById(R.id.fab_medicine_item);
                        eFAB.setText(medicine);
                        eFAB.setClickable(false);
                        eFAB.setBackgroundColor(Color.parseColor("#D81B60"));
                        g.addView(medicineDesign);
                    }

                    scrollLayout.addView(pillboxItem);
                }
            }
        });


        return root;
    }
}