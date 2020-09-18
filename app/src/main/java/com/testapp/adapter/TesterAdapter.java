package com.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.testapp.Models.Answer;
import com.testapp.Models.Question;
import com.testapp.R;
import com.testapp.viewHolder.AnswerViewHolder;
import com.testapp.viewHolder.QuestionViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TesterAdapter extends RecyclerView.Adapter<QuestionViewHolder> {

    public Context context;
    public List<Question> questionList;

    public TesterAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    public TesterAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tester_view,parent,false));
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder questionHolder, int position) {

        questionHolder.questionTxt.setText(questionList.get(position).getQuestionTitle());

        List<Answer> answerList = questionList.get(position).getANSWERS();

        TesterAdapter2 adapter2 = new TesterAdapter2(context, answerList);

        questionHolder.recyclerViewAnswer.setAdapter(adapter2);

    }

    @Override
    public int getItemCount() {
        return (questionList!=null ? questionList.size(): 0);
    }

}



