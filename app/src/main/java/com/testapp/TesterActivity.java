package com.testapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
import com.testapp.adapter.TesterAdapter;
import com.testapp.adapter.TesterAdapter2;
import com.testapp.viewHolder.AnswerViewHolder;
import com.testapp.viewHolder.QuestionViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TesterActivity extends AppCompatActivity {

    private String testID;
    private String questionID;

    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private FirebaseAuth auth;

    private TesterAdapter adapter;
    private List<Question> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester);

        testID = getIntent().getExtras().get("testID").toString();

        initToolbar();
        initList();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);

        myRef.child("TESTS").child(testID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Test test = new Test();
                test = dataSnapshot.getValue(Test.class);
                toolbar.setTitle(test.getTestTitle());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.finish) {
                    for (int i=0; i<list.size();i++){

                    }
                    Result result = new Result();

                    String resultKey=myRef.child("RESULTS").child(testID).push().getKey();

                    result.setResultID(resultKey);
                    result.setUserID(auth.getInstance().getCurrentUser().getUid());
                    result.setTestID(testID);

                    myRef.child("RESULTS").child(testID).child(resultKey).setValue(result);

                    finish();
                }

                return true;
            }
        });

        toolbar.inflateMenu(R.menu.menu4create_test);


    }

    private void initList() {
        list = new ArrayList<>();

        recyclerView = findViewById(R.id.recycleViewTester);

        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);

        adapter = new TesterAdapter(this, list);
        recyclerView.setAdapter(adapter);

        setData();
    }

    public void setData() {

        myRef.child("QUESTIONS").child(testID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot questionSnapshot:dataSnapshot.getChildren()){

                    final Question question = new Question();

                    questionID = questionSnapshot.getKey();

                    question.setQuestionTitle(questionSnapshot.child("questionTitle").getValue(true).toString());

                    GenericTypeIndicator<ArrayList<Answer>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Answer>>() {};
                    question.setANSWERS(questionSnapshot.child("ANSWERS").getValue(genericTypeIndicator));

                    list.add(question);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}