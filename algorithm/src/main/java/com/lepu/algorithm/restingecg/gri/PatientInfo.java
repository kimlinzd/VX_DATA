package com.lepu.algorithm.restingecg.gri;

import android.util.Log;

/**
 * Records information about the patient that does not vary for each ECG
 * recording. Provides methods for setting and retrieving patient 
 * information.
 *  
 * @author Brian Devine
 *
 */
public class PatientInfo {
	// ----------------------------------------------------------------------
	private static final String TAG = "MyActivity-PatientInfo";
	// ----------------------------------------------------------------------
	
	/**
	 * A patient identification number.
	 */
	String Id;
	
	/**
	 * The patient's lastname or surname.
	 */
	String LastName;
	
	/**
	 * The patient's firstname plus middle names, if any.
	 */
	String FirstName;
	
	/**
	 * The patient's date of birth, expressed as 'DDMMCCYY'. If unknown, it
	 * should be set to '00/00/0000'.
	 */
	String BirthDate;
	
	/**
	 * The patient's race. The race can be any one of 'C', 'M', 'N', 'O'
	 * or 'U'.
	 */
	byte	Race;
	
	/**
	 * The patient's gender. The gender can be any one of 'M', 'F' or 'U'.
	 */
	byte	Gender;

	
	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------

	/**
	 * Initialises the patient information record with default values.
	 * The default values are no id or names, date of birth of '00/00/0000'
	 * and unknown race and gender, which are both coded as 'U'.
	 */
	public PatientInfo()
	{
		this( "", "", "", "00/00/0000", (byte) 'U', (byte) 'U' );		
	}

	// ----------------------------------------------------------------------
	
	/**
	 * Initialises the patient information record with the designated values.
	 * 
	 * @param Id	Patient identification number
	 * @param LastName	Patient's last name
	 * @param FirstName	Patient's first name(s)
	 * @param BirthDate	The patient's date of birth in the format "DD/MM/CCYY".
	 * @param Race	Patient's race
	 * @param Gender	Patient's gender
	 */
	public PatientInfo(String Id, String LastName, String FirstName, String BirthDate, byte Race, byte Gender )
	{
		this.Id         = Id;
		this.LastName   = LastName;
		this.FirstName  = FirstName;
		this.BirthDate  = BirthDate;
		this.Race       = Race;
		this.Gender     = Gender;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the patient ID.
	 * 
	 * @param Id	The patient identification number, which is generally a
	 * hospital number.
	 */
	public void setId( String Id )
	{
		this.Id = Id;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the patient's lastname/surname.
	 * 
	 * @param LastName	The lastname or surname of the patient.
	 */
	public void setLastName( String LastName )
	{
		this.LastName = LastName;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the patients firstname/middlenames.
	 * 
	 * @param FirstName	The firstname and middlenames of the patient.
	 */
	public void setFirstName( String FirstName )
	{
		this.FirstName = FirstName;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the birth date of the patient. The birthdate should be in the
	 * format 'DD/MM/CCYY'.
	 * 
	 * @param BirthDate	The date of birth in the format 'DD/MM/CCYY'.
	 */
	public void setBirthDate( String BirthDate )
	{
		this.BirthDate = BirthDate;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the race of the patient to the given value.
	 * 
	 * @param Race	The race, which can be one of 'C', 'M', 'N', 'O' or 'U'.
	 */
	public void setRace( byte Race )
	{
		this.Race = Race;
	}

	// ----------------------------------------------------------------------
	
	/**
	 * Sets the gender of the patient. 
	 * 
	 * @param Gender	The gender, which can be one of 'M', 'F' or 'U'.
	 */
	public void setGender( byte Gender )
	{
		this.Gender = Gender;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Prints the contents of the {@link PatientInfo} instance to the debug
	 * output log.
	 */
	public void printContent()
	{
		Log.d( TAG, "PatientInfo" );
		Log.d( TAG, "===========" );
		Log.d( TAG, "Patient ID     : " + Id );
		Log.d( TAG, "Lastname       : " + LastName );
		Log.d( TAG, "Firstname(s)   : " + FirstName );
		Log.d( TAG, "Birth date     : " + BirthDate );
		Log.d( TAG, "Gender         : " + (char)Gender );
		Log.d( TAG, "Race           : " + (char)Race );
		Log.d( TAG, " " );
	}
}
