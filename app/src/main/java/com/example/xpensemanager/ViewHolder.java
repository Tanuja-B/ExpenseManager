package com.example.xpensemanager;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout row;
    public TextView txtDate;
    public TextView txtType;
    public TextView txtAmt;
    public TextView txtNote;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        row = itemView.findViewById(R.id.singlecard);
        txtDate = itemView.findViewById(R.id.date_txt_income);
        txtType = itemView.findViewById(R.id.type_txt_income);
        txtAmt = itemView.findViewById(R.id.amount_txt_income);
        txtNote = itemView.findViewById(R.id.note_txt_income);

    }

    public void setTxtDate(String string) {
        txtDate.setText(string);
    }

    public void setTxtType(String string) {
        txtType.setText(string);
    }

    public void setTxtAmt(String amount) {
        txtAmt.setText(amount);
    }

    public void setTxtNote(String string) {
        txtNote.setText(string);
    }

}
