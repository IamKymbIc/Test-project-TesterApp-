package com.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.testapp.Models.User;

public class Sign_In_Activity extends AppCompatActivity {

    private Button enterBtn;
    private TextView registerBtn;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference("USERS");
    private EditText emailF;
    private EditText passF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initToolbar();
        registrationBtn();
        initElements();
        signInBtn();

        }

    private void initToolbar() {
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_sign_in_tb);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user !=null){
            checkUser();
        }
    }

    private void signInBtn() {
        enterBtn=findViewById(R.id.enterBtn);
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(emailF.getText().toString())) {
                    Snackbar.make(v,"Введите Email", Snackbar.LENGTH_SHORT).show();
                    return;}

                if (TextUtils.isEmpty(passF.getText().toString())) {
                    Snackbar.make(v,"Введите пароль", Snackbar.LENGTH_SHORT).show();
                    return;}

                auth.signInWithEmailAndPassword(emailF.getText().toString(),passF.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                checkUser();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Ошибка авторизации", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void initElements() {
        emailF=findViewById(R.id.loginText);
        passF=findViewById(R.id.passText);
        auth=FirebaseAuth.getInstance();

    }

    private void registrationBtn() {
        registerBtn=findViewById(R.id.registrationBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign_In_Activity.this, RegistrationActivity.class));
            }
        });
    }

    private void checkUser() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User model=new User();
                model = dataSnapshot.child(auth.getInstance().getCurrentUser().getUid()).getValue(User.class);

                if (model.getUserRole()==true){
                    startActivity(new Intent(Sign_In_Activity.this,Teacher_Activity.class));
                    finish();
                }
                else
                    startActivity(new Intent(Sign_In_Activity.this,StudentActivity.class));
                    finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
