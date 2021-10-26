package com.lepu.algorithm.restingecg.gri;

/**
 * Analysis status enumerator that simplifies the status return values from the
 * main C ECG analysis library. If the analysis status is anything other than
 * {@link GRIERR_OK}, the content of the analysis results structures cannot be 
 * used and shoule be regarded as being in an 'undefined' state.
 * 
 * @author Brian Devine
 *
 */
public enum AnalysisStatus {
	
	/**
	 * Analysis successful.
	 */
	GRIERR_OK(0),
	
	/**
	 * Analysis terminated in library due to issue with allocating memory
	 * or initialising internal structures.
	 */
	GRIERR_PROC_INIT(10),
	
	/**
	 * Analysis terminated in library due to failure to obtain references
	 * for Java instance objects/fields.
	 */
	GRIERR_PROC_JAVAFIELD(11),
		
	/**
	 * Analysis terminated having failed to find a sufficient number of QRS
	 * complexes within the 10-second recording.
	 */
	GRIERR_ANLYS_NO_QRS(100),
	
	/**
	 * Analysis terminated after finding too many different types of QRS
	 * morphology within the 10-second recording or failed to align the
	 * QRS complexes properly.
	 */
	GRIERR_ANLYS_WAVETYPING(101),
	
	/**
	 * Analysis terminated after finding too many or too few QRS complexes
	 * or there were issues with the calibration values for the ECG
	 * leads.
	 */
	GRIERR_ANLYS_MEAS_INVALID(102),
	
	/**
	 * Analysis terminated after rejecting all leads at the wave measurement
	 * stage. This is generally an issue with QRS component identification.
	 */
	GRIERR_ANLYS_NOLEADS(103),
	
	/**
	 * Analysis terminated with an undefined error. This is a catch all error.
	 */
	GRIERR_ANLYS_UNDEFINED(104);
	
	
	/**
	 * The value of the enumeration.
	 */
	private final int	Status;

	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------


	/**
	 * Constructor that initialises enumerated analysis status. The enumerated
	 * values are consistent with the constants used within the C analysis 
	 * library.
	 *  
	 * @param Status	The enumerated value for error status.
	 */
	AnalysisStatus(int Status)
	{
		this.Status = Status;
	}

	// ----------------------------------------------------------------------

	/**
	 * Retrieves the value of the enumerated type
	 * 
	 * @return	The value of the enumeration.
	 */
	public int getValue()
	{
		return this.Status;
	}

}
