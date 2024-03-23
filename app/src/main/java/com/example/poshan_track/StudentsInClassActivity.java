package com.example.poshan_track;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentsInClassActivity extends AppCompatActivity {

    private RecyclerView recyclerViewStudents;
    private StudentListAdapter studentListAdapter;
    private ArrayList<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_in_class);

        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        studentListAdapter = new StudentListAdapter(this, studentList);
        recyclerViewStudents.setAdapter(studentListAdapter);

        // Retrieve the class name from the intent
        String className = getIntent().getStringExtra("className");

        retrieveStudentsFromFirebase(className);
    }

    private void retrieveStudentsFromFirebase(String className) {
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("/Schools/VIT/Classes/" + className + "/Students/");
        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String name = studentSnapshot.child("name").getValue(String.class);
                    String studentClass = studentSnapshot.child("cls").getValue(String.class);
                    Student student = new Student(name, studentClass);
                    studentList.add(student);
                }
                studentListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StudentsInClassActivity.this, "Failed to retrieve student data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
