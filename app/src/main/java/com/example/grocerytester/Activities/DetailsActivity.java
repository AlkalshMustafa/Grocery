package com.example.grocerytester.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.grocerytester.Data.DataBaseHandler;
import com.example.grocerytester.Modle.Grocery;
import com.example.grocerytester.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {


    private Context  context;
    private List<Grocery> groceriesItem;
    private TextView itemName;
    private TextView quantity;
    private TextView dateAdded;
    private Button editBtn;
    private Button deleteBtn;

    private int groceryId;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName = (TextView) findViewById(R.id.itemDetID);
        quantity = (TextView) findViewById(R.id.qtyDetID);
        dateAdded = (TextView) findViewById(R.id.dateDetID);

        Bundle bundle = getIntent().getExtras();

        if ( bundle != null ) {
            itemName.setText(bundle.getString("name"));
            quantity.setText(bundle.getString("quantity"));
            dateAdded.setText(bundle.getString("date"));
            groceryId = bundle.getInt("id");
        }


        editBtn = (Button) findViewById(R.id.editBtnBetID);


        deleteBtn = (Button) findViewById(R.id.deleteDetID);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteItem(groceryId);

            }
        });

    }



    public void deleteItem(final int id) {

        //create an AlertDialog
        alertDialogBuilder = new AlertDialog.Builder(this);

        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.confirmation_dialog, null);

        Button noButton = (Button) view.findViewById(R.id.noButton);
        Button yesButton = (Button) view.findViewById(R.id.yesButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete the item.
                DataBaseHandler db = new DataBaseHandler(context);
                //delete item
                db.deleteGrocery(groceryId);
                startActivity(new Intent(DetailsActivity.this, ListActivity.class));
                finish();



            }
        });

    }



    public void editItem(final Grocery grocery) {

        alertDialogBuilder = new AlertDialog.Builder(context);

        inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.popup, null);

        final EditText groceryItem = (EditText) view.findViewById(R.id.editNameID);
        final EditText quantity = (EditText) view.findViewById(R.id.editQtyID);
        final TextView title = (TextView) view.findViewById(R.id.titleId);

        title.setText("Edit Grocery");
        Button saveButton = (Button) view.findViewById(R.id.saveBtnID);


        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseHandler db = new DataBaseHandler(context);

                //Update item
                grocery.setName(groceryItem.getText().toString());
                grocery.setQuantity(quantity.getText().toString());

                if (!groceryItem.getText().toString().isEmpty()
                        && !quantity.getText().toString().isEmpty()) {
                    db.updateGrocery(grocery);
                    startActivity(new Intent(DetailsActivity.this, ListActivity.class));
                    finish();
                } else {
                    Snackbar.make(view, "Add Grocery and Quantity", Snackbar.LENGTH_LONG).show();
                }

                dialog.dismiss();

            }
        });

    }

}
