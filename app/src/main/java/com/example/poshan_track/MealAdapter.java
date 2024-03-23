package com.example.poshan_track;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private Context context;
    private ArrayList<MealDataHolder> list;
    private DatabaseReference databaseReference;

    public MealAdapter(Context context, ArrayList<MealDataHolder> list, SelectStudent selectStudent) {
        this.context = context;
        this.list = list;
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("meals");
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new MealViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealDataHolder mdh = list.get(position);
        holder.tv_name.setText(mdh.getName());
        holder.tv_cls.setText(mdh.getCls());

        // Set the state of the checkbox
        holder.checkBox.setChecked(mdh.isSelected());

        // Set a listener to update the selected state of the student in the list
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mdh.setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<MealDataHolder> getSelectedStudents() {
        ArrayList<MealDataHolder> selectedStudents = new ArrayList<>();
        for (MealDataHolder student : list) {
            if (student.isSelected()) {
                selectedStudents.add(student);
            }
        }
        return selectedStudents;
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_cls;
        CheckBox checkBox;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_cls = itemView.findViewById(R.id.tv_cls);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}