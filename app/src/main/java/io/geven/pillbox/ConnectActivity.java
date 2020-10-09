package io.geven.pillbox;

import androidx.appcompat.app.AppCompatActivity;
import io.geven.pillbox.util.FirebaseAdapter;
import io.geven.pillbox.util.FirebaseBoxAdapter;
import io.geven.pillbox.util.FirebaseObserver;
import io.geven.pillbox.util.Globals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ConnectActivity extends AppCompatActivity implements FirebaseObserver {

    private FirebaseBoxAdapter boxAdapter;

    private FloatingActionButton bConnect;
    private EditText eBoxID;
    private SharedPreferences sharedPref;

    private String prefID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize a firebase box adapter
        // it will notify this activity if the given box id is correct
        boxAdapter = new FirebaseBoxAdapter();
        boxAdapter.attachObserver(this);

        // get the shared preferences
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // check if the stored id is still correct
        prefID = sharedPref.getString("box_id", ""); // default value will be used if box_id is empty

        boxAdapter.setBoxID(prefID);

    }

    @Override
    public void firebaseUpdate(FirebaseAdapter adapter, Object arg) {
        if (!(adapter instanceof FirebaseBoxAdapter)) {
            return;
        }

        // if box id is correct
        if ((Boolean) arg) {
            // store the given id, so that next time you don't have to enter the id again
            sharedPref.edit().putString("box_id", ((Globals) getApplication()).getInstance().getBoxID()).apply();

            // detach observer, because this observer is not necessary anymore
            boxAdapter.detachObserver(this);

            // go to the main activity
            startActivity(new Intent(ConnectActivity.this, MainActivity.class));
        } else {
            // no correct is is found, hence show the connect activity
            setContentView(R.layout.activity_connect);

            eBoxID = (EditText) findViewById(R.id.editText_box_id);

            bConnect = (FloatingActionButton) findViewById(R.id.button_connect);
            bConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prefID = eBoxID.getText().toString();
                    boxAdapter.setBoxID(prefID);

                }
            });
            if (prefID.equals("")) {
                // if you start the app, but your saved id is not correct, then delete that saved id
                // also the snackbar won't appear
                prefID = "";
                sharedPref.edit().putString("box_id", "").apply();
            } else {
                // show a snackbar to notify the user that the id cannot be found
                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "ID cannot be found", Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(Color.parseColor("#D81B60"));
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        }

    }
}
