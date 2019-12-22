package com.example.grocerytester.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerytester.Activities.DetailsActivity;
import com.example.grocerytester.Activities.ListActivity;
import com.example.grocerytester.Activities.MainActivity;
import com.example.grocerytester.Data.DataBaseHandler;
import com.example.grocerytester.Modle.Grocery;
import com.example.grocerytester.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private Context context;
    private List<Grocery> groceriesItem;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private DataBaseHandler db;


    public RecyclerViewAdapter(Context context, List<Grocery> groceriesItem) {
        this.context = context;
        this.groceriesItem = groceriesItem;
    }
    /**
     *  ========  ========  ======== On Create View Holder ========  ========  ========
     */

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        return new ViewHolder(view, context);

    }

    /**
     * ========= ========= ========= ViewHolder ========= ========= =========
     */

    @Override
    public void onBindViewHolder( RecyclerViewAdapter.ViewHolder viewHolder, int i) {

        Grocery grocery = groceriesItem.get(i);
        viewHolder.groceryItemName.setText(grocery.getName());
        viewHolder.quantity.setText(grocery.getQuantity());
        viewHolder.dateAdded.setText(grocery.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return groceriesItem.size();
    }



    /**
     *  ========  ========  ======== activate the on click  ========  ========  ========
     *                                 for the card View
     */

    // يجب امبلمنت لل اون كلك لستنر لتمكين الكارت من التفاعل مع اللمس وعمل الحذف والتعديل
    // اضافة كونتكس للكلاس فيو هولدر
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editBtn;
        public Button deleteBtn;

        public int id;             // hold the id if each item


        public ViewHolder(View view, Context ctx) {         // الغرض من الكونتكس ctx هو لجلب بيانات الكونتكس من
            super(view);                                    //  الاكتفتي الي تحمل الادابتر لغرض عمل الانتنته


            context = ctx;

            groceryItemName = (TextView) view.findViewById(R.id.nameID);
            quantity = (TextView) view.findViewById(R.id.quantityID);
            dateAdded = (TextView) view.findViewById(R.id.creatDateID);

            editBtn = (Button) view.findViewById(R.id.editBtnID);
            editBtn.setOnClickListener(this);

            deleteBtn = (Button) view.findViewById(R.id.deleteBtnID);
            deleteBtn.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //go to next screen/ DetailsActivity
                    int position = getAdapterPosition();

                    Grocery grocery = groceriesItem.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("quantity", grocery.getQuantity());
                    intent.putExtra("id", grocery.getId());
                    intent.putExtra("date", grocery.getDateItemAdded());
                    context.startActivity(intent);


                }
            });
        }
        /**
         *  ========  ========  ======== Implementation of OnClick ========  ========  ========
         */
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.editBtnID:
                    int position = getAdapterPosition();
                    Grocery grocery = groceriesItem.get(position);

                    editItem(grocery);

                    break;
                case R.id.deleteBtnID:

                    position = getAdapterPosition();
                    grocery = groceriesItem.get(position);
                    deleteItem(grocery.getId());
            }

        }

     /**
     * ========= ========= ========= Delete Operation ========= ========= =========
     */


        public void deleteItem(final int id) {

            //create an AlertDialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
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
                    db.deleteGrocery(id);
                    groceriesItem.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();

                }
            });

            if (db.getGroceriesCount() == 0) {
                startActivity(new Intent(context, MainActivity.class));

            }

        }

        private void startActivity(Intent intent) {

            startActivity(intent);
        }

        /**
         *  ========  ========  ======== Edit Operation ========  ========  ========
         */

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
                        notifyItemChanged(getAdapterPosition(), grocery);
                    } else {
                        Snackbar.make(view, "Add Grocery and Quantity", Snackbar.LENGTH_LONG).show();
                    }

                    dialog.dismiss();

                }
            });

        }
    }

}
