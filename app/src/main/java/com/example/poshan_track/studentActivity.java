package com.example.poshan_track;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class studentActivity extends AppCompatActivity implements SelectListener {

    FloatingActionButton addbtn;
    RecyclerView recyclerView;
    DatabaseReference db;
    MyAdapter myAdapter;
    ArrayList<Dataholder> list;

    SearchView searchView;

    public String GNAME;
    public String className;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        setContentView(R.layout.activity_student);

        searchView = findViewById(R.id.serchV);
        searchView.clearFocus();

        ImageView  img_more = findViewById(R.id.img_more);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView img = findViewById(R.id.profileImg);

        addbtn = findViewById(R.id.btn_fab);
        recyclerView = findViewById(R.id.recylerV);
        db = FirebaseDatabase.getInstance().getReference("/Schools/VIT/"+className+"/Student");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list,this);
        recyclerView.setAdapter(myAdapter);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Dataholder dh = dataSnapshot.getValue(Dataholder.class);
                    list.add(dh);
                    GNAME = dh.getName();
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to retrieve data: " + error.getMessage());
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(studentActivity.this, AddStudentActivity.class));
                Log.d(TAG,"stuActDone");
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "srch: "+newText);
                searchList(newText);
                return true;
            }
        });


    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bootomsheetlayout);

        LinearLayout layout_edit = dialog.findViewById(R.id.layout_edit);

        layout_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call monitor page
                Toast.makeText(studentActivity.this, "Have to call monitor activity", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void searchList(String text) {
        ArrayList<Dataholder> searchlist = new ArrayList<>();
        for(Dataholder dataholder : list) {
            if(dataholder.getName().toLowerCase().contains(text.toLowerCase())) {
                searchlist.add(dataholder);
            }
        }

        myAdapter.searchDataList(searchlist);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Click on Name", Toast.LENGTH_SHORT).show();
    }
}
