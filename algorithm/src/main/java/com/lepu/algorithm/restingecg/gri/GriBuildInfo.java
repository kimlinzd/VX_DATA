package com.lepu.algorithm.restingecg.gri;

import android.util.Log;


/**
 * Maintains information relating to the build of the C ECG analysis library.
 * The C library is built with an entry point to allow information regarding the
 * build to be determined by a calling application. The main value of
 * interest from this class is the version of the analysis software.
 * 
 * There are no methods for setting the values in this class as the instance
 * will be populated by the C library JNI functions.
 * 
 * @author Brian Devine
 *
 */
public class GriBuildInfo {
	// ----------------------------------------------------------------------	
	private static final String TAG = "MyActivity-GriBuildInfo";
	// ----------------------------------------------------------------------	
		
	/**
	 * The vendor for whom the C analysis library was built. This will
	 * very likely be "Generic" unless a specific build variant has been
	 * created.
	 */
	String Vendor;
	
	/**
	 * The version of the software build. The version number is of the form
	 * 'major.minor.patch'.
	 */
    String Version;
    
    /**
     * The type of the software build. This should be "Release" for any
     * production ready software release.
     */
    String Type;
    
    /**
     * The date on which the C analysis library was built. The date is in the
     * format "CCYY-MM-DD".
     */
    String Date;
    
    /**
     * The time at which the C analysis library was built. The time is in the
     * format "HH:MM:SS".
     */
    String Time;
    
    /**
     * The preprocessor definitions that were used when creating the C
     * analysis library.
     */
    String Params;
    
    /**
     * The options available within the C analysis library build.
     */
    String Options;

	// ----------------------------------------------------------------------
	// NDK functions utilised by this class
	// ----------------------------------------------------------------------

    public native String jGetBuildInfo();
	  
	static {
		System.loadLibrary("GlasgowEcg");
	}


	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------

	/**
	 * Calls the JNI function jGetBuildInfo() to retrieve the information
	 * relating to the build version, build date etc of the C analysis
	 * library.
	 */
	public void getBuildInfo()
    {
        jGetBuildInfo();
    }

	// ----------------------------------------------------------------------	

	/**
	 * Retrieves the name of the vendor for whom the C analysis library
	 * was created. The vendor is generally set to 'Generic' unless a
	 * tailor made C analysis library has been created.
	 * 
	 * @return	The name of the vendor.
	 */
    public String getVendor()
    {
    	return this.Vendor;
    }

	// ----------------------------------------------------------------------	

    /**
     * Retrieves the version of the C ECG analysis software library. The
     * version number will be expressed in the form 'major'.'minor'.'patch'
     * e.g. 28.6.0
     * 
     * @return	The version number in the format 'major'.'minor'.'patch'.
     */
    public String getVersion()
    {
    	return this.Version;
    }       

	// ----------------------------------------------------------------------	

    /**
     * Retrieves the date on which the C ECG analysis library build was made.
     * 
     * @return	The date in the format 'CCYY-MM-DD'.
     */
    public String getDate()
    {
    	return this.Date;
    }       

	// ----------------------------------------------------------------------	

    /**
     * Retrieves the time at which the C ECG analysis library build was made.
     * 
     * @return	The 24-hour time in the format 'HH:MM:SS'.
     */
    public String getTime()
    {
    	return this.Time;
    }       

	// ----------------------------------------------------------------------	

    /**
     * Retrieves the options that the C ECG analysis library was built with.
     * 
     * @return	A comma separated list of options the are available in the 
     * C ECG analysis library.
     */
    public String getOptions()
    {
    	return this.Options;
    }       

	// ----------------------------------------------------------------------	

    /**
     * Retrieves the type of build, either 'Release' or 'Debug', that the C
     * ECG analysis library was built as. Official software releases will
     * be a 'Release' build.
     * 
     * @return	The build type which will be either 'Release' or 'Debug'.
     */
    public String getType()
    {
    	return this.Type;
    }       

	// ----------------------------------------------------------------------	

    /**
     * Prints the contents of the BuildInfo instance to the debug log.
     */
    public void printContent()
    {
    	
		Log.d( TAG, "BuildInfo" );
		Log.d( TAG, "=========" );
    	Log.d( TAG, "     Vendor         : " + this.Vendor );
    	Log.d( TAG, "     Version        : " + this.Version );
    	Log.d( TAG, "     BuildDate      : " + this.Date );
    	Log.d( TAG, "     BuildTime      : " + this.Time );
    	Log.d( TAG, "     BuildType      : " + this.Type );
    	Log.d( TAG, "     BuildParams    : " + this.Params );
    	Log.d( TAG, "     BuildOptions   : " + this.Options );
		Log.d( TAG, " " );
    }

};
