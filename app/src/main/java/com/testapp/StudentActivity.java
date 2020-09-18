package com.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.testapp.Models.User;
import com.testapp.adapter.TabsFragmentAdapterStudents;

public class StudentActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference("USERS");

    private Toolbar toolbar;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initTab();
    }

    private void initToolbar() {
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.akkInfo) {
                    showAlertDialog();
                }
                return true;
            }
        });
        toolbar.inflateMenu(R.menu.menu);

    }

    private void initTab() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsFragmentAdapterStudents adapter = new TabsFragmentAdapterStudents(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout =(TabLayout) findViewById(R.id.tadLayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void showAlertDialog(){
        MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(StudentActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.akk_info_layout,null);
        final TextView emailText= (TextView)view.findViewById(R.id.tv_Email);
        final TextView nameText= (TextView)view.findViewById(R.id.tv_Name);
        final TextView statText= (TextView)view.findViewById(R.id.tv_Stat);
        final Button signOutBtn =(Button) view.findViewById(R.id.signOut);

        builder.setView(view);
        builder.setTitle("Информация об аккаунте");
        final AlertDialog dialog = builder.create();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                User model=new User();
                model = dataSnapshot.child(userID).getValue(User.class);
                assert model != null;
                emailText.setText("Email: "+ model.getUserEmail());
                nameText.setText("ФИО: "+ model.getUserName());
                statText.setText("Кафедра: " + model.getUserStatus());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dialog.show();

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getApplicationContext(),Sign_In_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
