package com.jakupIndustries.apartmentRentClient.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakupIndustries.apartmentRentClient.R;
import com.jakupIndustries.apartmentRentClient.interfaces.IClickListener;
import com.jakupIndustries.apartmentRentClient.models.Apartment;
import com.jakupIndustries.apartmentRentClient.models.RentRequest;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RentRequestsAdapter extends RecyclerView.Adapter<RentRequestsAdapter.MyViewHolder> {

    private ArrayList<RentRequest> itemsList;
    private IClickListener clickListener;

    public RentRequestsAdapter(ArrayList<RentRequest> mItemList){
        this.itemsList = mItemList;
    }

    @Override
    public RentRequestsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apartment_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RentRequestsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final RentRequest item = itemsList.get(position);
        holder.name.setText(item.getUser().getName()+" >> "+item.getApartment().getName());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v,item,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void setOnItemClickListener(IClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        private CardView itemLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.apartmentNameTextView);
            itemLayout =  itemView.findViewById(R.id.apartmentLayout);
        }
    }
}

