package lehman.android;

import android.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.util.Log;

public class MainFragment extends Fragment {

	private final String TAG = getClass().getSimpleName();
	private final boolean D = Log.isLoggable(TAG, Log.DEBUG);

	public MainFragment() {}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (D) {Log.d(TAG, "Starting onCreateView");}
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		if (D) {Log.d(TAG, "onCreateView completed");}
		return rootView;
	}

}
