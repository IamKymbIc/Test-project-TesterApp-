package com.testapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.testapp.Models.Test;
import com.testapp.QuestionListActivity;
import com.testapp.R;
import com.testapp.TesterActivity2;
import com.testapp.adapter.StudentTestAdapter;

import java.util.ArrayList;
import java.util.List;

public class StudentTestFragment extends Fragment{
    private static final int LAYOUT = R.layout.fragment;

    private View view;
    private RecyclerView recyclerView;
    private List<Test> list;
    private StudentTestAdapter adapter;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference();

    private ArrayList<String> keys=new ArrayList<>();
    private  Test model = new Test();


    public static StudentTestFragment getInstance(){
        Bundle args = new Bundle();
        StudentTestFragment fragment = new StudentTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(LAYOUT,container,false);

        testList();
        updateList();
        onClickStart();

        return view;
    }

    private void testList(){
        list=new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerViewFragment);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager lim = new LinearLayoutManager(view.getContext());
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lim);

        adapter = new StudentTestAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    private void onClickStart() {
        adapter.setOnItemClickListener(new StudentTestAdapter.OnItemClickListener() {
            @Override
            public void onClickStart(final int position) {

                if (list.get(position).getTestPass()!=null){

                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());

                    final View view=getLayoutInflater().inflate(R.layout.edit_pass_layout,null);

                    final EditText testPass= (EditText)view.findViewById(R.id.editTestPass);
                    final Button nextBtn= view.findViewById(R.id.passBtn);

                    builder.setView(view);
                    final AlertDialog dialog = builder.create();
                    dialog.show();


                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!testPass.getText().toString().equals(list.get(position).getTestPass())){
                                Toast.makeText(view.getContext(), "Не верный пароль", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Intent intent = new Intent(view.getContext(), TesterActivity2.class);
                            intent.putExtra("testID",list.get(position).getTestID());
                            startActivity(intent);

                            dialog.dismiss();
                        }
                    });
                    return;
                }
                Intent intent = new Intent(view.getContext(), TesterActivity2.class);
                intent.putExtra("testID",list.get(position).getTestID());
                startActivity(intent);
            }
        });
    }

    private void updateList(){
        Query query =  myRef.child("TESTS").orderByChild("visible").equalTo(true);

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


}
