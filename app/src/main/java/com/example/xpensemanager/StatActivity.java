package com.example.xpensemanager;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;
import android.widget.Toast;

public class StatActivity extends AppCompatActivity {

    private PieChart pieChart;

    private BarChart barChart;
    private DatabaseReference mIncomeDatabase;

   // private BarChart barChartExpense;
    private DatabaseReference mExpenseDatabase;


    // Inside your ExpenseActivity class
    private BarChart barChartExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);




        //PIE CHART............
        // Retrieve totalIncome and totalExpense values from the intent
        float totalIncome = getIntent().getFloatExtra("totalIncome", 0.0f);
        float totalExpense = getIntent().getFloatExtra("totalExpense", 0.0f);

        // Create PieEntries based on totalIncome and totalExpense
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalIncome, "Total Income"));
        entries.add(new PieEntry(totalExpense, "Total Expense"));


        // Define colors for the slices (Income in green and Expense in red)
//        ArrayList<Integer> colors = new ArrayList<>();
//        colors.add(Color.GREEN);
//        colors.add(Color.RED);

        int[] colorClassArray=new int[]{0xFF2FAF34, 0xFFF82D1E};


        // Create a PieDataSet
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        // Apply the custom colors
        dataSet.setColors(colorClassArray);
        // dataSet.setColors(colors);
        // Set the text color of the entries to black
        dataSet.setValueTextColor(Color.BLACK);



        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));



        Legend l = pieChart.getLegend();
        l.setTextSize(18);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextColor(Color.BLACK);
        l.setEnabled(true);


        pieChart.setCenterText("Income vs Expense");
        pieChart.setData(pieData);
        pieChart.invalidate();




        // Initialize BarChart for income data (barChart)
        barChart = findViewById(R.id.barChart);
        barChart.getDescription().setEnabled(false);

        // Set up Firebase reference to income data
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData");

        // Query Firebase to fetch all income data
        queryAllIncomeData();

        //Expense bar chart1......
//        barChartExpense = findViewById(R.id.barChartExpense);
//        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData");
//
//        // Configure BarChart
//        configureBarChart();
//
//        // Fetch and display expense data
//        fetchExpenseData();

        

// Inside onCreate() method or wherever you want to initialize the chart
        barChartExpenses = findViewById(R.id.barChartExp);
        barChartExpenses.getDescription().setEnabled(false);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData");

// Assuming you have a method to query and fetch expense data similar to the income data
// Query Firebase to fetch all expense data
        queryAllExpenseData(); // Implement this method to fetch expense data


    }

    private void queryAllExpenseData() {

            mExpenseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Float> expenseByDate = new HashMap<>();

                    // Iterate through income records and calculate total income for each date
                    // Calculate total income for each date
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String date = snapshot.child("date").getValue(String.class);
                        String amountStr = snapshot.child("amount").getValue(String.class);

                        if (date != null && amountStr != null) {
                            float amount = Float.parseFloat(amountStr);
                            // Check if the date is already in the map
                            if (expenseByDate.containsKey(date)) {
                                // If it exists, add the income to the existing value
                                expenseByDate.put(date, expenseByDate.get(date) + amount);
                            } else {
                                // If it doesn't exist, create a new entry
                                expenseByDate.put(date, amount);
                            }
                        }
                    }


                    // Create BarEntry objects from incomeByDate
                    List<BarEntry> entries = new ArrayList<>();
                    List<String> dates = new ArrayList<>(expenseByDate.keySet());

                    for (int i = 0; i < dates.size(); i++) {
                        String date = dates.get(i);
                        float totalIncome = expenseByDate.get(date);
                        entries.add(new BarEntry(i, totalIncome));
                    }

                    // Create a dataset and data to populate the bar chart
                    BarDataSet dataSet = new BarDataSet(entries, "Expense per Day");
                    dataSet.setColor(Color.RED); // Customize the color

                    BarData barData = new BarData(dataSet);

                    // Set X-axis labels to dates
                    XAxis xAxis = barChartExpenses.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
                    xAxis.setLabelCount(dates.size()); // Display all dates

                    // Set the data to the chart and refresh
                    barChartExpenses.setData(barData);
                    barChartExpenses.invalidate();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error
                }
            });
        }



    //  Query Firebase to fetch all income data.
    private void queryAllIncomeData() {
        mIncomeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Float> incomeByDate = new HashMap<>();

                // Iterate through income records and calculate total income for each date
                // Calculate total income for each date
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String date = snapshot.child("date").getValue(String.class);
                    String amountStr = snapshot.child("amount").getValue(String.class);

                    if (date != null && amountStr != null) {
                        float amount = Float.parseFloat(amountStr);
                        // Check if the date is already in the map
                        if (incomeByDate.containsKey(date)) {
                            // If it exists, add the income to the existing value
                            incomeByDate.put(date, incomeByDate.get(date) + amount);
                        } else {
                            // If it doesn't exist, create a new entry
                            incomeByDate.put(date, amount);
                        }
                    }
                }


                // Create BarEntry objects from incomeByDate
                List<BarEntry> entries = new ArrayList<>();
                List<String> dates = new ArrayList<>(incomeByDate.keySet());

                for (int i = 0; i < dates.size(); i++) {
                    String date = dates.get(i);
                    float totalIncome = incomeByDate.get(date);
                    entries.add(new BarEntry(i, totalIncome));
                }

                // Create a dataset and data to populate the bar chart
                BarDataSet dataSet = new BarDataSet(entries, "Income per Day");
                dataSet.setColor(Color.BLUE); // Customize the color

                BarData barData = new BarData(dataSet);

                // Set X-axis labels to dates
                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
                xAxis.setLabelCount(dates.size()); // Display all dates

                // Set the data to the chart and refresh
                barChart.setData(barData);
                barChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }


//    private void configureBarChart() {
//        // Customize the appearance of the BarChart
//        barChartExpense.getDescription().setEnabled(false);
//        barChartExpense.setPinchZoom(false);
//        barChartExpense.setDrawGridBackground(false);
//
//        XAxis xAxis = barChartExpense.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setGranularity(1f); // Interval between labels
//        xAxis.setValueFormatter(new IndexAxisValueFormatter());
//
//        YAxis leftAxis = barChartExpense.getAxisLeft();
//        leftAxis.setDrawGridLines(false);
//        leftAxis.setLabelCount(6, false);
//        leftAxis.setValueFormatter(new LargeValueFormatter());
//
//        barChartExpense.getAxisRight().setEnabled(false);
//    }
//
//    private void fetchExpenseData() {
//        Query query = mExpenseDatabase;
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Map<String, Float> expensesByDate = new HashMap<>();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String date = snapshot.child("date").getValue(String.class);
//                    String amountStr = snapshot.child("amount").getValue(String.class);
//
//                    if (date != null && amountStr != null) {
//                        float amount = Float.parseFloat(amountStr);
//
//                        if (expensesByDate.containsKey(date)) {
//                            expensesByDate.put(date, expensesByDate.get(date) + amount);
//                        } else {
//                            expensesByDate.put(date, amount);
//                        }
//                    }
//                }
//
//                List<BarEntry> entries = new ArrayList<>();
//                List<String> dates = new ArrayList<>(expensesByDate.keySet());
//
//                for (int i = 0; i < dates.size(); i++) {
//                    String date = dates.get(i);
//                    float totalExpense = expensesByDate.get(date);
//                    entries.add(new BarEntry(i, totalExpense));
//                }
//
//                // Create a dataset and data to populate the bar chart
//                BarDataSet dataSet = new BarDataSet(entries, "Expense per Day");
//                dataSet.setColor(getResources().getColor(R.color.color_Expense)); // Customize the color
//
//                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//                dataSets.add(dataSet);
//
//                BarData barData = new BarData(dataSets);
//                barChartExpense.setData(barData);
//
//                // Set X-axis labels to dates
//                XAxis xAxis = barChartExpense.getXAxis();
//                xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
//                xAxis.setLabelCount(dates.size(), false); // Display all dates
//
//                // Refresh the chart
//                barChartExpense.invalidate();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle the error
//            }
//        });
//
//     }

//         <LinearLayout
//        android:layout_width="match_parent"
//        android:elevation="10dp"
//        android:layout_marginBottom="30dp"
//        android:orientation="vertical"
//        android:layout_height="wrap_content">
//
//            <com.github.mikephil.charting.charts.BarChart
//        android:id="@+id/barChartExpense"
//        android:layout_width="match_parent"
//        android:layout_height="400dp" />
//
//            <TextView
//        android:layout_width="match_parent"
//        android:textAppearance="?android:textAppearanceMedium"
//        android:layout_gravity="center"
//        android:textAlignment="center"
//        android:text="Expense Per Day"
//        android:textColor="@android:color/holo_orange_dark"
//        android:layout_height="wrap_content"/>
//        </LinearLayout>

}