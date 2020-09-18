package com.testapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.testapp.Models.Question;
import com.testapp.Models.Test;
import com.testapp.adapter.QuestionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class EditQuestionListActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference();
    private List<Question> list;
    private QuestionsAdapter adapter;

    private String testKey;
    private Test test = new Test();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        Intent intent=getIntent();
        testKey = intent.getStringExtra("onClickTest");

        myRef.child("TESTS").child(testKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                test = snapshot.getValue(Test.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        questionList();
        addQuestion();
        initToolbar();
    }

    private void questionList() {
        list=new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);

        adapter = new QuestionsAdapter(list);
        recyclerView.setAdapter(adapter);

        updateList();
        onClickItemList();
    }

    private void addQuestion() {
        Button addQuestionBtn = findViewById(R.id.addQuestionBtn);
        addQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditQuestionListActivity.this, CreateTestActivity.class);
                intent.putExtra("testKey",testKey);
                startActivity(intent);

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.question_list);

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
                    Toast.makeText(EditQuestionListActivity.this, "Сохранено", Toast.LENGTH_SHORT).show();
                    myRef.child("TESTS").child(testKey).child("amountQuestions").setValue(list.size() + "");
                    finish();
                }

                return true;
            }
        });

        toolbar.inflateMenu(R.menu.menu4create_test);

    }

    private int getItemIndex(Question question) {

        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getQuestionID().equals(question.getQuestionID())) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void updateList(){
        Query query = myRef.child("QUESTIONS").child(testKey).orderByChild("testID").equalTo(testKey);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.add(dataSnapshot.getValue(Question.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged( DataSnapshot dataSnapshot,  String s) {
                Question question = new Question();
                question= dataSnapshot.getValue(Question.class);

                int index = getItemIndex(question);
                list.set(index, question);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Question question = new Question();
                question= dataSnapshot.getValue(Question.class);

                int index = getItemIndex(question);
                list.remove(index);
                adapter.notifyItemRemoved(index);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onClickItemList(){
        adapter.setOnItemClickListener(new QuestionsAdapter.OnItemClickListener() {
            @Override
            public void onClickDelete(final int position) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EditQuestionListActivity.this);
                builder.setTitle("Подтверждение действий");
                builder.setMessage("Удалить вопрос?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myRef.child("QUESTIONS").child(testKey).child(list.get(position).getQuestionID()).removeValue();
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }

            @Override
            public void onClickItem(int position) {

                Intent intent = new Intent(EditQuestionListActivity.this,EditTestActivity.class);
                intent.putExtra("editQuestion",list.get(position).getQuestionID());
                intent.putExtra("testKey",testKey);
                startActivity(intent);
            }
        });
    }
}