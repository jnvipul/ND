package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;
import com.sam_chordas.android.stockhawk.widget.QuoteWidgetProvider;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WidgetIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String SHOW_STOCK_DATA = "com.sam_chordas.android.stockhawk.service.action.SHOW_STOCK_DATA";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.sam_chordas.android.stockhawk.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.sam_chordas.android.stockhawk.service.extra.PARAM2";

    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startWidgetService(Context context, String param1, String param2) {
        Intent intent = new Intent(context, WidgetIntentService.class);
        intent.setAction(SHOW_STOCK_DATA);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionShowStockData(param1, param2);
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionShowStockData(String param1, String param2) {
        // Get app widgets
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                QuoteWidgetProvider.class));


        // Refresh Views
        for (int appWidgetId : appWidgetIds) {

            Context context = getApplicationContext();
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_collection);
            Intent intent = new Intent(context, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

//            views.se
//            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
//
//            mCursorAdapter = new QuoteCursorAdapter(this, null);
//            recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this,
//                    new RecyclerViewItemClickListener.OnItemClickListener() {
//                        @Override public void onItemClick(View v, int position) {
//                            //TODO:
//
//                            mCursor.moveToPosition(position);
//                            String stockSymbol = mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL));
//                            Intent intent = new Intent(MyStocksActivity.this, StockDetailActivity.class);
//                            intent.putExtra(StockDetailActivity.STOCK_SYMBOL, stockSymbol);
//
//                            startActivity(intent);
//                        }
//                    }));
//            recyclerView.setAdapter(mCursorAdapter);


            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
