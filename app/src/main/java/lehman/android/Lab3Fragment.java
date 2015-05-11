package lehman.android;

import android.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.util.Log;

public class Lab3Fragment extends Fragment {

	private final String TAG = getClass().getSimpleName();
	private final boolean D = Log.isLoggable(TAG, Log.DEBUG);

	public Lab3Fragment() {}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (D) { Log.d(TAG, "Starting onCreateView"); }
		View rootView = inflater.inflate(R.layout.fragment_lab3, container, false);
		if (D) { Log.d(TAG, "onCreateView completed"); }
		return rootView;
	}

}
