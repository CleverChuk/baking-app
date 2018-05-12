package com.cleverchuk.bakingfun.adapters.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * a subclass of {@link SQLiteOpenHelper}
 * Created by chuk on 5/5/18,at 14:54.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "widget_db";
    private static final String CREATE_TABLE = "CREATE TABLE " + Contract.DbEntry.TABLE_NAME +
            "("
            + Contract.DbEntry.WIDGET_ID_COLUMN + " INTEGER,"
            + Contract.DbEntry.INGREDIENT_COLUMN + " TEXT"
            + ")";

    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + Contract.DbEntry.TABLE_NAME;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //don't care about upgrading for now just delete the table
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }
}
