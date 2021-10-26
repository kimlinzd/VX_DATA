package com.lepu.algorithm.restingecg.gri;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Maintains configuration parameters that will control various
 * aspects of the analysis. The bradycardia limit will always be pre-initialised
 * to 50 while the tachycardia limit will always be pre-initialised to 100.
 * Similarly, the default QTc forumla will be Hodges.
 * 
 * @author Brian Devine
 *
 */
public class ControlInfo {
	// --------------------------------------------------------------------------	
	private static final String TAG = "MyActivity-ControlInfo";
	// --------------------------------------------------------------------------	
	
	/**
	 * Default bradycardia limit.
	 */
	static final short GRI_BRADYCARDIALIMIT_DEFAULT = 50;
	
	/**
	 * Default tachycardia limit.
	 */
	static final short GRI_TACHYCARDIALIMIT_DEFAULT = 100;
	

	/**
	 * Flag to indicate if noise checks are to be applied.
	 */
	boolean	ApplyNoiseChecks;			
	
	/**
	 * Flag to indicate if Hodge QTc forumula is to be used.
	 */
	boolean	UseQTcHodge; 

	/**
	 * Flag to indicate if Bazett QTc forumula is to be used.
	 */
	boolean	UseQTcBazett;

	/**
	 * Flag to indicate if Fridericia QTc forumula is to be used.
	 */
	boolean	UseQTcFridericia;

	/**
	 * Flag to indicate if Framingham QTc forumula is to be used.
	 */
	boolean	UseQTcFramingham;
	
	/**
	 * Flag to indicate if 15-lead processing is to be performed
	 * (NOT CURRENTLY USED).
	 */
	boolean	Analysis15LeadProcessing;
	
	/**
	 * Flag to indicate if 15-lead processing is to use derived XYZ
	 * (NOT CURRENTLY USED).
	 */
	boolean	Analysis15LeadDerivedXYZ;
	
	/**
	 * Flag to indicate if reduced lead recordings are being provided
	 * (NOT CURRENTLY USED).
	 */
	boolean	AnalysisReducedLeadProcessing;
	
	/**
	 * Bit field indicating which input leads buffers are available for
	 * analysis.
	 */
	short	LeadAvailability;
	
	/**
	 * Bit field indicating which input leads buffers contain data that
	 * was recorded (NOT CURRENTLY USED).
	 */
	short	RecordedLeads;
	
	/**
	 * The limit for reporting bradycardia.
	 */
	short	BradycardiaLimit;
	
	/**
	 * The limit for reporting tachycardia.
	 */
	short	TachycardiaLimit;

	/**
	 * Flag to indicate if default demographics have been supplied
	 * for unknown patient demographic values.
	 */
	boolean	PatientDemoDefaultsProvided;

	/**
	 * Default gender to be used for adult ECG recordings when gender is unknown.
	 */
	byte	DefaultAdultGender;
	
	/**
	 * Default race to be used for adult ECG recordings when race is unknown.
	 */
	byte	DefaultAdultRace;
	
	/**
	 * Default age (in years) to be used for adult ECG recordings when
	 * age is unknown.
	 */
	short	DefaultAdultAgeYears;			// Default value provided by application
	
	
	/**
	 * Default gender to be used for paediatric ECG recordings when gender is unknown.
	 */
	byte	DefaultPaedGender;				// Default value provided by application

	/**
	 * Default race to be used for paediatric ECG recordings when race is unknown.
	 */
	byte	DefaultPaedRace;				// Default value provided by application

	/**
	 * Default age (in days) to be used for paediatric ECG recordings when
	 * age is unknown.
	 */
	short	DefaultPaedAgeDays;				// Default value provided by application


	/**
	 * Boolean set by the ECG analysis library to indicate if only the first
	 * half of the 10-second recording was used for analysis due to noise
	 * contamination.
	 */
	boolean	useFirstHalfOfRecording;

	/**
	 * Boolean set by the ECG analysis library to indicate if only the second
	 * half of the 10-second recording was used for analysis due to noise
	 * contamination.
	 */
	boolean	useSecondHalfOfRecording;

	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------

	/**
	 * Initialises the instance of the ControlInfo class. Default values are
	 * set for several parameters as follows:-
	 * <P>
	 * QTc formula is set to Hodges.
	 * Bradycardia limit is set to 50
	 * Tachycardia limit is set to 100
	 * It is indicated that no default demographic information is provided.
	 * Noise checks are disabled.
	 */
	public ControlInfo()
	{	
		
		this.ApplyNoiseChecks              = false;

		this.UseQTcHodge                   = true;			// Always default to Hodge QTc
		this.UseQTcBazett                  = false;
		this.UseQTcFridericia              = false;
		this.UseQTcFramingham              = false;

		this.Analysis15LeadProcessing      = false;
		this.Analysis15LeadDerivedXYZ      = false;
		this.AnalysisReducedLeadProcessing = false;

		this.LeadAvailability              = 0;
		this.RecordedLeads                 = 0;
		
		this.useFirstHalfOfRecording       = false;
		this.useSecondHalfOfRecording      = false;

		this.PatientDemoDefaultsProvided   = false;
		
		this.BradycardiaLimit              = GRI_BRADYCARDIALIMIT_DEFAULT;
		this.TachycardiaLimit              = GRI_TACHYCARDIALIMIT_DEFAULT;

		this.DefaultAdultGender            = 'M';			// Male
		this.DefaultAdultRace              = 'C';			// Caucasian
		this.DefaultAdultAgeYears          = 50;				// Age 50 years
		this.DefaultPaedGender             = 'M';			// Male
		this.DefaultPaedRace               = 'C';			// Caucasian
		this.DefaultPaedAgeDays            = 365;			// Age 1 year

	}

	// ----------------------------------------------------------------------

	/**
	 * Retrieves the current lead availability flag.
	 * @return	The bitwise lead availability flag.
	 */
	public short getLeadAvailability()
	{
		return this.LeadAvailability;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Converts the lead availability presented in the form of a string into
	 * a binary bit-field representation. The string that is supplied is not 
	 * a direct representation of what the binary equivalent lead availability
	 * flag will be. For example, the first character of the string represents 
	 * the availability of the first lead in the input buffer, the second
	 * character represents the availability of the second lead in the input
	 * buffer and so on. In essence, the string will be the reverse of the
	 * representation of the associated binary number.
	 * 
	 * @param leadAvail	The lead availability flags represented as a string of '0's and '1's.
	 */
	public void setLeadAvailability( String leadAvail )
	{
		short	LeadPos;
		int	FlagBitCount;
		int	_mask;
	
		FlagBitCount = leadAvail.length();
		LeadAvailability = 0;
		for ( LeadPos = 0, _mask = 1; LeadPos < RestingEcg.GRI_MAX_ECG_LEADS; LeadPos++, _mask = ( _mask << 1 ) )
		{
			if ( LeadPos < FlagBitCount && leadAvail.charAt(LeadPos) == '1' )
			{
				LeadAvailability |= _mask;
			}
		}
		Log.d( TAG, "Lead Availability string      : " + leadAvail );
		Log.d( TAG, "Lead Availability num         : " + LeadAvailability );
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Sets the lead availability flag to the given value. The lowest order
	 * bit will be true if there is available lead data within the first
	 * input buffer, the second-lowest order but will reflect the status of
	 * the lead contained in the seconds input buffer and so on.
	 * 
	 * @param leadAvail	Bitwise representation of the leads available in the
	 * input buffers
	 */
	public void setLeadAvailability( short leadAvail )
	{
		LeadAvailability = leadAvail;
	}

	// ----------------------------------------------------------------------

	/**
	 * Converts the lead availability presented in the form of an array of
	 * booleans into a binary bit-field representation. The array that is
	 * supplied is not a direct representation of what the binary equivalent
	 * lead availability flag will be. For example, the first array element
	 * represents the availability of the first lead in the input buffer,
	 * the second array element represents the availability of the second
	 * lead in the input buffer and so on. In essence, the booleans within
	 * the array will be the reverse of the representation of the associated
	 * binary number.
	 * 
	 * @param leadAvail	Array of boolean values indicating the availability of
	 * lead data in the input buffers.
	 */
	public void setLeadAvailability( boolean [] leadAvail )
	{
		short	LeadPos;
		int	FlagBitCount;
		int	_mask;

		FlagBitCount = leadAvail.length;

		this.LeadAvailability = 0;
		for ( LeadPos = 0, _mask = 1; LeadPos < RestingEcg.GRI_MAX_ECG_LEADS; LeadPos++, _mask = ( _mask << 1 ) )
		{
			if ( LeadPos < FlagBitCount && leadAvail[LeadPos] == true )
			{
				this.LeadAvailability |= _mask;
			}
		}


		Log.d( TAG, "Lead Availability num bool    : " + LeadAvailability );
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Sets the series of default values for gender, race and age for adult
	 * and paediatric ECGs that should be used by the diagnostic analysis
	 * when any of those values are missing from the demographic information.
	 * 
	 * @param AdultGender	Default gender of adult patient. Valid values are
	 * 'M' for male, 'F' for female and 'U' for unknown.
	 * @param AdultRace	Default race of adult patient. Valid values are 'C'
	 * for Caucasian/white, 'M' for Asian, 'N' for Black and 'O' for other races
	 * not listed.
	 * @param AdultAgeYears	Default age of adult patient expressed in years.
	 * @param PaedGender	Default gender of paediatric patient. Valid values are
	 * 'M' for male, 'F' for female and 'U' for unknown.
	 * @param PaedRace	Default race of paediatric patient. Valid values are 'C'
	 * for Caucasian/white, 'M' for Asian, 'N' for Black and 'O' for other races
	 * not listed.
	 * @param PaedAgeDays	Default age of paediatric patient expressed in days.
	 */
	public void setDemographicDefaults( char AdultGender, char AdultRace, int AdultAgeYears, char PaedGender, char PaedRace, int PaedAgeDays )
	{
		this.PatientDemoDefaultsProvided = true;
		
		this.DefaultAdultGender   = (byte) AdultGender;
		this.DefaultAdultRace     = (byte) AdultRace;
		this.DefaultAdultAgeYears = (short) AdultAgeYears;
		
		this.DefaultPaedGender    = (byte) PaedGender;
		this.DefaultPaedRace      = (byte) PaedRace;
		this.DefaultPaedAgeDays   = (short) PaedAgeDays;
				
	}

	// ----------------------------------------------------------------------
	
	/**
	 * Enables the use of user specified default demographic information
	 * for unknown values during the diagnostic analysis.
	 */
	public void enableDemographicDefaults()
	{
		this.PatientDemoDefaultsProvided = true;
	}

	// ----------------------------------------------------------------------

	/**
	 * Inhibits the use of user specified default demographic information
	 * for unknown values during the diagnostic analysis.
	 */
	public void disableDemographicDefaults()
	{
		this.PatientDemoDefaultsProvided = false;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Sets a control flag to indicate that the analysis should apply noise
	 * checks.
	 */
	public void enableNoiseChecks()
	{
		this.ApplyNoiseChecks = true;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Sets a control flag to indicate that the analysis should not apply
	 * noise checks.
	 */
	public void disableNoiseChecks()
	{
		this.ApplyNoiseChecks = false;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Sets the control flags to indicate that the Hodge QTc formula
	 * should be used by the main diagnostic analysis. Resets all other
	 * QTc formula flags to FALSE.
	 */
	public void setQTcHodge()
	{
		this.UseQTcHodge      = true;
		this.UseQTcBazett     = false;
		this.UseQTcFridericia = false;
		this.UseQTcFramingham = false;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the control flags to indicate that the Bazett QTc formula
	 * should be used by the main diagnostic analysis. Resets all other
	 * QTc formula flags to FALSE.
	 */
	public void setQTcBazett()
	{
		this.UseQTcHodge      = false;
		this.UseQTcBazett     = true;
		this.UseQTcFridericia = false;
		this.UseQTcFramingham = false;
	}

	// ----------------------------------------------------------------------
	
	/**
	 * Sets the control flags to indicate that the Fridericia QTc formula
	 * should be used by the main diagnostic analysis. Resets all other
	 * QTc formula flags to FALSE.
	 */
	public void setQTcFridericia()
	{
		this.UseQTcHodge      = false;
		this.UseQTcBazett     = false;
		this.UseQTcFridericia = true;
		this.UseQTcFramingham = false;
	}

	// ----------------------------------------------------------------------
	
	/**
	 * Sets the control flags to indicate that the Framingham QTc formula
	 * should be used by the main diagnostic analysis. Resets all other
	 * QTc formula flags to FALSE.
	 */
	public void setQTcFramingham()
	{
		this.UseQTcHodge      = false;
		this.UseQTcBazett     = false;
		this.UseQTcFridericia = false;
		this.UseQTcFramingham = true;
	}

	// ----------------------------------------------------------------------

	/**
	 * Determines if Bazett QTc formula is to be used.
	 *  
	 * @return	TRUE if Bazett QTc formula is to be used.
	 */
	public boolean isQTcBazett()
	{
		if ( this.UseQTcBazett )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Determines if Framingham QTc formula is to be used.
	 *  
	 * @return	TRUE if Framingham QTc formula is to be used.
	 */
	public boolean isQTcFramingham()
	{
		if ( this.UseQTcFramingham )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// ----------------------------------------------------------------------

	/**
	 * Determines if Fridericia QTc formula is to be used.
	 *  
	 * @return	TRUE if Fridericia QTc formula is to be used.
	 */
	public boolean isQTcFridericia()
	{
		if ( this.UseQTcFridericia )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// ----------------------------------------------------------------------

	/**
	 * Determines if Hodge QTc formula is to be used.
	 *  
	 * @return	TRUE if Hodge QTc formula is to be used.
	 */
	public boolean isQTcHodge()
	{
		if ( this.UseQTcHodge )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// ----------------------------------------------------------------------
	
	/**
	 * Sets the control flags to indicate which QTc formula is to be used
	 * by the main diagnostic analysis. The parameter indicates which
	 * QTc formula is to be used by the given name. The parameter is case
	 * insensitive. If the parameter is not a valid QTc formula then
	 * the default Hodge formula will be set.
	 * 
	 * @param QTcFormula	The QTc formula to be used. Options can be
	 * one of 'Hodge', 'Fridericia', 'Framingham' or 'Bazett'.
	 * @return	TRUE if a valid QTc formula has been supplied.
	 * Returns FALSE if an unrecognised QTc formula has been supplied.
	 */
	public boolean setQTcFormula( String QTcFormula )
	{
		String lowercaseQTc;
		boolean	Status;


		Status = true;
		lowercaseQTc = QTcFormula.toLowerCase();


		setQTcHodge();

		if ( lowercaseQTc.equals("hodge") )
		{
			setQTcHodge();
		}
		else if ( lowercaseQTc.equals("bazett") )
		{
			setQTcBazett();
		}
		else if ( lowercaseQTc.equals("fridericia") )
		{
			setQTcFridericia();
		}
		else if ( lowercaseQTc.equals("framingham") )
		{
			setQTcFramingham();
		}
		else
		{
			Status = false;		// Invalid parameter specified
		}
		
		return Status;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the heart rate at which the analysis will start to report
	 * bradycardia. This function is primarily intended for processing the single
	 * line entry within the GRIANLYS input file that specifies both bradycardia
	 * and tachycardia.
	 *  
	 * @param BradyTachyLimit	A string containing the bradycardia and tachycardia limits, separated by a space
	 * @return	Returns TRUE if the bradycardia limit is successfully set otherwise returns FALSE
	 */
	public boolean setBradycardiaLimit( String BradyTachyLimit )
	{
		boolean	Status = true;		// Assume success
		
		String patternString1 = "(\\d+) (\\d+)";
		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher = pattern.matcher(BradyTachyLimit);

		while(matcher.find())
		{
			try
			{
				this.BradycardiaLimit = Short.parseShort(matcher.group(1));
			}
			catch (NumberFormatException e)
			{
				System.err.println("The string containing the bradycardia limit is not properly formatted!");
				Status = false;
			}  
		}
		
		return Status;
	
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the heart rate at which the analysis will start to report
	 * tachycardia. This function is primarily intended for processing the single
	 * line entry within the GRIANLYS input file that specifies both bradycardia
	 * and tachycardia.
	 *  
	 * @param BradyTachyLimit	A string containing the bradycardia and tachycardia limits, separated by a space
	 * @return	Returns TRUE if the tachycardia limit is successfully set otherwise returns FALSE
	 */
	public boolean setTachycardiaLimit( String BradyTachyLimit )
	{
		boolean	Status = true;		// Assume success
		
		String patternString1 = "(\\d+) (\\d+)";
		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher = pattern.matcher(BradyTachyLimit);

		while(matcher.find())
		{
			try
			{
				this.TachycardiaLimit = Short.parseShort(matcher.group(2));
			}
			catch (NumberFormatException e)
			{
				System.err.println("The string containing the tachycardia limit is not properly formatted!");
				Status = false;
			}  
		}
		
		return Status;
	
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the heart rate at which the analysis will start to report
	 * sinus bradycardia.
	 *  
	 * @param BradyLimit	The heart rate at which bradycardia should be reported.
	 */
	public void setBradycardiaLimit( int BradyLimit )
	{
		this.BradycardiaLimit  = (short) BradyLimit;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Sets the heart rate at which the analysis will start to report
	 * sinus tachycardia.
	 * 
	 * @param TachyLimit	The heart rate at which tachycardia should be reported.
	 */
	public void setTachycardiaLimit( int TachyLimit )
	{
		this.TachycardiaLimit  = (short) TachyLimit;
	}

	// ----------------------------------------------------------------------
	
	/**
	 * Returns the heart rate at which the analysis software starts to report
	 * sinus bradycardia.
	 * 
	 * @return	The bradycardia heart rate in beats per minute.
	 */
	public int getBradycardiaLimit()
	{
		return (int) this.BradycardiaLimit;
	}

	// ----------------------------------------------------------------------

	/**
	 * Returns the heart rate at which the analysis software starts to report
	 * sinus tachycardia.
	 * 
	 * @return	The tachycardia heart rate in beats per minute.
	 */
	public int getTachycardiaLimit()
	{
		return (int) this.TachycardiaLimit;
	}

	// ----------------------------------------------------------------------
	
	/**
	 * Checks to see if the lead with the given lead identifier is available,
	 * taking into account the electrode positioning used and its effect on
	 * the bitwise lead availability flag. The lead availability flag
	 * must have been set prior to calling this method.
	 * 
	 * @param LeadId	The lead identifier for the lead to be tested.
	 * @param isPaediatricElectrodePositioning	Flag that is set to TRUE if
	 * paediatric electrode positioning has been used, otherwise set to FALSE.
	 * @return	TRUE if the lead is available, otherwise FALSE
	 */
	public boolean isLeadAvailable( GriLeadId LeadId, boolean isPaediatricElectrodePositioning )
	{
		short	LeadPos;
		int	_mask;
		GriLeadId []	LeadConfig;
		
		if ( isPaediatricElectrodePositioning )
		{
			// Define the paediatric lead selection and order
			LeadConfig = new GriLeadId [] {
					GriLeadId.GRI_LEADID_I, GriLeadId.GRI_LEADID_II,
					GriLeadId.GRI_LEADID_III, GriLeadId.GRI_LEADID_aVR,
					GriLeadId.GRI_LEADID_aVL, GriLeadId.GRI_LEADID_aVF,
					GriLeadId.GRI_LEADID_V4R, GriLeadId.GRI_LEADID_V1,
					GriLeadId.GRI_LEADID_V2, GriLeadId.GRI_LEADID_V4,
					GriLeadId.GRI_LEADID_V5, GriLeadId.GRI_LEADID_V6,
					GriLeadId.GRI_LEADID_UNDEFINED, GriLeadId.GRI_LEADID_UNDEFINED,
					GriLeadId.GRI_LEADID_UNDEFINED
			};
			
		}
		else
		{
			// Define the adult lead selection and order
			LeadConfig = new GriLeadId [] {
					// Define the standard/adult lead selection and order
					GriLeadId.GRI_LEADID_I, GriLeadId.GRI_LEADID_II,
					GriLeadId.GRI_LEADID_III, GriLeadId.GRI_LEADID_aVR,
					GriLeadId.GRI_LEADID_aVL, GriLeadId.GRI_LEADID_aVF,
					GriLeadId.GRI_LEADID_V1, GriLeadId.GRI_LEADID_V2,
					GriLeadId.GRI_LEADID_V3, GriLeadId.GRI_LEADID_V4,
					GriLeadId.GRI_LEADID_V5, GriLeadId.GRI_LEADID_V6,
					GriLeadId.GRI_LEADID_UNDEFINED, GriLeadId.GRI_LEADID_UNDEFINED,
					GriLeadId.GRI_LEADID_UNDEFINED
			};
		}

		// Scan LeadConfig to find matching lead
		for ( LeadPos = 0, _mask = 1; LeadPos < RestingEcg.GRI_MAX_ECG_LEADS; LeadPos++, _mask = ( _mask << 1 ) )
		{
			if ( LeadConfig[LeadPos] == LeadId )
			{
				if ( ( this.LeadAvailability & _mask ) == _mask )
				{
					return true;
				}
			}
		}
		
		// Didn't find a match so lead is unavailable
		
		return false;

	}
	
	// ----------------------------------------------------------------------

	/**
	 * Sets the default adult age (in years) to be used for the diagnostic
	 * analysis when the patient's age is unknown.
	 * 
	 * @param AgeYears	The default adult age in years.
	 * @see ControlInfo#enableDemographicDefaults()
	 * @see ControlInfo#disableDemographicDefaults()
	 */
	public void setDefaultAdultAge( int AgeYears )
	{
		this.DefaultAdultAgeYears = (short) AgeYears;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the default paediatric age (in days) to be used for the diagnostic
	 * analysis when the patient's age is unknown.
	 * 
	 * @param AgeDays	The default paediatric age in days.
	 * @see ControlInfo#setDemographicDefaults(char, char, int, char, char, int)
	 * @see ControlInfo#enableDemographicDefaults()
	 * @see ControlInfo#disableDemographicDefaults()
	 */
	public void setDefaultPaediatricAge( int AgeDays )
	{
		this.DefaultPaedAgeDays = (short) AgeDays;
	}

	// ----------------------------------------------------------------------

	/**
	 * Prints the contents of the {@link ControlInfo} instance to the debug
	 * output log.
	 */
	public void printContent()
	{
		Log.d( TAG, "ControlInfo" );
		Log.d( TAG, "===========" );
		Log.d( TAG, "Lead Availability             : " + LeadAvailability );
		Log.d( TAG, "Recorded Leads                : " + RecordedLeads );
		
		Log.d( TAG, "Apply noise checks            : " + ApplyNoiseChecks );
		
		Log.d( TAG, "Use first half of recording   : " + useFirstHalfOfRecording );
		Log.d( TAG, "Use second half of recording  : " + useSecondHalfOfRecording );

		Log.d( TAG, "Bradycardia limit             : " + BradycardiaLimit );
		Log.d( TAG, "Tachycardia limit             : " + TachycardiaLimit );
		
		Log.d( TAG, "Hodge QTc                     : " + UseQTcHodge );
		Log.d(TAG,  "Bazett QTc                    : " + UseQTcBazett );
		Log.d(TAG,  "Fridericia QTc                : " + UseQTcFridericia );
		Log.d(TAG,  "Framingham QTc                : " + UseQTcFramingham );
		
		Log.d(TAG,  "15-lead Processing            : " + Analysis15LeadProcessing );
		Log.d(TAG,  "Derived XYZ Processing        : " + Analysis15LeadDerivedXYZ );
		Log.d(TAG,  "Reduced lead Processing       : " + AnalysisReducedLeadProcessing );

		Log.d(TAG,  "Patient demo defaults?        : " + PatientDemoDefaultsProvided );
		
		Log.d(TAG,  "Default adult gender          : " + (char)DefaultAdultGender );
		Log.d(TAG,  "Default adult race            : " + (char)DefaultAdultRace );
		Log.d(TAG,  "Default adult age (years)     : " + DefaultAdultAgeYears );
		Log.d(TAG,  "Default paediatric gender     : " + (char)DefaultPaedGender );
		Log.d(TAG,  "Default paediatric race       : " + (char)DefaultPaedRace );
		Log.d(TAG,  "Default paediatric age (days) : " + DefaultPaedAgeDays );

		Log.d( TAG, " " );
	}
}

