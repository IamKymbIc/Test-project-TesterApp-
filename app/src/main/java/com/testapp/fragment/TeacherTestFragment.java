package com.testapp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.testapp.EditQuestionListActivity;
import com.testapp.Models.Question;
import com.testapp.Models.Result;
import com.testapp.Models.Test;
import com.testapp.QuestionListActivity;
import com.testapp.R;
import com.testapp.Teacher_Activity;
import com.testapp.adapter.TeacherTestAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeacherTestFragment extends Fragment {

    private static final int LAYOUT = R.layout.techer_test_list_fragment;

    private View view;
    private RecyclerView recyclerView;
    private List<Test> list;
    private TeacherTestAdapter adapter;

    private FloatingActionButton addActionBtn;

    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private Question question = new Question();


    public static TeacherTestFragment getInstance() {
        Bundle args = new Bundle();
        TeacherTestFragment fragment = new TeacherTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);


        initActionBtn();
        testList();
        updateList();
        onClickList();

        return view;
    }

    private void initActionBtn() {
        addActionBtn = view.findViewById(R.id.floatingAddTest);
        addActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), QuestionListActivity.class));
            }
        });
    }

    private void testList(){
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerViewFragmentTeacher);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager lim = new LinearLayoutManager(view.getContext());
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);

        adapter = new TeacherTestAdapter(list,view.getContext());
        recyclerView.setAdapter(adapter);
    }

    private int getItemIndex(Test test) {

        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTestID().equals(test.getTestID())) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void updateList(){
        String userKey=auth.getInstance().getCurrentUser().getUid();

        final Query query = myRef.child("TESTS").orderByChild("userID").equalTo(userKey);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                list.add(dataSnapshot.getValue(Test.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Test model = new Test();
                model = dataSnapshot.getValue(Test.class);

                int index = getItemIndex(model);
                list.set(index, model);
                adapter.notifyItemChanged(index);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Test model = new Test();
                model = dataSnapshot.getValue(Test.class);

                int index = getItemIndex(model);
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

    private void onClickList(){
        adapter.setOnItemClickListener(new TeacherTestAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(int position) {

                Intent intent = new Intent(view.getContext(), EditQuestionListActivity.class);
                intent.putExtra("onClickTest",list.get(position).getTestID());
                startActivity(intent);

            }

        });
    }

}
