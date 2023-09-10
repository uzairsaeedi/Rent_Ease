package com.example.rentease.admin.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.rentease.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCarFragment extends Fragment {
    EditText edt_capacity, edt_transmission, edt_name, edt_rent, edt_fuel, edt_company, edt_doors, edt_seats;
    Button btn_add_car;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ;
    CircleImageView car_image;
    final int PICK_IMAGE = 1;
    FirebaseStorage storage;
    StorageReference reference;
    String hybrid;
    StorageReference carRef;
    Uri imageUri;
    RadioGroup rg, rg2;
    RadioButton btn_hybrid, btn_luxury, btn_suv, btn_standard;
    String carUrl;
    boolean valid = false;

    public AddCarFragment() {
        // Required empty public constructor
    }

    public static AddCarFragment newInstance() {
        AddCarFragment fragment = new AddCarFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_car, container, false);

        rg2 = view.findViewById(R.id.radio2);
        btn_luxury = view.findViewById(R.id.btn_luxury);
        btn_suv = view.findViewById(R.id.btn_suv);
        btn_standard = view.findViewById(R.id.btn_standard);

        rg = view.findViewById(R.id.radio);
        btn_hybrid = view.findViewById(rg.getCheckedRadioButtonId());

        edt_capacity = view.findViewById(R.id.edt_capacity);
        edt_company = view.findViewById(R.id.edt_company);
        edt_doors = view.findViewById(R.id.edt_doors);
        edt_fuel = view.findViewById(R.id.edt_fuel);
        edt_transmission = view.findViewById(R.id.edt_transmission);
        edt_seats = view.findViewById(R.id.edt_seats);
        edt_rent = view.findViewById(R.id.edt_rent);
        edt_name = view.findViewById(R.id.edt_name);

        btn_add_car = view.findViewById(R.id.btn_add_car);


        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        reference = storage.getReference();

        carRef = reference.child("car");

        car_image = view.findViewById(R.id.car_image);
        car_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
            }
        });

        btn_add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkField(edt_seats);
                checkField(edt_rent);
                checkField(edt_company);
                checkField(edt_fuel);
                checkField(edt_transmission);
                checkField(edt_name);
                checkField(edt_doors);
                checkField(edt_capacity);


                if (valid) {
                    uploadcarImage();
                }


            }
        });

        return view;
    }

    public boolean checkField(EditText textfield) {
        if (textfield.getText().toString().isEmpty()) {
            textfield.setError("Empty");
            Toast.makeText(getContext(), "Please Fill", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {

                imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                car_image.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public void uploadcarImage() {


        carRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        carUrl = task.getResult().toString();
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveData(carUrl);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed not get URL", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveData(String carUrl) {

        String id = db.collection("cars").document().getId();

        hybrid = btn_hybrid.getText().toString();

        String name_value = edt_name.getText().toString();
        String company = edt_company.getText().toString();
        String rent = edt_rent.getText().toString();
        String fuel = edt_fuel.getText().toString();
        String seats = edt_seats.getText().toString();
        String doors = edt_doors.getText().toString();
        String transmission = edt_transmission.getText().toString();
        String capacity = edt_capacity.getText().toString();


        Map<String, Object> car_data = new HashMap<>();

        car_data.put("car_image", carUrl);
        car_data.put("name", name_value);
        car_data.put("car_id", id);
        car_data.put("company", company);
        car_data.put("seats", seats);
        car_data.put("doors", doors);
        car_data.put("capacity", capacity);
        car_data.put("transmission", transmission);
        car_data.put("fuel", fuel);
        car_data.put("hybrid", hybrid);
        car_data.put("rent", rent);
        if (btn_standard.isChecked()) {
            db.collection("cars").document(id).set(car_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Sorry " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
            if (btn_suv.isChecked()) {
                db.collection("cars_suv").document(id).set(car_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Sorry " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (btn_luxury.isChecked()){
                db.collection("cars_luxury").document(id).set(car_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Sorry " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }


    }
}
