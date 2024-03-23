package com.example.poshan_track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudentActivity extends AppCompatActivity {

    EditText edtName, edtAge, edtPhone, edtAddr;
    Spinner spinnerClass;
    String SchoolName = "VIT";
    Button btnDone, btnCancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        edtName = findViewById(R.id.editTextName);
        edtAge = findViewById(R.id.editTextAge);
        edtPhone = findViewById(R.id.editTextPhone);
        edtAddr = findViewById(R.id.editTextAddress);
        spinnerClass = findViewById(R.id.spinnerClass);
        btnDone = findViewById(R.id.btnSave);
        btnCancle = findViewById(R.id.btnCancel);

        // Define the array of class options
        final String[] classes = {"LKG", "UKG", "1st Class", "2nd Class", "3rd Class", "4th Class", "5th Class", "6th Class", "7th Class", "8th Class"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerClass.setAdapter(adapter);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sName = edtName.getText().toString();
                String sAge = edtAge.getText().toString();
                String sClass = spinnerClass.getSelectedItem().toString();
                String sPhone = edtPhone.getText().toString();
                String sAddr = edtAddr.getText().toString();


                if (sName.length() == 0 || sAge.length() == 0 || sClass.length() == 0 || sPhone.length() == 0 || sAddr.length() == 0) {
                    Toast.makeText(AddStudentActivity.this, "Please Enter all the Fields...", Toast.LENGTH_SHORT).show();
                } else {

                    Dataholder dh = new Dataholder(sName, sAge, sClass, sPhone, sAddr);
                    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
                    DatabaseReference node = fdb.getReference("/Schools/"+SchoolName+"/"+sClass+"/Student");

                    node.child(sName).setValue(dh);

                    Toast.makeText(AddStudentActivity.this, "Student created", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(AddStudentActivity.this, studentActivity.class));
                    finish();

                }
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddStudentActivity.this, studentActivity.class));
                finish();
            }
        });

    }
}
