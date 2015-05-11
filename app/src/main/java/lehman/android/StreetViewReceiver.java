package lehman.android;

import android.content.Intent;
import android.util.Log;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.view.View;

public class StreetViewReceiver extends BroadcastReceiver {

    private final String TAG = getClass().getSimpleName();
    private final boolean D = Log.isLoggable(TAG, Log.DEBUG);

    public static final String ACTION_STREETVIEW = "lehman.android.intent.action.STREETVIEW_RECEIVED";

    private View mView;

    public StreetViewReceiver(View view) {
        mView = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        ImageView imageView = (ImageView)mView.findViewById(R.id.locStreetView);
        Bitmap streetView = intent.getParcelableExtra(StreetViewService.STREETVIEW_IMG);
        imageView.setImageBitmap(streetView);
    }
}