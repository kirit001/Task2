package com.example.checklist.Profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "profilepic";
    final Context context = this;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int REQUEST_CAMERA = 1, SELCTE_FILE = 0;
    ImageView profilepic;
    TextView firstname, lastname, email1, address1;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Actionbar and its title
        setTitle("Profile");

        //init
        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        firstname = findViewById(R.id.tv_firstname);
        lastname = findViewById(R.id.tv_lastname);
        email1 = findViewById(R.id.tv_email);
        address1 = findViewById(R.id.tv_address);
        profilepic = findViewById(R.id.iv_profile);
        ImageView editfirstname = findViewById(R.id.editfirstname);
        ImageView editlastname = findViewById(R.id.editlastname);
        ImageView editemail = findViewById(R.id.EDITemail);
        ImageView editaddress = findViewById(R.id.editaddress);

        try {
            String result = sharedPreferences.getString("PROFILE_IMAGE", "");
            final InputStream imageStream = getApplicationContext().getContentResolver().openInputStream(Uri.parse(result));
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            profilepic.setImageBitmap(selectedImage);
        } catch (NullPointerException e) {
        } catch (FileNotFoundException e) {
        }

        String user = sharedPreferences.getString("email", "");

        UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
        UserDao userDao = userDatabase.userDao();
        new Thread(() -> {
            UserEntity userEntity = userDao.getuser(user);
            String first = userEntity.getFirstname();
            String last = userEntity.getLastname();
            String email = userEntity.getEmail();
            String address = userEntity.getAddress();
            firstname.setText(first);
            lastname.setText(last);
            email1.setText(email);
            address1.setText(address);
        }).start();

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

        editfirstname.setOnClickListener(view -> {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.edit_firstname, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = promptsView
                    .findViewById(R.id.edit_firstname);

            userInput.setText(firstname.getText());

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            (dialog, id) -> {
                                // get user input and set it to result
                                // edit text
                                String user1 = userInput.getText().toString();
                                String email = email1.getText().toString();

                                UserDatabase userDatabase1 = UserDatabase.getUserDatabase(getApplicationContext());
                                UserDao userDao1 = userDatabase1.userDao();
                                new Thread(() -> {
                                    userDao1.editfirstname(email, user1);
                                    UserEntity userEntity = userDao1.getuser(email);
                                    firstname.setText(userEntity.getFirstname());
                                }).start();
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        });

        editlastname.setOnClickListener(view -> {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.edit_lastname, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = promptsView
                    .findViewById(R.id.edit_lastname);

            userInput.setText(lastname.getText());

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            (dialog, id) -> {
                                // get user input and set it to result
                                // edit text
                                String user12 = userInput.getText().toString();
                                String email = email1.getText().toString();

                                UserDatabase userDatabase12 = UserDatabase.getUserDatabase(getApplicationContext());
                                UserDao userDao12 = userDatabase12.userDao();
                                new Thread(() -> {
                                    userDao12.editlastname(email, user12);
                                    UserEntity userEntity = userDao12.getuser(email);
                                    lastname.setText(userEntity.getLastname());
                                }).start();
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        });

        editemail.setOnClickListener(view -> {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.edit_email, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = promptsView
                    .findViewById(R.id.edit_email);

            userInput.setText(email1.getText());

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            (dialog, id) -> {
                                // get user input and set it to result
                                // edit text
                                String user13 = userInput.getText().toString();
                                String email = email1.getText().toString();

                                UserDatabase userDatabase12 = UserDatabase.getUserDatabase(getApplicationContext());
                                UserDao userDao12 = userDatabase12.userDao();
                                new Thread(() -> {
                                    UserEntity userEntity = userDao12.getuser(email);
                                    Integer ID = userEntity.getId();
                                    userDao12.editemail(ID, user13);
                                    email1.setText(userEntity.getEmail());
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    editor.clear();
                                    editor.putBoolean("savelogin", false);
                                    editor.commit();
                                }).start();
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        });

        editaddress.setOnClickListener(view -> {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.edit_address, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = promptsView
                    .findViewById(R.id.edit_address);

            userInput.setText(address1.getText());

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            (dialog, id) -> {
                                // get user input and set it to result
                                // edit text
                                String user14 = userInput.getText().toString();
                                String email = email1.getText().toString();

                                UserDatabase userDatabase13 = UserDatabase.getUserDatabase(getApplicationContext());
                                UserDao userDao13 = userDatabase13.userDao();
                                new Thread(() -> {
                                    userDao13.editaddress(email, user14);
                                    UserEntity userEntity = userDao13.getuser(email);
                                    address1.setText(userEntity.getAddress());
                                }).start();
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        });

    }

    @Override
    protected void onStart() {

        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);

        String user = sharedPreferences.getString("email", "");

        UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
        UserDao userDao = userDatabase.userDao();
        new Thread(() -> {
            UserEntity userEntity = userDao.getuser(user);
            String first = userEntity.getFirstname();
            String last = userEntity.getLastname();
            String email = userEntity.getEmail();
            String address = userEntity.getAddress();
            firstname.setText(first);
            lastname.setText(last);
            email1.setText(email);
            address1.setText(address);
        }).start();
        super.onStart();
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

    public void PickProfile(View view) {
        SelectImage();
    }

    private void SelectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    TedPermission.with(ProfileActivity.this)
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
                    TedPermission.with(ProfileActivity.this)
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
//                try {
////                    Uri imageUri = data.getData();
////                    sharedPreferences.edit().putString("PROFILE_IMAGE",imageUri.toString()).apply();
////                    final InputStream imageStream = ProfileActivity.this.getContentResolver().openInputStream(imageUri);
////                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
////                    profilepic.setImageBitmap(selectedImage);
////                    Log.d("image_uri", new Gson().toJson(imageUri.getPath()));
//                }
//
//                catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
//                }
            } else if (requestCode == SELCTE_FILE) {
                try {
                    Uri imageUri = data.getData();
                    Log.d("imageUri", imageUri.getPath());
                    sharedPreferences.edit().putString("PROFILE_IMAGE", imageUri.toString()).apply();
                    final InputStream imageStream = ProfileActivity.this.getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    profilepic.setImageBitmap(selectedImage);
                    Log.d("image_uri", new Gson().toJson(imageUri.getPath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}