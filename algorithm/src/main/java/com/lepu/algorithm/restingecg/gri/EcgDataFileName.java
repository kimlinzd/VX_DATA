package com.lepu.algorithm.restingecg.gri;

import android.util.Log;

import java.io.File;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Handles the construction of filenames for writing results (used for debugging
 * and test) based upon the original ECG filename.
 *
 * Note that a production ready build should not being running on the basis of a
 * GRIANLY formatted input file.
 *
 * @author Brian Devine
 *
 */
public class EcgDataFileName {
	// --------------------------------------------------------------------------
	private static final String TAG = "MyActivity-EcgDataFileName";
	// --------------------------------------------------------------------------

	/**
	 * Pathname component of the input file.
	 */
	private String PathName;

	/**
	 *  File part of the input file.
	 */
	private String FileBaseName;

	/**
	 * The file extension of the input file.
	 */
	private String FileExtension;

	/**
	 * The directory where the results are to be written (for debug purposes).
	 */
	private String ResultsDir;


	/**
	 * The name of the input file being processed.
	 */
	private String InputFile;

	private String NamePart;

	String InputFilePath;

	/**
	 * The character(s) using when separating directory paths.
	 */
	String PathSeparator;

	/**
	 * The name of the file for saving scalar measurement results.
	 */
	String MatrixFileName;

	/**
	 * The name of the file for saving vector measurement results.
	 */
	String VectorMatrixFileName;

	/**
	 * The name of the file for saving the complete ECG report.
	 */
	String EcgReportFileName;

	/**
	 * The name of the file for saving the spatial velocity data.
	 */
	String SVDataFileName;

	/**
	 * The name of the file for saving the representative beats.
	 */
	String RepBeatFileName;

	/**
	 * The name of the file for saving the ECG interpretation with
	 * statements.
	 */
	String InterpFileName;

	/**
	 * The name of the file for saving the statement tuple values.
	 */
	String TuplesFileName;


	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------

	/**
	 * Creates an instance of the {@link EcgDataFileName} with no input file
	 * specified.
	 */
	public EcgDataFileName()
	{
		this( "" );
	}

	// ----------------------------------------------------------------------

	/**
	 * Creates an instance of the {@link EcgDataFileName} with the given
	 * input file name.
	 *
	 * @param FileName	The name of the input file (including path) that is
	 * to be processed.
	 */
	public EcgDataFileName( String FileName )
	{
		this.InputFile     = FileName;
		this.PathSeparator = "/";

		this.PathName      = "";
		this.NamePart      = "";
		this.FileBaseName  = "";
		this.FileExtension = "";

		this.ResultsDir    = "";

		this.MatrixFileName       = "";
		this.VectorMatrixFileName = "";
		this.EcgReportFileName    = "";
		this.SVDataFileName       = "";
		this.RepBeatFileName      = "";
		this.InterpFileName       = "";
		this.TuplesFileName       = "";

	}

	// ----------------------------------------------------------------------

	/**
	 * Forms the series of output filenames to which analysis results should
	 * be written for the purpose of debugging. If the instance of
	 * EcgDataFileName was not created with a filename then default values
	 * will be provided. Note that the method
	 * {@link EcgDataFileName#setResultsPath(String)} should be called
	 * prior to calling this method as the output filenames will be
	 * constructed using the value of the results path.
	 */
	public void FormOutputFiles()
	{
		File myFile;


		if ( !this.InputFile.equals("") )
		{
			myFile = new File(this.InputFile);

			this.PathName      = myFile.getParent();  
			this.ResultsDir = this.PathName + "/Results";

			File resultsDirectory = new File(this.ResultsDir);
			resultsDirectory.mkdirs();

			this.NamePart      = myFile.getName();
			this.FileBaseName  = getFilePart( this.NamePart );
			this.FileExtension = getExtensionPart( this.NamePart );

			this.setMatrixFileName( this.ResultsDir + this.PathSeparator + this.FileBaseName + "_Matrix.txt" );
			this.setTuplesFileName( this.ResultsDir + this.PathSeparator + this.FileBaseName + "_Tuples.txt" );
			this.setVectorMatrixFileName( this.ResultsDir + this.PathSeparator + this.FileBaseName + "_VectMatrix.txt" );
			this.setRepresentativeBeatsFileName( this.ResultsDir + this.PathSeparator + this.FileBaseName + "_Medians.txt" );
			this.setInterpFileName( this.ResultsDir + this.PathSeparator + this.FileBaseName + "_Interp.txt" );
		    this.setEcgReportFileName( this.ResultsDir + this.PathSeparator + this.FileBaseName + "_EcgReport.txt" );
		    this.setSVDataFileName( this.ResultsDir + this.PathSeparator + this.FileBaseName + "_SV.txt" );
		}
		else
		{
		    this.setMatrixFileName( "./_Matrix.txt" );
		    this.setVectorMatrixFileName( "./_VectMatrix.txt" );
		    this.setEcgReportFileName( "./_EcgReport.txt" );
		    this.setTuplesFileName( "./_Tuples.txt" );
		    this.setInterpFileName( "./_Interp.txt" );
		}
	}

    // ----------------------------------------------------------------------

    /**
     * Examines the filename extension and determines if it matches that of
     * a GRIASC file.
     *
     * @return TRUE is the filename extension matches that of a GRIASC file.
     */
    public boolean isValidGRIASCFile()
    {
        Log.d(TAG,"FILE EXTENSION: "+this.FileExtension);
        if ( this.FileExtension.equals("asc") ) {
            return TRUE;
        }
        else {
            return FALSE;
        }
    }

    // ----------------------------------------------------------------------

    /**
     * Examines the filename extension and determines if it matches that of
     * a batch file intended to hold a list of ECGs to be processed.
     *
     * @return TRUE is the filename extension matches that of a batch file.
     */
    public boolean isValidBatchFile()
    {
        if ( this.FileExtension.equals("lst") ) {
            return TRUE;
        }
        else {
            return FALSE;
        }
    }

    // ----------------------------------------------------------------------

	/**
	 * Gets the file part of the filename, excluding the filename extension.
	 *
	 * @param FileName	The filename to be processed.
	 * @return	The name of the file excluding the path and filename extension.
	 */
	private String getFilePart(String FileName )
	{
	    if ( FileName == null )
	    {
            return null;
        }

        int extensionPos = FileName.lastIndexOf('.');
        int lastUnixPos = FileName.lastIndexOf('/');
        int lastWindowsPos = FileName.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);
        int index = lastSeparator > extensionPos ? -1 : extensionPos;

        if (index == -1)
        {
            return "";
        }
        else
        {
            return FileName.substring(0,index);
        }
	}

	// ----------------------------------------------------------------------

	private String getExtensionPart(String FileName )
	{
	    if ( FileName == null )
	    {
            return null;
        }

        int extensionPos = FileName.lastIndexOf('.');
        int lastUnixPos = FileName.lastIndexOf('/');
        int lastWindowsPos = FileName.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);

        int index = lastSeparator > extensionPos ? -1 : extensionPos;
        if (index == -1)
        {
            return "";
        }
        else
        {
            return FileName.substring(index + 1);
        }
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the input file to the given filename.
	 *
	 * @param FileName	The name of the input file (including path).
	 */
	public void setEcgDataFileName( String FileName )
	{
		this.InputFile = FileName;
	}

	// ----------------------------------------------------------------------

	/**
	 * Retrieves the name of the statement tuples file that should be used
	 * when writing out statement tuples. This is of primary use during
	 * debugging.
	 *
	 * @return	The name of the statemen tuple file, including any path.
	 */
	public String getTuplesFileName()
	{
		return this.TuplesFileName;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the name of the representative beats file to the given filename.
	 *
	 * @param FileName	The name of the file to which the representative beats
	 * will be written.
	 */
	private void setRepresentativeBeatsFileName( String FileName )
	{
		this.RepBeatFileName = FileName;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the name of the tuples file to the given filename
	 *
	 * @param FileName	The name of the file to which the tuples will be written.
	 */
	public void setTuplesFileName( String FileName )
	{
		this.TuplesFileName = FileName;
	}

	// ----------------------------------------------------------------------

	/**
	 * Sets the name of the scalar measurement matrix file to the given filename
	 *
	 * @param FileName	The name of the file to which the scalar measurement
	 * matrix should be written.
	 */
	public void setMatrixFileName( String FileName )
	{
		this.MatrixFileName = FileName;
	}

	// ----------------------------------------------------------------------

	public void setVectorMatrixFileName( String FileName )
	{
		this.VectorMatrixFileName = FileName;
	}

	// ----------------------------------------------------------------------

	public void setResultsPath( String Path )
	{
		this.ResultsDir = Path;
	}

	// ----------------------------------------------------------------------

	public void setInterpFileName( String FileName )
	{
		this.InterpFileName = FileName;
	}

	// ----------------------------------------------------------------------

	public void setEcgReportFileName( String FileName )
	{
		this.EcgReportFileName = FileName;
	}

	// ----------------------------------------------------------------------

	public void setSVDataFileName( String FileName )
	{
		this.SVDataFileName = FileName;
	}

	// ----------------------------------------------------------------------

	public void setEcgFileName( String FileName )
	{
		this.InputFile     = FileName;
	}

	// ----------------------------------------------------------------------

	/**
	 * Retrieves the name of the scalar measurement matrix file, including any path,
	 * to which the scalar measurements should be written.
	 *
	 * @return	The name of the scalar measurement file.
	 */
	public String getMatrixFileName()
	{
		if ( this.MatrixFileName.equals("") )
		{
			this.FormOutputFiles();
		}
		return this.MatrixFileName;
	}

	// ----------------------------------------------------------------------

	/**
	 * Retrieves the name of the vector measurement matrix file, including any path,
	 * to which the vector measurements should be written.
	 *
	 * @return	The name of the vector measurement file.
	 */
	public String getVectorMatrixFileName()
	{
		if ( this.VectorMatrixFileName.equals("") )
		{
			this.FormOutputFiles();
		}
		return this.VectorMatrixFileName;
	}

	// ----------------------------------------------------------------------

	/**
	 * Retrieves the name of the ECG interpretation file, including any path,
	 * to which the interpretation strings should be written.
	 *
	 * @return	The name of the ECG interpretation file.
	 */
	public String getInterpFileName()
	{
		if ( this.InterpFileName == null || this.InterpFileName.equals("") )
		{
			this.FormOutputFiles();
		}
		return this.InterpFileName;
	}

	// ----------------------------------------------------------------------

	/**
	 * Retrieves the name of the ECG report file, including any path, to which
	 * measurements and interpretation should be written.
	 *
	 * @return	The name of the ECG report file.
	 */
	public String getEcgReportFileName()
	{
		if ( this.EcgReportFileName == null || this.EcgReportFileName.equals("") )
		{
			this.FormOutputFiles();
		}
		return this.EcgReportFileName;
	}


    // ----------------------------------------------------------------------

    /**
	 * Retrieves the name of the ECG file, including any path, that is to
	 * be processed.
	 * 
	 * @return	The name of the input ECG file.
	 */
	public String getEcgFileName()
	{
		return this.InputFile;
	}

	// ----------------------------------------------------------------------

	/**
     * Returns the directory where analysis results files will be written to.
     *
     * @return	The directory path in which results files from the analysis
     * will be written.
     */
    public String getResultsPath() { return this.ResultsDir; }

	// ----------------------------------------------------------------------

	/**
	 * Provides a filename (with path) to which the representative beats
	 * should be written.
	 *
	 * @return	The filename, including path, of the file to which the medians
	 * should be written.
	 */
	public String getRepresentativeBeatsFileName()
	{

		// If it hasn't already been determined, form the medians filename
		if ( this.RepBeatFileName == null || this.RepBeatFileName.equals("") )
		{
			this.FormOutputFiles();
		}

		return this.RepBeatFileName;
	}

	// ----------------------------------------------------------------------


	/**
	 * Prints the contents of the {@link EcgDataFileName} instance to the debug
	 * output log.
	 */
	public void printContent()
	{
		Log.d( TAG, "EcgDataFileName" );
		Log.d( TAG, "===============" );

		Log.d( TAG, "PathName                      : " + PathName );
		Log.d( TAG, "FileBaseName                  : " + FileBaseName );
		Log.d( TAG, "FileExtension                 : " + FileExtension );
		Log.d( TAG, "ResultsDir                    : " + ResultsDir );

		Log.d( TAG, "Namepart                      : " + this.NamePart );
		Log.d( TAG, "InputFile                     : " + InputFile );

		Log.d( TAG, "InputFilePath                 : " + InputFilePath );
		Log.d( TAG, "PathSeparator                 : " + PathSeparator );

		Log.d( TAG, "MatrixFileName                : " + this.MatrixFileName );
		Log.d( TAG, "EcgReportFileName             : " + this.EcgReportFileName );
		Log.d( TAG, "SVDataFileName                : " + this.SVDataFileName );
		Log.d( TAG, "RepBeatFileName               : " + this.RepBeatFileName );
		Log.d( TAG, "InterpFileName                : " + this.InterpFileName );
		Log.d( TAG, "TuplesFileName                : " + this.TuplesFileName );

		Log.d( TAG, " " );
	}


}
