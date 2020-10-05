package io.geven.pillbox.ui.compartment_connection;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.geven.pillbox.R;
import io.geven.pillbox.ui.add_medicine.AddMedicineViewModel;
import io.geven.pillbox.ui.pillbox.PillboxFragment;

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

        final ProgressBar progressBar = root.findViewById(R.id.progress_loader);

        final ImageView image = root.findViewById(R.id.check_image);
        final FloatingActionButton fab = root.findViewById(R.id.fab_connection_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PillboxFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment);
                transaction.commit();
            }
        });

        compartmentConnectionViewModel.isConnected().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean b) {
                if (b) {
                    progressBar.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });


        return root;
    }

}
