package com.testapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.testapp.Models.Test;
import com.testapp.Models.User;
import com.testapp.R;

import java.util.List;

public class StudentTestAdapter extends RecyclerView.Adapter<StudentTestAdapter.StudentViewHolder>{

    private List<Test> studentList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onClickStart(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public StudentTestAdapter(List<Test> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_stud_view,parent,false),mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentViewHolder holder, final int position) {
        final Test model = studentList.get(position);

        holder.testTitle.setText(model.getTestTitle());

        if (model.getTestPass() == null){
            holder.testPassword.setText("Доступ: Открыт");
        } else
            holder.testPassword.setText("Доступ: По паролю");

        holder.userRef.child(model.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = new User();
                user= dataSnapshot.getValue(User.class);
                holder.userName.setText(user.getUserName());
                holder.userStatus.setText("Кафедра: "+user.getUserStatus());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return (studentList!=null ? studentList.size(): 0);
    }

    class StudentViewHolder extends RecyclerView.ViewHolder{

        TextView testTitle,userName,userStatus, testPassword;
        ImageButton startTest;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef=database.getReference("USERS");


        public StudentViewHolder(@NonNull  View itemView, final OnItemClickListener listener) {
            super(itemView);

            startTest = (ImageButton) itemView.findViewById(R.id.startBtn);
            testTitle = (TextView) itemView.findViewById(R.id.testTitleS);
            userName = (TextView) itemView.findViewById(R.id.nameUserS);
            userStatus = (TextView) itemView.findViewById(R.id.nameKafS);
            testPassword = (TextView) itemView.findViewById(R.id.testPass);

            startTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onClickStart(position);
                        }
                    }
                }
            });


        }

    }

}
