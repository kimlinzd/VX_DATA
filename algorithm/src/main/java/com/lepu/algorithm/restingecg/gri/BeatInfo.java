package com.lepu.algorithm.restingecg.gri;

import android.util.Log;

/**
 * Maintains information returned from the analysis in relation to beat
 * classification and fiducial information.
 * 
 * @author Brian Devine
 *
 */
public class BeatInfo {
	// --------------------------------------------------------------------------	
	private static final String TAG = "MyActivity-BeatInfo";
	// --------------------------------------------------------------------------
	
	/**
	 * Describes a beat detected within the 10-second recorded data.
	 * 
	 * @author Brian Devine
	 *
	 */
	class QrsInfo 
	{
		/**
		 * The fiducial point, or location of maximum SV in 2ms sample from
		 * the start of the data.
		 */
		short	FiducialPoint;
		
		/**
		 * The QRS onset in 2ms samples from the start of the data.
		 */
		short	Onset;
		
		/**
		 * The QRS termination in 2ms samples from the start of the data.
		 */
		short	Termination;
		
		/**
		 * The classification of the beat. 0 is a dominant beat class, 1-4 is
		 * other beat classes. 5 is a dominant beat class but has a different
		 * preceding RR interval. 
		 */
		short	ClassType;
		
		// ------------------------------------------------------------------
		// METHODS
		// ------------------------------------------------------------------

		public QrsInfo()
		{
			this.FiducialPoint = 0;
			this.Onset         = 0;
			this.Termination   = 0;
			this.ClassType     = 0;
		}
		
		public void printContent()
		{
			Log.d( TAG, "QrsInfo" );
			Log.d( TAG, "=======" );
			Log.d( TAG, "Fiducial point      : " + this.FiducialPoint );
			Log.d( TAG, "QRS onset           : " + this.Onset );
			Log.d( TAG, "QRS termination     : " + this.Termination );
			Log.d( TAG, "Beat classification : " + this.ClassType );
			Log.d( TAG, " " );		
			
		}
	}
	
	/**
	 * Describes the fiducial locations within the representative beat. These apply the
	 * representative beats for all leads.
	 * 
	 * @author Brian Devine
	 *
	 */
	class RepresentativeBeatInfo
	{
		/**
		 * 0-based sample number of fiducial point.
		 */
		short	FiducialPoint;
		
		/**
		 * 0-based sample number of QRS onset.
		 */
		short	QrsOnset;
		
		/**
		 * 0-based sample number of QRS termination.
		 */
		short	QrsTermination;

		/**
		 * First 0-based sample number containing valid data.
		 */
		short	AvBegin;
		
		/**
		 * Last 0-based sample number containing valid data.
		 */
		short	AvEnd;
		
		
		// ------------------------------------------------------------------
		// METHODS
		// ------------------------------------------------------------------
		
		/**
		 * Initialises the representative beat instance with default values.
		 */
		public RepresentativeBeatInfo()
		{
			this.FiducialPoint  = 0;
			this.QrsOnset       = 0;
			this.QrsTermination = 0;
			this.AvBegin        = 0;
			this.AvEnd          = 0;
		}
		
		// ------------------------------------------------------------------
		
		/**
		 * Prints the contents of the {@link RepresentativeBeatInfo} instance to the debug
		 * output log.
		 */
		public void printContent()
		{
			Log.d( TAG, "RepresentativeBeatInfo" );
			Log.d( TAG, "======================" );
			Log.d( TAG, "Fiducial point    : " + this.FiducialPoint );
			Log.d( TAG, "QRS onset         : " + this.QrsOnset );
			Log.d( TAG, "QRS termination   : " + this.QrsTermination );
			Log.d( TAG, "Data start sample : " + this.AvBegin );
			Log.d( TAG, "Data end sample   : " + this.AvEnd );
			Log.d( TAG, " " );		
		}
	}
	
	/**
	 * Maximum number of QRS complexes that can be stored.
	 */
	short	MaxQrs;

	/**
	 * The number of QRS complexes detected in the 10-second recording.
	 */
	short	QrsCount;

	QrsInfo []	Qrs;
	RepresentativeBeatInfo	RepBeat;
	
	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------
	
	public BeatInfo()
	{
		this( RestingEcg.GRI_MAX_QRS );
	}
	
	public BeatInfo( int MaxQrsCount )
	{
		this.MaxQrs                 = (short) MaxQrsCount;
		this.QrsCount               = 0;
		this.Qrs                    = new QrsInfo [MaxQrsCount];
		
		this.RepBeat                = new RepresentativeBeatInfo();
		this.RepBeat.FiducialPoint  = 0;
		this.RepBeat.QrsOnset       = 0;
		this.RepBeat.AvBegin        = 0;
		this.RepBeat.AvEnd          = 0;		
	}
	
	public void printContent()
	{
		int		i;
		

		Log.d( TAG, "BeatInfo" );
		Log.d( TAG, "========" );
		Log.d( TAG, "Maximum allowed QRS  : " + this.MaxQrs );
		Log.d( TAG, "QRS count            : " + this.QrsCount );
		Log.d( TAG, " " );

		this.RepBeat.printContent();
		
		for ( i = 0; i < this.QrsCount; i++ )
			this.Qrs[i].printContent();
		
	}
}
