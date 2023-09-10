package com.example.rentease.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentease.modules.MainActivity;
import com.example.rentease.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeleteUserFragment extends Fragment {

EditText edt_email,edt_password;

Button btn_delete;


    public DeleteUserFragment() {
        // Required empty public constructor
    }
    public static DeleteUserFragment newInstance() {
        DeleteUserFragment fragment = new DeleteUserFragment();
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
        View view =inflater.inflate(R.layout.fragment_delete_user, container, false);
        edt_email = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);
        btn_delete = view.findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage("Do you want to Delete Account ?");

                builder.setTitle("Alert !");

                builder.setCancelable(false);

                builder.setPositiveButton("Yes", (dialog, which) -> {

                    deleteuser(edt_email.getText().toString(), edt_password.getText().toString());
                });

                builder.setNegativeButton("No", (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }
    private void deleteuser(String email, String password) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        if (user != null) {
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "User account deleted.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getContext(), MainActivity.class));
                                                Toast.makeText(getContext(), "Deleted User Successfully,", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    });
        }
    }
}