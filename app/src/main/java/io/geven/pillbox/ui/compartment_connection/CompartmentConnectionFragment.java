package io.geven.pillbox.ui.compartment_connection;

import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import io.geven.pillbox.R;
import io.geven.pillbox.ui.add_medicine.AddMedicineViewModel;

public class CompartmentConnectionFragment extends Fragment {

    private CompartmentConnectionViewModel compartmentConnectionViewModel;

    public static CompartmentConnectionFragment newInstance() {
        return new CompartmentConnectionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        compartmentConnectionViewModel =
                ViewModelProviders.of(this).get(CompartmentConnectionViewModel.class);
        View root = inflater.inflate(R.layout.compartment_connection_fragment, container, false);

        // set compartment key
        compartmentConnectionViewModel.setcKey(getArguments().getString("compartment_key"));

        ProgressBar progressBar = root.findViewById(R.id.progress_loader);


        return root;
    }

}
