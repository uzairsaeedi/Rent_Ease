package com.example.rentease.modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentease.R;
import com.example.rentease.admin.Admin;
import com.example.rentease.user.HomeScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    EditText edt_email, edt_password;
    Button btn_signup, btn_login;
    TextView txt_reset;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        txt_reset = findViewById(R.id.txt_reset);
        mAuth = FirebaseAuth.getInstance();

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkField(edt_email);
                    checkField(edt_password);

                    String email = edt_email.getText().toString().trim();
                    String password = edt_password.getText().toString().trim();

                    if (valid) {
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                String user = firebaseUser.getUid().toString();
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                checkUserAccessLevel(user);
                            }


                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Sorry" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });



        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
            }
        });
        txt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResetPassword.class);
                startActivity(intent);
            }
        });
    }

    public boolean checkField(EditText textfield) {
        if (textfield.getText().toString().isEmpty()) {
            textfield.setError("Empty");
            Toast.makeText(this, "Please Fill Password and Email", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference documentReference = db.collection("users").document(uid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                if (documentSnapshot.getString("isAdmin") != null) {
                    Intent intent = new Intent(MainActivity.this, Admin.class);
                    startActivity(intent);
                }
                else if (documentSnapshot.getString("isUser") != null) {
                    Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                    startActivity(intent);
                }
            }
        });
    }
}