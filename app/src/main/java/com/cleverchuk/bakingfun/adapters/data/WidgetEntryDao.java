package com.cleverchuk.bakingfun.adapters.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * data access class for writing data to database
 * Created by chuk on 5/5/18,at 15:22.
 */

public class WidgetEntryDao {
    private final SQLiteHelper mDbHelper;

    public WidgetEntryDao(SQLiteHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    /**
     * insert data to database
     * @param id data id
     * @param ingredient data to insert
     */
    public void insert(int id, String ingredient) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.DbEntry.WIDGET_ID_COLUMN, id);
        values.put(Contract.DbEntry.INGREDIENT_COLUMN, ingredient);

        db.insert(Contract.DbEntry.TABLE_NAME, null, values);
    }

    /**
     * reads data with given id
     * @param id data id
     * @return array of strings
     */
    public String[] query(int id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {Contract.DbEntry.WIDGET_ID_COLUMN, Contract.DbEntry.INGREDIENT_COLUMN};
        String selection = Contract.DbEntry.WIDGET_ID_COLUMN + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        Cursor cursor = db.query(Contract.DbEntry.TABLE_NAME, projection, selection, selectionArgs, null, null,
                Contract.DbEntry.WIDGET_ID_COLUMN + " ASC");

        cursor.moveToFirst();
        int index = cursor.getColumnIndex(Contract.DbEntry.INGREDIENT_COLUMN);
        String ingredient = cursor.getString(index);
        cursor.close();

        return ingredient.split("\n");
    }

    /**
     * remove data with id
     * @param id data id
     */
    public void delete(int id){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = Contract.DbEntry.WIDGET_ID_COLUMN + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        db.delete(Contract.DbEntry.TABLE_NAME,selection,selectionArgs);
    }

}
