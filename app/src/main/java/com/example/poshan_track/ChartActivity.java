package com.example.poshan_track;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {
    private static final String TAG = "ChartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // Fetch all classes data
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference node = fdb.getReference("/Schools/VIT");

        // Plot charts
        plotClassData(node);
    }

    private void plotClassData(DatabaseReference node) {
        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, List<Integer>> monthlyHeightData = new HashMap<>();
                Map<String, List<Integer>> monthlyWeightData = new HashMap<>();
                Map<String, List<Integer>> yearlyHeightData = new HashMap<>();
                Map<String, List<Integer>> yearlyWeightData = new HashMap<>();
                List<String> classNames = new ArrayList<>();

                // Process data from Firebase snapshot
                for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                    classNames.add(classSnapshot.getKey());
                    for (DataSnapshot studentSnapshot : classSnapshot.child("Student").getChildren()) {
                        for (DataSnapshot yearSnapshot : studentSnapshot.child("care").getChildren()) {
                            for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
                                for (DataSnapshot daySnapshot : monthSnapshot.getChildren()) {
                                    Integer height = daySnapshot.child("height").getValue(Integer.class);
                                    Integer weight = daySnapshot.child("weight").getValue(Integer.class);
                                    String yearMonth = yearSnapshot.getKey() + "-" + monthSnapshot.getKey();

                                    // Update monthly height data
                                    monthlyHeightData.computeIfAbsent(yearMonth, k -> new ArrayList<>()).add(height != null ? height : 0);
                                    // Update monthly weight data
                                    monthlyWeightData.computeIfAbsent(yearMonth, k -> new ArrayList<>()).add(weight != null ? weight : 0);
                                    // Update yearly height data
                                    yearlyHeightData.computeIfAbsent(yearSnapshot.getKey(), k -> new ArrayList<>()).add(height != null ? height : 0);
                                    // Update yearly weight data
                                    yearlyWeightData.computeIfAbsent(yearSnapshot.getKey(), k -> new ArrayList<>()).add(weight != null ? weight : 0);
                                }
                            }
                        }
                    }
                }

                // Plot charts
                if (!monthlyHeightData.isEmpty() && !monthlyWeightData.isEmpty()) {
                    plotMonthlyDistributionCharts(monthlyHeightData, monthlyWeightData);
                }
                if (!yearlyHeightData.isEmpty() && !yearlyWeightData.isEmpty()) {
                    plotYearlyTrendCharts(yearlyHeightData, yearlyWeightData);
                }
                if (!classNames.isEmpty() && !yearlyHeightData.isEmpty() && !yearlyWeightData.isEmpty()) {
                    plotClasswiseComparisonChart(classNames, yearlyHeightData, yearlyWeightData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read class data from Firebase: " + error.getMessage());
            }
        });
    }

    private void plotMonthlyDistributionCharts(Map<String, List<Integer>> monthlyHeightData,
                                               Map<String, List<Integer>> monthlyWeightData) {
        // Plot monthly distribution chart for height
        PieChart heightChart = findViewById(R.id.monthlyHeightChart);
        plotPieChart(heightChart, "Monthly Height Distribution", monthlyHeightData);

        // Plot monthly distribution chart for weight
        PieChart weightChart = findViewById(R.id.monthlyWeightChart);
        plotPieChart(weightChart, "Monthly Weight Distribution", monthlyWeightData);
    }

    private void plotYearlyTrendCharts(Map<String, List<Integer>> yearlyHeightData,
                                       Map<String, List<Integer>> yearlyWeightData) {
        // Plot yearly trend chart for height
        PieChart yearlyHeightChart = findViewById(R.id.yearlyHeightChart);
        plotPieChart(yearlyHeightChart, "Yearly Height Trend", yearlyHeightData);

        // Plot yearly trend chart for weight
        PieChart yearlyWeightChart = findViewById(R.id.yearlyWeightChart);
        plotPieChart(yearlyWeightChart, "Yearly Weight Trend", yearlyWeightData);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void plotClasswiseComparisonChart(List<String> classNames,
                                              Map<String, List<Integer>> yearlyHeightData,
                                              Map<String, List<Integer>> yearlyWeightData) {
        // Plot classwise comparison chart
        BarChart classwiseComparisonChart = findViewById(R.id.classwiseComparisonChart);
        plotBarChart(classwiseComparisonChart, "Classwise Comparison", classNames, yearlyHeightData, yearlyWeightData);
    }

    private void plotPieChart(PieChart chart, String label, Map<String, List<Integer>> data) {
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : data.entrySet()) {
            String key = entry.getKey();
            List<Integer> values = entry.getValue();
            float total = calculateTotal(values);
            entries.add(new PieEntry(total, key));
        }

        PieDataSet dataSet = new PieDataSet(entries, label);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(12f);

        chart.setData(pieData);
        chart.getDescription().setEnabled(false);
        chart.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void plotBarChart(BarChart chart, String label, List<String> classNames,
                              Map<String, List<Integer>> yearlyHeightData,
                              Map<String, List<Integer>> yearlyWeightData) {
        List<BarEntry> heightEntries = new ArrayList<>();
        List<BarEntry> weightEntries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < classNames.size(); i++) {
            String className = classNames.get(i);
            float heightTotal = calculateTotal(yearlyHeightData.getOrDefault(className, new ArrayList<>()));
            float weightTotal = calculateTotal(yearlyWeightData.getOrDefault(className, new ArrayList<>()));

            heightEntries.add(new BarEntry(i, heightTotal));
            weightEntries.add(new BarEntry(i, weightTotal));
            labels.add(className);
        }

        BarDataSet heightDataSet = new BarDataSet(heightEntries, "Height");
        heightDataSet.setColor(Color.BLUE);

        BarDataSet weightDataSet = new BarDataSet(weightEntries, "Weight");
        weightDataSet.setColor(Color.RED);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(heightDataSet);
        dataSets.add(weightDataSet);

        BarData barData = new BarData(dataSets);
        barData.setValueTextColor(Color.BLACK);
        barData.setValueTextSize(10f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        chart.setData(barData);
        chart.getDescription().setEnabled(false);
        chart.groupBars(0f, 0.08f, 0.03f);
        chart.invalidate();
    }

    private float calculateTotal(List<Integer> values) {
        float total = 0;
        for (Integer value : values) {
            total += value != null ? value : 0;
        }
        return total;
    }
}
