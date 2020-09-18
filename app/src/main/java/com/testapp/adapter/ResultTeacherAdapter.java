package com.testapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.testapp.Models.Result;
import com.testapp.Models.Test;
import com.testapp.Models.User;
import com.testapp.R;

import java.util.List;

public class ResultTeacherAdapter extends RecyclerView.Adapter<ResultTeacherAdapter.ResultViewHolder>{

    private List<Result> list;

    public ResultTeacherAdapter(List<Result> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.result_view,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull final ResultViewHolder holder, int position) {

        holder.myRef.child("USERS").child(list.get(position).getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = new User();
                user = dataSnapshot.getValue(User.class);

                if (user != null){
                holder.textName.setText(user.getUserName());
                holder.textGroup.setText(user.getUserStatus());}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.myRef.child("TESTS").child(list.get(position).getTestID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Test test = new Test();
                test = dataSnapshot.getValue(Test.class);

                if (test!=null){
                holder.testTitle.setText(test.getTestTitle());
                holder.textScoreAll.setText(test.getAmountQuestions());}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.textScore.setText(list.get(position).getScore());
        holder.percent.setText(list.get(position).getPercent() + "%");

    }

    @Override
    public int getItemCount() {
        return (list!=null ? list.size(): 0);
    }

    class ResultViewHolder extends RecyclerView.ViewHolder{

        TextView testTitle, textName, textGroup, textScore, textScoreAll, percent;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference();

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);

            testTitle = (TextView) itemView.findViewById(R.id.testTitleR);
            textName = (TextView) itemView.findViewById(R.id.nameUserR);
            textGroup = (TextView) itemView.findViewById(R.id.nameKafR);
            textScore = (TextView) itemView.findViewById(R.id.scoreR);
            textScoreAll = (TextView) itemView.findViewById(R.id.scoreAllR);
            percent = (TextView) itemView.findViewById(R.id.percent);
        }
    }
}
