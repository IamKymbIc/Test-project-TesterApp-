package com.testapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.testapp.Models.User;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailField,nameField,passField,repeatPassField,StatusField;
    private TextInputLayout statusLayout;
    private RadioButton RBPrep,RBStud;
    private Button PositiveBtn;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private Boolean stats;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initToolbar();
        init();
        listenerOnRB();
        registration();
    }

    private void init() {
        emailField = (EditText) findViewById(R.id.EmailField);
        nameField =(EditText)findViewById(R.id.NameField);
        RBPrep = findViewById(R.id.RBPrep);
        RBStud= findViewById(R.id.RBStud);
        StatusField=(EditText)findViewById(R.id.StatusField);
        StatusField.setEnabled(false);
        passField=(EditText)findViewById(R.id.PassField);
        repeatPassField =(EditText)findViewById(R.id.RPassField);
        PositiveBtn = findViewById(R.id.positiveBtn);
        statusLayout = findViewById(R.id.statusLayout);
        auth = FirebaseAuth.getInstance();
    }

    private void listenerOnRB() {
        RBPrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusLayout.setEnabled(true);
                statusLayout.setHint("Кафедра");
                stats=true;
            }
        });

        RBStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusLayout.setEnabled(true);
                statusLayout.setHint("Группа");
                stats=false;
            }
        });
    }

    private void initToolbar() {
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_register_tb);
        toolbar.setNavigationIcon(R.drawable.arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void registration() {
        PositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (TextUtils.isEmpty(emailField.getText().toString())) {
                    Snackbar.make(v,"Введите Email",Snackbar.LENGTH_SHORT).show();
                    return; }
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString()).matches()){
                    Snackbar.make(v,"Email не валиден",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(nameField.getText().toString())) {
                    Snackbar.make(v,"Введите ФИО",Snackbar.LENGTH_SHORT).show();
                    return; }

                if (stats==null) {
                    Snackbar.make(v,"Выберите роль",Snackbar.LENGTH_SHORT).show();
                    return; }

                if (TextUtils.isEmpty(StatusField.getText().toString())) {
                    if (stats==true) Snackbar.make(v,"Введите наименование кафедры",Snackbar.LENGTH_SHORT).show();
                    else Snackbar.make(v,"Введите группу",Snackbar.LENGTH_SHORT).show();
                    return; }

                if (TextUtils.isEmpty(passField.getText().toString())) {
                    Snackbar.make(v,"Введите пароль",Snackbar.LENGTH_SHORT).show();
                    return;}

                if (passField.getText().length()<6) {
                    Snackbar.make(v,"Длина пароля менее 6 символов",Snackbar.LENGTH_SHORT).show();
                    return;}

                if (passField.getText().length()>12) {
                    Snackbar.make(v,"Длина пароля более 12 символов",Snackbar.LENGTH_SHORT).show();
                    return;}

                if (TextUtils.isEmpty(repeatPassField.getText().toString())) {
                    Snackbar.make(v,"Повторите пароль",Snackbar.LENGTH_SHORT).show();
                    return;}

                if (repeatPassField.getText().toString().equals(passField.getText().toString())){

                    auth.createUserWithEmailAndPassword(emailField.getText().toString(),passField.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    String userKey=auth.getCurrentUser().getUid();

                                    User user = new User();
                                    user.setUserEmail(emailField.getText().toString());
                                    user.setUserName(nameField.getText().toString());
                                    user.setUserStatus(StatusField.getText().toString());
                                    user.setUserPass(passField.getText().toString());
                                    user.setUserRole(stats);
                                    user.setUserID(userKey);

                                    myRef.child("USERS").child(userKey).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            finish();
                                            Snackbar.make(v,"Вы зарегистрировались",Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(v,"Ошибка регистрации",Snackbar.LENGTH_LONG).show();
                        }
                    });
                } else  Snackbar.make(v,"Пароль не соответствует введеному выше",Snackbar.LENGTH_SHORT).show();


            }
        });
    }


}


