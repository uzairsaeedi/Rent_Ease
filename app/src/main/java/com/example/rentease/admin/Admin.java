package com.example.rentease.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.rentease.admin.fragments.AddCarFragment;
import com.example.rentease.fragments.BottomFragment;
import com.example.rentease.fragments.DeleteUserFragment;
import com.example.rentease.fragments.LuxuryFragment;
import com.example.rentease.modules.MainActivity;
import com.example.rentease.fragments.ProfileFragment;
import com.example.rentease.R;
import com.example.rentease.admin.fragments.UserFragment;
import com.example.rentease.fragments.suvFragment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Admin extends AppCompatActivity {
    FrameLayout frameLayout;
    ShimmerFrameLayout shimmerFrameLayout;
    DrawerLayout drawerLayout;
    final static int Profile = 1;
    final static int Suv = 2;
    final static int Normal = 3;
    final static int Luxury = 4;
    final static int Add = 5;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = mAuth.getCurrentUser();
    String user = firebaseUser.getUid().toString();
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    MeowBottomNavigation meowBottomNavigation;
    TextView txt_name,txt_email;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


            shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
            frameLayout = findViewById(R.id.flFragment);

            frameLayout.setVisibility(View.INVISIBLE);
            shimmerFrameLayout.startShimmer();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    frameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.INVISIBLE);
                }
            }, 3000);

            meowBottomNavigation = findViewById(R.id.bottomNavigation);

            meowBottomNavigation.add(new MeowBottomNavigation.Model(Normal, R.drawable.alto));
            meowBottomNavigation.add(new MeowBottomNavigation.Model(Suv, R.drawable.tundra));
            meowBottomNavigation.add(new MeowBottomNavigation.Model(Add, R.drawable.baseline_add_24));
            meowBottomNavigation.add(new MeowBottomNavigation.Model(Luxury, R.drawable.rollsroyce));
            meowBottomNavigation.add(new MeowBottomNavigation.Model(Profile, R.drawable.baseline_account_circle_24));

            meowBottomNavigation.show(Normal, true);

            meowBottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
                @Override
                public Unit invoke(MeowBottomNavigation.Model model) {

                    Fragment fragment = null;
                    if (model.getId() == 1) {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction frt = fm.beginTransaction();
                        frt.replace(R.id.flFragment, new ProfileFragment());
                        frt.addToBackStack(null);
                        frt.commit();
                    }
                    if (model.getId() == 2) {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction frt = fm.beginTransaction();
                        frt.replace(R.id.flFragment, new suvFragment());
                        frt.addToBackStack(null);
                        frt.commit();
                    }
                    if (model.getId() == 3) {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction frt = fm.beginTransaction();
                        frt.replace(R.id.flFragment, new BottomFragment());
                        frt.addToBackStack(null);
                        frt.commit();
                    }
                    if (model.getId() == 4) {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction frt = fm.beginTransaction();
                        frt.replace(R.id.flFragment, new LuxuryFragment());
                        frt.addToBackStack(null);
                        frt.commit();
                    }
                    if (model.getId() == 5) {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction frt = fm.beginTransaction();
                        frt.replace(R.id.flFragment, new AddCarFragment());
                        frt.addToBackStack(null);
                        frt.commit();
                    }

                    return null;
                }
            });

            navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);

            txt_name = headerView.findViewById(R.id.user_name);
            txt_email = headerView.findViewById(R.id.user_email);
            image = headerView.findViewById(R.id.img_user);

            DocumentReference documentReference = db.collection("users").document(user);

            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            txt_name.setText(document.getString("name"));
                            txt_email.setText(document.getString("email"));
                            String imgUrl = document.getString("profile_image");
                            Glide.with(Admin.this).load(imgUrl).into(image);
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Admin.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            navigationView.setNavigationItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction frt = fm.beginTransaction();
                    frt.replace(R.id.flFragment, new BottomFragment());
                    frt.addToBackStack(null);
                    frt.commit();
                } else if (itemId == R.id.users) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction frt = fm.beginTransaction();
                    frt.replace(R.id.flFragment, new UserFragment());
                    frt.addToBackStack(null);
                    frt.commit();
                } else if (itemId == R.id.logout) {
                    mAuth.signOut();
                    Intent intent = new Intent(Admin.this, MainActivity.class);
                    startActivity(intent);

                } else if (itemId == R.id.delete) {

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction frt = fm.beginTransaction();
                    frt.replace(R.id.flFragment, new DeleteUserFragment());
                    frt.addToBackStack(null);
                    frt.commit();

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });
            drawerLayout = findViewById(R.id.drawer_layout);

            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            drawerLayout.addDrawerListener(actionBarDrawerToggle);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }


            actionBarDrawerToggle.syncState();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction frt = fm.beginTransaction();
            frt.add(R.id.flFragment, new BottomFragment());
            frt.addToBackStack(null);
            frt.commit();

        }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}