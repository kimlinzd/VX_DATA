package com.lepu.algorithm.restingecg.gri;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

/**
 * For development and testing only.
 * Temporary class to facilitate selection of an ECG for processing.
 * 
 * @author Brian Devine
 *
 */
public class StartDraw extends Activity {
	// --------------------------------------------------------------------------	
	private static final String TAG = "MyActivity-StartDraw";
	// --------------------------------------------------------------------------	
    DrawView drawView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawView = new DrawView(this);
        drawView.setBackgroundColor(Color.WHITE);
        setContentView(drawView);

    }
}
