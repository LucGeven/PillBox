package io.geven.pillbox.ui.pillbox;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import io.geven.pillbox.R;
import io.geven.pillbox.ui.add_medicine.AddMedicineFragment;

public class PillboxFragment extends Fragment {

    private PillboxViewModel pillboxViewModel;

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
                    Snackbar snackbar = Snackbar.make(getView().getRootView(), "No compartment is available", Snackbar.LENGTH_SHORT);
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

        return root;
    }
}