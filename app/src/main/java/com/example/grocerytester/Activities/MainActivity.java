package com.example.grocerytester.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.grocerytester.Data.DataBaseHandler;
import com.example.grocerytester.Modle.Grocery;
import com.example.grocerytester.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Half;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItems;
    private EditText quantity;
    private Button saveBtn;

    private DataBaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DataBaseHandler(this);
        byPassActivity();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createPopupDialog();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void createPopupDialog() {

// ============ ============ ============ ============ ============ ============ ============ ============
/**
 *  Dialog builder procedure
 */
        dialogBuilder = new AlertDialog.Builder(this);                  // انستينشيت للدايلوك بلدر
        View view = getLayoutInflater().inflate(R.layout.popup, null); // انستينشييت للفيو وربطها بلملف المطلوب
        groceryItems = (EditText) view.findViewById(R.id.editNameID);         //
        quantity = (EditText) view.findViewById(R.id.editQtyID);          // ربط المدخلات بالفيو المطلوبه
        saveBtn = (Button) view.findViewById(R.id.saveBtnID);          //

        dialogBuilder.setView(view);                                           //  ربط الفيو بالدايلوك بلدر
        dialog = dialogBuilder.create();                                        //  بناء الدايلوك
        dialog.show();                                                          //  اضهار الدايلوك

// ============ ============ ============ ============ ============ ============ ============ ============

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Todo: Save to Data base
                // Todo: Go to next screen

                if (!groceryItems.getText().toString().isEmpty()
                        && !quantity.getText().toString().isEmpty()) {
                    saveGroceryToDB(v);
                }

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

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {                         // العمليات بعد الدلاي

                dialog.dismiss();                       // اخفاء الدايلوك التي تم من خلالها اضافة البيانات
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                // تكملة العمل بالاكتفتي الجديدة

            }
        }, 1200); // 1.2 Seconds
    }

    public void byPassActivity() {
        //Checks if database is empty; if not, then we just
        //go to ListActivity and show all added items

        if (db.getGroceriesCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }


    }
}
