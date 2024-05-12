package com.example.okmanyirodaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String  LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordConfirmEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int SECRET_KEY = getIntent().getIntExtra("SECRET_KEY", 0);
        if (SECRET_KEY != 99) {
            finish();
        }
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirmEditText = findViewById(R.id.passwordAgainEditText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String password = preferences.getString("password", "");

        passwordEditText.setText(password);
        passwordConfirmEditText.setText(password);

        mAuth = FirebaseAuth.getInstance();

        Log.i(LOG_TAG, "oncreate");

    }


    public void register(View view) {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        if (!passwordConfirm.equals(password)) {
            Log.e(LOG_TAG, "Nem egyenlő a jelszó és a confirm!");
            return;
        }
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Kérlek add meg a felhasználónevet és a jelszót!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(LOG_TAG, "User created succesfully");
                    startBooking();
                } else {
                    Log.d(LOG_TAG,"User wasn't created succesfully");
                    Toast.makeText(RegisterActivity.this, "User wasn't created succesfully: "
                            + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        Log.i(LOG_TAG, "Regisztrált név, email: " + username + email);
    }

    public void cancel(View view) {
        finish();
    }

    public void startBooking(){
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);

    }
}