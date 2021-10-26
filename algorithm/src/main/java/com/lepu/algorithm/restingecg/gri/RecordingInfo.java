package com.lepu.algorithm.restingecg.gri;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Records information that is relevant at the time of recording. This
 * information can vary for each recording made.
 * 
 * @author Brian Devine
 *
 */
public class RecordingInfo {
	// --------------------------------------------------------------------------	
	private static final String TAG = "MyActivity-RecordingInfo";
	// --------------------------------------------------------------------------	
	static final short GRI_MAX_DRUGS = 2;
	static final short GRI_MAX_CLINCLASS = 2;
	
	int		AgeDays;				// Age of patient in days. -1 if unknown.
	short []	Drugs;
	short []	ClinicalClassification;
	
	String RecordingDate;
	String RecordingTime;

	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------

	/**
	 * Creates and initialises the RecordingInfo instance using the following
	 * default parameters:-
	 * Age unknown (-1), 
	 */
	public RecordingInfo()
	{
		this( -1, new short [] { 0, 0 }, new short [] { 0, 0 }, "00/00/0000", "00:00:00" );		
	}

	// ----------------------------------------------------------------------

	/**
	 * Creates and initialises the RecordingInfo instance with the designated
	 * parameters.
	 * 
	 * @param AgeDays	Age of patient in days
	 * @param Drugs	Array containing numerically coded drug information.
	 * @param ClinicalClassification	Array containing numerically coded
	 * clinical classification information.
	 * @param RecDate	The date of recording in the format "DD/MM/CCYY".
	 * @param RecTime	The time of recording in the format "HH:MM:SS".
	 */
	public RecordingInfo(int AgeDays, short [] Drugs, short [] ClinicalClassification, String RecDate, String RecTime )
	{
		short	i;
		
		this.Drugs                  = new short [GRI_MAX_DRUGS];
		this.ClinicalClassification = new short [GRI_MAX_CLINCLASS];

		this.AgeDays = AgeDays;
		for ( i = 0; i < 2; i++ )
		{
			if ( ClinicalClassification.length-1 <= i )
				this.ClinicalClassification[i] = ClinicalClassification[i];
			if ( Drugs.length-1 <= i )
				this.Drugs[i] = Drugs[i];
		}
		
		this.RecordingDate = RecDate;
		this.RecordingTime = RecTime;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Sets the patient age, expressed in days.
	 * 
	 * @param AgeDays	The patient's age in days.
	 */
	public void SetAgeDays( int AgeDays )
	{
		this.AgeDays = AgeDays;
	}

	// ----------------------------------------------------------------------
	
	/**
	 * Sets up to 2 patient drug/medication values for input to the analysis.
	 * The drug/medication codes are listed in the C library API documentation.
	 * 
	 * @param myDrugs	Array containing numerically coded drug information.
	 */
	public void SetDrugs( short [] myDrugs )
	{
		short i;

		
		// Store up to GRI_MAX_DRUGS drug values
		for ( i = 0; i < GRI_MAX_DRUGS; i++ )
		{
			if ( myDrugs.length < i )
			{
				Drugs[i] = myDrugs[i];
			}
			else
			{
				Drugs[i] = 0;
			}
		}
		
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the two patient drug/medication values for input to the analysis.
	 * 
	 * The drug/medication codes are listed in the C library API documentation.
	 * 
	 * @param Drug1	Numerically coded drug information.
	 * @param Drug2	Numerically coded drug information.
	 */
	public void SetDrugs( short Drug1, short Drug2 )
	{
		this.Drugs[0] = Drug1;
		this.Drugs[1] = Drug2;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the two patient drug/medication values for input to the analysis.
	 * The string parameter is in the format defined in the GRIANLYS ASCII
	 * input file.
	 * 
	 * The drug/medication codes are listed in the C library API documentation.
	 * 
	 * @param Drugs	Numerically coded drug information.
	 * @return	TRUE if a correctly formatted drugs string has been supplied, 
	 * otherwise returns FALSE.
	 */
	public boolean SetDrugs( String Drugs )
	{
		boolean	Status, Finished;

		Status = true;		// Assume success


		String patternString1 = "(\\d+) (\\d+)";
		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher = pattern.matcher(Drugs);

		Finished = false;
		while(matcher.find() && !Finished )
		{
			try
			{
				this.Drugs[0] = Short.parseShort(matcher.group(1));
				this.Drugs[1] = Short.parseShort(matcher.group(2));
				Finished = true;
			}
			catch (NumberFormatException e)
			{
				System.err.println("The string containing the drugs/medication is not properly formatted!");
				Status = false;
			}  
		}
		
		return Status;
		
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Set up to 2 patient clinical classifications values for input to the
	 * analysis.
	 * 
	 * @param ClinClass	Array containing numerically coded clinical
	 * classifications.
	 */
	public void SetClinicalClassification( short [] ClinClass )
	{
		short i;

		// Store up to GRI_MAX_CLINCLASS clinical classification values
		for ( i = 0; i < GRI_MAX_CLINCLASS; i++ )
		{
			
			if ( ClinClass.length < i )
			{
				this.ClinicalClassification[i] = ClinClass[i];
			}
			else
			{
				this.ClinicalClassification[i] = 0;
			}
		}
		
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the two patient clinical classification values for input to the
	 * analysis.
	 * 
	 * The clinical classification codes are listed in the C library API
	 * documentation.
	 * 
	 * @param ClinClass1	Numerically coded clinical classification.
	 * @param ClinClass2	Numerically coded clinical classification.
	 */
	public void SetClinicalClassification( short ClinClass1, short ClinClass2 )
	{
		this.ClinicalClassification[0] = ClinClass1;
		this.ClinicalClassification[1] = ClinClass2;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the two patient clinical classification values for input to the analysis.
	 * The string parameter is in the format defined in the GRIANLYS ASCII
	 * input file.
	 * 
	 * The clinical classification codes are listed in the C library API
	 * documentation.
	 * 
	 * @param ClinClass	Numerically coded clinical classification.
	 * @return	TRUE if a correctly formatted clinical classification string
	 * has been supplied, otherwise returns FALSE.
	 */
	public boolean SetClinicalClassification( String ClinClass )
	{
		boolean	Status, Finished;

		Status = true;		// Assume success


		String patternString1 = "(\\d+) (\\d+)";
		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher = pattern.matcher(ClinClass);

		Finished = false;
		while(matcher.find() && !Finished )
		{
			try
			{
				this.ClinicalClassification[0] = Short.parseShort(matcher.group(1));
				this.ClinicalClassification[1] = Short.parseShort(matcher.group(2));
				Finished = true;
			}
			catch (NumberFormatException e)
			{
				System.err.println("The string containing the clinical classification is not properly formatted!");
				Status = false;
			}  
		}
		
		return Status;
		
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the recording date of the ECG to the given value.
	 * 
	 * @param RecDate	The date on which the ECG was recorded in the format
	 * "DD/MM/CCYY".
	 */
	public void SetRecordingDate( String RecDate )
	{
		this.RecordingDate = RecDate;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Sets the recording time of the ECG to the given value.
	 * 
	 * @param myRecTime	The recording time expressed as "HH:MM:SS".
	 */
	public void SetRecordingTime( String myRecTime )
	{
		RecordingTime = myRecTime;
	}

	// ----------------------------------------------------------------------

	/**
	 * Prints the contents of the {@link RecordingInfo} instance to the debug
	 * output log.
	 */
	public void printContent()
	{
		short	i;
		String DebugLine;


		Log.d( TAG, "RecordingInfo" );
		Log.d( TAG, "=============" );
		Log.d( TAG, "Patient age (days)    : " + AgeDays );
		Log.d( TAG, "Recording date        : " + RecordingDate );
		Log.d( TAG, "Recording time        : " + RecordingTime );
		
		DebugLine = "Drugs                 : ";
		for ( i = 0; i < GRI_MAX_DRUGS; i++ )
		{
			DebugLine += Drugs[i] + " ";
		}
		Log.d( TAG, DebugLine );
		DebugLine = "Clinical class        : ";
		for ( i = 0; i < GRI_MAX_CLINCLASS; i++ )
		{
			DebugLine += ClinicalClassification[i] + " ";
		}
		Log.d( TAG, DebugLine );
		
		Log.d( TAG, " " );
	}
}
