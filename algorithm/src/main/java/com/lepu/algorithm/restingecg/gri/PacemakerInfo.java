package com.lepu.algorithm.restingecg.gri;

import android.util.Log;


/**
 * Maintains the list of pacemaker spikes
 * that are supplied by the calling application prior to undertaking
 * an ECG analysis.
 * <p>
 * All locations of pacemaker spike fiducials are 0-based sample numbers.
 * 
 * @author  Brian Devine
 */

public class PacemakerInfo {
	// --------------------------------------------------------------------------	
	private static final String TAG = "MyActivity-PacemakerInfo";
	// --------------------------------------------------------------------------	

	/**
	 * Maximum number of pacer spikes that can be accommodated.
	 */
	short		MaxPacers;
	
	/**
	 * Number of pacer spikes stored.
	 */
	short		PacerCount; 
	
	/**
	 * Flag to indicate if the application is supplying pacemaker spike information.
	 * Set TRUE if the calling application always supplies pacer locations, otherwise
	 * FALSE.
	 */
	boolean		bApplicationSuppliesPacers;
	
	/**
	 * Flag to indicate if pacemaker spikes have been removed from the ECG data.
	 * Set TRUE if spikes have been removed, otherwise FALSE.
	 */
	boolean		bPacersRemoved;
	
	/**
	 * Flag to indicate if pacemaker locations have been supplied. Set TRUE if
	 * pacemaker spike locations are supplied, otherwise FALSE.
	 */
	boolean		bPacerLocationSupplied;
	
	/**
	 * Flag to indicate is pacemaker spike onsets and terminations are supplied.
	 * Set TRUE is onsets and terminations are supplied, otherwise FALSE.
	 */
	boolean		bPacerWidthSupplied;
	
	/**
	 * Array of 0-based sample numbers of spike onsets.
	 */
	short []	PacerSpikeOnset;
	
	/**
	 * Array of 0-based sample numbers of spike locations.
	 */
	short []	PacerSpikeLocation;
	
	/**
	 * Array of 0-based sample numbers of spike terminations.
	 */
	short []	PacerSpikeTermination;


	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------

	/**
	 * Creates and initialises an instance of {@link PacemakerInfo} with the
	 * given maximum number pacemaker spikes allowed. The default setup is
	 * to signal that the application is not supplying pacemaker spikes,
	 * the pacer spikes have not been removed from the data and no information
	 * regarding the location and width of the any pacemaker spikes is
	 * provided by the application.
	 * 
	 * @param NumPacers	The maximum number of pacemaker spikes that can be
	 * accommodated.
	 */
	public PacemakerInfo( short NumPacers )
	{
		// Provide indication of the maximum number of pacers that can be stored
		this.MaxPacers = NumPacers;

		this.PacerCount = 0;
		
		this.bApplicationSuppliesPacers = false;
		
		this.bPacersRemoved             = false;
		this.bPacerLocationSupplied     = false;
		this.bPacerWidthSupplied        = false;

		this.PacerSpikeOnset       = new short [NumPacers];
		this.PacerSpikeLocation    = new short [NumPacers];
		this.PacerSpikeTermination = new short [NumPacers];

	}
	
	// ----------------------------------------------------------------------

	/**
	 * Signal that pacemaker spike information will be supplied by the
	 * application and that the default library pacemaker spike detection
	 * routines should be disabled.
	 */
	public void signalApplicationSuppliesPacers()
	{
		this.bApplicationSuppliesPacers = true;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Signal that pacemaker spikes have been removed from the raw ECG data.
	 */
	public void signalPacersHaveBeenRemoved()
	{
		this.bPacersRemoved = true;
	}

	// ----------------------------------------------------------------------

	/**
	 * Signal that pacemaker spikes have not been removed from the raw ECG data.
	 */
	public void signalPacersHaveNotBeenRemoved()
	{
		this.bPacersRemoved = false;
	}


	// ----------------------------------------------------------------------

	/**
	 * Signal that pacemaker spike locations have been supplied.
	 */
	public void signalPacerLocationsSupplied()
	{
		this.bPacerLocationSupplied = true;
	}

	// ----------------------------------------------------------------------

	/**
	 * Signal that pacemaker spike locations have not been supplied.
	 */
	public void signalPacerLocationsNotSupplied()
	{
		this.bPacerLocationSupplied = false;
	}

	// ----------------------------------------------------------------------

	/**
	 * Signal that pacemaker spike widths (onsets+terminations) have been supplied.
	 */
	public void signalPacerWidthsSupplied()
	{
		bPacerWidthSupplied = true;
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Signal that pacemaker spike widths (onsets+terminations) have not been supplied.
	 */
	public void signalPacerWidthsNotSupplied()
	{
		bPacerWidthSupplied = false;
	}

	// ----------------------------------------------------------------------
	
	/**
	 * Parses the list of pacemaker spike information presented in the 
	 * GRIANLYS input file format and adds the pacemaker information
	 * to the class instance. See GRIANLYS documentation for details of the
	 * format of the parameter.
	 * 
	 * @param	PacerList	String obtained directly from a GRIANLYS input
	 * file containing pacemaker spike information.
	 * @return	TRUE if the pacemaker spikes were successfully added,
	 * otherwise returns FALSE.
	 */
	public boolean addPacers( String PacerList )
	{
		int	pacerCount;

		if ( PacerList == "" )
		{
			return false;
		}
		else
		{
			// Get the number of pacer spikes
			String[] pacerArgs = PacerList.split(" ");


			// First, make sure the argument list is a valid length
			if ( pacerArgs.length >= 2 )
			{
				// Handle spike present or removed
				switch ( Integer.parseInt(pacerArgs[0]) )
				{
					case 0:
						signalPacersHaveBeenRemoved();
						break;

					case 1:
						signalPacersHaveNotBeenRemoved();
						break;
				}

				// Get count
				pacerCount = (short) Integer.parseInt(pacerArgs[1]);

				// Check that PacerCount matches the remaining number of arguments
				if ( (3*pacerCount) != (pacerArgs.length-2) )
				{
					return false;
				}
				else
				{
					int i;

					// Signal that the application is supplying pacemaker spikes
					this.signalApplicationSuppliesPacers();

					// Store the spike details
					for (i = 0; i < pacerCount; i++) {
						this.addPacer(Integer.parseInt(pacerArgs[2+(i*3)]), Integer.parseInt(pacerArgs[3+(i*3)]), Integer.parseInt(pacerArgs[4+(i*3)]) );
					}

					// Quick scan to see if ons/ter have been supplied
					boolean widthSupplied = true;
					boolean locationSupplied = true;
					for ( i = 0; i < pacerCount; i++ )
					{
						if (this.PacerSpikeOnset[i] == (short)-1 || this.PacerSpikeTermination[i] == (short)-1)
						{
							widthSupplied = false;
						}
						if ( this.PacerSpikeLocation[i] == (short)-1 )
						{
							locationSupplied = false;
						}
					}
					if ( widthSupplied )
					{
						this.signalPacerWidthsSupplied();
					}
					if ( locationSupplied )
					{
						this.signalPacerLocationsSupplied();
					}
					return true;
				}

			}

			else
			{
				return false;
			}

		}
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Appends a set of pacemaker spike metrics to the list of pacemaker spikes.
	 * A check will be made to ensure that there are sufficient entries within
	 * the pacemaker spike structure to accommodate the spike.
	 * 
	 * @param	Onset	The 0-based sample number of the spike onset.
	 * @param	Location	The 0-based sample number of the spike location.
	 * This is equivalent to the location of the peak of the pacemaker spike.
	 * @param	Termination	The 0-based sample number of the spike termination.
	 * @return	TRUE if the pacemaker spike information was successfully
	 * added,  otherwise returns FALSE.
	 */
	public boolean addPacer( int Onset, int Location, int Termination )
	{
		if ( PacerCount != MaxPacers )
		{
			PacerSpikeOnset[PacerCount]       = (short) Onset;
			PacerSpikeLocation[PacerCount]    = (short) Location;
			PacerSpikeTermination[PacerCount] = (short) Termination;
			
			PacerCount++;
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Appends a pacemaker spike location to the list of pacemaker spikes. The
	 * associated spike onset and termination will be set to -1 to indicate
	 * that these values are not supplied.
	 * 
	 * A check will be made to ensure that there are sufficient entries within
	 * the pacemaker spike structure to accommodate the spike.
	 * 
	 * @param Location	The 0-based sample number of the spike location.
	 * This is equivalent to the location of the peak of the pacemaker spike.
	 * @return	TRUE if the pacemaker spike information was successfully
	 * added,  otherwise returns FALSE.
	 */
	public boolean addPacer( int Location )
	{
		if ( PacerCount != MaxPacers )
		{
			PacerSpikeOnset[PacerCount]       = (short) -1;
			PacerSpikeLocation[PacerCount]    = (short) Location;
			PacerSpikeTermination[PacerCount] = (short) -1;
			
			PacerCount++;
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Prints the contents of the {@link PacemakerInfo} instance to the debug
	 * output log.
	 */
	public void printContent()
	{
		short	i;
		
		Log.d( TAG, "PatientInfo" );
		Log.d( TAG, "===========" );
		Log.d( TAG, "Max number of pacers  : " + MaxPacers );
		Log.d( TAG, "Pacer count           : " + PacerCount );
		for ( i = 0; i < PacerCount; i++ )
		{
			Log.d( TAG, "[" + (i+1) + "] " + PacerSpikeOnset[i] + " " + PacerSpikeLocation[i] + " " + PacerSpikeTermination[i] );
		}
		Log.d( TAG, " " );
	}
}
