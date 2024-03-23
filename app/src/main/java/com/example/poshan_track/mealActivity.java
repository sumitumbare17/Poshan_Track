package com.example.poshan_track;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class mealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CardView cardGvMeal = findViewById(R.id.cardGvMeal);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CardView cardmeal = findViewById(R.id.cardmeal);

        cardGvMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mealActivity.this,AddMeal.class));
            }
        });

        cardmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mealActivity.this,MealDetailsActivity.class));

            }
        });
    }
}