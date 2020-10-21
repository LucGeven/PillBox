package io.geven.pillbox.ui.pillbox;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import io.geven.pillbox.R;
import io.geven.pillbox.ui.add_medicine.AddMedicineFragment;
import io.geven.pillbox.util.Compartment;

public class PillboxFragment extends Fragment {

    private PillboxViewModel pillboxViewModel;

    private LinearLayout scrollLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pillboxViewModel =
                ViewModelProviders.of(this).get(PillboxViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pillbox, container, false);


        final FloatingActionButton fab = root.findViewById(R.id.fab_add_medicine);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if no compartment is available, then show a snackbar
                if (!pillboxViewModel.compartmentIsAvailable()) {
                    Snackbar snackbar = Snackbar.make(getView().getRootView(), "Refill is not allowed", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(Color.parseColor("#D81B60"));
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    // a compartment is available
                    // if you click on the floating action button, then show the fragment add medicine
                    Fragment fragment = new AddMedicineFragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, fragment);
                    transaction.commit();
                }
            }
        });

        scrollLayout = (LinearLayout) root.findViewById(R.id.scroll_pillbox);

        pillboxViewModel.getCompartments().observe(this, new Observer<ArrayList<Compartment>>() {
            @Override
            public void onChanged(ArrayList<Compartment> compartments) {
                scrollLayout.removeAllViews();

                for (Compartment compartment : compartments) {
                    View pillboxItem = LayoutInflater.from(getContext()).inflate(R.layout.pillbox_item, scrollLayout, false);

                    TextView date = (TextView) pillboxItem.findViewById(R.id.pillbox_date);
                    date.setText(compartment.getDate());

                    GridLayout g = (GridLayout) pillboxItem.findViewById(R.id.pillbox_medicines);

                    for (String medicine : compartment.getMedicines()) {
                        View medicineDesign = LayoutInflater.from(getContext()).inflate(R.layout.medicine_item, g, false);
                        final ExtendedFloatingActionButton eFAB = (ExtendedFloatingActionButton) medicineDesign.findViewById(R.id.fab_medicine_item);
                        eFAB.setText(medicine);
                        eFAB.setClickable(false);
                        eFAB.setBackgroundColor(Color.parseColor("#D81B60"));
                        g.addView(medicineDesign);
                    }


                    TextView compartmentID = (TextView) pillboxItem.findViewById(R.id.compartment_id);
                    compartmentID.setText(compartment.getId());

                    scrollLayout.addView(pillboxItem);
                }
            }
        });


        return root;
    }
}