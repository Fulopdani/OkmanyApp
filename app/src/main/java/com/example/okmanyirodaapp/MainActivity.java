package com.example.okmanyirodaapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String  LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;
    private static final int INTERNET_CODE = 123;

    EditText usernameET;
    EditText passwordET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (checkInternetPermission()) {
            usernameET = findViewById(R.id.editTextUsername);
            passwordET = findViewById(R.id.editTextPassword);


            preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
            mAuth = FirebaseAuth.getInstance();
        } else {
            requestInternetPermission();
        }
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Az internetkapcsolat ki van kapcsolva. Engedélyezze, hogy folytathassa az alkalmazás használatát?")
                    .setTitle("Nincs internetkapcsolat")
                    .setPositiveButton("Engedélyezés", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Az internet engedélyezése
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "Nincs internetkapcsolat!", Toast.LENGTH_SHORT).show();
                        }
                    });
            builder.create().show();
        }
    }

    //internet hozzáférés
    private boolean checkInternetPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestInternetPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.INTERNET},
                INTERNET_CODE);
    }

    public void login(View view) {
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Kérlek add meg a felhasználónevet és a jelszót!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(LOG_TAG, "Bejelentkezett, jelszó: " + username + password);

        mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(LOG_TAG, "User login was successful");
                    startBooking();
                } else {
                    Log.d(LOG_TAG,"User login failed");
                    Toast.makeText(MainActivity.this, "User wasn't logged in successfully: "
                            + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        //Notification
        NotificationHelper.showNotification(MainActivity.this, "Bejelentkezés", "Sikeres bejelentkezés!");

    }

    public void startBooking(){
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);

    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //Lifecycle Hook
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("password", passwordET.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void openLocation(View view) {
        String url = "Szeged kormányablak Rákóczi tér";

        Uri uri = Uri.parse("geo:0,0?q=" + url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void guest_login(View view) {
        if (checkInternetPermission()) {
            mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Anonym login was succesful");
                        startBooking();
                    } else {
                        Log.d(LOG_TAG, "Anonym login failed");
                        Toast.makeText(MainActivity.this, "Anonym user wasn't logged in succesfully: "
                                + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            requestInternetPermission();
        }
    }
}