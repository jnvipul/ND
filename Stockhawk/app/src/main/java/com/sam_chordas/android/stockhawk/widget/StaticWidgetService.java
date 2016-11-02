package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VJ on 12/06/16.
 */
public class StaticWidgetService extends RemoteViewsService {

    private static final String TAG = StaticWidgetService.class.getSimpleName();
    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        Log.v(TAG, "inside onGetviewFactory");
        return new StaticWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class StaticWidgetRemoteViewsFactory implements RemoteViewsFactory {
        private static final int           mCount       = 10;
        private              List<Integer> mWidgetItems = new ArrayList<Integer>();
        private Context mContext;
        private int     mAppWidgetId;

        public StaticWidgetRemoteViewsFactory(final Context context, final Intent intent) {
            Log.v(TAG, "inside onGetviewFactory");
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            Log.v(TAG, "Inside on create - ");
            for (int i = 0; i < mCount; i++) {
                mWidgetItems.add(i);
            }
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mWidgetItems.size();
        }

        @Override
        public RemoteViews getViewAt(final int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.static_widget);
            rv.setTextViewText(R.layout.static_widget, mWidgetItems.get(position) + "");

            return rv;
        }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
}
