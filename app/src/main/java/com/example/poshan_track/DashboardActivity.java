package com.example.poshan_track;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser user;

    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent i = new Intent();
        mode = i.getIntExtra("mode",1);
        CardView stu = findViewById(R.id.cardStu);
        CardView meal = findViewById(R.id.cardmeal);
        CardView prg = findViewById(R.id.cardProgress);


        TextView tv_name = findViewById(R.id.tv_name);
        ImageView lgout = findViewById(R.id.imgLogOut);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user == null){
            startActivity(new Intent(DashboardActivity.this, Login.class));
            finish();
        }

        else{
            tv_name.setText(user.getEmail());
        }

        lgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DashboardActivity.this, Login.class));
                finish();
            }
        });


        stu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, ClassesActivity.class);
                    intent.putExtra("purpose", ClassesActivity.VIEW_STUDENTS);
                    startActivity(intent);

            }
        });

        meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,mealActivity.class));
            }
        });
        prg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,ChartActivity.class));

            }
        });

    }
}