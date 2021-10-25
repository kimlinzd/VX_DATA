package com.lepu.algorithm.restingecg.gri;

public enum ClinicalClassification {
	GRI_DRUG_UNDEFINED(-1),

	GRI_CLIN_EMPTY(0),
	GRI_CLIN_NONE(1),
	GRI_CLIN_NORMAL(10),
	GRI_CLIN_INFARCT(11),
	GRI_CLIN_ISCHEMIA(12),
	GRI_CLIN_HYPERTENSION(13),
	GRI_CLIN_CONGENITAL(15),
	GRI_CLIN_RHEUMATIC(17),
	GRI_CLIN_PERICARDITIS(30),
	GRI_CLIN_RESPIRATORY(31),
	GRI_CLIN_ENDOCRINE(33),
	GRI_CLIN_PACEMAKER(46),
	GRI_CLIN_PULMONARYEMBOLISM(47),
	GRI_CLIN_POSTOPCHANGES(49),
	GRI_CLIN_OTHER(50),
	GRI_CLIN_CARDIOMYOPATHY(51),
	GRI_CLIN_UNKNOWN(52);

	
	/**
	 * The value of the enumeration.
	 */
	private final int	ClinClass;

	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------


	/**
	 * Constructor that initialises enumerated lead identifier. The enumerated
	 * values are consistent with the constants used within the C analysis 
	 * library.
	 *  
	 * @param LeadId	The enumerated value for the lead.
	 */
	ClinicalClassification(int ClinClass)
	{
		this.ClinClass = ClinClass;
	}

	// ----------------------------------------------------------------------

	/**
	 * Retrieves the value of the enumerated type
	 * 
	 * @return	The value of the enumeration.
	 */
	public int getValue()
	{
		return this.ClinClass;
	}
	
	// ----------------------------------------------------------------------
	
	/**
	 * Converts the enumerated value to a lead label.
	 * 
	 * @return	A string containing the text identifying the particular lead.
	 */
	public String getLabel()
	{
		String Label;
		
		switch ( this.ClinClass )
		{
			case 0:		Label = "I";
						break;
			case 1:		Label = "II";
						break;
			case 2:		Label = "III";
						break;
			case 3:		Label = "aVR";
						break;
			case 4:		Label = "aVL";
						break;
			case 5:		Label = "aVF";
						break;
			case 6:		Label = "V1";
						break;
			case 7:		Label = "V2";
						break;
			case 8:		Label = "V3";
						break;
			case 9:		Label = "V4";
						break;
			case 10:	Label = "V5";
						break;
			case 11:	Label = "V6";
						break;
			case 12:	Label = "V7";
						break;
			case 13:	Label = "V8";
						break;
			case 14:	Label = "V9";
						break;
			case 15:	Label = "V10";
						break;
			case 20:	Label = "V2R";
						break;
			case 21:	Label = "V3R";
						break;
			case 22:	Label = "V4R";
						break;
			case 23:	Label = "V5R";
						break;
			case 24:	Label = "V6R";
						break;
			case 25:	Label = "V7R";
						break;
			case 26:	Label = "V8R";
						break;
			case 27:	Label = "V9R";
						break;
			case 50:	Label = "X";
						break;
			case 51:	Label = "Y";
						break;
			case 52:	Label = "Z";
						break;
			case 53:	Label = "ADD1";
						break;
			case 54:	Label = "ADD2";
						break;
			case 55:	Label = "ADD3";
						break;
			default:	Label = "Undefined";
						break;
		}
		
		return Label;
	}

}
