package com.example.poshan_track;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMeal extends AppCompatActivity {

    private static final int pic_id = 510;
    private static final int Gal_Req_Code = 520;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri;

    private TextView dateTextView;
    private ImageView f_img;
    private File tempFile; // Declare a global variable to store the temporary file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        dateTextView = findViewById(R.id.dateTextView);
        setCurrentDate(); // Set today's date by default

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        Button btnCam = findViewById(R.id.btnCam);
        Button btnBrowse = findViewById(R.id.btnBrowse);
        f_img = findViewById(R.id.f_img);
        Button btnsub = findViewById(R.id.btn_submit);

        databaseReference = FirebaseDatabase.getInstance().getReference("meals");
        storageReference = FirebaseStorage.getInstance().getReference();

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, pic_id);
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallary = new Intent(Intent.ACTION_PICK);
                iGallary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallary, Gal_Req_Code);
            }
        });

        btnsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitMealToFirebase();
            }
        });
    }

    // Set today's date to dateTextView by default
    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        dateTextView.setText(currentDate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    // Save the captured image to a temporary file
                    tempFile = saveImageToFile(photo);
                    if (tempFile != null) {
                        // Display the image in ImageView
                        f_img.setImageBitmap(photo);
                        // Get URI from the temporary file
                        imageUri = Uri.fromFile(tempFile);
                    } else {
                        Toast.makeText(this, "Error: Failed to save image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Error: No image data received", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error: No image data received", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Gal_Req_Code && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData(); // Get the URI directly from the gallery intent
                f_img.setImageURI(imageUri);
            } else {
                Toast.makeText(this, "Error: Unable to get image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error: Unable to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private File saveImageToFile(Bitmap bitmap) {
        File file = null;
        try {
            file = File.createTempFile("temp_image", ".jpg", getExternalCacheDir());
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void submitMealToFirebase() {
        final String mealName = ((MaterialAutoCompleteTextView) findViewById(R.id.edt_weight)).getText().toString().trim();
        if (mealName.isEmpty()) {
            Toast.makeText(this, "Please enter meal name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the date from dateTextView
        final String selectedDate = dateTextView.getText().toString().trim();

        final StorageReference imageRef = storageReference.child("meal_images/" + selectedDate + ".jpg");
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageURL = uri.toString();
                                Meal meal = new Meal(mealName, selectedDate, imageURL);
                                databaseReference.child(selectedDate).setValue(meal);
                                Toast.makeText(AddMeal.this, "Meal added successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddMeal.this, ClassesActivity.class);
                                intent.putExtra("mealName", mealName);
                                intent.putExtra("imageUri", imageURL);
                                intent.putExtra("currentDate", selectedDate);
                                intent.putExtra("purpose", ClassesActivity.SELECT_STUDENTS_FOR_MEAL);
                                startActivity(intent);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddMeal.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected date to the dateTextView
                        dateTextView.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
}


class Meal {
    private String name;
    private String date;
    private String imageURL;

    public Meal() {
        // Default constructor required for calls to DataSnapshot.getValue(Meal.class)
    }

    public Meal(String name, String date, String imageURL) {
        this.name = name;
        this.date = date;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
