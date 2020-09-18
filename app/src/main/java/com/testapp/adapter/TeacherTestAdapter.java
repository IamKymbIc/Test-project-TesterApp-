package com.testapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.testapp.EditTestActivity;
import com.testapp.Models.Result;
import com.testapp.Models.Test;
import com.testapp.Models.User;
import com.testapp.QuestionListActivity;
import com.testapp.R;
import com.testapp.SettingsActivity;

import java.util.List;

public class TeacherTestAdapter extends RecyclerView.Adapter<TeacherTestAdapter.TestViewHolder>{

    private List<Test> list;
    private Context context;

    private OnItemClickListener mListener;

    public TeacherTestAdapter(List<Test> list, Context context) {
        this.list = list;
        this.context = context;
    }


    public interface OnItemClickListener{

       // void onClickDelete(int position);
        void onClickItem(int position);


    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_list_view, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final TestViewHolder holder, final int position) {
        final Test model = list.get(position);

        holder.testTitle.setText(model.getTestTitle());
        holder.amountQuestions.setText("Вопросов: " + model.getAmountQuestions());

        if (model.getVisible().equals(false))
            holder.testAccess.setText("Статус: Скрыт");
        else holder.testAccess.setText("Статус: Доступен другим пользователям");

        if (model.getTestPass()!=null) {
            holder.testPassword.setText("Пароль: " + model.getTestPass());
        } else
            holder.testPassword.setText("Пароль: Без пароля");

        holder.contextMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.contextMenu);
                popupMenu.inflate(R.menu.option_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.edit: {
                                    Intent intent = new Intent(context, SettingsActivity.class);
                                    intent.putExtra("testKey",list.get(position).getTestID());
                                    context.startActivity(intent);
                                return true;}
                                case R.id.delete:{
                                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                                    builder.setTitle("Удалить "  + list.get(position).getTestTitle() + "?");
                                    builder.setMessage("Все результаты связанные с этим тестом также будут удалены.");
                                    builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Query query = holder.myRef.child("RESULTS").orderByChild("testID").equalTo(list.get(position).getTestID());
                                            query.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                                                        Result result = new Result();
                                                        result = ds.getValue(Result.class);

                                                        assert result != null;
                                                        holder.myRef.child("RESULTS").child(result.getResultID()).removeValue();
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                            holder.myRef.child("QUESTIONS").child(list.get(position).getTestID()).removeValue();
                                            holder.myRef.child("TESTS").child(list.get(position).getTestID()).removeValue();
                                        }
                                    });

                                    builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();
                                return true;}
                                default:return false;
                            }
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list!=null ? list.size(): 0);
    }

    static class  TestViewHolder extends RecyclerView.ViewHolder{

        TextView testTitle,amountQuestions,testAccess, testPassword, contextMenu;
        private FirebaseDatabase database = FirebaseDatabase.getInstance();
        private DatabaseReference myRef = database.getReference();
        //ImageButton del;

        public TestViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            testTitle = (TextView) itemView.findViewById(R.id.testTitle);
            amountQuestions = (TextView) itemView.findViewById(R.id.amountQuestion);
            testAccess = (TextView) itemView.findViewById(R.id.testAccess);
            testPassword = (TextView) itemView.findViewById(R.id.testPassword);
            contextMenu =(TextView) itemView.findViewById(R.id.contextMenu);
           // del = (ImageButton) itemView.findViewById(R.id.delTestBtn);

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


/*            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onClickDelete(position);
                        }
                    }
                }
            });*/
        }
    }
}
