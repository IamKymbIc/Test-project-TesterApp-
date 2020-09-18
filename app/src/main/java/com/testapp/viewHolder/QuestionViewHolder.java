package com.testapp.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.testapp.R;

public class QuestionViewHolder extends RecyclerView.ViewHolder {
    public TextView questionTxt;
    public RecyclerView recyclerViewAnswer;


    public QuestionViewHolder(@NonNull View itemView) {
        super(itemView);

        questionTxt = itemView.findViewById(R.id.questionTxt);
        recyclerViewAnswer = itemView.findViewById(R.id.recycleViewAnswer);
        
        LinearLayoutManager lim = new LinearLayoutManager(itemView.getContext());
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAnswer.setLayoutManager(lim);
    }
}
