package com.example.checklist.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.checklist.Dashboard.DashboardActivity;
import com.example.checklist.Database.UserDatabase.UserDao;
import com.example.checklist.Database.UserDatabase.UserDatabase;
import com.example.checklist.Database.UserDatabase.UserEntity;
import com.example.checklist.GoogleMaps.GoogleMapsActivity;
import com.example.checklist.Login.LoginActivity;
import com.example.checklist.Notes.NotesListActivity;
import com.example.checklist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    ProgressDialog tempDialog = null;
    private TextView username,email1,address1;
    private ImageView profilepic;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Actionbar and its title
        setTitle("Profile");

        //init
        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
        username = findViewById(R.id.tv_username1);
        email1 = findViewById(R.id.tv_email);
        address1 = findViewById(R.id.tv_address);
        profilepic = findViewById(R.id.iv_profile);

        showProgressDialog("Loading Profile...");

        UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
        UserDao userDao = userDatabase.userDao();
        new Thread(() -> {
            UserEntity userEntity = userDao.getall();
                if (userEntity == null) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show());
                } else {
                    String name = userEntity.getFirstname() + " " + userEntity.getLastname();
                    String email = userEntity.getEmail();
                    String address = userEntity.getAddress();
                    username.setText(name);
                    email1.setText(email);
                    address1.setText(address);
                }
        }).start();

        dismissProgressDialog();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);

        bottomNavigationView.setSelectedItemId(R.id.action_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_home:
                    Intent a = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(a);
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.action_profile:
                    return true;
                case R.id.action_List:
                    Intent b = new Intent(getApplicationContext(), NotesListActivity.class);
                    startActivity(b);
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.action_maps:
                    Intent c = new Intent(getApplicationContext(), GoogleMapsActivity.class);
                    startActivity(c);
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
        UserDao userDao= userDatabase.userDao();
        new Thread(() -> {
            UserEntity userEntity = userDao.getall();
            if (userEntity == null){
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show());
            }
            else {
                String name = userEntity.getFirstname() + " " + userEntity.getLastname();
                String email = userEntity.getEmail();
                String address = userEntity.getAddress();
                username.setText(name);
                email1.setText(email);
                address1.setText(address);
            }
        }).start();
        super.onStart();
    }

    public void setTitle(String title){
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
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        super.onBackPressed();
    }

    void showProgressDialog(String displayMessage){
        tempDialog = new ProgressDialog(this);
        tempDialog.setCancelable(true);
        tempDialog.setMessage(displayMessage);
        tempDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        tempDialog.show();
    }

    void dismissProgressDialog(){
        if (tempDialog!=null) tempDialog.dismiss();
    }
}
