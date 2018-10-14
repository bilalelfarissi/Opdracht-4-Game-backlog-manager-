package com.example.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGameActivity extends AppCompatActivity {

    /**
     * Binding
     */
    @BindView(R.id.addTitelEditText)
    EditText mAddTitelEditText;
    @BindView(R.id.addPlatformEditText)
    EditText mAddPlatformEditText;
    @BindView(R.id.addNotesEditText)
    EditText mAddNotesEditText;
    @BindView(R.id.statusSpinner)
    Spinner mStatusSpinner;

    /**
     * Create adapter
     */
    private ArrayAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerItems,
                 R.layout.support_simple_spinner_dropdown_item);
        mStatusSpinner.setAdapter(spinnerAdapter);
        Intent intent = getIntent();
        checkIfUpdate(intent);
    }

    /**
     * navigate back to home screen when the user is finished
     */
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    /**
     * This method is used to check if there is an update. IF there is an update then the values
     * will be set again.
     */


    public void checkIfUpdate(Intent intent){
        if(intent.getAction().equals(MainActivity.ACTION_UPDATE)){
            Game game = intent.getParcelableExtra(MainActivity.EXTRA_GAME);
            mAddTitelEditText.setText(game.getTitel());
            mAddPlatformEditText.setText(game.getPlatform());
            mAddNotesEditText.setText(game.getNotes());
            int statusPosition = spinnerAdapter.getPosition(game.getStatus());
            mStatusSpinner.setSelection(statusPosition);
        }
    }

    /**
     * This method is used when the user wants to add or update a "game". In this method there is an
     * if and else statement that is used to detect wheter an user filled everything or not. Besides
     * that it checks if it is an update or insert.
     */
    @OnClick(R.id.saveFloatingButton)
    public void addGame(View view) {
        String titel = mAddTitelEditText.getText().toString();
        String platform = mAddPlatformEditText.getText().toString();
        String notes = mAddNotesEditText.getText().toString();
        String status = mStatusSpinner.getSelectedItem().toString();

        if (!TextUtils.isEmpty(titel) && !TextUtils.isEmpty(platform) && !TextUtils.isEmpty(status)) {
            Intent resultIntent = new Intent();
            resultIntent.setAction(getIntent().getAction());
            Game game;
            if(getIntent().getAction().equals(MainActivity.ACTION_UPDATE)){
                game = getIntent().getParcelableExtra(MainActivity.EXTRA_GAME);
                game.setTitel(mAddTitelEditText.getText().toString());
                game.setPlatform(mAddPlatformEditText.getText().toString());
                game.setNotes(mAddNotesEditText.getText().toString());
                game.setStatus(mStatusSpinner.getSelectedItem().toString());
            } else {
                game = new Game(titel, platform, notes, status);
            }
            resultIntent.putExtra(MainActivity.EXTRA_GAME, game);
            setResult(Activity.RESULT_OK, resultIntent);
        } else {
            Snackbar.make(view, "Please enter some data first!", Snackbar.LENGTH_LONG);
        }
        finish();
    }

}
