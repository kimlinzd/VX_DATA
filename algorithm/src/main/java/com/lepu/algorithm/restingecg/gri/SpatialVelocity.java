package com.lepu.algorithm.restingecg.gri;

import android.util.Log;

/**
 * Maintains the spatial velocity vector that is generated during the analysis
 * of the ECG. The spatial velocity vector is computed from all input leads
 * and is used for the identification of QRS complexes within the raw
 * ECG signal. The spatial velocity vector is of little value as an
 * output from a routine ECG analysis - it is only of real use for determining
 * the likely cause of missed beat detection in a debugging situation.
 * 
 * @author Brian Devine
 *
 */
public class SpatialVelocity {
	// --------------------------------------------------------------------------	
	private static final String TAG = "MyActivity-SpatialVelocity";
	// --------------------------------------------------------------------------

	/**
	 * The number of samples stored in the spatial velocity array.
	 */
	short		NumSamples;

	/**
	 * Array containing the floating point spatial velocity data.
	 */
	float []	Data;


	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------
	
	/**
	 * Creates an initialises an instance of the {@link SpatialVelocity}
	 * class.
	 */
	public SpatialVelocity()
	{
		this.NumSamples = RestingEcg.GRI_ECG_SAMPLES;
		
		this.Data = new float [this.NumSamples];
	}

	// ----------------------------------------------------------------------

	/**
	 * Prints the contents of the {@link SpatialVelocity} instance to the
	 * debug output log. Only the first and last 10 samples of data are
	 * printed in order to keep the output concise.
	 */
	public void printContent()
	{
		int	i;
		String results;
		
		
		Log.d( TAG, "SpatialVelocity" );
		Log.d( TAG, "===============" );

		results = "First 10 samples: ";
		for ( i = 0; i < 10; i++ )
		{
			results = results + String.format( "%f ", this.Data[i] );
		}
		Log.d( TAG, results + "\n" );
		results = "Last 10 samples : ";
		for ( i = this.NumSamples-10; i < this.NumSamples; i++ )
		{
			results = results + String.format( "%f ", this.Data[i] );
		}
		Log.d( TAG, results + "\n" );
		
		Log.d( TAG, " " );
	}
	
}
