package com.cleverchuk.bakingfun.utils;

import android.content.Context;

/**
 * a utility class for retrieving endpoints
 * Created by chuk on 4/30/18,at 17:56.
 */

public class Query {

    public static String getEnpoint(Context context, int stringId){
        return context.getString(stringId);
    }
}
