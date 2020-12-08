package com.example.checklist.Notes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.checklist.Dashboard.DashboardActivity;
import com.example.checklist.Database.NotesDatabase.NotesDao;
import com.example.checklist.Database.NotesDatabase.NotesDatabase;
import com.example.checklist.Database.NotesDatabase.NotesEntity;
import com.example.checklist.GoogleMaps.GoogleMapsActivity;
import com.example.checklist.Profile.ProfileActivity;
import com.example.checklist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddNotesActivity extends AppCompatActivity {

    EditText title,message;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        title = findViewById(R.id.Title);
        message = findViewById(R.id.Message);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_backarrow_layout);
        View view = getSupportActionBar().getCustomView();

        ImageButton imageButton = view.findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(v -> finish());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);

        bottomNavigationView.setSelectedItemId(R.id.action_List);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent a = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(a);
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.action_profile:
                    Intent b = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(b);
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.action_List:
                    Intent c = new Intent(getApplicationContext(), NotesListActivity.class);
                    startActivity(c);
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.action_maps:
                    Intent d = new Intent(getApplicationContext(), GoogleMapsActivity.class);
                    startActivity(d);
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), NotesListActivity.class));
        super.onBackPressed();
    }

    public void submit(View view) {

        NotesEntity notesEntity = new NotesEntity();
        notesEntity.setTitle(title.getText().toString());
        notesEntity.setNotes(message.getText().toString());

        NotesDatabase notesDatabase = NotesDatabase.getNotesDatabase(getApplicationContext());
        NotesDao notesDao = notesDatabase.notesDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                notesDao.Insertnotes(notesEntity);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddNotesActivity.this, "Notes Added", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
            }
        }).start();
        finish();
    }
}