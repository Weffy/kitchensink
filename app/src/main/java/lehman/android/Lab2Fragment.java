package lehman.android;

import android.app.Fragment;
import android.graphics.BitmapFactory;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class Lab2Fragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private final boolean D = Log.isLoggable(TAG, Log.DEBUG);

    public static int btnVal = 0;


    public Lab2Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (D) {
            Log.d(TAG, "Starting onCreateView");
        }
        View rootView = inflater.inflate(R.layout.fragment_lab2, container, false);


        Button off = (Button) rootView.findViewById(R.id.off);
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVal = 0;

            }
        });

        Button lo = (Button) rootView.findViewById(R.id.lo);
        lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnVal = 3;

            }
        });

        Button med = (Button) rootView.findViewById(R.id.med);
        med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnVal = 6;

            }
        });

        Button hi = (Button) rootView.findViewById(R.id.hi);
        hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnVal = 20;

            }
        });

        Button btnFan = (Button) rootView.findViewById(R.id.fan1);
        btnFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyView.fan = BitmapFactory.decodeResource(getResources(), R.drawable.fan2);
            }
        });

        Button btnFlower = (Button) rootView.findViewById(R.id.fan2);
        btnFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyView.fan = BitmapFactory.decodeResource(getResources(), R.drawable.flower);
            }
        });


        if (D) {
            Log.d(TAG, "onCreateView completed");
        }
        return rootView;


    }
}
