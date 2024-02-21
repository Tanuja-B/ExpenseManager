package com.example.xpensemanager;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ExpViewHolder extends RecyclerView.ViewHolder{

    private CardView cardView;
    private TextView textAmount;
    private TextView textType;
    private TextView textNote;
    private TextView textDate;


    public ExpViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.cardView);
        textAmount = itemView.findViewById(R.id.textAmount);
        textType = itemView.findViewById(R.id.textType);
        textNote = itemView.findViewById(R.id.textNote);
        textDate = itemView.findViewById(R.id.textDate);

    }

    public void setTxtAmount(String amount) {
        textAmount.setText("Amount: " + amount);
    }

    public void setTxtType(String type) {
        textType.setText("Type: " + type);
    }

    public void setTxtNote(String note) {
        textNote.setText("Note: " + note);
    }

    public void setTxtDate(String date) {
        textDate.setText("Date: " + date);
    }
}
