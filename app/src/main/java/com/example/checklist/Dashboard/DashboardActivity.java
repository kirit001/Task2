package com.example.checklist.Dashboard;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checklist.Database.UserDatabase.UserDao;
import com.example.checklist.Database.UserDatabase.UserDatabase;
import com.example.checklist.Database.UserDatabase.UserEntity;
import com.example.checklist.GoogleMaps.GoogleMapsActivity;
import com.example.checklist.Login.LoginActivity;
import com.example.checklist.Notes.NotesListActivity;
import com.example.checklist.Profile.ProfileActivity;
import com.example.checklist.R;
import com.example.checklist.RestApi.Adapter;
import com.example.checklist.RestApi.Api;
import com.example.checklist.RestApi.Model;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    SharedPreferences sharedPreferences;
    ProgressDialog tempDialog = null;
    TextView username;
    private RecyclerView recyclerView;
    private Adapter adapter;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Actionbar and its title
        setTitle("DashBoard");

        username = findViewById(R.id.tv_username1);
        recyclerView = findViewById(R.id.recyclerView);
        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);

        UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
        UserDao userDao= userDatabase.userDao();
        Log.d(TAG, "onCreate: " + userDao.toString());
        new Thread(() -> {
            UserEntity userEntity = userDao.getall();
            if (userEntity == null){
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show());
            }
            else {
                String name = userEntity.getFirstname() + " " + userEntity.getLastname();
                username.setText(name);
            }
        }).start();

        showProgressDialog("Loading Data...");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);

        bottomNavigationView.setSelectedItemId(R.id.action_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_home:
                    return true;
                case R.id.action_profile:
                    Intent a = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(a);
                    overridePendingTransition(0, 0);
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

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager((DashboardActivity.this)));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<Model> call = api.getPhotos();

        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(@NotNull Call<Model> call, @NotNull Response<Model> response) {
                Log.d(TAG, "onResponse: " + new Gson().toJson(response.body()));

                assert response.body() != null;
                ArrayList<Model.Data> models = response.body().getData();

                adapter = new Adapter(DashboardActivity.this, models);
                recyclerView.setAdapter(adapter);
                dismissProgressDialog();
            }

            @Override
            public void onFailure(@NotNull Call<Model> call, @NotNull Throwable t) {
                Toast.makeText(DashboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }
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
                username.setText(name);
            }
        }).start();
        super.onStart();
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
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
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
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    void showProgressDialog(String displayMessage) {
        tempDialog = new ProgressDialog(this);
        tempDialog.setCancelable(true);
        tempDialog.setMessage(displayMessage);
        tempDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        tempDialog.show();
    }

    void dismissProgressDialog() {
        if (tempDialog != null) tempDialog.dismiss();
    }
}