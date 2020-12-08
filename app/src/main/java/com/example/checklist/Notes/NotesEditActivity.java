package com.example.checklist.Notes;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.checklist.Dashboard.DashboardActivity;
import com.example.checklist.Database.NotesDatabase.NotesDao;
import com.example.checklist.Database.NotesDatabase.NotesDatabase;
import com.example.checklist.GoogleMaps.GoogleMapsActivity;
import com.example.checklist.Profile.ProfileActivity;
import com.example.checklist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotesEditActivity extends AppCompatActivity {

    EditText Title, Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_edit);

        //Actionbar and its title
        setTitle("Edit Notes");

        String title = NotesEditActivity.this.getIntent().getStringExtra("title");
        String notes = NotesEditActivity.this.getIntent().getStringExtra("notes");

        Title = findViewById(R.id.EditTitle);
        Message = findViewById(R.id.EditMessage);

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

        Title.setText(title);
        Message.setText(notes);
    }

    public void setTitle(String title) {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), NotesListActivity.class));
        super.onBackPressed();
    }

    public void save(View view) {
        Integer id = NotesEditActivity.this.getIntent().getIntExtra("id", 0);

        String title =Title.getText().toString();
        String notes =Message.getText().toString();

        NotesDatabase notesDatabase = NotesDatabase.getNotesDatabase(getApplicationContext());
        NotesDao notesdao = notesDatabase.notesDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                notesdao.editinfo(id,title,notes);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Note has been updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), NotesListActivity.class));
                    }
                });
            }
        }).start();
    }

    public void delete(View view) {
        Integer id = NotesEditActivity.this.getIntent().getIntExtra("id", 0);
        NotesDatabase notesDatabase = NotesDatabase.getNotesDatabase(getApplicationContext());
        NotesDao notesdao = notesDatabase.notesDao();

        new Thread(new Runnable() {
            @Override
            public void run() {
                notesdao.deleteById(id);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Note has been deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), NotesListActivity.class));
                    }
                });
            }
        }).start();
    }
}