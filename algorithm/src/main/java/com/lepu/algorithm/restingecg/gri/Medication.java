package com.lepu.algorithm.restingecg.gri;

public enum Medication {
	GRI_DRUG_UNDEFINED(-1),

	GRI_DRUG_EMPTY(0),
	GRI_DRUG_NONE(1),
	GRI_DRUG_UNKNOWN(2),
	GRI_DRUG_DIGITALIS(3),
	GRI_DRUG_DIURETIC(4),
	GRI_DRUG_BETABLOCKER(5),
	GRI_DRUG_QUINIDINE(6),
	GRI_DRUG_PROCAINMIDE(7),
	GRI_DRUG_AMIODARONE(8),
	GRI_DRUG_DISOPYRAMIDE(9),
	GRI_DRUG_LIGNOCAINE(10),
	GRI_DRUG_OTH_ANTIARRHYTHMIC(11),
	GRI_DRUG_PSYCHOTROPIC(12),
	GRI_DRUG_STEROID(13),
	GRI_DRUG_OTHER(14);
	
	
	/**
	 * The value of the enumeration.
	 */
	private final int	Drug;

	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------

	/**
	 * Creates and initialises enumerated medication. The enumerated
	 * values are consistent with the constants used within the C analysis 
	 * library.
	 *  
	 * @param LeadId	The enumerated value for the lead.
	 */
	Medication(int Drug)
	{
		this.Drug = Drug;
	}

	// ----------------------------------------------------------------------

	/**
	 * Retrieves the value of the enumerated type
	 * 
	 * @return	The value of the enumeration.
	 */
	public int getValue()
	{
		return this.Drug;
	}
	
	// ----------------------------------------------------------------------
	
	/**
	 * Converts the enumerated value to a descriptive label.
	 * 
	 * @return	A string containing the descriptive text for the medication.
	 */
	public String getLabel()
	{
		String Label;
		
		switch ( this.Drug )
		{
			case 0:		Label = "Unknown";
						break;
			case 1:		Label = "No medication";
						break;
			case 2:		Label = "Unknown medication";
						break;
			case 3:		Label = "Digitalis";
						break;
			case 4:		Label = "Diuretic";
						break;
			case 5:		Label = "Beta blocker";
						break;
			case 6:		Label = "Quinidine";
						break;
			case 7:		Label = "Procainamide";
						break;
			case 8:		Label = "Amiodarone";
						break;
			case 9:		Label = "Disopyramide";
						break;
			case 10:	Label = "Lignocaine";
						break;
			case 11:	Label = "Other Antiarrhythmics";
						break;
			case 12:	Label = "Psychotropic";
						break;
			case 13:	Label = "Steroid";
						break;
			case 14:	Label = "Other medication";
						break;
			default:	Label = "Undefined";
						break;
		}
		
		return Label;
	}

}
