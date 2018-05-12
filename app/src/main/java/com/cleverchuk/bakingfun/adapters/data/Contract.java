package com.cleverchuk.bakingfun.adapters.data;

import android.provider.BaseColumns;

/**
 *  a simple contract class for widget entry
 * Created by chuk on 5/5/18,at 15:03.
 */

class Contract {
    private Contract(){}

    public static class DbEntry implements BaseColumns{
        public static final String TABLE_NAME ="ingredients";
        public static final String WIDGET_ID_COLUMN="widget_id";
        public static  final String INGREDIENT_COLUMN="ingredient";
    }
}
