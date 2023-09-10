package com.example.rentease.fragments;

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

import com.example.rentease.CarModel;
import com.example.rentease.R;
import com.example.rentease.SuvAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class suvFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<CarModel> arrayList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SuvAdapter suvAdapter;
    public suvFragment() {
        // Required empty public constructor
    }


    public static suvFragment newInstance( ) {
        suvFragment fragment = new suvFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suv, container, false);
        recyclerView = view.findViewById(R.id.rv_suv);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        db.collection("cars_suv").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshots : task.getResult()){
                        CarModel obj = documentSnapshots.toObject(CarModel.class);
                        arrayList.add(obj);

                        suvAdapter = new SuvAdapter(getContext(), arrayList);

                        recyclerView.setAdapter(suvAdapter);
                        suvAdapter.notifyDataSetChanged();
                    }

                }else {
                    Toast.makeText(getContext(), "No car found", Toast.LENGTH_SHORT).show();
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