package com.example.poshan_track;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class stu_profile extends AppCompatActivity {
    TextView name, fname, age, cls, phone, tv_height, tv_weight, lastupdate,tv_updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_profile);

        name = findViewById(R.id.tv_wel);
        fname = findViewById(R.id.tv_name);
        age = findViewById(R.id.tv_age);
        cls = findViewById(R.id.tv_class);
        phone = findViewById(R.id.tv_phone);
        tv_height = findViewById(R.id.tv_height);
        tv_weight = findViewById(R.id.tv_weight);
        lastupdate = findViewById(R.id.tv_update_date);
        tv_updated =findViewById(R.id.tv_updated);
        // Get student profile information from intent extras
        String studentName = getIntent().getStringExtra("Bname");
        String studentAge = getIntent().getStringExtra("Bage");
        String studentClass = getIntent().getStringExtra("Bclass");
        String studentPhone = getIntent().getStringExtra("Bphone");

        // Set student profile information to TextViews
        name.setText(studentName);
        age.setText(studentAge);
        cls.setText(studentClass);
        phone.setText(studentPhone);
        fname.setText(studentName);

        // Retrieve the care details
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference node = fdb.getReference("/Schools/VIT/" + studentClass + "/Student/" + studentName + "/care");

        // Attach a listener for the query
        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if any data exists
                if (snapshot.exists()) {
                    // Deserialize the data into a CareDataHolder object
                    Double height = snapshot.child("height").getValue(Double.class);
                    Double weight = snapshot.child("weight").getValue(Double.class);
                    String lastupdatestr = snapshot.child("lastupdate").getValue(String.class);

                    // Display the fetched details
                    if (height != null && weight != null) {
                        tv_height.setText(String.valueOf(height));
                        tv_weight.setText(String.valueOf(weight));
                        lastupdate.setText(lastupdatestr);
                        tv_updated.setText(lastupdatestr);
                    } else {
                        // If data is missing, display N/A
                        tv_height.setText("N/A");
                        tv_weight.setText("N/A");
                    }
                    // Set the last update text
                } else {
                    // If no data is found, display N/A
                    tv_height.setText("N/A");
                    tv_weight.setText("N/A");
                    lastupdate.setText("N/A");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }
}
