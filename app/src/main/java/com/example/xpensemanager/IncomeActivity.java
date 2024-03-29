package com.example.xpensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.os.Bundle;

public class IncomeActivity extends AppCompatActivity {


    private EditText editamount, edittype, editnote;
    private Button button,cancel;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter<Model, ViewHolder> adapter;
    private DatabaseReference mIncomeDatabase;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);



        FirebaseApp.initializeApp(this);

        editamount = findViewById(R.id.amt_inc);
        edittype = findViewById(R.id.type_inc);
        editnote = findViewById(R.id.note_inc);
        button = findViewById(R.id.save_inc);
        cancel=findViewById(R.id.cancel_inc);
        recyclerView = findViewById(R.id.income_list);





        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addIncome();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Clear the input fields when the "Cancel" button is clicked
                editamount.setText("");
                edittype.setText("");
                editnote.setText("");

            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        loadIncomeData(); // Load the income data using FirebaseRecyclerAdapter
    }

    private void addIncome() {
        String amount = editamount.getText().toString();
        String type = edittype.getText().toString();
        String note = editnote.getText().toString();
        String date = getCurrentDate(); // Get the current date



        if (!amount.isEmpty() && !type.isEmpty()) {
            DatabaseReference newIncomeRef = mIncomeDatabase.push();
            String id = newIncomeRef.getKey(); // Get the unique ID generated by Firebase


            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("amount", amount);
            map.put("type", type);
            map.put("note", note);
            map.put("date", date); // Store the date
            newIncomeRef.setValue(map);

            // Clear the input fields
            editamount.setText("");
            edittype.setText("");
            editnote.setText("");
        } else {
            Toast.makeText(this, "Please enter amount and type", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to get the current date
    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void loadIncomeData() {
        Query query = mIncomeDatabase;
        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(query, new SnapshotParser<Model>() {
                    @Override
                    public Model parseSnapshot(DataSnapshot snapshot) {
                        String id = snapshot.getKey();
                        String amount = snapshot.child("amount").getValue(String.class);
                        String type = snapshot.child("type").getValue(String.class);
                        String note = snapshot.child("note").getValue(String.class);
                        String date = snapshot.child("date").getValue(String.class);

                        // Handle null values gracefully
                        if (amount == null) {
                            amount = "";
                        }
                        if (type == null) {
                            type = "";
                        }
                        if (note == null) {
                            note = "";
                        }
                        if (date == null) {
                            date = "";
                        }

                        return new Model(id, amount, type, note, date);
                    }
                }).build();

        adapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.income_recycler_data, parent, false);
                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;
            }

            @Override
            protected void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position, Model model) {
                holder.setTxtDate(model.getDate());
                holder.setTxtType(model.getType());
                holder.setTxtAmt(model.getAmount());
                holder.setTxtNote(model.getNote());

                // Set an onClick listener for each item
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle item click here
                        String itemID = adapter.getRef(position).getKey();
                        showOptionsForItem(itemID);
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }



    // Method to show options for update/delete
    private void showOptionsForItem(final String itemID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(IncomeActivity.this); // Use IncomeActivity.this as the context
        builder.setTitle("Options")
                .setItems(new CharSequence[]{"Update", "Delete"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Edit item (open an edit dialog or activity)
                                editItem(itemID);
                                break;
                            case 1:
                                // Delete item
                                deleteItem(itemID);
                                break;
                        }
                    }
                }).create().show();
    }

    // Method to edit an item (you can use a dialog or activity for editing)
    private void editItem(String itemID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.update_data_item, null);
        builder.setView(dialogView);

        EditText editAmount = dialogView.findViewById(R.id.edit_amt);
        EditText editType = dialogView.findViewById(R.id.edit_type);
        EditText editNote = dialogView.findViewById(R.id.edit_note);

        // Retrieve the current item's data and populate the edit fields
        DatabaseReference itemRef = mIncomeDatabase.child(itemID);
        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String amount = snapshot.child("amount").getValue(String.class);
                    String type = snapshot.child("type").getValue(String.class);
                    String note = snapshot.child("note").getValue(String.class);

                    editAmount.setText(amount);
                    editType.setText(type);
                    editNote.setText(note);

                    // Create and show the edit dialog
                    builder.setTitle("Edit Item")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Save the edited data back to the database
                                    String newAmount = editAmount.getText().toString();
                                    String newType = editType.getText().toString();
                                    String newNote = editNote.getText().toString();

                                    // Update the data in the database
                                    itemRef.child("amount").setValue(newAmount);
                                    itemRef.child("type").setValue(newType);
                                    itemRef.child("note").setValue(newNote);

                                    Toast.makeText(IncomeActivity.this, "Item edited successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


    // Method to delete an item
    private void deleteItem(String itemID) {
        // Delete the item from the Firebase Realtime Database using its unique ID
        DatabaseReference itemRef = mIncomeDatabase.child(itemID);
        itemRef.removeValue();
        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
    }


    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
