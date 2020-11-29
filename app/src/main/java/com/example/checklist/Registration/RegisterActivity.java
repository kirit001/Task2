package com.example.checklist.Registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.checklist.Database.UserDatabase.UserDao;
import com.example.checklist.Database.UserDatabase.UserDatabase;
import com.example.checklist.Database.UserDatabase.UserEntity;
import com.example.checklist.Login.LoginActivity;
import com.example.checklist.R;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button register;
    private EditText Firstname, Lastname, Email, Password, ConfirmPassword, Address;
    private CheckBox chbox;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Actionbar and its title
        setTitle("Register");

        Firstname = findViewById(R.id.tv_firstname);
        Lastname = findViewById(R.id.tv_lastname);
        Password = findViewById(R.id.tv_password);
        ConfirmPassword = findViewById(R.id.tv_confirm_password);
        Email = findViewById(R.id.tv_email);
        Address = findViewById(R.id.tv_address);
        chbox = findViewById(R.id.chkBox1);
        register = findViewById(R.id.btn_register);

        sharedPreferences = getSharedPreferences("registerref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("firstname", Firstname.getText().toString());
                editor.putString("lastname", Lastname.getText().toString());
                editor.putString("email", Email.getText().toString());
                editor.putString("address", Address.getText().toString());
                editor.commit();

                UserEntity userEntity = new UserEntity();
                userEntity.setFirstName(Firstname.getText().toString());
                userEntity.setLastname(Lastname.getText().toString());
                userEntity.setPassword(ConfirmPassword.getText().toString());
                userEntity.setEmail(Email.getText().toString());
                userEntity.setAddress(Address.getText().toString());

                if (validateInput(userEntity)) {
                    UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                    UserDao userDao = userDatabase.userDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            userDao.registerUser(userEntity);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(RegisterActivity.this, "Fill All Fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public Boolean validateInput(UserEntity userEntity) {
        if (userEntity.getFirstname().isEmpty() || userEntity.getLastname().isEmpty() ||
                userEntity.getEmail().isEmpty() || userEntity.getPassword().isEmpty() ||
                userEntity.getAddress().isEmpty() ){
            return false;
        } else {
            return true;
        }
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

    public void goback(View view) {
        Intent a = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(a);
    }
}