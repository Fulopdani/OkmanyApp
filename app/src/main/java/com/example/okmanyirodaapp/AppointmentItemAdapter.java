package com.example.okmanyirodaapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppointmentItemAdapter extends RecyclerView.Adapter<AppointmentItemAdapter.ViewHolder> {
    private ArrayList<AppointmentItem> mAppointmentItemsData;
    private ArrayList<AppointmentItem> mAppointmentItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;
    AppointmentItemAdapter(Context context, ArrayList<AppointmentItem> itemsData){
        this.mAppointmentItemsData = itemsData;
        this.mAppointmentItemsDataAll = itemsData;
        this.mContext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent,false));
    }

    @Override
        public void onBindViewHolder(@NonNull AppointmentItemAdapter.ViewHolder holder, int position) {
            AppointmentItem currentItem = mAppointmentItemsData.get(position);

            holder.bindTo(currentItem);

            if (holder.getAdapterPosition() > lastPosition) {
                //animáció 1
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
                holder.itemView.startAnimation(animation);
                //animáció 2
                Animation fadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                holder.mTitleText.startAnimation(fadeInAnimation);
                holder.mDate.startAnimation(fadeInAnimation);

                lastPosition = holder.getAdapterPosition();
            }

        }

    @Override
    public int getItemCount() {
        return mAppointmentItemsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleText;
        private TextView mDate;
        public ViewHolder(View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.itemTitle);
            mDate = itemView.findViewById(R.id.itemDate);

            itemView.findViewById(R.id.bookButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Activity", "Booking button clicked!");
                }
            });
        }

        public void bindTo(AppointmentItem currentItem) {
            mTitleText.setText(currentItem.getName());
            mDate.setText(currentItem.getDate());

        }
    }

}


