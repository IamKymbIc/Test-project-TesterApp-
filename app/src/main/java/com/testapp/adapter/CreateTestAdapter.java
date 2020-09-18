package com.testapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.testapp.Models.Answer;
import com.testapp.R;

import java.util.List;


public class CreateTestAdapter extends RecyclerView.Adapter<CreateTestAdapter.AdapterViewHolder>{

    private List<Answer>list;
    private RadioButton selectedRadioButton;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onClickDelete(int position);
        void onClickItem(int position);
    }

    public CreateTestAdapter(RadioButton selectedRadioButton) {
        this.selectedRadioButton = selectedRadioButton;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public CreateTestAdapter(List<Answer> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.create_test_view,parent,false),mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder holder, final int position) {
        final Answer model = list.get(position);

        holder.number.setText(position + 1 + ".");
        holder.ansText.setText(model.getAnswerTitle());

        if (model.getSuccess() != null){
        holder.rBtn.setChecked(model.getSuccess());}

        holder.rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Answer answer: list)
                    answer.setSuccess(false);

                list.get(position).setSuccess(true);

                if(null != selectedRadioButton && !v.equals(selectedRadioButton))
                    selectedRadioButton.setChecked(false);

                selectedRadioButton = (RadioButton) v;
                selectedRadioButton.setChecked(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list!=null ? list.size(): 0);
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder{

        TextView ansText, number;
        ImageButton delBtn;
        RadioButton rBtn;


        public AdapterViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            ansText = (TextView) itemView.findViewById(R.id.ansT);
            number = (TextView) itemView.findViewById(R.id.numAns);
            delBtn = (ImageButton) itemView.findViewById(R.id.delAnsBtn);
            rBtn = (RadioButton) itemView.findViewById(R.id.choiceAns);


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

