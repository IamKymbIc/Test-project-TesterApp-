package com.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.testapp.Models.Answer;
import com.testapp.R;

import java.util.List;

public class TesterAdapter2 extends RecyclerView.Adapter<TesterAdapter2.AnswerViewHolder> {

    public Context context;
    public List<Answer> list;
    public RadioButton selectedRadioButton;
    public OnItemClickListener mListener;
    public int score;

    public TesterAdapter2(Context context, List<Answer> list) {
        this.context = context;
        this.list = list;
    }

    public TesterAdapter2(int score) {
        this.score = score;
    }

    public interface  OnItemClickListener{
        void onRBtnClick(int position);
    }

public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnswerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tester_view_answer,parent,false),mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder answerHolder, final int position) {
        answerHolder.answerTxt.setText(list.get(position).getAnswerTitle());

        answerHolder.num.setText(position + 1 + ".");

       final Boolean checked = list.get(position).getSuccess();

        answerHolder.checkedAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Answer answer:list)
                    answer.setSuccess(false);

                list.get(position).setSuccess(true);
                if(null != selectedRadioButton && !v.equals(selectedRadioButton))
                    selectedRadioButton.setChecked(false);

                selectedRadioButton = (RadioButton) v;
                selectedRadioButton.setChecked(true);


                if (list.get(position).getSuccess().equals(checked.booleanValue() && checked.equals(true))){
                    score++;
                } else score = 0;

            }
        });
    }

    @Override
    public int getItemCount() {
        return (list!=null ? list.size(): 0);
    }


    class AnswerViewHolder extends RecyclerView.ViewHolder{
        public TextView answerTxt, num;
        public RadioButton checkedAnswer;

        public AnswerViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            answerTxt = (TextView) itemView.findViewById(R.id.answerTxt);
            num = (TextView) itemView.findViewById(R.id.numAnswer);
            checkedAnswer = itemView.findViewById(R.id.choiceAnswer);

            checkedAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onRBtnClick(position);
                        }
                    }
                }
            });
        }
    }

}
