package com.example.xpensemanager;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class IncViewHolder extends RecyclerView.ViewHolder {

    private CardView cardViewInc;
    private TextView textAmountInc;
    private TextView textTypeInc;
    private TextView textNoteInc;
    private TextView textDateInc;


    public IncViewHolder(@NonNull View itemView) {
        super(itemView);

        cardViewInc = itemView.findViewById(R.id.cardInc);
        textAmountInc = itemView.findViewById(R.id.textAmountInc);
        textTypeInc = itemView.findViewById(R.id.textTypeInc);
        textNoteInc = itemView.findViewById(R.id.textNoteInc);
        textDateInc = itemView.findViewById(R.id.textDateInc);


    }

    public void setTxtAmount(String amount) {
        textAmountInc.setText("Amount: " + amount);
    }

    public void setTxtType(String type) {
        textTypeInc.setText("Type: " + type);
    }

    public void setTxtNote(String note) {
        textNoteInc.setText("Note: " + note);
    }

    public void setTxtDate(String date) {
        textDateInc.setText("Date: " + date);
    }

}
