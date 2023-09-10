package com.example.rentease.modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.auth.AuthResult;
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

public class Signup extends AppCompatActivity {
    EditText edt_email, edt_username, edt_name, edt_password,edt_phone,edt_repassword,edt_city,edt_country,edt_age,edt_address;
    Button btn_signup, btn_login;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    CircleImageView profile_image;
    final int PICK_IMAGE = 1;
    FirebaseStorage storage;
    StorageReference reference;
    StorageReference profileRef;
    Uri imageUri = Uri.parse("");
    RadioButton btn_admin,btn_user;
    String profileUrl;
    boolean valid=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edt_password = findViewById(R.id.edt_password);
        edt_email = findViewById(R.id.edt_email);
        edt_username = findViewById(R.id.edt_username);
        edt_phone = findViewById(R.id.edt_phone);
        edt_name = findViewById(R.id.edt_name);
        edt_age = findViewById(R.id.edt_age);
        edt_city = findViewById(R.id.edt_city);
        edt_country = findViewById(R.id.edt_country);
        edt_address = findViewById(R.id.edt_address);
        edt_repassword = findViewById(R.id.edt_re_password);

        btn_admin = findViewById(R.id.btn_admin);
        btn_user = findViewById(R.id.btn_user);





        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        reference = storage.getReference();

        profileRef = reference.child("profile");

        profile_image = findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
            }
        });

    btn_signup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            checkField(edt_address);
            checkField(edt_age);
            checkField(edt_city);
            checkField(edt_country);
            checkField(edt_email);
            checkField(edt_address);
            checkField(edt_name);
            checkField(edt_password);
            checkField(edt_repassword);
            checkField(edt_phone);
            checkField(edt_username);

            String email = edt_email.getText().toString().trim();
            String password = edt_password.getText().toString().trim();
            if (!(btn_admin.isChecked() || btn_user.isChecked())) {
                Toast.makeText(Signup.this, "Select Account Role", Toast.LENGTH_SHORT).show();
            } else {
                if (valid) {
                    if (password.equals(edt_repassword.getText().toString())) {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    uploadProfileImage();
                                    Toast.makeText(Signup.this, "Successfully Registered", Toast.LENGTH_SHORT).show();


                                    Intent intent = new Intent(Signup.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Signup.this, "Sorry" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        edt_password.setError("Password do not match");
                    }

                }
            }
        }
    });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public boolean checkField(EditText textfield){
        if(textfield.getText().toString().isEmpty()){
            textfield.setError("Empty");
            Toast.makeText(this, "Please Fill", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else {
            valid = true;
        }
        return valid;
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profile_image.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Signup.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(Signup.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public void uploadProfileImage() {


        profileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Signup.this, "Successful", Toast.LENGTH_SHORT).show();
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                profileUrl = task.getResult().toString();
                                FirebaseUser user = mAuth.getCurrentUser();
                                String uid = user.getUid().toString();
                                saveData(uid, profileUrl);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Signup.this, "Failed not get URL", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Signup.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void saveData(String userID, String profileUrl) {
        RadioGroup rg = (RadioGroup) findViewById(R.id.radio);

        String gender = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();

        String name_value = edt_name.getText().toString();
        String email = edt_email.getText().toString();
        String username = edt_username.getText().toString();
        String phone_number_value = edt_phone.getText().toString();
        String city_value = edt_city.getText().toString();
        String country_value = edt_country.getText().toString();
        String address_value = edt_address.getText().toString();
        String age_value = edt_age.getText().toString();


        Map<String, Object> user_data = new HashMap<>();
        if (btn_admin.isChecked()){
            user_data.put("isAdmin","1");
        }
        if (btn_user.isChecked()){
            user_data.put("isUser","1");
        }

        user_data.put("name", name_value);
        user_data.put("email", email);
        user_data.put("username", username);
        user_data.put("phone_number", phone_number_value);
        user_data.put("city", city_value);
        user_data.put("country", country_value);
        user_data.put("address", address_value);
        user_data.put("age", age_value);
        user_data.put("gender", gender);
        user_data.put("user_id", userID);
        user_data.put("profile_image", profileUrl);
        db.collection("users").document(userID).set(user_data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Signup.this, "Done", Toast.LENGTH_SHORT).show();
                }
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Signup.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}