package com.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.testapp.Models.Answer;
import com.testapp.Models.Question;
import com.testapp.adapter.CreateTestAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.text.TextUtils.isEmpty;

public class EditTestActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton addAnswerBtn;
    private EditText questionField,answerField;

    private RecyclerView recyclerView;
    private List<Answer> list;
    private CreateTestAdapter adapter;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference();

    public String testKey;
    public String editQ;
    public Answer answer =new Answer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);

        Intent intent=getIntent();
        testKey = intent.getStringExtra("testKey");
        editQ = intent.getStringExtra("editQuestion");

        init();
        initToolbar();
        ansList();
    }

    private void ansList() {
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);

        adapter = new CreateTestAdapter(list);
        recyclerView.setAdapter(adapter);

        setQuestion();
        addAnswer();
        onClickList();
        moveItem();

    }
    private void init() {
        questionField = findViewById(R.id.questionField);
        addAnswerBtn = findViewById(R.id.addAnswerBtn);
        answerField = findViewById(R.id.answerField);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_create_test_tb);
        toolbar.setNavigationIcon(R.drawable.arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.finish) {

                    if (isEmpty(questionField.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Введите вопрос", Toast.LENGTH_SHORT).show();
                        return false;}

                    if (list.size()==0) {
                        Toast.makeText(getApplicationContext(), "Добавьте варианты ответов", Toast.LENGTH_SHORT).show();
                        return false;}

                    editQuestion();

                    finish();

                }
                return true;
            }
        });

        toolbar.inflateMenu(R.menu.menu4create_test);
    }

    private void moveItem() {
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(list, position_dragged,position_target);
                adapter.notifyItemMoved(position_dragged,position_target);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        helper.attachToRecyclerView(recyclerView);
    }

    private void onClickList(){
        adapter.setOnItemClickListener(new CreateTestAdapter.OnItemClickListener() {
            @Override
            public void onClickDelete(int position) {
                list.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onClickItem(int position) {
                answerField.setText(list.get(position).getAnswerTitle());
                list.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

    }

    private void addAnswer(){
        addAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(answerField.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Введите текст", Toast.LENGTH_SHORT).show();
                    return;
                }

                Answer answer =new Answer();
                answer.setAnswerTitle(answerField.getText().toString());
                list.add(answer);
                adapter.notifyDataSetChanged();
                answerField.setText("");
            }
        });
    }

    private void setQuestion(){
        myRef.child("QUESTIONS").child(testKey).child(editQ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Question question = new Question();
                question = dataSnapshot.getValue(Question.class);
                if (question!=null){
                    questionField.setText(question.getQuestionTitle());

                    for (DataSnapshot ds: dataSnapshot.child("ANSWERS").getChildren()) {
                        Answer answer = new Answer();

                        answer = ds.getValue(Answer.class);
                        list.add(answer);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void editQuestion(){

        myRef.child("QUESTIONS").child(testKey).child(editQ).child("questionTitle").setValue(questionField.getText().toString());

        myRef.child("QUESTIONS").child(testKey).child(editQ).child("ANSWERS").setValue(list);

    }
}