package com.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.testapp.Models.Test;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("TESTS");

    private EditText editTitleTest,editPassTest;
    private AutoCompleteTextView editVisibleTest;
    private Button applyChanges;

    private String [] visible = {"Открыт", "Закрыт"};
    private ArrayAdapter adapter;

    String testKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent=getIntent();
        testKey = intent.getStringExtra("testKey");
        if (testKey==null) finish();

        init();
        initToolbar();
        setData();
        applyChanges();

    }

    private void applyChanges() {
        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editTitleTest.getText())){
                    editTitleTest.setError("Поле не должно быть пустым");
                } else
                    myRef.child(testKey).child("testTitle").setValue(editTitleTest.getText().toString());

                if (TextUtils.isEmpty(editPassTest.getText())){
                    myRef.child(testKey).child("testPass").setValue(null);
                }else
                myRef.child(testKey).child("testPass").setValue(editPassTest.getText().toString());

                if (editVisibleTest.getText().toString().equals(visible[0])){
                    myRef.child(testKey).child("visible").setValue(true);
                } else
                    myRef.child(testKey).child("visible").setValue(false);

                Toast.makeText(SettingsActivity.this, "Изменеия успешно применены", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        editTitleTest = findViewById(R.id.editTitleTest);
        editVisibleTest = findViewById(R.id.editVisibleTest);
        editPassTest = findViewById(R.id.editPassTest);

        applyChanges= findViewById(R.id.applyChanges);
    }

    private void initToolbar() {
        Toolbar toolbar;
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Параметнры теста");
        toolbar.setNavigationIcon(R.drawable.arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               onBackPressed();
            }
        });
    }

    private void setData() {
        myRef.child(testKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Test test = new Test();
                test =  snapshot.getValue(Test.class);

                if (test!=null){
                editTitleTest.setText(test.getTestTitle());
                editPassTest.setText(test.getTestPass());

                if (test.getVisible().equals(true)){
                    editVisibleTest.setText(visible[0]);
                } else editVisibleTest.setText(visible[1]);

                adapter = new ArrayAdapter(SettingsActivity.this, android.R.layout.simple_list_item_1, visible);
                editVisibleTest.setAdapter(adapter);}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}