package com.example.okmanyirodaapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AppointmentActivity extends AppCompatActivity {
    private static final String LOG_TAG = AppointmentActivity.class.getName();
    private FirebaseUser user;
    private RecyclerView mRecyclerView;
    private ArrayList<AppointmentItem> mItemList;
    private int gridNumber = 1;

    private AppointmentItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment_booking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new AppointmentItemAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);
        initializeData();


    }

    private void initializeData() {
        String itemName = getResources().getString(R.string.title);
        String[] itemDate = getResources().getStringArray(R.array.item_dates);

        mItemList.clear();

        for (int i = 0; i < itemDate.length; i++) {
            mItemList.add(new AppointmentItem(itemName, itemDate[i]));
        }
    }

    public void cancel(View view) {
        finish();
    }

    //Lifecycle hook
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}