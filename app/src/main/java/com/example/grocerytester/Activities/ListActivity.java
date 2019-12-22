package com.example.grocerytester.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.grocerytester.Data.DataBaseHandler;
import com.example.grocerytester.Modle.Grocery;
import com.example.grocerytester.R;
import com.example.grocerytester.UI.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItems;
    private EditText quantity;
    private Button saveBtn;

    private RecyclerView        rec;
    private RecyclerViewAdapter recAdapter;
    private List<Grocery>       groceryList;
    private List<Grocery>       listItem;
    private DataBaseHandler     db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();
            }
        });

        db = new DataBaseHandler(this);
        //  تضبيط الرسايكلر فيو
        rec = (RecyclerView) findViewById(R.id.recyclerViewId);
        rec.setHasFixedSize(true);                                      // للتاكيد ان كل الايتم مرتبين
        rec.setLayoutManager(new LinearLayoutManager(this));    // نثبت ال لاي اوت للرسايكلر فيو


        groceryList = new ArrayList<>();
        listItem    = new ArrayList<>();

    /**
    *  ========  ========  ======== Get Items from DB ========  ========  ========
    */
        groceryList = db.getAllGroceries();                             // جلب البيانات من الداتابيس

        // Enhanced for loop
        //   grocery type object take us through our list
        for (Grocery c : groceryList){

            Grocery grocery = new Grocery();

            grocery.setName(c.getName());                               // نخزن المعلومات من اللست للاوبجكت الي سويناه قبل سطر
            grocery.setQuantity("َQty: " + c.getQuantity());
            grocery.setId(c.getId());
            grocery.setDateItemAdded("َAdded on: " + c.getDateItemAdded());

            listItem.add(grocery);
        }

        recAdapter = new RecyclerViewAdapter(this, listItem);             // انشاء ادبتر جديد
        rec.setAdapter(recAdapter);                                               // اضافة الادبتر الى الرسايكلر
        recAdapter.notifyDataSetChanged();                                        // اشعار النضام بالتغيرات
    }

    /**
     *  ========  ========  ======== Dialog builder procedure ========  ========  ========
     */
        private void createPopupDialog() {

            dialogBuilder = new AlertDialog.Builder(this);                  // انستينشيت للدايلوك بلدر
            View view = getLayoutInflater().inflate(R.layout.popup, null); // انستينشييت للفيو وربطها بلملف المطلوب
            groceryItems = (EditText) view.findViewById(R.id.editNameID);         //
            quantity = (EditText) view.findViewById(R.id.editQtyID);          // ربط المدخلات بالفيو المطلوبه
            saveBtn = (Button) view.findViewById(R.id.saveBtnID);          //

            dialogBuilder.setView(view);                                           //  ربط الفيو بالدايلوك بلدر
            dialog = dialogBuilder.create();                                        //  بناء الدايلوك
            dialog.show();                                                          //  اضهار الدايلوك

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveGroceryToDB(v);
                }
            });
        }

    private void saveGroceryToDB(View v) {

        Grocery grocery = new Grocery();
        // To hold the new grocery
        String newGrocery = groceryItems.getText().toString();
        String newQuantity = quantity.getText().toString();


        grocery.setName(newGrocery);
        grocery.setQuantity(newQuantity);

        //Save to DB
        db.addGrocery(grocery);
        Snackbar.make(v, "Item Saved..!", Snackbar.LENGTH_LONG).show();
        Log.d("Item Added..", String.valueOf(db.getGroceriesCount()));

        // حتى يتم قرائة السناك بار يجب اضافة دلاي وبعد انتهاء الدلاي يتم النقل الى الصفحة التالية
        // ============ ============ ============ Add Delay ============ ============ ============

        dialog.dismiss();
        //start a new activity
        startActivity(new Intent(ListActivity.this, ListActivity.class));
        finish();
    }


}
