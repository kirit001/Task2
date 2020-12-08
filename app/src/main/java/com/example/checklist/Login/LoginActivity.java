package com.example.checklist.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.checklist.Dashboard.DashboardActivity;
import com.example.checklist.Database.UserDatabase.UserDao;
import com.example.checklist.Database.UserDatabase.UserDatabase;
import com.example.checklist.Database.UserDatabase.UserEntity;
import com.example.checklist.PasswordReset.PasswordResetActivity;
import com.example.checklist.R;
import com.example.checklist.Registration.RegisterActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    //declaring shared preference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //declaring views,checkbox
    EditText username, password;
    CheckBox checkbox;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Actionbar and its title
        setTitle("Login");

        //init
        username = findViewById(R.id.tv_username);
        password = findViewById(R.id.password);
        checkbox = findViewById(R.id.checkBox);
        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //creating sharedpreference
        boolean savelogin = sharedPreferences.getBoolean("savelogin", true);

        //storing email and password in shared preference when checkbox is checked
        if (savelogin) {
            username.setText(sharedPreferences.getString("email", null));
        }
    }

    //on click handler for login button
    public void login(View view) {
        String Username = username.getText().toString();
        String Password = password.getText().toString();
        //checking if email and password is entered or not
        if (!Patterns.EMAIL_ADDRESS.matcher(Username).matches()) {
            username.setError("Invalid Email");
            username.setFocusable(true);
        } else if (Username.isEmpty()) {
            password.setError("Email is empty");
            password.setFocusable(true);
        } else if (Password.isEmpty()) {
            password.setError("Password is empty");
            password.setFocusable(true);
        } else {
            signIn(Username, Password);
        }
    }

    public void signIn(String Username, String Password) {

        if (checkbox.isChecked()) {
            //save username and password when remember me is ticked
            editor.clear();
            editor.putBoolean("savelogin", true);
            editor.putString("email", Username);
            editor.putBoolean("islogged", true);
            editor.commit();

            UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
            UserDao userDao = userDatabase.userDao();
            new Thread(() -> {
                UserEntity userEntity = userDao.login(Username, Password);
                if (userEntity == null) {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show());
                } else {
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                }
            }).start();
        } else {
            editor.clear();
            editor.putBoolean("savelogin", false);
            editor.putString("email", Username);
            editor.commit();

            UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
            UserDao userDao = userDatabase.userDao();
            new Thread(() -> {
                UserEntity userEntity = userDao.login(Username, Password);
                if (userEntity == null) {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show());
                } else {
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                }
            }).start();
        }
    }

    //back pressed handler
    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    public void setTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
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

    public void forgotpassword(View view) {
        startActivity(new Intent(getApplicationContext(), PasswordResetActivity.class));
    }

    public void register(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }
}