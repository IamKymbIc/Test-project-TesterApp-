package com.testapp.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.testapp.Models.Question;
import com.testapp.Models.Test;
import com.testapp.Models.User;
import com.testapp.R;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    private List<Question> list;
    private OnItemClickListener mListener;

    public QuestionsAdapter(List<Question> list) {
        this.list = list;
    }

    public interface OnItemClickListener{

        void onClickDelete(int position);
        void onClickItem(int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list_view,parent,false),mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionViewHolder holder, int position) {
        final Question model  = list.get(position);
        holder.questionText.setText(model.getQuestionTitle());
        holder.num.setText(position + 1 + ".");
    }

    @Override
    public int getItemCount() {
        return (list!=null ? list.size(): 0);
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder{

        TextView questionText, num;
        ImageButton delBtn;

        public QuestionViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            questionText = (TextView) itemView.findViewById(R.id. questionsText);
            num = (TextView) itemView.findViewById(R.id. num);

            delBtn = itemView. findViewById(R.id.delQuestionBtn);

            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onClickDelete(position);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onClickItem(position);
                        }
                    }
                }
            });
        }
    }
}
