package com.lepu.algorithm.restingecg.gri;

/**
 * Lead identification enumerator that creates lead identifiers consistent
 * with those used by the C analysis library. Additional lead identifiers
 * have been included are are not currently recognised by the C analysis
 * library.
 * 
 * @author Brian Devine
 *
 */
public enum GriLeadId {
	GRI_LEADID_I(0),
	GRI_LEADID_II(1),
	GRI_LEADID_III(2),
	GRI_LEADID_aVR(3),
	GRI_LEADID_aVL(4),
	GRI_LEADID_aVF(5),
	GRI_LEADID_V1(6),
	GRI_LEADID_V2(7),
	GRI_LEADID_V3(8),
	GRI_LEADID_V4(9),
	GRI_LEADID_V5(10),
	GRI_LEADID_V6(11),
	GRI_LEADID_V7(12),
	GRI_LEADID_V8(13),
	GRI_LEADID_V9(14),
	GRI_LEADID_V10(15),
	GRI_LEADID_V2R(20),
	GRI_LEADID_V3R(21),
	GRI_LEADID_V4R(22),
	GRI_LEADID_V5R(23),
	GRI_LEADID_V6R(24),
	GRI_LEADID_V7R(25),
	GRI_LEADID_V8R(26),
	GRI_LEADID_V9R(27),
	GRI_LEADID_X(50),
	GRI_LEADID_Y(51),
	GRI_LEADID_Z(52),
	GRI_LEADID_ADD1(53),
	GRI_LEADID_ADD2(54),
	GRI_LEADID_ADD3(55),
	
	GRI_LEADID_UNDEFINED(-1);
	
	/**
	 * The value of the enumeration.
	 */
	private final int	LeadId;

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
	GriLeadId(int LeadId)
	{
		this.LeadId = LeadId;
	}

	// ----------------------------------------------------------------------

	/**
	 * Retrieves the value of the enumerated type
	 * 
	 * @return	The value of the enumeration.
	 */
	public int getValue()
	{
		return this.LeadId;
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
		
		switch ( this.LeadId )
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
			default:	Label = "-";
						break;
		}
		
		return Label;
	}
}
