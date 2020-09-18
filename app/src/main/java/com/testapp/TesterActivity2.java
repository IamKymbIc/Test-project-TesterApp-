package com.testapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.testapp.Models.Answer;
import com.testapp.Models.Question;
import com.testapp.Models.Result;
import com.testapp.Models.Test;
import com.testapp.Models.User;
import com.testapp.adapter.TesterAdapter2;

import java.util.ArrayList;
import java.util.List;

import static android.R.color.transparent;

public class TesterActivity2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textQuestion, textTitle, userName, userStatus, amountQuestions;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private DatabaseReference myRes = database.getReference("RESULTS");
    private FirebaseAuth auth;
    private TesterAdapter2 adapter;

    private List<Question> questionList;
    private List<Answer> answerList;

    private LinearLayout linearLayout;
    private ConstraintLayout maneLayout;
    private ProgressBar pb;
    public String testID;


    public Boolean res;

    public Button startBtn, finishBtn;
    public int i =0;
    public int scoreF =0;
    int counter=0;
    int allCounter = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester2);

        testID = getIntent().getExtras().get("testID").toString();

        init();
        initToolbar();
        initList();

    }


    private void initToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);

        myRef.child("TESTS").child(testID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Test test = new Test();
                test = dataSnapshot.getValue(Test.class);

                if (test!=null)
                    toolbar.setTitle(test.getTestTitle());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void init() {
        pb= findViewById(R.id.pb);

        linearLayout = findViewById(R.id.infoLayout);
        maneLayout = findViewById(R.id.maneLayout);
        startBtn = findViewById(R.id.startBtn);
        finishBtn = findViewById(R.id.finishBtn);

        textQuestion = findViewById(R.id.textQuestion);

        textTitle = findViewById(R.id.textTitle);
        userName = findViewById(R.id.userName);
        userStatus = findViewById(R.id.userStatus);
        amountQuestions = findViewById(R.id.amountQuestions);

        myRef.child("TESTS").child(testID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Test test = new Test();
                test = dataSnapshot.getValue(Test.class);

                if (test != null) {
                    textTitle.setText(test.getTestTitle());
                    amountQuestions.setText("Всего вопросов: " + test.getAmountQuestions());
                    allCounter = Integer.parseInt(test.getAmountQuestions());
                    pb.setMax(allCounter);

                    myRef.child("USERS").child(test.getUserID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = new User();
                            user = dataSnapshot.getValue(User.class);

                            userName.setText(user.getUserName());
                            userStatus.setText("Кафедра: " + user.getUserStatus());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initList() {
        questionList = new ArrayList<>();
        answerList = new ArrayList<>();


        recyclerView = findViewById(R.id.testerList);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);

        adapter = new TesterAdapter2(getApplicationContext(), answerList);
        recyclerView.setAdapter(adapter);

        setData();

    }

    private void setData() {
        myRef.child("QUESTIONS").child(testID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    final Question question = new Question();

                    question.setQuestionTitle(snapshot.child("questionTitle").getValue(true).toString());

                    GenericTypeIndicator<ArrayList<Answer>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Answer>>() {};
                    question.setANSWERS(snapshot.child("ANSWERS").getValue(genericTypeIndicator));

                    questionList.add(question);

                    startBtn.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onClick(View v) {

                           counter++;
                            pb.setProgress(counter);

                            linearLayout.setVisibility(View.INVISIBLE);
                            maneLayout.setBackgroundColor(transparent);

                            scoreF = scoreF + adapter.score;

                            if (i <= questionList.size()) {
                                textQuestion.setText(questionList.get(i).getQuestionTitle());
                                answerList = questionList.get(i).getANSWERS();

                                adapter = new TesterAdapter2(getApplicationContext(), answerList);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                i++;

                                startBtn.setText("Далее");

                                if (i == questionList.size()) {
                                    startBtn.setVisibility(View.INVISIBLE);
                                    finishBtn.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });

                    finishBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scoreF = scoreF + adapter.score;

                            Result result = new Result();

                            String resultKey=myRes.child(testID).push().getKey();

                            result.setResultID(resultKey);
                            result.setUserID(auth.getInstance().getCurrentUser().getUid());
                            result.setTestID(testID);
                            result.setScore(scoreF + "");

                            float percent = scoreF * 100 / i;

                            result.setPercent(percent + "");

                            myRes.child(resultKey).setValue(result);

                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
