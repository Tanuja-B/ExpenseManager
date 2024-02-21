package com.example.xpensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

   // private ImageView income, expense, statistics, emi,acc,tax,logout;
    private TextView totalIncomeResult, totalExpenseResult, totalBalance;
    private RecyclerView recyclerIncomeHome, recyclerExpenseHome;

    CardView cardView1,cardView2,cardView3,cardView4,cardView5,cardView6,cardView7,rules,info;

    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;

    private FirebaseRecyclerAdapter<Model, IncViewHolder> incAdapter;
    private FirebaseRecyclerAdapter<Model, ExpViewHolder> expAdapter;


    private FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        cardView1=findViewById(R.id.card1);
        cardView2=findViewById(R.id.card2);
        cardView3=findViewById(R.id.card3);
        cardView4=findViewById(R.id.card4);
        cardView5=findViewById(R.id.card5);
        cardView6=findViewById(R.id.card6);
        cardView7=findViewById(R.id.card7);


        rules=findViewById(R.id.card_rules);
        info=findViewById(R.id.cardInfo);

        mAuth=FirebaseAuth.getInstance();


        totalIncomeResult = findViewById(R.id.incomeResult);
        totalExpenseResult = findViewById(R.id.expenseResult);
        totalBalance = findViewById(R.id.balanceView);

        recyclerIncomeHome = findViewById(R.id.recycler_income_home);
        recyclerExpenseHome = findViewById(R.id.recycler_expense_home);

        // Initialize Firebase Database references
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData");
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData");

        // Set up RecyclerView for Income
        setUpIncomeRecyclerView();

        // Set up RecyclerView for Expense
        setUpExpenseRecyclerView();


        calculateBalanceFromTotalIncomeAndExpense();


        calculateTotalIncome();
        calculateTotalExpense();


        // Handle button clicks
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, IncomeActivity.class));

                // Recalculate balance after adding income
                calculateBalanceFromTotalIncomeAndExpense();
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ExpenseActivity.class));

                // Recalculate balance after adding expense
                calculateBalanceFromTotalIncomeAndExpense();
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle statistics button click
                float totalIncome = Float.parseFloat(totalIncomeResult.getText().toString());
                float totalExpense = Float.parseFloat(totalExpenseResult.getText().toString());

                Intent intent = new Intent(HomeActivity.this, StatActivity.class);
                intent.putExtra("totalIncome", totalIncome);
                intent.putExtra("totalExpense", totalExpense);
                startActivity(intent);
            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, EmiActivity.class));
            }
        });


        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AccountActivity.class));
            }
        });


        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, tax1Activity.class));
            }
        });

        cardView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

       rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, sec80cActivity.class));
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, section1Activity.class));
            }
        });



    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setUpIncomeRecyclerView() {
        LinearLayoutManager inclayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        inclayoutManager.setReverseLayout(true);
        inclayoutManager.setStackFromEnd(true);
        recyclerIncomeHome.setLayoutManager(inclayoutManager);

        FirebaseRecyclerOptions<Model> incOptions = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(mIncomeDatabase, Model.class)
                .build();

        incAdapter = new FirebaseRecyclerAdapter<Model, IncViewHolder>(incOptions) {
            @Override
            protected void onBindViewHolder(@NonNull IncViewHolder holder, int position, @NonNull Model model) {
                holder.setTxtAmount(model.getAmount());
                holder.setTxtType(model.getType());
                holder.setTxtNote(model.getNote());
                holder.setTxtDate(model.getDate());
            }

            @NonNull
            @Override
            public IncViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new IncViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.dashboard_income, parent, false));
            }
        };

        recyclerIncomeHome.setAdapter(incAdapter);
    }

    private void setUpExpenseRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerExpenseHome.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Model> expOptions = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(mExpenseDatabase, Model.class)
                .build();

        expAdapter = new FirebaseRecyclerAdapter<Model, ExpViewHolder>(expOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ExpViewHolder holder, int position, @NonNull Model model) {
                holder.setTxtAmount(model.getAmount());
                holder.setTxtType(model.getType());
                holder.setTxtNote(model.getNote());
                holder.setTxtDate(model.getDate());
            }

            @NonNull
            @Override
            public ExpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ExpViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.expense_item_layout, parent, false));
            }
        };

        recyclerExpenseHome.setAdapter(expAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening to Firebase database changes
        incAdapter.startListening();
        expAdapter.startListening();
        calculateBalanceFromTotalIncomeAndExpense();


    }



    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening to Firebase database changes
        incAdapter.stopListening();
        expAdapter.stopListening();
    }

    private void calculateTotalIncome() {
        mIncomeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalIncome = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Model data = dataSnapshot.getValue(Model.class);

                    if (data != null && data.getAmount() != null) {
                        try {
                            int intAmt = Integer.parseInt(data.getAmount());
                            totalIncome += intAmt;
                        } catch (NumberFormatException e) {
                            // Handle the case where data.getAmount() is not a valid integer
                            e.printStackTrace(); // Log the error for debugging
                        }
                    }
                }


                totalIncomeResult.setText(String.valueOf(totalIncome));
                calculateBalanceFromTotalIncomeAndExpense();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void calculateTotalExpense() {
        mExpenseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalExpense = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Model data = dataSnapshot.getValue(Model.class);

                    if (data != null && data.getAmount() != null) {
                        try {
                            int intAmt = Integer.parseInt(data.getAmount());
                            totalExpense += intAmt;
                        } catch (NumberFormatException e) {
                            // Handle the case where data.getAmount() is not a valid integer
                            e.printStackTrace(); // Log the error for debugging
                        }
                    }
                }


                totalExpenseResult.setText(String.valueOf(totalExpense));
                calculateBalanceFromTotalIncomeAndExpense();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // Add this method to calculate balance based on total income and total expense
    private void calculateBalanceFromTotalIncomeAndExpense() {
        String incomeText = totalIncomeResult.getText().toString();
        String expenseText = totalExpenseResult.getText().toString();

        float totalIncome = Float.parseFloat(incomeText);
        float totalExpense = Float.parseFloat(expenseText);

        float balance = totalIncome - totalExpense;

        // Update the balance TextView
        totalBalance.setText(String.valueOf(balance));
    }
}
