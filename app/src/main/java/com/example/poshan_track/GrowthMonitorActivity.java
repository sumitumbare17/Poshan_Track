package com.example.poshan_track;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GrowthMonitorActivity extends AppCompatActivity {

    TextView tv_predate, tv_height, tv_weight;
    EditText edt_height, edt_weight;
    Button btn_submit;
    String className, studentName;
    Double height, weight;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_monitor);

        tv_predate = findViewById(R.id.edt_predate);
        tv_height = findViewById(R.id.tv_hight);
        tv_weight = findViewById(R.id.tv_weight);

        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        btn_submit = findViewById(R.id.btn_submit);

        // Retrieve class name and student name from intent extras
        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        studentName = intent.getStringExtra("studentName");

        // Display the current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = sdf.format(new Date());
        tv_predate.setText(currentDate);

        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference node = fdb.getReference("/Schools/VIT/" + className + "/Student/" + studentName);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    height = Double.parseDouble(edt_height.getText().toString());
                    weight = Double.parseDouble(edt_weight.getText().toString());

                    if (height < 0 || weight < 0) {
                        throw new NumberFormatException();
                    }

                    CareDataHolder cdh = new CareDataHolder( height,weight);

                    // Save care data with the current date as the tag
                    DatabaseReference careRef = node.child("care").child(currentDate);
                    careRef.setValue(cdh);
                    node.child("care").child("height").setValue(height);
                    node.child("care").child("weight").setValue(weight);
                    node.child("care").child("lastupdate").setValue(currentDate);

                    Toast.makeText(GrowthMonitorActivity.this, "Record entered", Toast.LENGTH_SHORT).show();

                    // Navigate to another activity
                    startActivity(new Intent(GrowthMonitorActivity.this, DashboardActivity.class));
                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(GrowthMonitorActivity.this, "Please enter valid values for height and weight", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Read and display care data for the current date if available
    }
}
