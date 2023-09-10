package com.example.rentease.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentease.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditFragment extends Fragment {

    EditText edt_name,edt_phone,edt_city,edt_country,edt_address,edt_username,edt_age;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    String user = firebaseUser.getUid();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btn_edit;
    boolean valid =true;

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance() {
        EditFragment fragment = new EditFragment();
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

         View view = inflater.inflate(R.layout.fragment_edit, container, false);
         edt_address = view.findViewById(R.id.edt_address);
         edt_age = view.findViewById(R.id.edt_age);
         edt_city = view.findViewById(R.id.edt_city);
         edt_country = view.findViewById(R.id.edt_country);
         edt_name = view.findViewById(R.id.edt_name);
         edt_username = view.findViewById(R.id.edt_username);
         edt_phone = view.findViewById(R.id.edt_phone);

         btn_edit = view.findViewById(R.id.btn_edit);

         btn_edit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                checkField(edt_phone);
                checkField(edt_name);
                checkField(edt_address);
                checkField(edt_city);
                checkField(edt_country);
                checkField(edt_username);
                checkField(edt_age);
                updateData(user);
             }
         });
        return view;
    }


    public void updateData(String userID ){

        String name_value = edt_name.getText().toString();
        String username = edt_username.getText().toString();
        String phone_number_value = edt_phone.getText().toString();
        String city_value = edt_city.getText().toString();
        String country_value = edt_country.getText().toString();
        String address_value = edt_address.getText().toString();
        String age_value = edt_age.getText().toString();

        Map<String, Object> user_data = new HashMap<>();

        user_data.put("name", name_value);
        user_data.put("username", username);
        user_data.put("phone_number", phone_number_value);
        user_data.put("city", city_value);
        user_data.put("country", country_value);
        user_data.put("address", address_value);
        user_data.put("age", age_value);

        db.collection("users").document(user).update(user_data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed " +e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean checkField(EditText textfield){
        if(textfield.getText().toString().isEmpty()){
            textfield.setError("Empty");
            Toast.makeText(getContext(), "Please Fill", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else {
            valid = true;
        }
        return valid;
    }
}