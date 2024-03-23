package com.example.poshan_track;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
public class SelectStudent extends AppCompatActivity {

    RecyclerView recview;
    DatabaseReference db;
    MealAdapter mealAdapter;
    Button submitButton;

    ArrayList<MealDataHolder> list;
    String cls ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student);

        // Retrieve intent extras
        Intent intent = getIntent();
        final String mealName = intent.getStringExtra("mealName");
        final Uri imageUri = Uri.parse(intent.getStringExtra("imageUri"));
        final String currentDate = intent.getStringExtra("currentDate");
        cls = intent.getStringExtra("className");

        recview = findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseDatabase.getInstance().getReference("/Schools/"+"VIT"+"/"+cls+"/Student");
        recview.setHasFixedSize(true);

        list = new ArrayList<>();
        mealAdapter = new MealAdapter((Context) this, list, this);
        recview.setAdapter(mealAdapter);

        submitButton = findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitMealDetails(mealName, imageUri, currentDate);
            }
        });

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MealDataHolder mdh = dataSnapshot.getValue(MealDataHolder.class);
                    list.add(mdh);
                }
                mealAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void submitMealDetails(String mealName, Uri imageUri, String currentDate) {
        // Get the selected students from the adapter
        ArrayList<MealDataHolder> selectedStudents = mealAdapter.getSelectedStudents();

        // Check if any students are selected
        if (selectedStudents.isEmpty()) {
            Toast.makeText(this, "Please select at least one student", Toast.LENGTH_SHORT).show();
            return;
        }

        // Iterate through selected students and append meal details for each student to the Firebase database
        for (final MealDataHolder student : selectedStudents) {
            // Append meal details to the database
            DatabaseReference mealRef = FirebaseDatabase.getInstance().getReference()
                    .child("/Schools/"+"VIT"+"/"+cls+"/Student/"+student.getName())
                    .child("meal_Details")
                    .child(currentDate);

            Meal meal = new Meal(mealName, currentDate, imageUri.toString());
            mealRef.setValue(meal)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SelectStudent.this, "Meal details submitted For  ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SelectStudent.this, "Failed to submit meal details for " + student.getName(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        finish();
    }
}
