package com.lepu.algorithm.restingecg.gri;

import android.util.Log;

/**
 * Maintains information relating to the recorded ECG that are used for
 * signal processing aspects of the analysis.
 * 
 * @author Brian Devine
 *
 */
public class EcgInfo {
	// ----------------------------------------------------------------------
	private static final String TAG = "MyActivity-EcgInfo";
	// ----------------------------------------------------------------------
	static final short	GRI_NORMAL_ELECTRODES = 0;
	static final short	GRI_PAEDV4R_ELECTRODES = 2; 

	
	/**
	 * The sampling rate of the recording. This must be 500.
	 */
	short	EcgSamplingFrequency;
	
	/**
	 * The number of samples in each lead. This must be 5000, which conforms
	 * to 10 seconds of data sampled at 500 samples/sec.
	 */
	int		EcgSamplesPerLead;
	
	/**
	 * The number of LSB per millivolt. This value will be changed by the
	 * analysis process to reflect any scaling that may be applied to the
	 * original raw ECG data.
	 */
	short	LSBPerMV;
	
	/**
	 * The electrode positioning used when recording the ECG. 0 indicated
	 * adult electrode positioning and 2 indicated paediatric (V4R) 
	 * electrode positioning.
	 */
	short	ElectrodePositioning;
	
	/**
	 * The mains frequency filter to be applied. This should be 50 or 60
	 * for 50Hz and 60Hz filters or 0 if the raw data has already been
	 * filtered and no filter it to be applied. 
	 */
	short	MainsFilterToApply;
	
	/**
	 * The number of ECG leads in the recording. This should be set to 
	 * 12.
	 */
	short	NumberOfLeads;
	
	/**
	 * The lead configuration used. This must conform to standard adult
	 * or paediatric lead configuration as described within the C library API
	 * document.
	 */
	short []	LeadConfig;

	
	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------
	
	/**
	 * Creates an instance of {@link EcgInfo} with a default lead configuration
	 * for normal electrode placement i.e. I, II, III, aVR, aVF, V1, V2, V3,
	 * V4, V5, V6.
	 */
	public EcgInfo()
	{

		this( new GriLeadId [] {
				GriLeadId.GRI_LEADID_I, GriLeadId.GRI_LEADID_II,
				GriLeadId.GRI_LEADID_III, GriLeadId.GRI_LEADID_aVR,
				GriLeadId.GRI_LEADID_aVL, GriLeadId.GRI_LEADID_aVF,
				GriLeadId.GRI_LEADID_V1, GriLeadId.GRI_LEADID_V2,
				GriLeadId.GRI_LEADID_V3, GriLeadId.GRI_LEADID_V4,
				GriLeadId.GRI_LEADID_V5, GriLeadId.GRI_LEADID_V6,
				GriLeadId.GRI_LEADID_UNDEFINED, GriLeadId.GRI_LEADID_UNDEFINED,
				GriLeadId.GRI_LEADID_UNDEFINED }
		 );
		
	}

	// ----------------------------------------------------------------------

	/**
	 * Creates an instance of {@link EcgInfo} with the given lead configuration.
	 * 
	 * @param LeadId	Array of {@link GriLeadId} lead identifiers that
	 * specify the order of leads presented in the data buffers.
	 */
	public EcgInfo( GriLeadId [] LeadId )
	{
		short	i;

		// Set defaults
		this.EcgSamplingFrequency = RestingEcg.GRI_COMPLEX_SAMPLES;	// Glasgow software can only use 500 samples/sec data
		this.LSBPerMV             = 200;
		this.ElectrodePositioning = GRI_NORMAL_ELECTRODES;
		this.MainsFilterToApply   = 0;
		this.NumberOfLeads        = 12;
		this.EcgSamplesPerLead    = RestingEcg.GRI_ECG_SAMPLES;
		this.LeadConfig           = new short [RestingEcg.GRI_MAX_ECG_LEADS];
		

		// Get the lead identifier constant that will be used by C library.
		for ( i = 0; i < RestingEcg.GRI_MAX_ECG_LEADS; i++ )
		{
			this.LeadConfig[i] = (short) LeadId[i].getValue();
		}
		
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the particular electrode positioning used when recording the
	 * ECG.
	 * 
	 * @param ElecPositioning	The case insensitive electrode positioning
	 * used. Can be one of "adult" / "normal" or "paeiatric" / "pediatric" / "v4r". 
	 * @return	TRUE if the electrode positioning parameter is valid, otherwise
	 * returns FALSE
	 */
	public boolean setElectrodePositioning( String ElecPositioning )
	{
		String lowercaseElecPositioning;
		boolean	Status;
		

		Status = true;
		lowercaseElecPositioning = ElecPositioning.toLowerCase().trim();
		if ( lowercaseElecPositioning.equals("adult") || lowercaseElecPositioning.equals("normal") )
		{
			this.ElectrodePositioning = GRI_NORMAL_ELECTRODES;
		}
		else if ( lowercaseElecPositioning.equals("paediatric") || lowercaseElecPositioning.equals("pediatric") || lowercaseElecPositioning.equals("v4r") )
		{
			this.ElectrodePositioning = GRI_PAEDV4R_ELECTRODES;
		}
		else
		{
			Status = false;
		}

		return Status;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Sets the sampling rate at which the ECG data was acquired.
	 * 
	 * @param EcgSampFreq	The data sampling rate in samples/sec.
	 */
	public void setEcgSamplingFrequency( short EcgSampFreq )
	{
		this.EcgSamplingFrequency = EcgSampFreq;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the mains frequency filter that is to be applied.
	 * 
	 * @param MainsFilterToApply	The frequency of the mains filter to be
	 * applied. This can be either 50, 60 or 0 for no filter.
	 */
	public void setMainsFilterToApply( short MainsFilterToApply )
	{
		this.MainsFilterToApply = MainsFilterToApply;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Sets the electrode positioning to the numerically coded value.
	 * 
	 * @param ElectrodePositioning	The numerically coded electrode position
	 * which can be GRI_NORMAL_ELECTRODES or GRI_PAEDV4R_ELECTRODES.
	 */
	public void setElectrodePositioning( short ElectrodePositioning )
	{
		this.ElectrodePositioning = ElectrodePositioning;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Sets the LSB per mV for the input data.
	 * 
	 * @param LSBPerMV	The resolution of the input data.
	 */
	public void setLSBPerMV( short LSBPerMV )
	{
		this.LSBPerMV = LSBPerMV;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the number of leads available for analysis.
	 * 
	 * @param NumberOfLeads	The number of leads available in the input buffers.
	 */
	public void setNumberOfLeads( short NumberOfLeads )
	{
		this.NumberOfLeads = NumberOfLeads;
	}

	// ----------------------------------------------------------------------
	
	/**
	 * Sets the lead configuration to the order indicated. The lead 
	 * configuration will be stored as the enumerated value that can be
	 * used directly by the C analysis library.
	 * 
	 * @param LeadConfig	Array containing {@link GriLeadId} lead identifiers.
	 */
	public void setLeadConfig( GriLeadId [] LeadConfig )
	{
		short	i;
		
		for ( i = 0; i < this.LeadConfig.length; i++ )
		{
			if ( i < LeadConfig.length )
			{
				this.LeadConfig[i] = (short) LeadConfig[i].getValue();
			}
			else
			{
				this.LeadConfig[i] = (short) GriLeadId.GRI_LEADID_UNDEFINED.getValue();		// Unused
			}
		}
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the number of samples per lead to the give value.
	 * 
	 * @param EcgSamplesPerLead	The number of samples in a lead.
	 */
	public void setEcgSamplesPerLead( short EcgSamplesPerLead )
	{
		this.EcgSamplesPerLead = EcgSamplesPerLead;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Examines the status of the electrode positioning and returns TRUE
	 * if the electrode positioning uses the V4R setup for paediatric
	 * ECG recordings.
	 * 
	 * @return	TRUE is the electrode positioning used is for paediatric
	 * recordings, otherwise returns FALSE.
	 */
	public boolean isPaediatricElectrodePlacement()
	{
		if ( this.ElectrodePositioning == GRI_PAEDV4R_ELECTRODES )
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
	 * Prints out the contents of the {@link EcgInfo} instance.
	 */
	public void printContent()
	{
		String DebugLine;

		Log.d( TAG, "EcgInfo" );
		Log.d( TAG, "=======" );
		Log.d( TAG, "Sampling frequency    : " + EcgSamplingFrequency );
		Log.d( TAG, "LSBPerMV              : " + LSBPerMV );
		DebugLine = "Electrode positioning : ";
		if ( ElectrodePositioning == 0 )
		{
			DebugLine += "Adult (" + ElectrodePositioning + ")";
		}
		else if ( ElectrodePositioning == 2 )
		{
			DebugLine += "Paediatric V4R (" + ElectrodePositioning + ")";
		}
		else
		{
			DebugLine += "Unknown (" + ElectrodePositioning + ")";
		}
		Log.d( TAG, DebugLine );
		Log.d( TAG, "Mains filter to apply : " + MainsFilterToApply );
		Log.d( TAG, "Number of leads       : " + NumberOfLeads );
		Log.d( TAG, "Samples per lead      : " + EcgSamplesPerLead );

		Log.d( TAG, " " );
		
	}
}
