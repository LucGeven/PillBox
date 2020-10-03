package io.geven.pillbox.ui.add_medicine;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.geven.pillbox.R;
import io.geven.pillbox.ui.compartment_connection.CompartmentConnectionFragment;
import io.geven.pillbox.ui.pillbox.PillboxFragment;
import io.geven.pillbox.ui.pillbox.PillboxViewModel;
import io.geven.pillbox.util.FirebaseItem;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class AddMedicineFragment extends Fragment {

    private AddMedicineViewModel addMedicineViewModel;

    private EditText eDefineDate;
    private EditText eDefineTime;
    private final Calendar calendar = Calendar.getInstance();

    private GridLayout gridLayout;

    private HashMap<String, String> selectedMedicines;

    public static AddMedicineFragment newInstance() {
        return new AddMedicineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        addMedicineViewModel =
                ViewModelProviders.of(this).get(AddMedicineViewModel.class);
        View root = inflater.inflate(R.layout.add_medicine_fragment, container, false);


        FloatingActionButton fabRemove = root.findViewById(R.id.fab_delete_medicine);
        FloatingActionButton fabSave = root.findViewById(R.id.fab_push_medicine);
        fabRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PillboxFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment);
                transaction.commit();
            }
        });

        eDefineDate = (EditText) root.findViewById(R.id.editText_define_date);
        eDefineTime = (EditText) root.findViewById(R.id.editText_define_time);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // first check if some medicine is selected, and if not then show a snackbar
                if (!(selectedMedicines.size() > 0)) {
                    Snackbar snackbar = Snackbar.make(getView().getRootView(), "No medicines are selected", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(Color.parseColor("#D81B60"));
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }

                // check if a date is filled in, and if not then show a snackbar
                else if (!(eDefineDate.getText().toString().length() > 0)) {
                    Snackbar snackbar = Snackbar.make(getView().getRootView(), "No date is defined", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(Color.parseColor("#D81B60"));
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }

                // check if a time is filled in, and if not then show a snackbar
                else if (!(eDefineTime.getText().toString().length() > 0)) {
                    Snackbar snackbar = Snackbar.make(getView().getRootView(), "No time is defined", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(Color.parseColor("#D81B60"));
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    // everything is filled in
                    String d = eDefineDate.getText().toString() + " " + eDefineTime.getText().toString();

                    // get compartment key, null if no compartment is available
                    String cKey = addMedicineViewModel.addMedicines(d, new ArrayList<String>(selectedMedicines.values()));
                    if (cKey == null) {
                        // no compartment is available
                        Snackbar snackbar = Snackbar.make(getView().getRootView(), "No compartment is available", Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(Color.parseColor("#D81B60"));
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    } else {
                        Fragment fragment = new CompartmentConnectionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("compartment_key", cKey);
                        fragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, fragment);
                        transaction.commit();

                    }
                }
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        eDefineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateTimeLabel();
            }
        };

        eDefineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(v.getContext(), time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        selectedMedicines = new HashMap<>();

        gridLayout = (GridLayout) root.findViewById(R.id.medicines_list);

        addMedicineViewModel.getmMedicines().observe(this, new Observer<LinkedList<FirebaseItem<String>>>() {
            @Override
            public void onChanged(LinkedList<FirebaseItem<String>> medicines) {

                // delete all current views
                gridLayout.removeAllViews();

                for (final FirebaseItem<String> medicine : medicines) {
                    // create view from layout
                    View medicineItem = LayoutInflater.from(getContext()).inflate(R.layout.medicine_item, gridLayout, false);
                    final ExtendedFloatingActionButton eFAB = (ExtendedFloatingActionButton) medicineItem.findViewById(R.id.fab_medicine_item);
                    eFAB.setText(medicine.item);

                    eFAB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (selectedMedicines.containsKey(medicine.getId())) {
                                // deselect button
                                eFAB.setBackgroundColor(Color.GRAY);
                                selectedMedicines.remove(medicine.getId());
                            } else {
                                // select button
                                eFAB.setBackgroundColor(Color.parseColor("#D81B60"));
                                selectedMedicines.put(medicine.getId(), medicine.item);
                            }
                        }
                    });

                    gridLayout.addView(medicineItem);
                }

                // add other button
                // create view from layout
                View otherItem = LayoutInflater.from(getContext()).inflate(R.layout.medicine_item, gridLayout, false);
                final ExtendedFloatingActionButton eFAB = (ExtendedFloatingActionButton) otherItem.findViewById(R.id.fab_medicine_item);
                eFAB.setText("Other");

                eFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedMedicines.containsKey("Other")) {
                            // deselect button
                            eFAB.setBackgroundColor(Color.GRAY);
                            selectedMedicines.remove("Other");
                        } else {
                            // select button
                            eFAB.setBackgroundColor(Color.parseColor("#D81B60"));
                            selectedMedicines.put("Other", "Other");
                        }
                    }
                });

                gridLayout.addView(otherItem);

            }
        });



        return root;
    }

    private void updateDateLabel() {
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        eDefineDate.setText(sdf.format(calendar.getTime()));
    }

    private void updateTimeLabel() {
        eDefineTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
    }

}
