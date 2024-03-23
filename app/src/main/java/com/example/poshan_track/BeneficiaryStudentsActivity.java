package com.example.poshan_track;

import android.content.Intent;
import android.os.Bundle;
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

public class BeneficiaryStudentsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBeneficiaries;
    private BeneficiaryStudentsAdapter beneficiaryStudentsAdapter;
    private ArrayList<Student> studentList;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_students);

        Intent intent = getIntent();
        date = intent.getStringExtra("currentDate");

        recyclerViewBeneficiaries = findViewById(R.id.recyclerViewBeneficiaries);
        recyclerViewBeneficiaries.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        beneficiaryStudentsAdapter = new BeneficiaryStudentsAdapter(this, studentList);
        recyclerViewBeneficiaries.setAdapter(beneficiaryStudentsAdapter);

        retrieveBeneficiaryStudentsFromFirebase(date);
    }

    private void retrieveBeneficiaryStudentsFromFirebase(final String mealDate) {
        DatabaseReference schoolRef = FirebaseDatabase.getInstance().getReference("/Schools/VIT/");
        schoolRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();

                // Iterate over each class
                for (DataSnapshot classSnapshot : dataSnapshot.getChildren()) {
                    // Get the reference to the students under the current class
                    DatabaseReference studentRef = classSnapshot.child("Student").getRef();

                    // Retrieve students for the current class
                    studentRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                if (studentSnapshot.hasChild("meal_Details/" + mealDate)) {
                                    String name = studentSnapshot.child("name").getValue(String.class);
                                    String studentClass = studentSnapshot.child("cls").getValue(String.class);
                                    Student student = new Student(name, studentClass);
                                    studentList.add(student);
                                    System.out.println("name: " + name);
                                }
                            }
                            beneficiaryStudentsAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(BeneficiaryStudentsActivity.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println("Failed to retrieve data: " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BeneficiaryStudentsActivity.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Failed to retrieve data: " + databaseError.getMessage());
            }
        });
    }
}



class Student {
    private String name;
    private String cls;

    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }

    public Student(String name, String cls) {
        this.name = name;
        this.cls = cls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }
}