package io.geven.pillbox;

import androidx.appcompat.app.AppCompatActivity;
import io.geven.pillbox.util.FirebaseAdapter;
import io.geven.pillbox.util.FirebaseBoxAdapter;
import io.geven.pillbox.util.FirebaseObserver;
import io.geven.pillbox.util.Globals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.android.material.snackbar.Snackbar;

public class ConnectActivity extends AppCompatActivity implements FirebaseObserver {

    private FirebaseBoxAdapter boxAdapter;

    private Button bConnect;
    private EditText eBoxID;
    private SharedPreferences sharedPref;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        boxAdapter = new FirebaseBoxAdapter();
        boxAdapter.attachObserver(this);

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);

        eBoxID = (EditText) findViewById(R.id.editText_box_id);

        bConnect = (Button) findViewById(R.id.button_connect);
        bConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = eBoxID.getText().toString();
                boxAdapter.setBoxID(id);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        String id = sharedPref.getString("box_id", ""); // default value is when box_id is empty

        if (id != "") {
            boxAdapter.setBoxID(id);
        }

    }

    @Override
    public void firebaseUpdate(FirebaseAdapter adapter, Object arg) {
        if (!(adapter instanceof FirebaseBoxAdapter)) {
            return;
        }

        if ((Boolean) arg) {
            sharedPref.edit().putString("box_id", ((Globals) getApplication()).getInstance().getBoxID()).apply();
            startActivity(new Intent(ConnectActivity.this, MainActivity.class));
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "ID cannot be found", Snackbar.LENGTH_SHORT).show();
        }

    }
}
