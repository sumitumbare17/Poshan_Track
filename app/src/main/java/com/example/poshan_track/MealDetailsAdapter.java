package com.example.poshan_track;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MealDetailsAdapter extends RecyclerView.Adapter<MealDetailsAdapter.MealViewHolder> {

    private Context context;
    private ArrayList<Meal> mealList;
    private OnMealClickListener listener;
    private OnBeneficiaryClickListener beneficiaryClickListener; // New listener for button click

    public MealDetailsAdapter(Context context, ArrayList<Meal> mealList, OnMealClickListener listener, OnBeneficiaryClickListener beneficiaryClickListener) {
        this.context = context;
        this.mealList = mealList;
        this.listener = listener;
        this.beneficiaryClickListener = beneficiaryClickListener; // Initialize the listener
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meal_details, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        final Meal meal = mealList.get(position);
        holder.textViewName.setText(meal.getName());
        holder.textViewDate.setText(meal.getDate());
        Picasso.get().load(meal.getImageURL()).into(holder.imageViewMeal);

        holder.buttonBeneficiaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beneficiaryClickListener != null) {
                    beneficiaryClickListener.onBeneficiaryClick(meal); // Notify the activity about button click
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMealClick(meal);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    public interface OnBeneficiaryClickListener {
        void onBeneficiaryClick(Meal meal);
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewMeal;
        TextView textViewDate;
        TextView textViewName;
        Button buttonBeneficiaries;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMeal = itemView.findViewById(R.id.imageViewMeal);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewName = itemView.findViewById(R.id.textViewName);
            buttonBeneficiaries = itemView.findViewById(R.id.buttonBeneficiaries);
        }
    }
}
