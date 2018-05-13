/*
 * MIT License
 *
 * Copyright (c) 2018 Chukwubuikem Ume-Ugwa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
