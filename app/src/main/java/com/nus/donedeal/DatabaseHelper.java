package com.nus.donedeal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * This database based on SQLITE
 * keeps track of each members
 * name, expenditure and contribution
 *
 * it includes functions for easy access and
 * manipulation of the database
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public SQLiteDatabase db;
    private static final String TABLE_NAME = "Table_1";
    private static final String COL0 = "ID";
    private static final String COL1 = "Name";
    private static final String COL2 = "Expenditure";
    private static final String COL3 = "Contribution";
    public static DatabaseHelper instance;
    public static Context context;

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME,null,1);
        // Init the singleton
        if (instance == null){
            instance = this;
            instance.context = context;
            instance.db = instance.getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL1 + " TEXT, " +
                             COL2 + " REAL, " + COL3 + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add item to COL1 of database
    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, item);
        long result = db.insert(TABLE_NAME, null, contentValues);
        //if data inserted incorrectly, it will return -1
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    //returns all data from database
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //returns only the ID that matches the name
    public Cursor getItemID(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL0 + " FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateName(String newName, int id, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL1 + " = '" + newName + "' WHERE " + COL0 +
                       " = '" + id + "' AND " + COL1 + " = '" + oldName + "'";
        db.execSQL(query);
    }

    public void deleteName(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL0 + " = '" + id + "' AND " + COL1 +
                       " = '" + name + "'";
        db.execSQL(query);
    }

    public ArrayList<String> getAllNames() {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        if (data != null) {
            while (!data.isAfterLast()) {
                arrayList.add(data.getString(data.getColumnIndex(COL1)));
                data.moveToNext();
            }
        }
        return arrayList;
    }

    public ArrayList<Float> getAllExpenditure() {
        ArrayList<Float> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        if (data != null) {
            while (!data.isAfterLast()) {
                arrayList.add(data.getFloat(data.getColumnIndex(COL2)));
                data.moveToNext();
            }
        }
        return arrayList;
    }

    public ArrayList<Float> getAllContribution() {
        ArrayList<Float> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        if (data != null) {
            while (!data.isAfterLast()) {
                arrayList.add(data.getFloat(data.getColumnIndex(COL3)));
                data.moveToNext();
            }
        }
        return arrayList;
    }


    public void addExpenditureEqually(Float expenditure) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (c != null) {
            while (!c.isAfterLast()) {
                Integer id = c.getInt(c.getColumnIndex(COL0));
                Float currentExpenditure = c.getFloat(c.getColumnIndex(COL2));
                Float updatedExpenditure = currentExpenditure + expenditure;
                db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL2 + " = '" + updatedExpenditure +
                           "' WHERE " + COL0 + " = '" + id + "'");
                c.moveToNext();
            }
        }
    }

    public void addExpenditureManually(Float expenditure, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        for(int i = 0; i < id; i++) {
            c.moveToNext();
        }
        Float currentExpenditure = c.getFloat(c.getColumnIndex(COL2));
        Float updatedExpenditure = currentExpenditure + expenditure;
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL2 + " = '" + updatedExpenditure +
                "' WHERE " + COL0 + " = '" + id + "'");
    }


    public void addContribution(Float price, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (c != null) {
            while (!c.getString(c.getColumnIndex(COL1)).equals(name)) {
                c.moveToNext();
            }
            Integer id = c.getInt(c.getColumnIndex(COL0));
            Float currentContribution = c.getFloat(c.getColumnIndex(COL3));
            Float updatedContribution = currentContribution + price;
            db.execSQL("UPDATE " + TABLE_NAME + " SET " + COL3 + " = '" + updatedContribution +
                    "' WHERE " + COL0 + " = '" + id + "'");
        }
    }

    public void deleteData(){
        instance.context.deleteDatabase(TABLE_NAME);
    }

}
