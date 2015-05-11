package lehman.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.net.URL;
import java.io.IOException;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;


public class StreetViewService extends Service {

    private final String TAG = getClass().getSimpleName();
    private final boolean D = Log.isLoggable(TAG, Log.DEBUG);

    public static final String LAT_LON = "LAT_LON";

    private final String STREETVIEW_BASE_URL = "https://maps.googleapis.com/maps/api/streetview?";
    private final String IMG_SIZE_PARAM = "size";
    private final String LOC_PARAM = "location";

    public static final String STREETVIEW_IMG = "STREETVIEW_IMG";

    @Override
    public void onCreate() {
        super.onCreate();
        if(D) {Log.d(TAG, "onCreated");}
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        if(D) {Log.d(TAG, "onStarted");}

        String latLon = intent.getStringExtra(LAT_LON);
        StreetViewTask task = new StreetViewTask();
        task.execute(latLon);

        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(D) {Log.d(TAG, "onDestroyed");}
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class StreetViewTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            if (D) {Log.d(TAG, "doInBackground");}
            if (D) {Log.d(TAG, "params: " + params[0] );}
            Bitmap streetView = null;
            try {
                Uri builtUri = Uri.parse(STREETVIEW_BASE_URL).buildUpon()
                        .appendQueryParameter(IMG_SIZE_PARAM, "350x350")
                        .appendQueryParameter(LOC_PARAM, params[0])
                        .build();
                URL url = new URL(builtUri.toString());

                Log.d(TAG, builtUri.toString());

                streetView = BitmapFactory.decodeStream((InputStream) url.getContent());
            } catch (MalformedURLException e) {
                Log.e(TAG, "Failed to read URL", e);
            } catch (IOException e) {
                Log.e(TAG, "Failed to connect to Google StreetView");
            }
            return streetView;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (D) {Log.d(TAG, "onPostExecute");}

            Intent intent = new Intent();
            intent.setAction(StreetViewReceiver.ACTION_STREETVIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra(STREETVIEW_IMG, result);
            sendBroadcast(intent);
        }
    }
}