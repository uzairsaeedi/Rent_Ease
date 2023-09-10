package com.example.rentease.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentease.R;

public class ContactFragment extends Fragment {

Button btn_submit;
EditText edt_email,edt_message;
Boolean valid = true;
    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
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
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        btn_submit = view.findViewById(R.id.btn_submit);

        edt_email = view.findViewById(R.id.edt_email);
        edt_message = view.findViewById(R.id.edt_message);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(edt_email);
                checkField(edt_message);

                if(valid) {
                    Toast.makeText(getContext(), "Thankyou For your message we we will contact you soon", Toast.LENGTH_LONG).show();
                }
                }
        });
        return view;
    }
    public boolean checkField(EditText textfield) {
        if (textfield.getText().toString().isEmpty()) {
            textfield.setError("Empty");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

}