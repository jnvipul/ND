package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.WidgetIntentService;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by VJ on 05/06/16.
 */
public class StaticWidgetProvider extends AppWidgetProvider{

    public static final String TAG = StaticWidgetProvider.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);
        if(WidgetSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())){
            Log.d(TAG, "Got correct action");
            context.startService(new Intent(context, WidgetIntentService.class));
        }else{
            Log.d(TAG, "Incorrect action - " + intent.getAction());
        }
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {

        // update each of the app widgets with the remote adapter
        Log.v(TAG, "Inside on update");
        for (int i = 0; i < appWidgetIds.length; i++) {

            // Set up the intent that starts the StackViewService, which will
            // provide the views for this collection.
            Intent intent = new Intent(context, StaticWidgetService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.static_widget);
            // Set up the RemoteViews object to use a RemoteViews adapter.
            // This adapter connects
            // to a RemoteViewsService  through the specified intent.
            // This is how you populate the data.
            rv.setRemoteAdapter(appWidgetIds[i], R.layout.list_item_quote, intent);

            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews
            // object above.
            rv.setEmptyView(R.id.listview, R.id.empty_view);

            //
            // Do additional processing specific to this app widget...
            //

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
//        context.startService(new Intent(context, WidgetIntentService.class));
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }
}
