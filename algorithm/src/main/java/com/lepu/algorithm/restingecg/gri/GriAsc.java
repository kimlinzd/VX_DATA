package com.lepu.algorithm.restingecg.gri;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// Basic information identifying the header lines present in the GRIANLYS
// input file.
enum GRIASCLineType { GRIASC_VER, GRIASC_PATID, GRIASC_SURNAME, GRIASC_FORENAMES,
	GRIASC_GENDER, GRIASC_RACE, GRIASC_AGEDAYS, GRIASC_DATES, GRIASC_RECTIME,
	GRIASC_ELEC, GRIASC_MEDS, GRIASC_CLIN, GRIASC_MAINS, GRIASC_BRADTACH, GRIASC_QTC,
	GRIASC_PACERS, GRIASC_ANATYPE, GRIASC_LEADAVA, GRIASC_LEADIDENT, GRIASC_CAL,
	GRIASC_SRATE, GRIASC_SLEAD, GRIASC_LEADCNT, GRIASC_LEADDATA, GRIASC_END
};


/**
 * Retrieval and storage of information from a GRIANLYS ASCII formatted data
 * file. This is the main method for obtaining
 * ECG data for processing during the software development stage and is
 * consistent with the operation of the C GRIANLYS application. The GRIANLYS
 * documentation should be consulted to determine the format of each field
 * within this class definition.
 * 
 * Note that only GRIANLYS input file versions 1.3 and 1.4 are currently
 * supported.
 * 
 * @author Brian Devine
 *
 */
public class GriAsc {
	// ----------------------------------------------------------------------
	private static final String TAG = "MyActivity-GriAsc";
	// ----------------------------------------------------------------------
	
	/**
	 * The version number of the input file.
	 */
	String FileVersion;
	
	/**
	 * The patient identification.
	 */
	String PatientId;
	
	/**
	 * The surname of the patient.
	 */
	String PatientSurname;
	
	/**
	 * The firstname and middle name(s) of the patient.
	 */
	String PatientForenames;			// Future release
	
	/**
	 * The gender of the patient. Can be one of 'M', 'F' or 'U'.
	 */
	byte	Gender;
	
	/**
	 * The race of the patient. Can be one of 'C', 'M', 'N', 'O' or 'U'.
	 */
	byte	Race;
	
	/**
	 * The patient's age expressed in days. Should be -1 if unknown.
	 */
	int		AgeDays;
	
	/**
	 * The electrode positioning used when recording the ECG. This can 
	 * be either "Adult" or "Paediatric".
	 */
	String ElectrodePositioning;
	
	/**
	 * The coded clinical classification for the patient.
	 */
	String Clinical;
	
	/**
	 * The coded drug/medication for the patient.
	 */
	String Drugs;
	
	/**
	 * The mains frequency filter that is to be applied.
	 */
	short	MainsFilterFrequency;
	
	/** 
	 * The limits at which bradycardia and tachycardia are to be reported.
	 */
	String BradyTachyLimit;
	
	/**
	 * The lead availability flag bitmap.
	 */
	String LeadAvailability;
	
	/**
	 * The recorded leads flag bitmap. This is not currently used but is
	 * included for a future release.
	 */
	String RecordedLeads;				// Future release
	
	/**
	 * The number of LSB per millivolt.
	 */
	short	LSBperMV;
	
	/**
	 * The sampling rate (samples/sec) of the raw ECG signal. This must be 500
	 * to be compatible with the C analysis library.
	 */
	short	SamplingRate;
	
	/**
	 * The number of samples in each lead. This must be 5000 to be compatible
	 * with the C analysis library.
	 */
	short	NumSamplesPerLead;
	
	/**
	 * The number of ECG leads in the recording. This must be 12 to be compatible
	 * with the C analysis library.
	 */
	short	NumLeads;
	
	String QTcMeasure;
	
	/**
	 * The time at which the ECG was recorded, in the format "HH:MM:SS".
	 */
	String RecordingTime;
	
	
	/** 
	 * The birth date of the patient and the date on which the ECG was recorded.
	 * The dates are in the format "DD/MM/CCYY" and are separated by a comma.
	 * Unknown dates should be expressed as "00/00/0000".
	 */
	String Dates;
	
	/**
	 * The list of pacemaker spikes and an indication as to whether or not the
	 * pacemake spikes are present within the raw data. See the GRIANLYS
	 * documentation for a description of the format.
	 */
	String Pacers;
	
	/**
	 * The type of analysis to be performed. Currently only "12Lead" is
	 * supported in the Android build.
	 */
	String AnalysisType;
	
	/**
	 * The order of leads presented in the input file. This is not currently
	 * used but is included for a future software release.
	 */
	String LeadConfig;
	
	/**
	 * Array containing the raw ECG lead samples. 
	 */
	IntLead[] Leads;


	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------

	/**
	 * Constructor that sets initial default values for the any input
	 * file that will be processed.
	 */
	public GriAsc()
	{
		this.FileVersion          = "1.3";							// Earliest supported file format
		this.PatientId            = "-";
		this.PatientSurname       = "";								// Future release
		this.PatientForenames     = "";								// Future release
		this.AgeDays              = -1;
		this.Race                 = 'U';
		this.Gender               = 'U';
		this.Dates                = "00/00/0000,00/00/0000";
		this.RecordingTime        = "00:00:00";
		this.ElectrodePositioning = "Adult";
		this.Drugs                = "0 0";
		this.Clinical             = "0 0";
		this.MainsFilterFrequency = 0;
		this.BradyTachyLimit      = "50 100";
		this.QTcMeasure           = "QTcHodge";
		this.Pacers               = "0 0";
		this.AnalysisType         = "12Lead";
		this.LeadAvailability     = "110000111111";					// I, II, V1-V6 available
		this.RecordedLeads        = "111111111111";					// Future release
		this.LSBperMV             = 200;							// 5uV/LSB
		this.SamplingRate         = 500;
		this.NumSamplesPerLead    = 5000;
		this.NumLeads             = 12;
		this.LeadConfig           = "";								// Future release
	}
	

	// ----------------------------------------------------------------------

	/**
	 * Gets the version of the GRIANLYS input file to be read. The version
	 * number is present in the input file from versions 1.1 and later, where
	 * the version number will be prefixed by '##'. Version 1.0 files did not
	 * have a version number.
	 *
	 * @param	FileName	The name of the GRIANLYS input file containing the ECG data.
	 * @return	A string containing the version number of the GRIANLYS input file.
	 */
	private String getFileVersion(String FileName )
	{
		String FileVersion;
		File GriASCFile;

	    // ------------------------------------------------------------------
		// Default to earliest file version as the ASCII input file did not
		// contain a version number for the first version of the file.
	    // ------------------------------------------------------------------
		FileVersion = "1.0";


	    // ------------------------------------------------------------------
	    // Read the contents of the file, catching any errors that may occur.
	    // ------------------------------------------------------------------
	    BufferedReader br = null;
		GriASCFile = new File(FileName);
	    try
	    {
	        br = new BufferedReader(new FileReader(GriASCFile));
	        String line;
	
	        line = br.readLine();
	        if ( line != null )
	        {
	        	if ( line.substring(0,2).equals("##"))
	        	{
	        		FileVersion = line.substring(2,line.length());
	        	}
	        }
	    } catch (IOException e)
	    {
	        //You'll need to add proper error handling here
	    	Log.w(TAG, "Error reading file " + FileName );
	    	Log.w(TAG, "exception", e);
	    }


	    // ------------------------------------------------------------------
	    // Close the file
	    // ------------------------------------------------------------------
	    try
	    {
	    	if ( br != null )
	    	{
	            br.close();
	    	}
	    } catch ( IOException ex )
	    {
	    	ex.printStackTrace();
	    }

		return FileVersion;
	}

	// ----------------------------------------------------------------------

	/**
	 * Returns descriptive text for the given LineType.
	 *
	 * @param	LineType	The type of the line that is being processed.
	 * @return	A string containing text that indicates the type of line that is being processed.
	 */
	private String getLineTypeText(GRIASCLineType LineType )
	{
		String LineTypeText = "UNKNOWN";
		
		switch ( LineType )
		{
			case GRIASC_VER:
					LineTypeText = "Version       :";
					break;
			case GRIASC_PATID:
					LineTypeText = "Patient ID    :";
					break;
			case GRIASC_GENDER:
					LineTypeText = "Gender        :";
					break;
			case GRIASC_RACE:
					LineTypeText = "Race          :";
					break;
			case GRIASC_AGEDAYS:
					LineTypeText = "AgeDays       :";
					break;
			case GRIASC_DATES:
					LineTypeText = "Dates         :";
					break;
			case GRIASC_RECTIME:
					LineTypeText = "Rec Time      :";
					break;
			case GRIASC_ELEC:
					LineTypeText = "ElecPos       :";
					break;
			case GRIASC_MEDS:
					LineTypeText = "Meds          :";
					break;
			case GRIASC_CLIN:
					LineTypeText = "Clin          :";
					break;
			case GRIASC_MAINS:
					LineTypeText = "Mains filt    :";
					break;
			case GRIASC_BRADTACH:
					LineTypeText = "Brad/Tach     :";
					break;
			case GRIASC_QTC:
					LineTypeText = "QTc           :";
					break;
			case GRIASC_PACERS:
					LineTypeText = "Pacers        :";
					break;
			case GRIASC_ANATYPE:
					LineTypeText = "Analysis type :";
					break;
			case GRIASC_LEADAVA:
					LineTypeText = "Lead avail    :";
					break;
			case GRIASC_CAL:
					LineTypeText = "Calibration   :";
					break;
			case GRIASC_SRATE:
					LineTypeText = "Sample rate   :";
					break;
			case GRIASC_SLEAD:
					LineTypeText = "Lead samps    :";
					break;
			case GRIASC_LEADCNT:
					LineTypeText = "Lead count    :";
					break;
			case GRIASC_LEADDATA:
					LineTypeText = "Lead data     :";
					break;
			default:
					LineTypeText = "Lead Data     :";
					break;
		}
		
		return LineTypeText;
	}

	// ----------------------------------------------------------------------

	/**
	 * Returns the type of the line at the given point in the input
	 * file based on the version of the file.
	 * NOTE: This function does not currently handle file versions earlier
	 * than version 1.3.
	 * 
	 * @param Version	The version of the input file.
	 * @param LineNumber	The current line number offset into the file.
	 * @return	The type of the line that has been read from the file.
	 */
	private GRIASCLineType getLineType(String Version, int LineNumber )
	{
		GRIASCLineType LineType;


		// @NOTIFY Debug only
		//Log.d(TAG,"Called with version " + Version);
		LineType = GRIASCLineType.GRIASC_END;
		if ( Version.equals("1.4") )
		{
			// @NOTIFY Debug only
			//Log.d(TAG,"Version 1.4 reading");
			switch ( LineNumber )
			{
				case 1:		LineType = GRIASCLineType.GRIASC_VER;
							break;
				case 2:		LineType = GRIASCLineType.GRIASC_PATID;
							break;
				case 3:		LineType = GRIASCLineType.GRIASC_GENDER;
							break;
				case 4:		LineType = GRIASCLineType.GRIASC_RACE;
							break;
				case 5:		LineType = GRIASCLineType.GRIASC_AGEDAYS;
							break;
				case 6:		LineType = GRIASCLineType.GRIASC_DATES;
							break;
				case 7:		LineType = GRIASCLineType.GRIASC_RECTIME;
							break;
				case 8:		LineType = GRIASCLineType.GRIASC_ELEC;
							break;
				case 9:		LineType = GRIASCLineType.GRIASC_MEDS;
							break;
				case 10:	LineType = GRIASCLineType.GRIASC_CLIN;
							break;
				case 11:	LineType = GRIASCLineType.GRIASC_MAINS;
							break;
				case 12:	LineType = GRIASCLineType.GRIASC_BRADTACH;
							break;
				case 13:	LineType = GRIASCLineType.GRIASC_QTC;
							break;
				case 14:	LineType = GRIASCLineType.GRIASC_PACERS;
							break;
				case 15:	LineType = GRIASCLineType.GRIASC_ANATYPE;
							break;
				case 16:	LineType = GRIASCLineType.GRIASC_LEADAVA;
							break;
				case 17:	LineType = GRIASCLineType.GRIASC_CAL;
							break;
				case 18:	LineType = GRIASCLineType.GRIASC_SRATE;
							break;
				case 19:	LineType = GRIASCLineType.GRIASC_SLEAD;
							break;
				case 20:	LineType = GRIASCLineType.GRIASC_LEADCNT;
							break;
				case 21:	LineType = GRIASCLineType.GRIASC_LEADDATA;
							break;
				default:
						
			
			}
		}
		else if ( Version.equals("1.3") )
		{
			// @NOTIFY Debug only
			//Log.d(TAG,"Version 1.3 reading");
			switch ( LineNumber )
			{
				case 1:		LineType = GRIASCLineType.GRIASC_VER;
							break;
				case 2:		LineType = GRIASCLineType.GRIASC_PATID;
							break;
				case 3:		LineType = GRIASCLineType.GRIASC_GENDER;
							break;
				case 4:		LineType = GRIASCLineType.GRIASC_RACE;
							break;
				case 5:		LineType = GRIASCLineType.GRIASC_AGEDAYS;
							break;
				case 6:		LineType = GRIASCLineType.GRIASC_DATES;
							break;
				case 7:		LineType = GRIASCLineType.GRIASC_RECTIME;
							break;
				case 8:		LineType = GRIASCLineType.GRIASC_ELEC;
							break;
				case 9:		LineType = GRIASCLineType.GRIASC_MEDS;
							break;
				case 10:	LineType = GRIASCLineType.GRIASC_CLIN;
							break;
				case 11:	LineType = GRIASCLineType.GRIASC_MAINS;
							break;
				case 12:	LineType = GRIASCLineType.GRIASC_BRADTACH;
							break;
				case 13:	LineType = GRIASCLineType.GRIASC_QTC;
							break;
				case 14:	LineType = GRIASCLineType.GRIASC_PACERS;
							break;
				case 15:	LineType = GRIASCLineType.GRIASC_LEADAVA;
							break;
				case 16:	LineType = GRIASCLineType.GRIASC_CAL;
							break;
				case 17:	LineType = GRIASCLineType.GRIASC_SRATE;
							break;
				case 18:	LineType = GRIASCLineType.GRIASC_SLEAD;
							break;
				case 19:	LineType = GRIASCLineType.GRIASC_LEADDATA;
							break;
				default:
						
			
			}
		}
		
		return LineType;
	}
		
	// ----------------------------------------------------------------------
	
	/**
	 * Loads an ECG from a GRIANLYS format input file. Data is loaded into the
	 * class instance for passing to the {@link RestingEcg#Analyse()} method.
	 * 
	 * Currently, only version 1.3 and 1.4 file formats are supported
	 * 
	 * @param EcgFile	The filename of the GRIANLYS formatted file.
	 */
	public void LoadFromFile( String EcgFile )
	{
		
		File InputFile;
		int		LineCount;
		String GriascVersion;
		GRIASCLineType LineType;
		int		LeadSampleCount, LeadCount;

	    Log.d( TAG, "Loading ECG data from file '" + EcgFile + "'");

	    // ------------------------------------------------------------------
		// Get the text file and version
	    // ------------------------------------------------------------------
	    InputFile = new File(EcgFile);
	    GriascVersion = this.getFileVersion( EcgFile );

	    Log.d( TAG, "    Input file version : " + GriascVersion );
	    
	    // ------------------------------------------------------------------
	    // Read the contents of the file, and catch any errors
	    // that may occur
	    // ------------------------------------------------------------------

	    BufferedReader br = null;
	    LineCount = 0;
	    LeadSampleCount = 0;
	    LeadCount = 0;
	    try
	    {
	        br = new BufferedReader(new FileReader(InputFile));
	        String line;
	
	        while ((line = br.readLine()) != null) {
	        	LineCount++;
	        	LineType = getLineType(GriascVersion,LineCount);
	        	switch ( LineType )
	        	{
	        		case GRIASC_VER:
	        			this.FileVersion = GriascVersion;
	        			break;
	        		case GRIASC_PATID:
	        			this.PatientId = line;
	        			break;
	        		case GRIASC_GENDER:
	        			this.Gender = (byte) line.charAt(0);
	        			break;
	        		case GRIASC_RACE:
        				this.Race = (byte) line.charAt(0);
        				break;
	        		case GRIASC_AGEDAYS:
        				this.AgeDays = Integer.parseInt(line);
        				break;
	        		case GRIASC_DATES:
	        			this.Dates = line;
        				break;
	        		case GRIASC_RECTIME:
	        			int Hrs = Integer.parseInt(line.substring(0,2));
	        			int Min = Integer.parseInt(line.substring(3,5));
	        			int Sec = Integer.parseInt(line.substring(6,8));
	        			this.RecordingTime = line;
        				break;
	        		case GRIASC_ELEC:
	        			this.ElectrodePositioning = line;
        				break;
	        		case GRIASC_MEDS:
	        			this.Drugs = line;
        				break;
	        		case GRIASC_CLIN:
	        			this.Clinical = line;
        				break;
	        		case GRIASC_MAINS:
	        			this.MainsFilterFrequency = (short) Short.parseShort(line);
        				break;
	        		case GRIASC_BRADTACH:
	        			this.BradyTachyLimit = line;
        				break;
	        		case GRIASC_QTC:
	        			this.QTcMeasure = line;
        				break;
	        		case GRIASC_PACERS:
	        			this.Pacers = line;
        				break;
	        		case GRIASC_ANATYPE:
	        			this.AnalysisType = line;
        				break;
	        		case GRIASC_LEADAVA:
	        			this.LeadAvailability = line;
        				break;
	        		case GRIASC_CAL:
	        			this.LSBperMV = (short) Integer.parseInt(line);
        				break;
	        		case GRIASC_SRATE:
	        			this.SamplingRate = (short) Integer.parseInt(line);
        				break;
	        		case GRIASC_SLEAD:
	        			this.NumSamplesPerLead = (short) Integer.parseInt(line);
        				break;
	        		case GRIASC_LEADCNT:
	        			this.NumLeads = (short) Integer.parseInt(line);
        				break;
        			default:	// GRIASC_LEADDATA
        				if ( LeadCount == 0 && LeadSampleCount == 0 )
        				{
        					// First, allocate arrays for the lead data
        				    this.Leads = new IntLead[this.NumLeads];
        				    for ( int i = 0; i < this.NumLeads; i++ )
        				    {
        				    	this.Leads[i] = new IntLead(this.NumSamplesPerLead);
        				    }
        				}

        				this.Leads[LeadCount].SetSample(LeadSampleCount, (short) Integer.parseInt(line));
        				LeadSampleCount++;
        				if ( LeadSampleCount >= this.NumSamplesPerLead)
        				{
        					LeadSampleCount = 0;
        					LeadCount++;
        				}
        				
        				break;
        				
        				
	        	}
	        }
	    }
	    catch (IOException e)
	    {
	    	// @NOTIFY: Need to add proper error handling here
	    	Log.w( TAG, "Error reading file '" + InputFile + "'" );
	    }


	    // ------------------------------------------------------------------
	    // Close the file
	    // ------------------------------------------------------------------
	    try
	    {
	    	if ( br != null )
	    	{
	            br.close();
	    	}
	    }
	    catch ( IOException ex )
	    {
	    	// @NOTIFY: Need to add proper error handling here
	    	Log.w( TAG, "Error closing file '" + InputFile + "'" );
	    	ex.printStackTrace();
	    }
	}
	
// --------------------------------------------------------------------------

	/**
	 * Gets the patient identification string for the given file.
	 * 
	 * @return	The patient identification
	 */
	public String getId()
	{
		return PatientId;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Gets the patient's gender for the given file.
	 * 
	 * @return	The gender of the patient which can be 'M' for male,
	 * 'F' for female or 'U' for unknown or undefined.
	 */
	public byte getGender()
	{
		return (byte) Gender;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Gets the QTc formula that is to be used for analysis purposes.
	 * 
	 * @return	A string representing the QTc formula that is to be used.
	 */
	public String getQTcFormula()
	{
		return QTcMeasure;
	}

	// ----------------------------------------------------------------------

	/**
	 * Gets the mains filter frequency to be applied at the pre-processing
	 * stage of the analysis.
	 * 
	 * @return	The mains frequency filter in Hz that is to be applied.
	 */
	public short getMainsFilterFrequency()
	{
		return MainsFilterFrequency;
	}

	// ----------------------------------------------------------------------
	
	/**
	 * Extracts the date of birth from the dates line of the input file.
	 * If the line does not contain valid dates, "00/00/0000" will be returned.
	 * 
	 * @return	The date in the form DD/MM/CCYY. If no valid date is
	 * available, "00/00/0000" is returned.
	 */
	public String getDateOfBirth()
	{
		int SeparatorPos;
		
		SeparatorPos = Dates.indexOf( "," );
		if ( SeparatorPos != -1 )
		{
			// Get leftmost part, which is date of birth
			return Dates.substring(0,SeparatorPos);
			
		}
		else
		{
			// Date of birth is unavailable
			return "00/00/0000";
		}

	}
	
	// ----------------------------------------------------------------------

	public String getDateOfRecording()
	{
		int SeparatorPos;
		
		SeparatorPos = Dates.indexOf( "," );
		if ( SeparatorPos != -1 )
		{
			// Get rightmost part, which is date of recording
			return Dates.substring(SeparatorPos+1, Dates.length());
			
		}
		else
		{
			// Date of recording is unavailable
			return "00/00/0000";
		}

	}
	
// --------------------------------------------------------------------------

	public String getRecordingTime()
	{
		return RecordingTime;
	}
	
// --------------------------------------------------------------------------

	public String getElectrodePositioning()
	{
		return ElectrodePositioning;
	}
	
// --------------------------------------------------------------------------

	public short getLSBPerMV()
	{
		return LSBperMV;
	}
	
// --------------------------------------------------------------------------

	public String getFirstName()
	{
		return "";
	}
	
// --------------------------------------------------------------------------

	public String getLastName()
	{
		return "";
	}

// --------------------------------------------------------------------------

	public String getFileVersion()
	{
		return FileVersion;
	}
	
// --------------------------------------------------------------------------

	public short getSamplingFrequency()
	{
		return SamplingRate;
	}
	
// --------------------------------------------------------------------------

	public short getSamplesPerLead()
	{
		return NumSamplesPerLead;
	}
	
// --------------------------------------------------------------------------

	public String getPacers()
	{
		return Pacers;
	}
	
// --------------------------------------------------------------------------

	public String getDrugs()
	{
		return Drugs;
	}

// --------------------------------------------------------------------------

	public short getDrug( int Index )
	{
		short 	i, Drug;
		boolean	Finished;


		String patternString1 = "(\\d+) (\\d+)";
		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher = pattern.matcher(Drugs);

		Finished = false;
		Drug = 0;		// Default 'undefined'
		if ( Index >= 0 && Index <= 2)
		{
			while(matcher.find() && !Finished )
			{
				try
				{
					Drug = Short.parseShort(matcher.group(Index+1));
					Finished = true;
				}
				catch (NumberFormatException e)
				{
					System.err.println("The string containing the drugs/medication is not properly formatted!");
				}  
			}
		}
		
		return Drug;
		
	}

// --------------------------------------------------------------------------

	public short getClinicalClassification( int Index )
	{
		short 	ClinClass;
		boolean	Finished;


		String patternString1 = "(\\d+) (\\d+)";
		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher = pattern.matcher(Clinical);

		Finished = false;
		ClinClass = 0;		// Default 'undefined'
		if ( Index >= 0 && Index <= 2)
		{
			while(matcher.find() && !Finished )
			{
				try
				{
					ClinClass = Short.parseShort(matcher.group(Index+1));
					Finished = true;
				}
				catch (NumberFormatException e)
				{
					System.err.println("The string containing the clinical classifications is not properly formatted!");
				}  
			}
		}
		
		return ClinClass;
		
	}

// --------------------------------------------------------------------------

	public String getClinicalClassifications()
	{
		return this.Clinical;
	}

// --------------------------------------------------------------------------

	public String getBradyTachyLimit()
	{
		return BradyTachyLimit;
	}

// --------------------------------------------------------------------------

	public String getAnalysisType()
	{
		return AnalysisType;
	}

// --------------------------------------------------------------------------

	public byte getRace()
	{
		return (byte) Race;
	}

// --------------------------------------------------------------------------

	/**
	 * 
	 * @return	The patient age in days. This will be -1 if the age is unknown.
	 */
	public int getAgeDays()
	{
		return AgeDays;
	}

// --------------------------------------------------------------------------
	
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
	 * @return	Returns the 16-bit lead availabilty flag.
	 */
	public short getLeadAvailability()
	{
		short	LeadPos;
		short	LeadAvail;
		int	FlagBitCount;
		int	_mask;
		
	
		FlagBitCount = LeadAvailability.length();
		LeadAvail = 0;
		for (LeadPos = 0, _mask = 1; LeadPos < RestingEcg.GRI_MAX_ECG_LEADS; LeadPos++, _mask = ( _mask << 1 ) )
		{
			if ( LeadPos < FlagBitCount && LeadAvailability.charAt(LeadPos) == '1' )
			{
				LeadAvail |= _mask;
			}
		}
		
		return LeadAvail;
		
	}

	// --------------------------------------------------------------------------

	/**
	 * Extracts the bradycardia rate limit from the string encoding bradycardia
	 * and tachycardia limits.
	 * 
	 * @return	The bradycardia limit
	 */
	public int getBradycardiaLimit()
	{
		boolean	Finished;
		short	BradycardiaLimit;


		// Set pattern matching fields
		String patternString1 = "(\\d+) (\\d+)";
		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher = pattern.matcher(BradyTachyLimit);

		// Extract and return the rate limit if found, otherwise return
		// a default value
		BradycardiaLimit = 50;				// Default to 50
		Finished = false;
		while(matcher.find() && !Finished )
		{
			try
			{
				BradycardiaLimit = Short.parseShort(matcher.group(1));
				Finished = true;
			}
			catch (NumberFormatException e)
			{
				System.err.println("The string containing the bradycardia/tachycardia limits is not properly formatted!");
			}  
		}
		
		return (int) BradycardiaLimit;
	}

	// --------------------------------------------------------------------------

	/**
	 * Extracts the tachycardia rate limit from the string encoding bradycardia
	 * and tachycardia limits.
	 * 
	 * @return	The tachycardia limit
	 */
	public int getTachycardiaLimit()
	{
		boolean	Finished;
		short	TachycardiaLimit;


		// Set pattern matching fields
		String patternString1 = "(\\d+) (\\d+)";
		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher = pattern.matcher(BradyTachyLimit);

		// Extract and return the rate limit if found, otherwise return
		// a default value
		TachycardiaLimit = 100;				// Default to 100
		Finished = false;
		while(matcher.find() && !Finished )
		{
			try
			{
				TachycardiaLimit = Short.parseShort(matcher.group(2));
				Finished = true;
			}
			catch (NumberFormatException e)
			{
				System.err.println("The string containing the bradycardia/tachycardia limits is not properly formatted!");
			}  
		}
		
		return (int) TachycardiaLimit;
	}

	// --------------------------------------------------------------------------

	/**
	 * Retrieves the number of leads stored in the instance.
	 * 
	 * @return	The total number of leads that are stored.
	 */
	public short getNumberOfLeads()
	{
		return NumLeads;
	}
	
	// --------------------------------------------------------------------------

	/**
	 * Returns an array of lead identifiers that indicate the specific leads
	 * occupying the input buffers.
	 * 
	 * @return	Array of lead identifiers indicating the leads present in the input buffers.
	 */
	public GriLeadId[] getLeadConfig()
	{
		
		if ( this.isPaediatricElectrodePlacement() )
		{
			// Define the paediatric lead selection and order
			GriLeadId[] myLeadConfig = {
					GriLeadId.GRI_LEADID_I, GriLeadId.GRI_LEADID_II,
					GriLeadId.GRI_LEADID_III, GriLeadId.GRI_LEADID_aVR,
					GriLeadId.GRI_LEADID_aVL, GriLeadId.GRI_LEADID_aVF,
					GriLeadId.GRI_LEADID_V4R, GriLeadId.GRI_LEADID_V1,
					GriLeadId.GRI_LEADID_V2, GriLeadId.GRI_LEADID_V4,
					GriLeadId.GRI_LEADID_V5, GriLeadId.GRI_LEADID_V6,
					GriLeadId.GRI_LEADID_UNDEFINED, GriLeadId.GRI_LEADID_UNDEFINED,
					GriLeadId.GRI_LEADID_UNDEFINED
			};
			
			return myLeadConfig;

		}
		else
		{
			GriLeadId[] myLeadConfig = {
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

			return myLeadConfig;

		}
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
		if ( this.ElectrodePositioning.equals("Paediatric") )
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
	 * Examines the status of the electrode positioning and returns TRUE
	 * if the electrode positioning used is for standard adult recordings.
	 * 
	 * @return	TRUE is the electrode positioning used is for standard adult
	 * recordings, otherwise returns FALSE.
	 */
	public boolean isStandardElectrodePlacement()
	{
		if ( this.ElectrodePositioning.equals("Adult") )
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
	 * Retrieves the lead data for the given lead index. The lead index is the
	 * index of the array and should not be used as an indication of a
	 * specific lead.
	 * 
	 * @param LeadIndex	The index into the ECG lead array.
	 * @return	An array containing all samples for the lead at the given
	 * index.
	 */
	public short [] getLeadData( int LeadIndex )
	{
		return Leads[LeadIndex].Data;	
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Prints out the contents of the GriAsc data structure to the Log.d() stream.
	 * Used for debugging purposes.
	 */
	public void printContent()
	{
		Log.d( TAG, "GriAsc" );
		Log.d( TAG, "======" );
		Log.d( TAG, "File Version                  : " + FileVersion );

		Log.d( TAG, "Patient ID                    : " + PatientId );
		Log.d( TAG, "Lead Availability             : " + LeadAvailability );

		Log.d( TAG, "Gender                        : " + (char) Gender );
		Log.d( TAG, "Race                          : " + (char) Race );

		Log.d( TAG, "Age (days)                    : " + AgeDays );

		Log.d( TAG, "Dates                         : " + Dates );
		Log.d( TAG, "Recording Time                : " + RecordingTime );

		//Log.d( TAG, "Recorded Leads                : " + RecordedLeads );						// Future release
		Log.d( TAG, "Electrode Positioning         : " + ElectrodePositioning );

		Log.d( TAG, "Drugs                         : " + Drugs );
		Log.d( TAG, "Clinical Classification       : " + Clinical );

		Log.d( TAG, "Mains filter to apply         : " + MainsFilterFrequency );

		Log.d( TAG, "Pacers                        : " + Pacers );

		Log.d( TAG, "Analysis Type                 : " + AnalysisType );

		Log.d( TAG, "Brady/Tachy limits            : " + BradyTachyLimit );
		Log.d( TAG, "QTc Formula                   : " + QTcMeasure );
		Log.d( TAG, "Analysis type                 : " + AnalysisType );

		Log.d( TAG, "LSB Per MV                    : " + LSBperMV );
		Log.d( TAG, "Sampling Rate                 : " + SamplingRate );
		Log.d( TAG, "Samples per lead              : " + NumSamplesPerLead );
		Log.d( TAG, "Number of leads               : " + NumLeads );
//		IntLead	[] Leads;

		Log.d( TAG, " " );
		
	}
	
}
