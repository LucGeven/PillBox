package io.geven.pillbox.ui.pillbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import io.geven.pillbox.R;

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
        return root;
    }
}