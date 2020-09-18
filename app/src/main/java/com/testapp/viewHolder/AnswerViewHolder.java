package com.testapp.viewHolder;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.testapp.R;
import com.testapp.adapter.TesterAdapter2;

public class AnswerViewHolder extends RecyclerView.ViewHolder {
    public TextView answerTxt;
    public RadioButton checkedAnswer;

    public AnswerViewHolder(@NonNull View itemView) {
        super(itemView);

        answerTxt = (TextView) itemView.findViewById(R.id.answerTxt);
        checkedAnswer = itemView.findViewById(R.id.choiceAnswer);

    }
}
