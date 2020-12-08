package com.example.checklist.Registration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.checklist.Database.UserDatabase.UserDao;
import com.example.checklist.Database.UserDatabase.UserDatabase;
import com.example.checklist.Database.UserDatabase.UserEntity;
import com.example.checklist.Login.LoginActivity;
import com.example.checklist.Profile.ProfileActivity;
import com.example.checklist.R;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    Button register;
    ImageView profile;
    SharedPreferences sharedPreferences;
    Integer REQUEST_CAMERA = 1, SELCTE_FILE = 0;
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
        profile = findViewById(R.id.IV_Profile);
        register = findViewById(R.id.btn_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(Firstname.getText().toString().trim(), Lastname.getText().toString().trim(),
                        Password.getText().toString().trim(), ConfirmPassword.getText().toString().trim(),
                        Email.getText().toString().trim(),
                        Address.getText().toString().trim());

            }

            private void signup(String Firstname, String Lastname, String Password, String ConfirmPassword, String Email, String Address) {
                if (Firstname.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "First name cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if (Lastname.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Last name cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if (Password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!Password.equals(ConfirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Password and Confirm Password must be same", Toast.LENGTH_LONG).show();
                    return;
                }

                if (Email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Email cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if (Address.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Address cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if (chbox.isChecked()) {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setFirstName(Firstname);
                    userEntity.setLastname(Lastname);
                    userEntity.setPassword(ConfirmPassword);
                    userEntity.setEmail(Email);
                    userEntity.setAddress(Address);

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
                    Toast.makeText(getApplicationContext(), "Terms and Conditions not accepted", Toast.LENGTH_LONG).show();
                }
            }
        });

        try {
            String result = sharedPreferences.getString("PROFILE_IMAGE", "");
            final InputStream imageStream = getApplicationContext().getContentResolver().openInputStream(Uri.parse(result));
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            profile.setImageBitmap(selectedImage);
        }

        catch (NullPointerException e){
        }

        catch (FileNotFoundException e){
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

    public void changeProfile(View view) {
        SelectImage();
    }

    private void SelectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    TedPermission.with(getApplicationContext())
                            .setPermissionListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted() {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, REQUEST_CAMERA);
                                }

                                @Override
                                public void onPermissionDenied(List<String> deniedPermissions) {

                                }
                            })
                            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                            .setPermissions(Manifest.permission.CAMERA)
                            .check();
                } else if (items[i].equals("Gallery")) {
                    TedPermission.with(getApplicationContext())
                            .setPermissionListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted() {
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    //set intent type to image
                                    intent.setType("image/*");
                                    startActivityForResult(intent, SELCTE_FILE);
                                }

                                @Override
                                public void onPermissionDenied(List<String> deniedPermissions) {

                                }
                            })
                            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                            .check();
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                Uri imageUri = data.getData();
                Log.d("imageUri", imageUri.getPath());
                sharedPreferences.edit().putString("PROFILE_IMAGE",imageUri.toString()).apply();
                final InputStream imageStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profile.setImageBitmap(selectedImage);
                Log.d("image_uri", new Gson().toJson(imageUri.getPath()));
            }
                catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
            } else if (requestCode == SELCTE_FILE) {
                try {
                    Uri imageUri = data.getData();
                    Log.d("imageUri", imageUri.getPath());
                    sharedPreferences.edit().putString("PROFILE_IMAGE",imageUri.toString()).apply();
                    final InputStream imageStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    profile.setImageBitmap(selectedImage);
                    Log.d("image_uri", new Gson().toJson(imageUri.getPath()));
                }

                catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}