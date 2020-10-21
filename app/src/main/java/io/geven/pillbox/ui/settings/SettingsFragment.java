package io.geven.pillbox.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import io.geven.pillbox.ConnectActivity;
import io.geven.pillbox.MainActivity;
import io.geven.pillbox.R;
import io.geven.pillbox.util.FirebaseItem;
import io.geven.pillbox.util.Globals;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    private GridLayout gridLayout;
    private ExtendedFloatingActionButton fabAddMedicine;
    private ExtendedFloatingActionButton fabChooseSong;
    private FloatingActionButton fabLogOut;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);


        gridLayout = root.findViewById(R.id.settings_medicines_list);

        settingsViewModel.getmMedicines().observe(this, new Observer<LinkedList<FirebaseItem<String>>>() {
            @Override
            public void onChanged(LinkedList<FirebaseItem<String>> medicines) {
                // delete all current views
                View addMedicineView = gridLayout.findViewById(R.id.fab_settings_add_medicine);
                gridLayout.removeAllViews();

                for (final FirebaseItem<String> medicine : medicines) {
                    // create view from layout
                    View medicineItem = LayoutInflater.from(getContext()).inflate(R.layout.settings_medicine_item, gridLayout, false);
                    final ExtendedFloatingActionButton eFAB = (ExtendedFloatingActionButton) medicineItem.findViewById(R.id.fab_settings_medicine_item);
                    eFAB.setText(medicine.item);

                    eFAB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            settingsViewModel.removeMedicine(medicine.getId());
                        }
                    });

                    gridLayout.addView(medicineItem);
                }

                gridLayout.addView(addMedicineView);



            }
        });

        fabAddMedicine = (ExtendedFloatingActionButton) root.findViewById(R.id.fab_settings_add_medicine);
        fabAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show a snackbar that can be used to enter the new medicine
                final Snackbar snackbar = Snackbar.make(v.getRootView(), "", Snackbar.LENGTH_INDEFINITE);
                snackbar.getView().setBackgroundColor(Color.parseColor("#D81B60"));
                snackbar.setTextColor(Color.BLACK);

                // Fetch the layout
                Snackbar.SnackbarLayout sLayout = (Snackbar.SnackbarLayout) snackbar.getView();

                // remove the default textView
                sLayout.removeAllViews();
                //textView.setVisibility(View.GONE);

                // Inflate custom view (have an edittext in this)
                View newView = LayoutInflater.from(getContext()).inflate(R.layout.snackbar_add_medicine, sLayout, false);
                Button bAddMedicine = (Button) newView.findViewById(R.id.snackbar_add_button);
                final EditText eMedicineText = (EditText) newView.findViewById(R.id.snackbar_edittext_text);
                bAddMedicine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String medicine = eMedicineText.getText().toString();
                        settingsViewModel.addMedicine(medicine);
                        snackbar.dismiss();
                    }
                });

                sLayout.addView(newView, 0);

                snackbar.show();
            }
        });

        fabLogOut = (FloatingActionButton) root.findViewById(R.id.fab_logout);
        fabLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete saved files
                SharedPreferences sharedPref = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
                sharedPref.edit().putString("box_id", "").apply();

                // go to connect activity
                startActivity(new Intent(getActivity(), ConnectActivity.class));
            }
        });

        fabChooseSong = (ExtendedFloatingActionButton) root.findViewById(R.id.button_choose_song);
        fabChooseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("audio/mpeg");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, 1);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == -1) {
            Uri file = data.getData();
            settingsViewModel.uploadFile(file);
            Log.d("FILLED", "COMPLETE");
        }
    }
}