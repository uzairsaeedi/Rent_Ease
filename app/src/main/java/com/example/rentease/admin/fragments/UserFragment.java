package com.example.rentease.admin.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rentease.R;
import com.example.rentease.admin.model.UserModel;
import com.example.rentease.admin.adapters.UserAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserFragment extends Fragment{
RecyclerView recyclerView;
ArrayList<UserModel> userModelArrayList = new ArrayList<>();
UserAdapter userAdapter;
FirebaseFirestore db = FirebaseFirestore.getInstance();
    public UserFragment() {
        // Required empty public constructor
    }
    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView = view.findViewById(R.id.rv_users);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshots : task.getResult()){
                        UserModel  obj = documentSnapshots.toObject(UserModel.class);
                        userModelArrayList.add(obj);

                        userAdapter = new UserAdapter(getContext(), userModelArrayList);
                        recyclerView.setAdapter(userAdapter);
                        userAdapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(getContext(), "No user found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    }
