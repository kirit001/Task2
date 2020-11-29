package com.example.checklist.Notes;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.checklist.Dashboard.DashboardActivity;
import com.example.checklist.Database.NotesDatabase.NotesDao;
import com.example.checklist.Database.NotesDatabase.NotesDatabase;
import com.example.checklist.Database.NotesDatabase.NotesEntity;
import com.example.checklist.GoogleMaps.GoogleMapsActivity;
import com.example.checklist.Login.LoginActivity;
import com.example.checklist.Profile.ProfileActivity;
import com.example.checklist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotesEditActivity extends AppCompatActivity {

    EditText Title, Message;
    NotesEntity notesEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_edit);

        //Actionbar and its title
        setTitle("Edit Notes");

        Title = findViewById(R.id.EditTitle);
        Message = findViewById(R.id.EditMessage);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);

        bottomNavigationView.setSelectedItemId(R.id.action_List);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
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
                    return true;
                case R.id.action_maps:
                    Intent c = new Intent(getApplicationContext(), GoogleMapsActivity.class);
                    startActivity(c);
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });

        NotesDatabase notesDatabase = NotesDatabase.getNotesDatabase(getApplicationContext());
        NotesDao notesDao = notesDatabase.notesDao();

        new Thread(() -> {
            try {
                notesEntity = notesDao.getinfobyid(NotesEditActivity.this.getIntent().getLongExtra("id",
                        0L));
            }
            finally {
                String title = notesEntity.getTitle();
                String message = notesEntity.getNotes();

                Title.setText(title);
                Message.setText(message);

            }


        }).start();
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
        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        super.onBackPressed();
    }

    /*inflate option menu*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*handel menu item clicks*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}