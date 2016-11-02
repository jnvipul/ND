package com.sam_chordas.android.stockhawk.widget;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by VJ on 09/06/16.
 */
public class WidgetSyncAdapter extends AbstractThreadedSyncAdapter{

    public static final String ACTION_DATA_UPDATED = "ACTION_DATA_UPDATED";

    public WidgetSyncAdapter(final Context context, final boolean autoInitialize) {
        super(context, autoInitialize);
    }


    @Override
    public void onPerformSync(final Account account, final Bundle extras, final String authority, final ContentProviderClient provider, final SyncResult syncResult) {

        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED);
        getContext().sendBroadcast(dataUpdatedIntent);
    }
}
