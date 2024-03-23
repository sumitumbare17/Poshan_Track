package com.example.poshan_track;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerActivity extends AppCompatActivity {

    TextView lgn;
    EditText sch_name, city, district, per_name, reg_email, reg_password;
    Button reg_button;

    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(registerActivity.this,DashboardActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        lgn = findViewById(R.id.login_text);
        sch_name = findViewById(R.id.sch_name);
        city = findViewById(R.id.city);
        district = findViewById(R.id.district);
        per_name = findViewById(R.id.Per_name);
        reg_email = findViewById(R.id.reg_email);
        reg_password = findViewById(R.id.reg_password);
        reg_button = findViewById(R.id.reg_button);



        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String scName = sch_name.getText().toString();
                String City = city.getText().toString();
                String Dist = district.getText().toString();
                String PerName = per_name.getText().toString();
                String Uemail = reg_email.getText().toString();
                String Upass = reg_password.getText().toString();

                if (scName.length() == 0 || City.length() == 0 || Dist.length() == 0 || PerName.length() == 0 || Uemail.length() == 0 || Upass.length() == 0) {
                    Toast.makeText(registerActivity.this, "Please Enter all the Fields...", Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.createUserWithEmailAndPassword(Uemail, Upass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        /*FirebaseDatabase fdb = FirebaseDatabase.getInstance();
                                        DatabaseReference node = fdb.getReference("/School");

                                        node.setValue(scName);*/

                                        Toast.makeText(registerActivity.this, "Account Created.",
                                                Toast.LENGTH_SHORT).show();



                                        startActivity(new Intent(registerActivity.this,Login.class));

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(registerActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });

        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(registerActivity.this,Login.class);
                startActivity(i);
            }
        });
    }
}