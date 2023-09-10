package com.example.rentease;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rentease.admin.fragments.UserDetailFragment;
import com.example.rentease.fragments.CarFragment;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarVH> {
    Context context;

    ArrayList<CarModel> arrayList= new ArrayList<>();
    public CarAdapter(){

    }
    public CarAdapter(Context context, ArrayList<CarModel> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }
    @NonNull
    @Override
    public CarVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_design,parent,false);
        return new CarVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarVH holder, int position) {
        holder.txt_capacity.setText(arrayList.get(position).getCapacity());
        holder.txt_price.setText(arrayList.get(position).getRent());
        holder.txt_name.setText(arrayList.get(position).getName());
        String car_img = arrayList.get(position).getCar_image();
        Glide.with(context).load(car_img).into(holder.img);
        holder.cv_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarFragment.carID = arrayList.get(position).getCar_id();
                FragmentManager fm = ((FragmentActivity)view.getContext()).getSupportFragmentManager();
                FragmentTransaction frt = fm.beginTransaction();
                frt.replace(R.id.flFragment, new CarFragment());
                frt.addToBackStack(null);
                frt.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CarVH extends RecyclerView.ViewHolder{
        CardView cv_cars;
        ImageView img;
        TextView txt_name,txt_capacity,txt_price;
        public CarVH(@NonNull View itemView) {
            super(itemView);
            txt_capacity = itemView.findViewById(R.id.txt_car_capacity);
            txt_name = itemView.findViewById(R.id.txt_car_name);
            txt_price = itemView.findViewById(R.id.txt_car_price);
            cv_cars = itemView.findViewById(R.id.cv_cars);
            img = itemView.findViewById(R.id.img_car);
        }
    }
}
