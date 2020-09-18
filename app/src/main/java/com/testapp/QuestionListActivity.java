package com.testapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.testapp.Models.Question;
import com.testapp.Models.SettingsModel;
import com.testapp.Models.Test;
import com.testapp.adapter.QuestionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuestionListActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference();
    private List<Question> list;
    private QuestionsAdapter adapter;

    public String testKey;
    public Test test = new Test();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        showAlertDialog();
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

    public void finalDialog(){
        MaterialAlertDialogBuilder build = new MaterialAlertDialogBuilder(QuestionListActivity.this);
        build.setTitle(R.string.last_step);
        final View finishView=getLayoutInflater().inflate(R.layout.finish_create_test_layout,null);

        final EditText testPass = finishView.findViewById(R.id.testPass);
        final TextView nowBtn = finishView.findViewById(R.id.nowBtn);
        final TextView later = finishView.findViewById(R.id.laterBtn);

        build.setView(finishView);
        final AlertDialog dialog = build.create();
        dialog.show();

        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(testPass.getText().toString())){
                    test.setTestPass(testPass.getText().toString());
                }

                test.setVisible(false);
                test.setAmountQuestions(list.size()+"");

                myRef.child("TESTS").child(testKey).setValue(test);

                dialog.dismiss();
                finish();
            }
        });

        nowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(testPass.getText().toString())){
                    test.setTestPass(testPass.getText().toString());
                }

                test.setVisible(true);
                test.setAmountQuestions(list.size()+"");

                myRef.child("TESTS").child(testKey).setValue(test);

                dialog.dismiss();
                Toast.makeText(QuestionListActivity.this, "Сохранено", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public void showAlertDialog(){
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        final View view = getLayoutInflater().inflate(R.layout.name_of_test, null);

        builder.setTitle("Введите наименование теста");

        final EditText nameTest= (EditText)view.findViewById(R.id.nameOfTest);
        TextInputLayout nameField = (TextInputLayout) view.findViewById(R.id.name_field);
        builder.setView(view);

        builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        nameField.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nameTest.getText())){
                    Toast.makeText(QuestionListActivity.this, "Заполниет поле", Toast.LENGTH_SHORT).show();
                return;}
                test.setTestTitle(nameTest.getText().toString());
                test.setUserID(auth.getInstance().getCurrentUser().getUid());
                test.setVisible(false);
                testKey = myRef.push().getKey();
                test.setTestID(testKey);
                myRef.child("TESTS").child(testKey).setValue(test);

                dialog.dismiss();
                questionList();
        }
        });






/*        AlertDialog.Builder builder=new AlertDialog.Builder(QuestionListActivity.this);

        final View view=getLayoutInflater().inflate(R.layout.name_of_test,null);

        final EditText nameTest= (EditText)view.findViewById(R.id.nameOfTest);
        TextView addBtn= (TextView)view.findViewById(R.id.addBtn);
        TextView cancelBtn= (TextView)view.findViewById(R.id.cancelBtn);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.cancel();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nameTest.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"Заполните поле", Toast.LENGTH_SHORT).show();
                    return;}

                test.setTestTitle(nameTest.getText().toString());
                test.setUserID(auth.getInstance().getCurrentUser().getUid());
                test.setVisible(false);
                testKey = myRef.push().getKey();
                test.setTestID(testKey);
                myRef.child("TESTS").child(testKey).setValue(test);

                dialog.cancel();
                questionList();
            }
        });*/
    }

    private void addQuestion() {
        Button addQuestionBtn = findViewById(R.id.addQuestionBtn);
        addQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuestionListActivity.this, CreateTestActivity.class);
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

                    finalDialog();

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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(QuestionListActivity.this);
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

                Intent intent = new Intent(QuestionListActivity.this,EditTestActivity.class);
                intent.putExtra("editQuestion",list.get(position).getQuestionID());
                intent.putExtra("testKey",testKey);
                startActivity(intent);
            }
        });
    }
}

