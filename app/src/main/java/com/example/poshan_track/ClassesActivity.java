package com.example.poshan_track;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassesActivity extends AppCompatActivity implements ClassAdapter.OnClassClickListener {

    private RecyclerView recyclerView;
    public static final int VIEW_STUDENTS = 1;
    public static final int SELECT_STUDENTS_FOR_MEAL = 2;
    private int purpose; // Purpose of the activity

    private ClassAdapter adapter;
    private ArrayList<ClassModel> classList;
    private DatabaseReference databaseReference;
    String mealName, currentDate,imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        Intent intent2 = getIntent();
        mealName = intent2.getStringExtra("mealName");
        imageUri = intent2.getStringExtra("imageUri");
        currentDate = intent2.getStringExtra("currentDate");

        purpose = getIntent().getIntExtra("purpose", VIEW_STUDENTS);

        recyclerView = findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        classList = new ArrayList<>();
        adapter = new ClassAdapter(classList, this);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("/Schools/VIT");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String className = dataSnapshot.getKey();
                    classList.add(new ClassModel(className));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClassesActivity.this, "Failed to fetch classes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClassClick(int position) {
        // Get the selected class model
        ClassModel selectedClass = classList.get(position);
        if (purpose == VIEW_STUDENTS) {
            Intent intent = new Intent(ClassesActivity.this, studentActivity.class);
            intent.putExtra("className", selectedClass.getClassName());
            startActivity(intent);
        } else if (purpose == SELECT_STUDENTS_FOR_MEAL) {
            // For selecting students for meals

            Intent intent = new Intent(ClassesActivity.this, SelectStudent.class);
            intent.putExtra("mealName", mealName);
            intent.putExtra("imageUri", imageUri);
            intent.putExtra("currentDate", currentDate);
            intent.putExtra("className", selectedClass.getClassName());

            startActivity(intent);        }


        // Start SelectStudent activity and send intent with selected class name

    }
}


class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private ArrayList<ClassModel> classList;
    private OnClassClickListener mListener;

    public interface OnClassClickListener {
        void onClassClick(int position);
    }

    public ClassAdapter(ArrayList<ClassModel> classList, OnClassClickListener listener) {
        this.classList = classList;
        this.mListener = listener;
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView classNameTextView;

        public ClassViewHolder(View itemView) {
            super(itemView);
            classNameTextView = itemView.findViewById(R.id.classNameTextView);
        }
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        final int pos = position;
        ClassModel classModel = classList.get(position);
        holder.classNameTextView.setText(classModel.getClassName());

        // Handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClassClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }
}

class ClassModel {
    private String className;

    public ClassModel() {
        // Default constructor required for Firebase
    }

    public ClassModel(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
