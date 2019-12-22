package com.example.grocerytester.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.grocerytester.Modle.Grocery;
import com.example.grocerytester.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DataBaseHandler extends SQLiteOpenHelper {


    // ============ ========== ========= تعريف الجدول ============ ========== =========

    private Context ctx;
    public DataBaseHandler(Context context) {
        super(context, Constants.DataBase_NAME, null, Constants.DB_VERSION);
        this.ctx = context;
    }



    // ============ ========== ========= انشاء محتويات الجدول ============ ========== =========

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + Constants.KEY_GROCERY_ITEM + " TEXT,"
                + Constants.KEY_QTY_NUMBER + " TEXT,"
                + Constants.KEY_DATE_NAME + " LONG);");


    }



    // ============ ========== ========= Upgrade Grocery ============ ========== =========

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }


    /**
    **          Operations
     */

    // ============ ========== ========= Add Grocery ============ ========== =========

    public void addGrocery (Grocery g){

        SQLiteDatabase db = this.getWritableDatabase();                             // Allow to write on DB
        // اخذ المعلومات من اليوزر
        ContentValues values = new ContentValues();                                 // #Map object allow us to create key values pairs to add to DB
        values.put(Constants.KEY_GROCERY_ITEM, g.getName());                        // keep the eateries to the values
        values.put(Constants.KEY_QTY_NUMBER, g.getQuantity());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());  // Accessing to the time to put it in DB

        // كتابة البيانات للداتا بيس
        db.insert(Constants.TABLE_NAME, null, values);
        Log.d("Saved!!", "Saved to DB");


    }
    // ============ ========== ========= Get Grocery ============ ========== =========

    public Grocery getGrocery(int id){

        SQLiteDatabase db = this.getReadableDatabase();                              // Allow to write on DB

        // Cursor Allow us to cross through DB easily
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID,
                        Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER,
                        Constants.KEY_DATE_NAME},Constants.KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

        // Set the Data from DB
        Grocery grocery = new Grocery();
        grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
        grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

        // for the time we should convert what we saved from ms to m
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(cursor.getLong(
                cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
        // set the date
        grocery.setDateItemAdded(formattedDate);

        return grocery;

    }

    // ============ ========== ========= Get all Groceries ============ ========== =========

    public List<Grocery> getAllGroceries() {

        SQLiteDatabase db = this.getReadableDatabase();                              // Allow to write on DB

        List<Grocery> groceryList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID,
                        Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER,
                        Constants.KEY_DATE_NAME},null,
                null, null,null, Constants.KEY_DATE_NAME + " DESC");

        // معناها اذا الكورسر بي محتويات
        if(cursor.moveToFirst()){
            do {

                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

                // for the time we should convert what we saved from ms to m
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(
                        cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
                // set the date
                grocery.setDateItemAdded(formattedDate);

                // Add to the groceryList
                groceryList.add(grocery);

            }while(cursor.moveToNext());
        }


        return groceryList;
    }

    // ============ ========== ========= Update Grocery ============ ========== =========

    public int updateGrocery(Grocery grocery){

        SQLiteDatabase db = this.getWritableDatabase();                             // Allow to write on DB

        // اخذ المعلومات من اليوزر
        ContentValues values = new ContentValues();                                 // #Map object allow us to create key values pairs to add to DB
        values.put(Constants.KEY_GROCERY_ITEM, grocery.getName());                  // keep the eateries to the values
        values.put(Constants.KEY_QTY_NUMBER, grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());  // Accessing to the time to put it in DB

        // Updated Row
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?",
                new String[]{String.valueOf(grocery.getId())});

    }

    // ============ ========== ========= Delete Grocery ============ ========== =========

    public void deleteGrocery(int id){

        SQLiteDatabase db = this.getWritableDatabase();                             // Allow to write on DB

        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?", new String[] {String.valueOf(id)});

        db.close();
    }




    // ============ ========== ========= Get Count ============ ========== =========
    public int getGroceriesCount(){

        //                  select all from --> Table Name
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();                              // Allow to write on DB
        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
