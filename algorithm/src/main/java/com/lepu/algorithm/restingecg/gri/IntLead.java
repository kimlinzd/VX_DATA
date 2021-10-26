package com.lepu.algorithm.restingecg.gri;


/**
 * Handles the raw integer ECG lead data to be analysed.
 * This class is primarily for use with the {@link GriAsc} class when
 * reading raw lead data from the ASCII input file.
 * 
 * @author Brian Devine
 *
 */
public class IntLead
{
	// --------------------------------------------------------------------------	
	private static final String TAG = "MyActivity-IntLead";
	// --------------------------------------------------------------------------	

	short	NumSamples;
	short [] Data;
	
	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------
	
	/**
	 * Creates an ECG lead capable of storing the given number of samples.
	 * 
	 * @param NumSamples	The number of samples that the ECG lead is to hold.
	 */
	public IntLead(int NumSamples )
	{
		//Log.d( TAG, "Creating array with " + NumSamples + " elements");
		this.Data = new short[NumSamples];
		this.NumSamples = (short) NumSamples;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the lead data to the given array of sample values. A check will
	 * be made to ensure that the number of samples being copied does not
	 * exceed the size of the array in which the data is to be stored.
	 * 
	 * @param LeadData	The array of sample values to initialise the lead with.
	 */
	public void SetLeadData( short [] LeadData )
	{
		int		i, MaxSamples;


		// Make sure the passed in array isn't bigger than the one it's populating
		MaxSamples = LeadData.length;
		if ( this.NumSamples < MaxSamples )
		{
			MaxSamples = this.NumSamples;
		}
		
		for ( i = 0; i < LeadData.length; i++ )
		{
			this.Data[i] = LeadData[i];
		}
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the sample value at the given sample number to the given value.
	 * 
	 * @param SampleNum	The 0-based sample number to be set.
	 * @param SampleValue	The value that the sample is to be set to.
	 */
	public void SetSample( int SampleNum, short SampleValue )
	{
		this.Data[SampleNum] = SampleValue;
	}

	// ----------------------------------------------------------------------

	/**
	 * Gets the sample value at the given sample number within the lead.
	 * 
	 * @param SampleNum	The 0-based sample number value to retrieve.
	 * @return	The sample value in the native resolution.
	 */
	public short GetSample( int SampleNum )
	{
		return this.Data[SampleNum];
	}
}