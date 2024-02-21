package com.example.xpensemanager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ExpenseViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout row;
    public TextView txtAmount;
    public TextView txtType;
    public TextView txtNote;
    public TextView txtDate;

    public ExpenseViewHolder(@NonNull View itemView) {
        super(itemView);

        row = itemView.findViewById(R.id.singlerow);
        txtAmount = itemView.findViewById(R.id.amount_txt_expense);
        txtType = itemView.findViewById(R.id.type_txt_expense);
        txtNote = itemView.findViewById(R.id.note_txt_expense);
        txtDate = itemView.findViewById(R.id.date_txt_expense);

    }

    public void setTxtAmount(String amount) {
        txtAmount.setText(amount);
    }

    public void setTxtType(String type) {
        txtType.setText(type);
    }

    public void setTxtNote(String note) {
        txtNote.setText(note);
    }


    public void setTxtDate(String string) {
        txtDate.setText(string);
    }


}
