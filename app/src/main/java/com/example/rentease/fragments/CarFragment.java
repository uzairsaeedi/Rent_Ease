package com.example.rentease.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rentease.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CarFragment extends Fragment {
    public static String carID;
    TextView txt_name, txt_capacity, txt_rent, txt_doors, txt_transmission, txt_fuel, txt_hybrid, txt_company, txt_seats;
    ImageView imageView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseFirestore mdb = FirebaseFirestore.getInstance();
    Button btn_book;

    public CarFragment() {
        // Required empty public constructor
    }

    public static CarFragment newInstance() {
        CarFragment fragment = new CarFragment();
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
        View view = inflater.inflate(R.layout.fragment_car, container, false);
        txt_transmission = view.findViewById(R.id.txt_transmission);
        txt_doors = view.findViewById(R.id.car_doors);
        txt_rent = view.findViewById(R.id.txt_price1);
        txt_capacity = view.findViewById(R.id.txt_capacity1);
        txt_name = view.findViewById(R.id.txt_name1);
        txt_company = view.findViewById(R.id.car_company);
        txt_seats = view.findViewById(R.id.seats);
        txt_fuel = view.findViewById(R.id.fuel);
        txt_hybrid = view.findViewById(R.id.hybrid);
        imageView = view.findViewById(R.id.car_img);

        DocumentReference documentReference = db.collection("cars").document(carID);

            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            txt_name.setText(document.getString("name"));
                            txt_company.setText(document.getString("company"));
                            txt_capacity.setText(document.getString("capacity"));
                            txt_doors.setText(document.getString("doors"));
                            txt_seats.setText(document.getString("seats"));
                            txt_fuel.setText(document.getString("fuel"));
                            txt_rent.setText(document.getString("rent"));
                            txt_hybrid.setText(document.getString("hybrid"));
                            txt_transmission.setText(document.getString("transmission"));
                            String imgUrl = document.getString("car_image");
                            Glide.with(getContext()).load(imgUrl).into(imageView);
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        btn_book = view.findViewById(R.id.btn_book);

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "You have Booked this car", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}