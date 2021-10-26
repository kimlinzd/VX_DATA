package com.lepu.algorithm.restingecg.gri;

import android.content.Context;
import android.util.Log;

/**
 * Maintains the statement tuple and determines the statement text and
 * acronym associated with it. The statement tuple contains 3 elements that
 * identify interpretation
 * statements contained within the statement dictionary.
 * The analysis of the ECG will generate one or more statement tuples that
 * should be mapped to the appropriate statement within the Strings resource.
 *  
 * @author Brian Devine
 *
 */
public class StatementTuple {
	// ----------------------------------------------------------------------	
	private static final String TAG = "MyActivity-StatementTuple";
	// ----------------------------------------------------------------------	
	
	/**
	 *  Boolean to signify if statement tuple has been updated with dictionary text.
	 */
	private	boolean isUpdated;

	/**
	 * The group number of the statement tuple. Valid groups range from 0 to 99.
	 */
	short	Group;
	
	/**
	 * The type of the statement tuple [0-2].
	 */
	short	Type;
	
	/**
	 * The code of the statement tuple.
	 */
	int		Code;
	
	/**
	 * The statement text that is mapped to the tuple. This text will be
	 * retrieved from the Strings resource.
	 */
	String StatementText;
	
	/**
	 * The numerical summary code for the given statement tuple.
	 */
	short	SummaryCode;
	
	/**
	 * Short acronym to identify the statement
	 */
	String Acronym;
	
	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------

	/**
	 * Creates a statement tuple with group, type and code set to 0.
	 */
	StatementTuple()
	{
		this( (short) 0, (short) 0, (short) 0 );
	}

	// ----------------------------------------------------------------------

	/**
	 * Creates and initialises a statement tuple using the supplied Group,
	 * Type and Code.
	 * 
	 * @param Group	The group number of the given statement.
	 * @param Type	The type of the statement (reason, diagnostic
	 * or additional).
	 * @param Code	The statement code
	 */
	StatementTuple( short Group, short Type, int Code )
	{
		this.Group    = Group;
		this.Type     = Type;
		this.Code     = Code;
		
		this.SummaryCode   = 0;
		this.StatementText = "";
		this.Acronym       = "";
	}

	// ----------------------------------------------------------------------

	public short getGroup()
	{
		return this.Group;
	}

	// ----------------------------------------------------------------------

	public short getType()
	{
		return this.Type;
	}
	
	// ----------------------------------------------------------------------

	public int getCode()
	{
		return this.Code;
	}

	// ----------------------------------------------------------------------

	public String getStatementText()
	{
		return this.StatementText;
	}

	// ----------------------------------------------------------------------

	public String getAcronym()
	{
		return this.Acronym;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Examines the given statement tuple to determine whether or not it is
	 * a 'null tuple. A null tuple is generally used as a delimiter for 
	 * related sets of interpretation statements and is identified by
	 * having the Group, TextType and Code values of 0.
	 * 
	 * @return	Returns TRUE if the given tuple is a 'null' tuple, otherwise
	 * returns FALSE.
	 */
	public boolean isNullTuple()
	{
		if ( Group == 0 && Type == 0 && Code == 0 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

// --------------------------------------------------------------------------

	/**
	 * Removes whitespace character surrounding the give tuples statement
	 * text to determine if the statement contains any printable text.
	 * 
	 * @return	Returns TRUE if the statement text for the tuple is blank
	 * or contains only whitespace.
	 */
	public boolean isBlankText()
	{
		if ( StatementText.trim().length() == 0 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

// --------------------------------------------------------------------------

	/**
	 * Examines the statement tuple to determine if the statement is an
	 * advisory statement. Advisory statements should be the first statements
	 * printed on the ECG report.
	 * 
	 * @return	Returns TRUE if the statement is an advisory statement,
	 * otherwise returns FALSE.
	 */
	public boolean isAdvisoryStatement()
	{
		if ( Group == 1 && Type == 1 && Code == 25 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
// --------------------------------------------------------------------------
	
	/**
	 * Examines the statement tuple to determine if the statement is
	 * a rhythm statement.
	 * 
	 * @return	Returns TRUE if the statement is a rhythm statement,
	 * otherwise returns FALSE.
	 */
	public boolean isRhythmStatement()
	{
		if ( Group == 17 || Group == 18 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

// --------------------------------------------------------------------------

	/**
	 * Examines the statement tuple to determine if the statement is a
	 * headline statement. Headline statements should appear at the top
	 * of the ECG report, below any advisory statements.
	 * 
	 * @return	Returns TRUE if the statement is a headline statement,
	 * otherwise returns FALSE.
	 */
	public boolean isHeadlineStatement()
	{
		if ( Group == 16 || ( Group == 9 && Type == 1 && Code == 12368 ) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

//--------------------------------------------------------------------------

	/**
	 * Examines the statement tuple to determine if the statement is a
	 * contour reason or diagnostic statement
	 * 
	 * @return	Returns TRUE if the statement is an advisory statement,
	 * otherwise returns FALSE.
	 */
	public boolean isContourStatement()
	{
		if ( Group == 17 || Group == 18 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

// --------------------------------------------------------------------------
	/**
	 * Examines the given statement text and tests the first character to
	 * see if it is the concatenation character '~'.
	 * @return	Returns TRUE if the statement is to be concatenated with the
	 * previously printed statement, otherwise returns FALSE.
	 */
	public boolean isConcatenatedStatement()
	{
		if ( StatementText != "" && StatementText.charAt(0) == '~' )
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
	 * Examines the statement tuple to determine if the statement is a
	 * summary statement. There should only be a single summary statement
	 * in the ECG interpretation and it should appear at the very
	 * bottom of the ECG report.
	 * 
	 * @return	Returns TRUE if the statement is a summary statement,
	 * otherwise returns FALSE.
	 */
	public boolean isSummaryStatement()
	{
		if ( Group == 14 )
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
	 * Examines the status of the given tuple to determine if the statement
	 * text associated with the tuple has been retrieved from the statement
	 * dictionary contained within the Strings resource file.
	 * 
	 * @return	Returns TRUE if the tuple has been updated with the associated
	 * statement text, otherwise returns FALSE.
	 */
	public boolean isUpdated()
	{
		if ( this.isUpdated )
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
	 * Builds the statement reference ID to be used in the lookup of the
	 * statement text in the Strings resource. The statement reference ID
	 * is composed of the Group, Type and Code, and is of the form
	 * 
	 * 	STAT_grp_type_code.
	 * 
	 * @return	The statement reference ID of the form STAT_grp_type_code
	 */
	public String GetStatementID()
	{
		return String.format("STAT_%d_%d_%d", this.Group, this.Type, this.Code );
	}

	// ----------------------------------------------------------------------

	private static int getStringArrayIdentifier(Context context, String name) {
	    return context.getResources().getIdentifier(name, "array", context.getPackageName());
	}

	// ----------------------------------------------------------------------

	/**
	 * Initialises the statemet tuple with the given statement group,
	 * text type and code.
	 *  
	 * @param Group	That statement group to which the statement belongs.
	 * @param Type	The type of statement.
	 * @param Code	The statement code.
	 */
	public void setTuple( short Group, short Type, int Code )
	{
		this.Group    = Group;
		this.Type     = Type;
		this.Code     = Code;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * For the given statement tuple, retrieves associated statement text
	 * from the Strings resource if it has not previously been retrieved.
	 *  
	 * @param context	The context of the current activity, which is used
	 * to access the strings resource file.
	 */
	public void updateDictionaryInfo( Context context )
	{
		String strWanted;
		String[] strTestArray;

		if ( !this.isUpdated() )
		{
			if ( isNullTuple() )
			{
				// Special case for NULL tuple at is does not exist in the
				// statement dictionary
				
				this.StatementText = "";			// Statement text
				this.SummaryCode   = 0;				// Summary code
				this.Acronym       = "NUL";			// Statement acronym
				
			}
			else
			{
				// Form name for lookup within the string resource
				strWanted = GetStatementID();
				// @DEBUG: Log.d( TAG, "Getting String array for [" + strWanted + "]");
				strTestArray = context.getResources().getStringArray(getStringArrayIdentifier(context, strWanted) );
				
				this.StatementText = strTestArray[0];
				this.SummaryCode   = Short.parseShort(strTestArray[1]);
				this.Acronym       = strTestArray[2];
	
			}

			this.isUpdated     = true;

		}
	}
	
	public String getStatementPrefix()
	{
		String StatementPrefix;


		if ( this.isRhythmStatement() )
		{
			StatementPrefix = "[R]";
		}
		else if ( this.isHeadlineStatement() || this.isAdvisoryStatement() )
		{
			StatementPrefix = "[H]";
		}
		else if ( this.isSummaryStatement() )
		{
			StatementPrefix = "[S]";
		}
		else
		{
			StatementPrefix = "[D]";
		}

		return StatementPrefix;
	}
	// ----------------------------------------------------------------------

	/**
	 * Retrieves the statement text, type and acronym for the given tuple
	 * and returns a string array containing the resulting values.
	 * 
	 * @return	Returns a string array containing the statement text in the
	 * currently selected language, the statement type and an acronym
	 * describing the statement.
	 */
	public String[] getInfo()
	{
		String[] strTestArray;
		
		// @DEBUG: Log.d( TAG, "GetInfo(): Called" );

		strTestArray    = new String[3];
		
		strTestArray[0] = this.StatementText;
		strTestArray[1] = String.valueOf(this.SummaryCode);
		strTestArray[2] = this.Acronym;	// Statement acronym

		return strTestArray;
	}

	// ----------------------------------------------------------------------

	/**
	 * Prints the given statement tuple to the information Log.
	 */
	public void printTuple()
	{
		Log.i( TAG,"Tuple:  Group = " + Group +  "  Type = " + Type + "  Code = " + Code );
	}

	// ----------------------------------------------------------------------

	/**
	 * Prints the contents of the {@link StatementTuple} instance to the
	 * debug output log.
	 */
	public void printContent()
	{
		Log.d( TAG, "StatementTuple" );
		Log.d( TAG, "==============" );
		Log.d( TAG, "Group            : " + this.Group );
		Log.d( TAG, "Type             : " + this.Type );
		Log.d( TAG, "Code             : " + this.Code );
		Log.d( TAG, "Text             : " + this.StatementText );
		Log.d( TAG, "Summary Code     : " + this.SummaryCode );
		Log.d( TAG, "Acronym          : " + this.Acronym );
		Log.d( TAG, " ");
	}
}