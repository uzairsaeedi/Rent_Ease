package com.example.rentease.admin.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rentease.R;
import com.example.rentease.fragments.BottomFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDetailFragment extends Fragment {
    public static String userID;
    Button btn_delete;
    TextView txt_name, txt_age, txt_email, txt_phone, txt_username, txt_country, txt_city, txt_address, txt_gender;
    ImageView imageView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public UserDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        btn_delete = view.findViewById(R.id.btn_delete);
        txt_address = view.findViewById(R.id.txt_address);
        txt_age = view.findViewById(R.id.txt_age);
        txt_name = view.findViewById(R.id.txt_name);
        txt_gender = view.findViewById(R.id.txt_gender);
        txt_username = view.findViewById(R.id.txt_username);
        txt_phone = view.findViewById(R.id.txt_phone);
        txt_email = view.findViewById(R.id.txt_email);
        txt_city = view.findViewById(R.id.txt_city);
        txt_country = view.findViewById(R.id.txt_country);
        imageView = view.findViewById(R.id.profile_image);





        DocumentReference documentReference = db.collection("users").document(userID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        txt_name.setText(document.getString("name"));
                        txt_email.setText(document.getString("email"));
                        txt_age.setText(document.getString("age"));
                        txt_phone.setText(document.getString("phone_number"));
                        txt_gender.setText(document.getString("gender"));
                        txt_city.setText(document.getString("city"));
                        txt_country.setText(document.getString("country"));
                        txt_address.setText(document.getString("address"));
                        txt_username.setText(document.getString("username"));
                        String imgUrl = document.getString("profile_image");
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

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage("Do you want to Delete Account ?");

                builder.setTitle("Alert !");

                builder.setCancelable(false);

                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    deleteUserAccount();
                    FragmentManager fm = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                    FragmentTransaction frt = fm.beginTransaction();
                    frt.replace(R.id.flFragment, new BottomFragment());
                    frt.addToBackStack(null);
                    frt.commit();
                });

                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        return view;
    }
    // Replace "userToDeleteUid" with the UID of the user you want to delete

    private void deleteUserAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // User account deleted successfully
                                Toast.makeText(getContext(), "Account deleted!", Toast.LENGTH_SHORT).show();
                            } else {
                                // An error occurred while deleting the account
                                Toast.makeText(getContext(), "Failed to delete account. " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}