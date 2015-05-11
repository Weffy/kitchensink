package lehman.android;

import android.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.HashMap;
import android.widget.Toast;
import android.widget.AdapterView;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lab0Fragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private final boolean D = Log.isLoggable(TAG, Log.DEBUG);

    private HashMap<String,String> mURLs;
    private ArrayAdapter<String> mRedditAdapter;
    private ListView mListView;
    private String[] mRedditResult;
    private final String REDDIT_RESULT_KEY = "reddit_result";
    private final String REDDIT_URLS = "reddit_urls";

    public Lab0Fragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

        if (D) {Log.d(TAG, "onCreate");}
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.lab0fragment, menu);
        if (D) {Log.d(TAG, "onCreateOptionsMenu");}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (D) {Log.d(TAG, "onOptionsItemSelected");}
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            fetchReddit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchReddit() {
        FetchRedditTask redditTask = new FetchRedditTask();
        redditTask.execute();
        if(D) {Log.d(TAG, "fetchReddit");}
    }

    private void updateAdapterWithResult(String[] result) {
        mRedditResult = result;
        mRedditAdapter.clear();
        for (int i = 0; i < result.length; i++) {
            mRedditAdapter.add(result[i]);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        if(D) {Log.d(TAG, "savedInstanceState: " + savedInstanceState);}

        savedInstanceState.putCharSequenceArray(REDDIT_RESULT_KEY, mRedditResult);
        savedInstanceState.putSerializable(REDDIT_URLS, mURLs);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
        if(D) {Log.d(TAG, "onSaveInstanceState");}
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(D) {Log.d(TAG, "onViewStateRestored");}
        if(D) {Log.d(TAG, "savedInstanceState: " + savedInstanceState);}
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (D) { Log.d(TAG, "Starting onCreateView"); }

        // (1)
        String[] data = {};
        List<String> reddit = new ArrayList<String>(Arrays.asList(data));

        // (2)
        // ArrayAdapter will take data from a source (data) and
        // use it to populate the listView it's attached to.
        mRedditAdapter = new ArrayAdapter<String>(
                getActivity(), // context
                R.layout.reddit_list_item, // layout to use for each item
                R.id.reddit_list_item_textview // textview to update with each item
                , reddit); // data

        View rootView = inflater.inflate(R.layout.fragment_lab0, container, false);

        // (3)
        // attach ListView
        mListView = (ListView) rootView.findViewById(R.id.listview_reddit);
        mListView.setOnItemClickListener(new ItemClickListener());
        mListView.setAdapter(mRedditAdapter);

        if (D) { Log.d(TAG, "state: " + savedInstanceState); }

        if (savedInstanceState != null) {
            String[] result = (String[])savedInstanceState.getCharSequenceArray(REDDIT_RESULT_KEY);
            if (result != null) {
                if(D) {Log.d(TAG, "result.length : " + result.length);}
                updateAdapterWithResult(result);
            }
            mURLs = (HashMap)savedInstanceState.getSerializable(REDDIT_URLS);
        } else {
            fetchReddit();
        }

        if (D) { Log.d(TAG, "onCreateView completed"); }
        return rootView;
    }

    public class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            String title = (String)adapter.getItemAtPosition(position);
            String url = mURLs.get(title);
            Intent intent = new Intent(Intent.ACTION_VIEW);

            if (D) {Log.d(TAG, String.format("title: %s url: %s", title, url));}

            Uri uri = Uri.parse(url);
            intent.setData(uri);
            startActivity(intent);

            if (D) {Log.d(TAG, "onItemClick");}
        }
    }


    public class FetchRedditTask extends AsyncTask<Void,Void,String[]> {

        @Override
        protected String[] doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String redditJson = null;

            try {
                URL url = new URL("http://www.reddit.com/r/androiddev/hot/.json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }

                redditJson = buffer.toString();
                if (D) { Log.d(TAG, "redditJson: " + redditJson);}

                return getRedditDataFromJson(redditJson);

            } catch (IOException e) {
                Log.e(TAG, "Failed to connect to reddit", e);
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse JSON data", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream");
                    }
                }
            }
            // if we get here, some error happened.
            Log.wtf(TAG, "have fun with that");
            return null;
        }

        private String[] getRedditDataFromJson(String redditJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String REDDIT_DATA = "data";
            final String REDDIT_CHILDREN = "children";
            final String REDDIT_TITLE = "title";
            final String REDDIT_URL = "url";

            JSONObject redditJson = new JSONObject(redditJsonStr);
            JSONObject data = redditJson.getJSONObject(REDDIT_DATA);
            JSONArray redditArray = data.getJSONArray(REDDIT_CHILDREN);

            String[] entries = new String[redditArray.length()];
            mURLs = new HashMap<String,String>();

            for (int i = 0; i < redditArray.length(); i++) {

                JSONObject child = redditArray.getJSONObject(i);
                JSONObject item = child.getJSONObject(REDDIT_DATA);
                String title = item.getString(REDDIT_TITLE);
                String url = item.getString(REDDIT_URL);

                mURLs.put(title, url);
                entries[i] = title;

                if(D) {Log.d(TAG, String.format("title: %s url: %s", title, url));}
            }

            if(D) {Log.d(TAG, String.format("%d entries parsed", entries.length));}

            return entries;
        }

        @Override
        protected void onPostExecute(String[] result) {
            Toast toast = Toast.makeText(getActivity(),
                    String.format("%d entries", result.length),
                    Toast.LENGTH_SHORT);
            toast.show();

            updateAdapterWithResult(result);

            if (D) { Log.d(TAG, "onPostExecute");}
        }
    }

}