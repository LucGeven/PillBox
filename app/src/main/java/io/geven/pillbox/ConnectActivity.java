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

                if (boxAdapter.setBoxID(eBoxID.getText().toString())) {
                    startActivity(new Intent(ConnectActivity.this, MainActivity.class));
                } else {
                    Snackbar.make(v, "ID cannot be found", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        String id = sharedPref.getString("box_id", ""); // default value is when box_id is empty

        if (boxAdapter.setBoxID(id)) {
            startActivity(new Intent(ConnectActivity.this, MainActivity.class));
        }



    }

    @Override
    public void firebaseUpdate(FirebaseAdapter adapter, Object arg) {

    }
}
