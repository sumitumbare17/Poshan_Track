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

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsAdapter.OnMealClickListener, MealDetailsAdapter.OnBeneficiaryClickListener {

    private RecyclerView recyclerViewMealDetails;
    private MealDetailsAdapter mealDetailsAdapter;
    private ArrayList<Meal> mealList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        recyclerViewMealDetails = findViewById(R.id.recyclerViewMealDetails);
        recyclerViewMealDetails.setLayoutManager(new LinearLayoutManager(this));

        mealList = new ArrayList<>();
        mealDetailsAdapter = new MealDetailsAdapter(this, mealList, this, this);
        recyclerViewMealDetails.setAdapter(mealDetailsAdapter);

        retrieveMealDetailsFromFirebase();
    }

    private void retrieveMealDetailsFromFirebase() {
        DatabaseReference mealsRef = FirebaseDatabase.getInstance().getReference("meals");
        mealsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mealList.clear();
                for (DataSnapshot yearSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                        for (DataSnapshot daySnapshot : monthSnapshot.getChildren()) {
                            String mealName = daySnapshot.child("name").getValue(String.class);
                            String mealDate = daySnapshot.child("date").getValue(String.class);
                            String mealImageURL = daySnapshot.child("imageURL").getValue(String.class);
                            Meal meal = new Meal(mealName, mealDate, mealImageURL);
                            mealList.add(meal);
                        }
                    }
                }
                mealDetailsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MealDetailsActivity.this, "Failed to retrieve meal details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMealClick(Meal meal) {
        // Handle meal click
        Intent intent = new Intent(this, BeneficiaryStudentsActivity.class);
        intent.putExtra("currentDate", meal.getDate());
      //  startActivity(intent);
    }

    @Override
    public void onBeneficiaryClick(Meal meal) {
        Intent intent = new Intent(this, BeneficiaryStudentsActivity.class);
        intent.putExtra("currentDate", meal.getDate());
        startActivity(intent);

    }
}
