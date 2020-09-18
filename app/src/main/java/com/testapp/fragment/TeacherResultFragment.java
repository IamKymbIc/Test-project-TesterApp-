package com.testapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.testapp.Models.Result;
import com.testapp.Models.Test;
import com.testapp.R;
import com.testapp.adapter.ResultTeacherAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeacherResultFragment extends Fragment {
    private static final int LAYOUT = R.layout.fragment;

    private View view;
    private RecyclerView recyclerView;
    private List<Result> list;
    private ResultTeacherAdapter adapter;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference();
    private FirebaseAuth auth;

    public static TeacherResultFragment getInstance(){
        Bundle args = new Bundle();
        TeacherResultFragment fragment = new TeacherResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container,false);

        initList();

        return view;
    }

    private void initList() {

        list=new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerViewFragment);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(view.getContext());
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);

        adapter = new ResultTeacherAdapter(list);
        recyclerView.setAdapter(adapter);

        setData();
    }

    private void setData() {
        String userKey = auth.getInstance().getCurrentUser().getUid();

        Query query = myRef.child("TESTS").orderByChild("userID").equalTo(userKey);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sh: dataSnapshot.getChildren()){
                    Test test = new Test();
                    test = sh.getValue(Test.class);

                    Query query1 = myRef.child("RESULTS").orderByChild("testID").equalTo(test.getTestID());

                    query1.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            list.add(dataSnapshot.getValue(Result.class));
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Result model = new Result();
                            model = dataSnapshot.getValue(Result.class);

                            int index = getItemIndex(model);
                            list.set(index, model);
                            adapter.notifyItemChanged(index);
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                            Result model = new Result();
                            model = dataSnapshot.getValue(Result.class);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private int getItemIndex(Result result) {

        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getResultID().equals(result.getResultID())) {
                index = i;
                break;
            }
        }
        return index;
    }

}

