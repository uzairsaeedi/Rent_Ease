package com.example.rentease.admin.adapters;

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
import com.example.rentease.R;
import com.example.rentease.admin.fragments.UserDetailFragment;
import com.example.rentease.admin.model.UserModel;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersVH> {
    Context context;
    ArrayList<UserModel> arrayList= new ArrayList<>();
    public UserAdapter(Context context, ArrayList<UserModel> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }



    @NonNull
    @Override
    public UsersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_design,parent,false);
        return new UsersVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersVH holder, int position) {
        holder.txt_email.setText(arrayList.get(position).getEmail());
        holder.txt_username.setText(arrayList.get(position).getUsername());
        holder.txt_name.setText(arrayList.get(position).getName());
        holder.txt_uid.setText(arrayList.get(position).getId());
        String profile_img = arrayList.get(position).getProfile_image();
        Glide.with(context).load(profile_img).into(holder.img);
        holder.cv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = ((FragmentActivity)view.getContext()).getSupportFragmentManager();
                FragmentTransaction frt = fm.beginTransaction();
                frt.replace(R.id.flFragment, new UserDetailFragment());
                UserDetailFragment.userID = arrayList.get(position).getId();
                frt.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class UsersVH extends RecyclerView.ViewHolder{
        CardView cv_user;
        ImageView img;
        TextView txt_name,txt_email,txt_username,txt_uid;
        public UsersVH(@NonNull View itemView) {
            super(itemView);
            txt_email = itemView.findViewById(R.id.txt_user_email);
            txt_name = itemView.findViewById(R.id.txt_user_name);
            txt_username = itemView.findViewById(R.id.txt_user_username);
            cv_user = itemView.findViewById(R.id.cv_users);
            img = itemView.findViewById(R.id.img_user);
            txt_uid = itemView.findViewById(R.id.txt_id);
        }
    }

}
