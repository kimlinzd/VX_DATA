package com.lepu.algorithm.restingecg.gri;

/**
 * Maintains the wave fiducials, beat classification and spike information
 * determined during the analysis of the ECG data by the C analysis library.
 * 
 * @author Brian Devine
 *
 */
public class WaveRecognition {
	// --------------------------------------------------------------------------	
	private static final String TAG = "MyActivity-WaveRecognition";
	// --------------------------------------------------------------------------	
	
	static final short GRI_MAX_REC_SIZE = 50;
	static final short GRI_MAX_PACERS = 50;

	/**
	 * Total number of QRS complexes detected within the 10-seconds recording.
	 */
	short		QrsCount;
	
	/**
	 * Array of 0-based sample numbers for all QRS onsets identified within the
	 * 10-second recording.
	 */
	short []	QrsOnsets;

	/**
	 * Array of 0-based sample numbers for all QRS terminations identified
	 * within the 10-second recording.
	 */
	short []	QrsTerminations;
	
	/**
	 * QRS classification
	 */
	short []	QrsClass;
	short []	FinalArray;
	
	/**
	 * The alignment point for the QRS which is the point of maxumum spatial
	 * velocity for the beat.
	 */
	short []	AlignmentPoint;
	
	
	/**
	 * The number of pacemaker spikes detected by the analysis. If the application
	 * supplied pacemaker spike locations, this will be the count of the number
	 * of spikes used from that this.
	 */
	short		SpikeCount;
	
	/** 
	 * Array of 0-based spike locations detected by the analysis. If the 
	 * application supplied pacemaker spike locations, this will contain the spike
	 * locations that were used from that list.
	 */
	short []	SpikeLocation;

	/**
	 * Array containing an indication of the type of pacing for each QRS detected.
	 * Pacing type will be 0 for atrial pacing, 1 for ventricular pacing and 2 for
	 * A-V pacing.
	 */
	short []	QrsPacingType;
	
	/**
	 * Output status array from wave recognition module.
	 */
	short []	OutputStatus;
	
	/**
	 * Flag indicating if raw data exhibits the ventricular fibrillation pattern.
	 */
	boolean		VentricularFibrillation;
	
	
	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------

	public WaveRecognition()
	{
		QrsCount        = 0;
		SpikeCount      = 0;
		
		QrsOnsets       = new short [RestingEcg.GRI_MAX_QRS]; 
		QrsTerminations = new short [RestingEcg.GRI_MAX_QRS];
		QrsClass        = new short [RestingEcg.GRI_MAX_QRS];
		FinalArray      = new short [RestingEcg.GRI_MAX_QRS];
		AlignmentPoint  = new short [RestingEcg.GRI_MAX_QRS];

		SpikeLocation   = new short [GRI_MAX_REC_SIZE];
		QrsPacingType   = new short [GRI_MAX_REC_SIZE];
		OutputStatus    = new short [GRI_MAX_REC_SIZE];
		
		VentricularFibrillation = false;
		
	}
}

