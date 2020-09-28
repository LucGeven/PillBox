package io.geven.pillbox.ui.pillbox;

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
import io.geven.pillbox.ui.home.HomeFragment;

public class PillboxFragment extends Fragment {

    private PillboxViewModel pillboxViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pillboxViewModel =
                ViewModelProviders.of(this).get(PillboxViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pillbox, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        pillboxViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        final FloatingActionButton fab = root.findViewById(R.id.fab_add_medicine);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if you click on the floating action button, then show the fragment add medicine
                Fragment fragment = new AddMedicineFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment);
                transaction.commit();
            }
        });

        return root;
    }
}