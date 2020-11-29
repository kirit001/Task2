package com.example.checklist.SplashLogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.checklist.Dashboard.DashboardActivity;
import com.example.checklist.Database.UserDatabase.UserDao;
import com.example.checklist.Database.UserDatabase.UserDatabase;
import com.example.checklist.Database.UserDatabase.UserEntity;
import com.example.checklist.Login.LoginActivity;
import com.example.checklist.R;

public class SplashLoginActivity extends AppCompatActivity {

    ImageView image;

    protected boolean _active = true;
    protected int _splashTime = 1000; // time to display the splash screen in ms
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        //Actionbar and its title
        setTitle("CheckList");

        image = findViewById(R.id.Logo);
        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);

//        Picasso.with(this).load(R.drawable.app_logo)
//                .into(image);

        final Boolean islogged = sharedPreferences.getBoolean("islogged", false);

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (Exception e) {

                } finally {

                    if (islogged) {
                        UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                        UserDao userDao= userDatabase.userDao();
                        new Thread(() -> {
                            UserEntity userEntity = userDao.getall();
                            if (userEntity == null){
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show());
                            }
                            else {
                                String name = userEntity.getFirstname() + " " + userEntity.getLastname();
                                startActivity(new Intent(SplashLoginActivity.this,
                                        DashboardActivity.class).putExtra("name",name));
                            }
                        }).start();
                    } else {
                        startActivity(new Intent(SplashLoginActivity.this,
                                LoginActivity.class));
                    }
                }
            }
        };

        splashTread.start();
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
}