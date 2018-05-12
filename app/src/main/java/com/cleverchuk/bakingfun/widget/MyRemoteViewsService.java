package com.cleverchuk.bakingfun.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * a subclass of {@link RemoteViewsService}
 * Created by chuk on 5/5/18,at 13:28.
 */

public class MyRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsAdapter(this,intent);
    }
}
