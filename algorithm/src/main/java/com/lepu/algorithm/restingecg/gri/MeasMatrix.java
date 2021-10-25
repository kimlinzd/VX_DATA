
package com.lepu.algorithm.restingecg.gri;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


/**
 * Maintains the global and individual lead measurements for the scalar and
 * vector measurement matrices that are generated during the analysis of an ECG.
 * Two measurement matrices are created by the analysis, the scalar and vector
 * matrices. The C analysis
 * library will return a 2-dimensional array for each of the matrices, and
 * these will subsequently be converted to more structured forms to be more
 * easily accessible from program code.
 * <P>
 * Some care must be taken over the ordering of measurements within each of
 * the data structures. The scalar matrices will retain the same order of
 * measurements that are returned by the C analysis library. For the purpose
 * of consistency with the leads provided for analysis, the array of
 * {@link LeadMeas} instance will retain the same order as the input leads supplied
 * for analysis. This will only impact paediatric recordings where the input
 * leads are in the order V4R,V1,V2 while the scalar measurement matrix would
 * be in the order V1,V2,V4R.
 * 
 * @author Brian Devine
 *
 */
public class MeasMatrix {
	// --------------------------------------------------------------------------	
	private static final String TAG = "MyActivity-MeasMatrix";
	// --------------------------------------------------------------------------	
	
	/** Measurements that are not quantifiable are determined to be undefined. */
	
	/** Bit-flag to indicate if P wave measurements are reliable */
	static final short GRI_PWAVEFLAG = 0x00000001;
	static final short GRI_QRSFLAG = 0x00000002;			// Indicate if QRS axis is reliable
	
	static final short GRI_DEFAULTGENDERUSED = 0x00000010;		// Flag to indicate if default gender used by analysis
	static final short GRI_DEFAULTRACEUSED = 0x00000010;		// Flag to indicate if default race used by analysis
	static final short GRI_DEFAULTAGEUSED = 0x00000010;		// Flag to indicate if default age used by analysis
	
	static final long GRI_FLAG_QTC_HODGE = 0x00000200;	// Indicate use of Hodges QTc
	static final long GRI_FLAG_QTC_BAZETT = 0x00000400;	// Indicate use of Bazetts QTc
	static final long GRI_FLAG_QTC_FRIDERICIA = 0x00000800;	// Indicate use of Fridericias QTc
	static final long GRI_FLAG_QTC_FRAMINGHAM = 0x00001000;	// Indicate use of Framinghams QTc

	static final short JPO = 0;
	static final short JPD = 1;
	static final short JQO = 2;
	static final short JQRSD = 3;
	static final short JR1D = 4;
	static final short JST80 = 4;
	static final short JQD = 5;
	static final short JRD = 6;
	static final short JSD = 7;
	static final short JRDD = 8;
	static final short JSDD = 9;
	static final short JRD2D = 10;
	static final short JSD2D = 11;
	static final short JSTD = 12;
	static final short JTO = 13;
	static final short JTD = 14;
	static final short JPPD = 15;
	static final short JTPD = 16;
	static final short JQIDD = 17;
	static final short JPPA = 18;
	static final short JPMA = 19;
	static final short JQPPA = 20;
	static final short JR1A = 21;
	static final short JQA = 22;
	static final short JRA = 23;
	static final short JSA = 24;
	static final short JRDA = 25;
	static final short JSDA = 26;
	static final short JRD2A = 27;
	static final short JSD2A = 28;
	static final short JSTA = 29;
	static final short J28STA = 30;
	static final short J38STA = 31;
	static final short JTPA = 32;
	static final short JTMA = 33;
	static final short JQAR = 34;
	static final short JPAR = 35;
	static final short JTAR = 36;
	static final short JPMOR = 37;
	static final short JTMOR = 38;
	static final short JRN = 39;
	static final short JDC = 40;
	static final short JSTS = 41;
	static final short JAA = 42;
	static final short JAO = 43;
	static final short JAD = 44;
	static final short JAI = 45;
	static final short JQTI = 46;
	static final short JSTM = 47;
	static final short JST60 = 48;
	static final short JSTTMID = 49;
	static final short J18STA = 49;

	static final short JAQAX = 0;
	static final short JAPAX = 1;
	static final short JAJAX = 2;
	static final short JATAX = 3;
	static final short J48SV = 4;
	static final short J58SV = 5;
	static final short J68SV = 6;
	static final short J78SV = 7;
	static final short JMQPV = 8;
	static final short JDWAI = 9;
	static final short JDWAII = 10;
	static final short JDWAIII = 11;
	static final short JDWAAVF = 12;
	static final short JDWFAX = 13;
	
	static final short JAPO = 0;
	static final short JAPT = 1;
	static final short JAQO = 2;
	static final short JAQT = 3;
	static final short JATO = 4;
	static final short JATT = 5;
	static final short JAPBL = 6;
	static final short JVPBL = 7;
	static final short JHRV = 8;
	static final short JSDNN = 9;
	static final short JLVH = 10;
	static final short JLVSTN = 11;
	static final short JAGE = 12;
	static final short JSEX = 13;
	static final short JRACE = 14;
	static final short JAPD = 0;
	static final short JAQD = 1;
	static final short JASTD = 2;
	static final short JATD = 3;
	static final short JRMAVR = 4;
	static final short JRMAVL = 5;

	static final short JAPRI = 0;
	static final short JAQTI = 1;
	static final short JRATE = 2;
	static final short JPV1TF = 3;
	static final short JQTDP = 4;
	static final short JQTC = 5;
	static final short JFLAG = 6;
	static final short JQTCH = 7;
	static final short JQTCB = 8;
	static final short JQTCF = 9;
	static final short JQTCR = 10;
	static final short JDRATE = 11;	/* Heart rate based on dominant beats. */
	static final short JAVDRR = 12;	/* Average dominant RR interval in ms. */
	static final short JRRATE = 13;
	static final short JAVRR = 14;


	static final short JMPV = 0;
	static final short JMQV = 1;
	static final short JMTV = 2;
	static final short JMPPV = 3;
	static final short JP03QV = 4;
	static final short JM03QV = 5;
	static final short J38QV = 6;
	static final short J48QV = 7;
	static final short J58QV = 8;
	static final short J68STV = 9;
	static final short JMQWV = 10;
	static final short JMTWV = 11;
	static final short JSTXA = 12;
	static final short JSTYA = 13;
	static final short JSTZA = 14;
	static final short JQV8R = 15;
	static final short JQV10R = 16;
	static final short JQW8R = 17;
	static final short JQW10R = 18;
	static final short JAV = 19;

	static final short JFAV = 0;
	static final short JFLV = 1;
	static final short JRSAV = 2;
	static final short JRSLV = 3;
	static final short JTAV = 4;
	static final short JTLV = 5;
	static final short JMV = 6;
	static final short JPV = 7;
	static final short JXAV = 8;
	static final short JYAV = 9;
	static final short JZAV = 10;

	static final short J18 = 0;
	static final short J28 = 1;
	static final short J38 = 2;
	static final short J48 = 3;
	static final short J58 = 4;
	static final short J68 = 5;
	static final short J78 = 6;

	static final short JP01 = 0;
	static final short JP02 = 1;
	static final short JP03 = 2;
	static final short JM01 = 3;
	static final short JM02 = 4;
	static final short JM03 = 5;

	static final short JMQVR = 0;
	static final short JMTVR = 1;
	static final short JMQWR = 2;
	static final short JMTWR = 3;
	static final short JQTSAV = 4;
	static final short JQTSAW = 5;

	static final short JSAV1 = 6;
	static final short JRAV5 = 7;
	static final short JASV1RV5 = 8;

	static final short JXV = 0;
	static final short JYV = 1;
	static final short JZV = 2;

	static final short JFPWAV = 0x0001;		//* For JFLAG - Indicates if p-waves found */
	static final short JFQAX = 0x0002;		//* For JFLAG - Indicates if indeterminate axis */

	static final short JFSEX = 0x0004;
	static final short JFRACE = 0x0008;
	static final short JFAGE = 0x0010;
	
	//static final short GRI_MAX_MATRIX_ROWS = 50;	// TODO: Review with each release
	//static final short GRI_USED_MATRIX_ROWS = 50;	// TODO: Review with each release

	static final short GRI_MAX_MATRIX_ROWS = 75;	// TODO: Review with each release
	static final short GRI_USED_MATRIX_ROWS = 55;	// TODO: Review with each release

	static final short GRI_MAX_VECTMATRIX_ROWS = 8;
	static final short GRI_MAX_VECTMATRIX_COLS = 20;
	
	// The various scalar lead measurements that are currently represented
	LeadMeas[] leadMeas;
	GlobalMeas globalMeas;

	VectorMeas	vectorMeas;

	
	// Should this be private as it's only needed to pass data via JNI
	private short [][] Scalar;
	private short [][] Vector;
	
	
	// ----------------------------------------------------------------------
	// METHODS
	// ----------------------------------------------------------------------

	
	/**
	 * Creates an instance of the {@link MeasMatrix} with the lead configuration
	 * defaulting to adult electrode positioning.
	 */
	public MeasMatrix()
	{

		this( new GriLeadId[] {
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
	 * Creates an instance of the {@link MeasMatrix} and initialises all
	 * values to GRI_UNDEFINED.
	 * 
	 * @param LeadConfig	Array of {@link GriLeadId} lead identifiers that
	 * will be used to identify patricular lead measurements in the {@link LeadMeas}
	 * array instance.
	 */
	public MeasMatrix( GriLeadId[] LeadConfig )
	{
		short	i;
		short	MatrixRowCount, MatrixColCount;
		short	MatrixRowNum, MatrixColNum;

		
		// Create structures visible to all
		this.leadMeas    = new LeadMeas[RestingEcg.GRI_MAX_ECG_LEADS];
		for (i = 0; i < RestingEcg.GRI_MAX_ECG_LEADS; i++ )
		{
			this.leadMeas[i] = new LeadMeas( LeadConfig[i] );
		}
		this.globalMeas  = new GlobalMeas();
		this.vectorMeas  = new VectorMeas();

		
		// Create structure to hold results from C via JNI
		this.Scalar      = new short [GRI_MAX_MATRIX_ROWS][RestingEcg.GRI_MAX_ECG_LEADS];
		this.Vector      = new short [GRI_MAX_VECTMATRIX_ROWS][GRI_MAX_VECTMATRIX_COLS];

		
		// Set everything in the Scalar matrix to GRI_UNDEFINED
		
		MatrixRowCount = GRI_MAX_MATRIX_ROWS;
		MatrixColCount = RestingEcg.GRI_MAX_ECG_LEADS;
			
		for ( MatrixRowNum = 0; MatrixRowNum < MatrixRowCount; MatrixRowNum++ )
		{
			for ( MatrixColNum = 0; MatrixColNum < MatrixColCount; MatrixColNum++ )
			{
				Scalar[MatrixRowNum][MatrixColNum] = RestingEcg.GRI_UNDEFINED;
			}
		}
		
		for ( MatrixRowNum = 0; MatrixRowNum < GRI_MAX_VECTMATRIX_ROWS; MatrixRowNum++ )
		{
			for ( MatrixColNum = 0; MatrixColNum < GRI_MAX_VECTMATRIX_COLS; MatrixColNum++ )
			{
				Vector[MatrixRowNum][MatrixColNum] = RestingEcg.GRI_UNDEFINED;
			}
		}
	
	}

	// ----------------------------------------------------------------------

	public void GlobalMeasToVectMeas()
	{
		
	}
	
	// ----------------------------------------------------------------------

	public void LeadMeasToVectMeas()
	{
		
	}
	
	// ----------------------------------------------------------------------
	
	public void VectMeasToLeadMeas()
	{
		
	}
	
	// ----------------------------------------------------------------------

	public void VectMeasToGlobalMeas()
	{
		
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Converts the global measurements within the {@link GlobalMeas}
	 * instance to the scalar measurement matrix.
	 */
	public void GlobalMeasToScalarMeas()
	{
		// ------------------------------------------------------------------
		// Copy global measurements
		// ------------------------------------------------------------------
		
		this.Scalar[JAA][JAQAX]   = this.globalMeas.QrsFrontalAxis;
		this.Scalar[JAA][JAPAX]   = this.globalMeas.PFrontalAxis;
		this.Scalar[JAA][JAJAX]   = this.globalMeas.STFrontalAxis;
		this.Scalar[JAA][JATAX]   = this.globalMeas.TFrontalAxis;
		this.Scalar[JAA][J48SV]   = this.globalMeas.QrsPsdoVec48SpatVel;
		this.Scalar[JAA][J58SV]   = this.globalMeas.QrsPsdoVec58SpatVel;
		this.Scalar[JAA][J68SV]   = this.globalMeas.QrsPsdoVec68SpatVel;
		this.Scalar[JAA][J78SV]   = this.globalMeas.QrsPsdoVec78SpatVel;
		this.Scalar[JAA][JMQPV]   = this.globalMeas.QrsPsdoVecMaxAmpl;
		this.Scalar[JAA][JDWAI]   = this.globalMeas.DeltaWaveAmpI;
		this.Scalar[JAA][JDWAII]  = this.globalMeas.DeltaWaveAmpII;
		this.Scalar[JAA][JDWAIII] = this.globalMeas.DeltaWaveAmpIII;
		this.Scalar[JAA][JDWAAVF] = this.globalMeas.DeltaWaveAmpaVF;
		this.Scalar[JAA][JDWFAX]  = this.globalMeas.DeltaWaveFrontalAxis;
		// SPARE 1
		
		this.Scalar[JAO][JAPO]    = this.globalMeas.OverallPOnset;
		this.Scalar[JAO][JAPT]    = this.globalMeas.OverallPTermination;
		this.Scalar[JAO][JAQO]    = this.globalMeas.OverallQrsOnset;
		this.Scalar[JAO][JAQT]    = this.globalMeas.OverallQrsTermination;
		this.Scalar[JAO][JATO]    = this.globalMeas.OverallTOnset;
		this.Scalar[JAO][JATT]    = this.globalMeas.OverallTTermination;
		this.Scalar[JAO][JAPBL]   = this.globalMeas.AtrialPacerPeak;
		this.Scalar[JAO][JVPBL]   = this.globalMeas.VentricularPacerPeak;
		this.Scalar[JAO][JHRV]    = this.globalMeas.HeartRateVariability;
		this.Scalar[JAO][JSDNN]   = this.globalMeas.StdDevNormRRIntervals;
		this.Scalar[JAO][JLVH]    = this.globalMeas.LVHscore;
		this.Scalar[JAO][JLVSTN]  = this.globalMeas.LVstrain;
		this.Scalar[JAO][JAGE]    = this.globalMeas.DefaultAge;
		this.Scalar[JAO][JSEX]    = this.globalMeas.DefaultSex;
		this.Scalar[JAO][JRACE]   = this.globalMeas.DefaultRace;

		this.Scalar[JAD][JAPD]    = this.globalMeas.OverallPDuration;
		this.Scalar[JAD][JAQD]    = this.globalMeas.OverallQrsDuration;
		this.Scalar[JAD][JASTD]   = this.globalMeas.OverallSTDuration;
		this.Scalar[JAD][JATD]    = this.globalMeas.OverallTDuration;
		this.Scalar[JAD][JRMAVR]  = this.globalMeas.RmaxaVR;
		this.Scalar[JAD][JRMAVL]  = this.globalMeas.RmaxaVL;
		this.Scalar[JAD][JRMAVL]  = this.globalMeas.RmaxaVL;
		this.Scalar[JAD][JSAV1]   = this.globalMeas.SAmpV1;
		this.Scalar[JAD][JRAV5]   = this.globalMeas.RAmpV5;
		this.Scalar[JAD][JASV1RV5]= this.globalMeas.SAmpV1PlusRAmpV5;
		// SPARE 6

		this.Scalar[JAI][JAPRI]   = this.globalMeas.OverallPRInterval;
		this.Scalar[JAI][JAQTI]   = this.globalMeas.OverallQTInterval;
		this.Scalar[JAI][JRATE]   = this.globalMeas.HeartRate;
		this.Scalar[JAI][JPV1TF]  = this.globalMeas.PTerminalForceInV1;
		this.Scalar[JAI][JQTDP]   = this.globalMeas.QTDispersion;
		this.Scalar[JAI][JQTC]    = this.globalMeas.QTc;
		this.Scalar[JAI][JFLAG]   = this.globalMeas.Flags;
		this.Scalar[JAI][JQTCH]   = this.globalMeas.QTcHodge;
		this.Scalar[JAI][JQTCB]   = this.globalMeas.QTcBazett;
		this.Scalar[JAI][JQTCF]   = this.globalMeas.QTcFridericia;
		this.Scalar[JAI][JQTCR]   = this.globalMeas.QTcFramingham;
		this.Scalar[JAI][JDRATE]  = this.globalMeas.SinusRate;
		this.Scalar[JAI][JAVDRR]  = this.globalMeas.SinusAverageRR;
		this.Scalar[JAI][JRRATE]  = this.globalMeas.VentRate;
		this.Scalar[JAI][JAVRR]   = this.globalMeas.VentAverageRR;

	}

	// ----------------------------------------------------------------------
	
	/**
	 * Converts the global measurements within the scalar measurement matrix
	 * to the {@link GlobalMeas} instance.
	 */
	public void ScalarMeasToGlobalMeas()
	{
		// ------------------------------------------------------------------
		// Copy global measurements
		// ------------------------------------------------------------------
		
		globalMeas.QrsFrontalAxis        = Scalar[JAA][JAQAX];
		globalMeas.PFrontalAxis          = Scalar[JAA][JAPAX];
		globalMeas.STFrontalAxis         = Scalar[JAA][JAJAX];
		globalMeas.TFrontalAxis          = Scalar[JAA][JATAX];
		globalMeas.QrsPsdoVec48SpatVel   = Scalar[JAA][J48SV]; 
		globalMeas.QrsPsdoVec58SpatVel   = Scalar[JAA][J58SV];
		globalMeas.QrsPsdoVec68SpatVel   = Scalar[JAA][J68SV];
		globalMeas.QrsPsdoVec78SpatVel   = Scalar[JAA][J78SV];
		globalMeas.QrsPsdoVecMaxAmpl     = Scalar[JAA][JMQPV];
		globalMeas.DeltaWaveAmpI         = Scalar[JAA][JDWAI];
		globalMeas.DeltaWaveAmpII        = Scalar[JAA][JDWAII];
		globalMeas.DeltaWaveAmpIII       = Scalar[JAA][JDWAIII];
		globalMeas.DeltaWaveAmpaVF       = Scalar[JAA][JDWAAVF];
		globalMeas.DeltaWaveFrontalAxis  = Scalar[JAA][JDWFAX];
		// SPARE 1
		
		globalMeas.OverallPOnset         = Scalar[JAO][JAPO];
		globalMeas.OverallPTermination   = Scalar[JAO][JAPT];
		globalMeas.OverallQrsOnset       = Scalar[JAO][JAQO];
		globalMeas.OverallQrsTermination = Scalar[JAO][JAQT];
		globalMeas.OverallTOnset         = Scalar[JAO][JATO];
		globalMeas.OverallTTermination   = Scalar[JAO][JATT];
		globalMeas.AtrialPacerPeak       = Scalar[JAO][JAPBL];
		globalMeas.VentricularPacerPeak  = Scalar[JAO][JVPBL];
		globalMeas.HeartRateVariability  = Scalar[JAO][JHRV];
		globalMeas.StdDevNormRRIntervals = Scalar[JAO][JSDNN];
		globalMeas.LVHscore              = Scalar[JAO][JLVH];
		globalMeas.LVstrain              = Scalar[JAO][JLVSTN];
		globalMeas.DefaultAge            = Scalar[JAO][JAGE];
		globalMeas.DefaultSex            = Scalar[JAO][JSEX];
		globalMeas.DefaultRace           = Scalar[JAO][JRACE];

		globalMeas.OverallPDuration      = Scalar[JAD][JAPD];
		globalMeas.OverallQrsDuration    = Scalar[JAD][JAQD];
		globalMeas.OverallSTDuration     = Scalar[JAD][JASTD];
		globalMeas.OverallTDuration      = Scalar[JAD][JATD];
		globalMeas.RmaxaVR               = Scalar[JAD][JRMAVR];
		globalMeas.RmaxaVL               = Scalar[JAD][JRMAVL];
		globalMeas.RmaxaVL               = Scalar[JAD][JRMAVL];
		globalMeas.SAmpV1                = Scalar[JAD][JSAV1];
		globalMeas.RAmpV5                = Scalar[JAD][JRAV5];
		globalMeas.SAmpV1PlusRAmpV5      = Scalar[JAD][JASV1RV5];
		// SPARE 6

		globalMeas.OverallPRInterval     = Scalar[JAI][JAPRI];
		globalMeas.OverallQTInterval     = Scalar[JAI][JAQTI];
		globalMeas.HeartRate             = Scalar[JAI][JRATE];
		globalMeas.PTerminalForceInV1    = Scalar[JAI][JPV1TF];
		globalMeas.QTDispersion          = Scalar[JAI][JQTDP];
		globalMeas.QTc                   = Scalar[JAI][JQTC];
		globalMeas.Flags                 = Scalar[JAI][JFLAG];
		globalMeas.QTcHodge              = Scalar[JAI][JQTCH];
		globalMeas.QTcBazett             = Scalar[JAI][JQTCB];
		globalMeas.QTcFridericia         = Scalar[JAI][JQTCF];
		globalMeas.QTcFramingham         = Scalar[JAI][JQTCR];
		globalMeas.SinusRate             = Scalar[JAI][JDRATE];
		globalMeas.SinusAverageRR        = Scalar[JAI][JAVDRR];
		globalMeas.VentRate              = Scalar[JAI][JRRATE];
		globalMeas.VentAverageRR         = Scalar[JAI][JAVRR];
		
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Extracts the individual lead measurements from the Scalar measurement
	 * matrix and stores them within LeadMeas instances. A boolean flag must
	 * be provided which indicates if paediatric electrode positioning is 
	 * used or not as this will affect the order of measurement extraction.
	 * 
	 * @param isPaediatricElectrodePlacement	Flag should be TRUE if
	 * paediatric electrode placement is used, otherwise FALSE.
	 */
	public void ScalarMeasToLeadMeas( boolean isPaediatricElectrodePlacement )
	{
		short	i;
		short [] LeadMap;
	
		
		// Determine lead order for measurements in scalar array
		if ( isPaediatricElectrodePlacement )
		{
			LeadMap = new short [] {
					0, 1, 2, 3, 4, 5, 8, 6, 7, 9, 10, 11, 12, 13, 14
			};	
		}
		else
		{
			LeadMap = new short [] {
					0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14
			};
		}
		
		//if ( isPaediatricElectrodePlacement )
		//{
		//	Log.d(TAG, "Paediatric electrodes" );
		//}
		//else
		//{
		//	Log.d(TAG, "Adult electrodes" );
		//}
		//for ( i = 0; i < RestingEcg.GRI_MAX_ECG_LEADS; i++ )
		//{
		//	Log.d(TAG, "ArrayMap[" + i + "] = " + LeadMap[i] );
		//}
		
		// ------------------------------------------------------------------
		// Copy individual lead measurements
		// ------------------------------------------------------------------

		for (i = 0; i < RestingEcg.GRI_MAX_ECG_LEADS; i++ )
		{
			leadMeas[i].POnset                 = Scalar[JPO][LeadMap[i]];
			leadMeas[i].PDuration              = Scalar[JPD][LeadMap[i]];
			leadMeas[i].QrsOnset               = Scalar[JQO][LeadMap[i]];
			leadMeas[i].QrsDuration            = Scalar[JQRSD][LeadMap[i]];
			leadMeas[i].ST80Amplitude          = Scalar[JST80][LeadMap[i]];
			leadMeas[i].QDuration              = Scalar[JQD][LeadMap[i]];
			leadMeas[i].RDuration              = Scalar[JRD][LeadMap[i]];
			leadMeas[i].SDuration              = Scalar[JSD][LeadMap[i]];
			leadMeas[i].RDashDuration          = Scalar[JRDD][LeadMap[i]];
			leadMeas[i].SDashDuration          = Scalar[JSDD][LeadMap[i]];
			leadMeas[i].RDashDashDuration      = Scalar[JRD2D][LeadMap[i]];
			leadMeas[i].SDashDashDuration      = Scalar[JSD2D][LeadMap[i]];
			leadMeas[i].STDuration             = Scalar[JSTD][LeadMap[i]];
			leadMeas[i].TOnset                 = Scalar[JTO][LeadMap[i]];
			leadMeas[i].TDuration              = Scalar[JTD][LeadMap[i]];
			leadMeas[i].PPosDuration           = Scalar[JPPD][LeadMap[i]];
			leadMeas[i].TPosDuration           = Scalar[JTPD][LeadMap[i]];
			leadMeas[i].QrsIntrinsicoidDefl    = Scalar[JQIDD][LeadMap[i]];
			leadMeas[i].PPosAmplitude          = Scalar[JPPA][LeadMap[i]];
			leadMeas[i].PNegAmplitude          = Scalar[JPMA][LeadMap[i]];
			leadMeas[i].PeakToPeakQrsAmplitude = Scalar[JQPPA][LeadMap[i]];
			leadMeas[i].R1Amplitude            = Scalar[JR1A][LeadMap[i]];
			leadMeas[i].QAmplitude             = Scalar[JQA][LeadMap[i]];
			leadMeas[i].RAmplitude             = Scalar[JRA][LeadMap[i]];
			leadMeas[i].SAmplitude             = Scalar[JSA][LeadMap[i]];
			leadMeas[i].RDashAmplitude         = Scalar[JRDA][LeadMap[i]];
			leadMeas[i].SDashAmplitude         = Scalar[JSDA][LeadMap[i]];
			leadMeas[i].RDashDashAmplitude     = Scalar[JRD2A][LeadMap[i]];
			leadMeas[i].SDashDashAmplitude     = Scalar[JSD2A][LeadMap[i]];
			leadMeas[i].STAmplitude            = Scalar[JSTA][LeadMap[i]];
			leadMeas[i].STT28Amplitude         = Scalar[J28STA][LeadMap[i]];
			leadMeas[i].STT38Amplitude         = Scalar[J38STA][LeadMap[i]];
			leadMeas[i].TposAmplitude          = Scalar[JTPA][LeadMap[i]];
			leadMeas[i].TnegAmplitude          = Scalar[JTMA][LeadMap[i]];
			leadMeas[i].QrsArea                = Scalar[JQAR][LeadMap[i]];
			leadMeas[i].PArea                  = Scalar[JPAR][LeadMap[i]];
			leadMeas[i].TArea                  = Scalar[JTAR][LeadMap[i]];
			leadMeas[i].PMorphology            = Scalar[JPMOR][LeadMap[i]];
			leadMeas[i].TMorphology            = Scalar[JTMOR][LeadMap[i]];
			leadMeas[i].RNotches               = Scalar[JRN][LeadMap[i]];
			leadMeas[i].DeltaWaveConfidence    = Scalar[JDC][LeadMap[i]];
			leadMeas[i].STSlope                = Scalar[JSTS][LeadMap[i]];
			leadMeas[i].QTInterval             = Scalar[JQTI][LeadMap[i]];
			leadMeas[i].STMAmplitude           = Scalar[JSTM][LeadMap[i]];
			leadMeas[i].ST60Amplitude          = Scalar[JST60][LeadMap[i]];
			leadMeas[i].STTMidAmplitude        = Scalar[JSTTMID][LeadMap[i]];
		}
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Populates the 2-dimensional scalar measurement matrix with the
	 * individual lead measurements. 
	 * 
	 * @param isPaediatricElectrodePlacement	Flag to indicate if
	 * the ECG is recorded using paediatric electrode placement
	 */
	public void LeadMeasToScalarMeas(boolean isPaediatricElectrodePlacement )
	{
		short	i;
		short [] LeadMap;
	
		
		// ------------------------------------------------------------------
		// Determine lead order for measurements in scalar array. This 
		// mapping is required to ensure that the leadMeas content matches
		// the order of the raw leads in the input.
		// ------------------------------------------------------------------
		if ( isPaediatricElectrodePlacement )
		{
			LeadMap = new short [] {
					0, 1, 2, 3, 4, 5, 8, 6, 7, 9, 10, 11, 12, 13, 14
			};	
		}
		else
		{
			LeadMap = new short [] {
					0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14
			};
		}


		// ------------------------------------------------------------------
		// Copy individual lead measurements
		// ------------------------------------------------------------------

		for (i = 0; i < RestingEcg.GRI_MAX_ECG_LEADS; i++ )
		{
			Scalar[JPO][LeadMap[i]]     = leadMeas[i].POnset;
			Scalar[JPD][LeadMap[i]]     = leadMeas[i].PDuration;
			Scalar[JQO][LeadMap[i]]     = leadMeas[i].QrsOnset;
			Scalar[JQRSD][LeadMap[i]]   = leadMeas[i].QrsDuration;
			Scalar[JST80][LeadMap[i]]   = leadMeas[i].ST80Amplitude;
			Scalar[JQD][LeadMap[i]]     = leadMeas[i].QDuration;
			Scalar[JRD][LeadMap[i]]     = leadMeas[i].RDuration;
			Scalar[JSD][LeadMap[i]]     = leadMeas[i].SDuration;
			Scalar[JRDD][LeadMap[i]]    = leadMeas[i].RDashDuration;
			Scalar[JSDD][LeadMap[i]]    = leadMeas[i].SDashDuration;
			Scalar[JRD2D][LeadMap[i]]   = leadMeas[i].RDashDashDuration;
			Scalar[JSD2D][LeadMap[i]]   = leadMeas[i].SDashDashDuration;
			Scalar[JSTD][LeadMap[i]]    = leadMeas[i].STDuration;
			Scalar[JTO][LeadMap[i]]     = leadMeas[i].TOnset;
			Scalar[JTD][LeadMap[i]]     = leadMeas[i].TDuration;
			Scalar[JPPD][LeadMap[i]]    = leadMeas[i].PPosDuration;
			Scalar[JTPD][LeadMap[i]]    = leadMeas[i].TPosDuration;
			Scalar[JQIDD][LeadMap[i]]   = leadMeas[i].QrsIntrinsicoidDefl;
			Scalar[JPPA][LeadMap[i]]    = leadMeas[i].PPosAmplitude;
			Scalar[JPMA][LeadMap[i]]    = leadMeas[i].PNegAmplitude;
			Scalar[JQPPA][LeadMap[i]]   = leadMeas[i].PeakToPeakQrsAmplitude;
			Scalar[JR1A][LeadMap[i]]    = leadMeas[i].R1Amplitude;
			Scalar[JQA][LeadMap[i]]     = leadMeas[i].QAmplitude;
			Scalar[JRA][LeadMap[i]]     = leadMeas[i].RAmplitude;
			Scalar[JSA][LeadMap[i]]     = leadMeas[i].SAmplitude;
			Scalar[JRDA][LeadMap[i]]    = leadMeas[i].RDashAmplitude;
			Scalar[JSDA][LeadMap[i]]    = leadMeas[i].SDashAmplitude;
			Scalar[JRD2A][LeadMap[i]]   = leadMeas[i].RDashDashAmplitude;
			Scalar[JSD2A][LeadMap[i]]   = leadMeas[i].SDashDashAmplitude;
			Scalar[JSTA][LeadMap[i]]    = leadMeas[i].STAmplitude;
			Scalar[J28STA][LeadMap[i]]  = leadMeas[i].STT28Amplitude;
			Scalar[J38STA][LeadMap[i]]  = leadMeas[i].STT38Amplitude;
			Scalar[JTPA][LeadMap[i]]    = leadMeas[i].TposAmplitude;
			Scalar[JTMA][LeadMap[i]]    = leadMeas[i].TnegAmplitude;
			Scalar[JQAR][LeadMap[i]]    = leadMeas[i].QrsArea;
			Scalar[JPAR][LeadMap[i]]    = leadMeas[i].PArea;
			Scalar[JTAR][LeadMap[i]]    = leadMeas[i].TArea;
			Scalar[JPMOR][LeadMap[i]]   = leadMeas[i].PMorphology;
			Scalar[JTMOR][LeadMap[i]]   = leadMeas[i].TMorphology;
			Scalar[JRN][LeadMap[i]]     = leadMeas[i].RNotches;
			Scalar[JDC][LeadMap[i]]     = leadMeas[i].DeltaWaveConfidence;
			Scalar[JSTS][LeadMap[i]]    = leadMeas[i].STSlope;
			Scalar[JQTI][LeadMap[i]]    = leadMeas[i].QTInterval;
			Scalar[JSTM][LeadMap[i]]    = leadMeas[i].STMAmplitude;
			Scalar[JST60][LeadMap[i]]   = leadMeas[i].ST60Amplitude;
			Scalar[JSTTMID][LeadMap[i]] = leadMeas[i].STTMidAmplitude;
		}
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Constructs a string containing the measurements presented in the array
	 * parameter. Measurements that have the value {@link RestingEcg.GRI_UNDEFINED}
	 * are replaced by '***'.
	 * 
	 * @param fp	The output stream to which the measurements are to be written
	 * @param Title	The row title, to be printed at the start of the string.
	 * @param MeasArray	The array of measurements to be printed.
	 * @param NumVals	The number of measurements in the array.
	 */
	private void PrintMeasRow(OutputStream fp, String Title, short [] MeasArray, short NumVals )
	{
		short	i;
		String FormattedMeas;

		FormattedMeas = String.format("%s  ", Title);
		
		for ( i = 0; i < NumVals; i++ )
		{
			if ( MeasArray[i] == RestingEcg.GRI_UNDEFINED )
			{
				FormattedMeas = FormattedMeas + String.format("  ***  ");
			}
			else
			{
				FormattedMeas = FormattedMeas + String.format("%5d  ", MeasArray[i]);
			}
		}

		Log.d( TAG, FormattedMeas );
	}

	// --------------------------------------------------------------------------

	/**
	 * Prints the contents of the 2-d scalar measurement matrix to the 
	 * system screen.
	 */
	public void ListScalarMeas()
	{
		String OutputText;
		short MatrixRowNum, MatrixColNum;

		//System.out.println( ">> SCALAR MATRIX\n" );
		Log.d( TAG, ">> SCALAR MATRIX" );
		for ( MatrixRowNum = 0; MatrixRowNum < GRI_MAX_MATRIX_ROWS; MatrixRowNum++ )
		{
			OutputText = "";
			for (MatrixColNum = 0; MatrixColNum < RestingEcg.GRI_MAX_ECG_LEADS; MatrixColNum++ )
			{
				OutputText = OutputText + String.format("%7d", Scalar[MatrixRowNum][MatrixColNum]);
				if ( MatrixColNum == (RestingEcg.GRI_MAX_ECG_LEADS-1) )
				{
					OutputText=OutputText + '\n';
				}
				else
				{
					OutputText=OutputText + '\t';
				}
			}
			//System.out.println(OutputText);
			Log.d( TAG, OutputText );
		}
	}

	// --------------------------------------------------------------------------

	/**
	 * Writes the complete scalar measurement matrix to the given file.
	 * Undefined measurements are printed as they are i.e. there is no
	 * substitution of values GRI_UNDEFINED with '***'.
	 *
	 * @param FileName	The file to which the scalar measurements are to be
	 * written.
	 * @param NumberOfLeads	The number of leads processed by the analysis. This
	 * will determine the numbe of columns of measurements that will be printed.
	 */
	public void saveScalarToTextFile(String FileName, short NumberOfLeads )
	{
		String OutputText;
		short MatrixRowNum, MatrixColNum;

		Log.d( TAG,  "Saving matrix to file " + FileName);
		
        try
        {

	       	File myFile = new File( FileName );
	       	myFile.createNewFile();
	       	FileOutputStream fOut = new FileOutputStream(myFile);
	
	       	OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
	       	
			for ( MatrixRowNum = 0; MatrixRowNum < GRI_USED_MATRIX_ROWS; MatrixRowNum++ )
			{
				OutputText = "";
				for ( MatrixColNum = 0; MatrixColNum < NumberOfLeads; MatrixColNum++ )
				{
					OutputText = OutputText + String.format("%7d", Scalar[MatrixRowNum][MatrixColNum]);
					if ( MatrixColNum == (NumberOfLeads-1) )
					{
						OutputText=OutputText + '\n';
					}
					else
					{
						OutputText=OutputText + '\t';
					}
				}
	     		myOutWriter.append( OutputText );
			}
	       	
			myOutWriter.close();
			fOut.close();
  	 
	    } 
		catch (java.io.IOException e)
		{

		    //do something if an IOException occurs.
			//Toast.makeText(this,"Sorry Text could't be added",Toast.LENGTH_LONG).show();
			Log.e( TAG,  "Problem saving matrix to file");

	    }

	}

	// --------------------------------------------------------------------------

	/**
	 * Prints out the contents of the 2-d vector measurement matrix. The raw
	 * values stored within the matrix are printed.
	 *
	 */
	public void ListVectorMeas()
	{
		String OutputText;
		short MatrixRowNum, MatrixColNum;


 		Log.d( TAG, ">> VECTOR MATRIX" );
		for ( MatrixRowNum = 0; MatrixRowNum < GRI_MAX_VECTMATRIX_ROWS; MatrixRowNum++ )
		{
			OutputText = "";
			for ( MatrixColNum = 0; MatrixColNum < GRI_MAX_VECTMATRIX_COLS; MatrixColNum++ )
			{
				OutputText = OutputText + String.format("%7d", Vector[MatrixRowNum][MatrixColNum]);
				if ( MatrixColNum == (GRI_MAX_VECTMATRIX_COLS-1))
				{
					OutputText=OutputText + '\n';
				}
				else
				{
					OutputText=OutputText + '\t';
				}
			}
			//System.out.println( OutputText );
			Log.d( TAG, OutputText );
		}
	}

	// --------------------------------------------------------------------------

	/**
	 * Writes the complete vector measurement matrix to the given file.
	 * Undefined measurements are printed as they are i.e. there is no
	 * substitution of values GRI_UNDEFINED with '***'.
	 *
	 * @param FileName	The file to which the vector measurements are to be
	 * written.
	 */
	public void saveVectorToTextFile( String FileName )
	{
		String OutputText;
		short MatrixRowNum, MatrixColNum;


        try
        {

	       	File myFile = new File( FileName );
	       	myFile.createNewFile();
	       	FileOutputStream fOut = new FileOutputStream(myFile);

	       	OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

	       	//System.out.println( ">> VECTOR MATRIX\n" );
			for ( MatrixRowNum = 0; MatrixRowNum < GRI_MAX_VECTMATRIX_ROWS; MatrixRowNum++ )
			{
				OutputText = "";
				for ( MatrixColNum = 0; MatrixColNum < GRI_MAX_VECTMATRIX_COLS; MatrixColNum++ )
				{
					OutputText = OutputText + String.format("%7d", Vector[MatrixRowNum][MatrixColNum]);
					if ( MatrixColNum == (GRI_MAX_VECTMATRIX_COLS-1))
					{
						OutputText=OutputText + '\n';
					}
					else
					{
						OutputText=OutputText + '\t';
					}
				}
	     		myOutWriter.append( OutputText );
				//System.out.println( OutputText );
			}

			myOutWriter.close();
			fOut.close();

	    }
		catch (java.io.IOException e)
		{

		    //do something if an IOException occurs.
			//Toast.makeText(this,"Sorry Text could't be added",Toast.LENGTH_LONG).show();
			Log.e( TAG,  "Problem saving vector matrix to file");

	    }

	}

	// --------------------------------------------------------------------------

	private String getMeasHead( ) // ControlParams Flags )
	{
		String FormattedMeas;

		FormattedMeas = "               I     II    III    aVR    aVL    aVF     V1     V2  V3/V4R    V4     V5     V6";

		// Currently no 15-lead option
		//if ( Flags & GRI_FLAG_15LEADPROCESSING )
		//{
		//	if ( Flags & GRI_FLAG_DERIVEDXYZ )
		//		fprintf( fpOut, "      X      Y      Z" );
		//	else
		//		fprintf( fpOut, "   ADD1   ADD2   ADD3" );
		//}
		FormattedMeas = FormattedMeas + "\n";

		return FormattedMeas;
	}

	// --------------------------------------------------------------------------

	private String getMeasRow(String Label, short [] MeasArray, short NumVals )
	{
		short	i;
		String FormattedMeas;

		FormattedMeas = String.format("%s  ", Label);

		for ( i = 0; i < NumVals; i++ )
		{
			if ( MeasArray[i] == RestingEcg.GRI_UNDEFINED )
			{
				FormattedMeas = FormattedMeas + String.format("  ***  ");
			}
			else
			{
				FormattedMeas = FormattedMeas + String.format("%5d  ", MeasArray[i]);
			}
		}
		FormattedMeas = FormattedMeas + "\n";

		return FormattedMeas;

	}

	// --------------------------------------------------------------------------

	/**
	 * Prints the individual scalar lead measurements to the given output
	 * stream.
	 *
	 * @param myOutWriter	The opened stream to which the measurements are to be
	 * written.
 	 * @param NumberOfLeads	The number of leads processed by the analysis. This
	 * will determine the numbe of columns of measurements that will be printed.
	 */
	private void printScalarLeadsToTextFile(OutputStreamWriter myOutWriter, short NumberOfLeads )
	{
        try
        {

	 		myOutWriter.append( "Individual Lead Measurements\n" );
	 		myOutWriter.append( "============================\n" );
	 		myOutWriter.append( "\n" );

	 		myOutWriter.append( getMeasHead() );
			myOutWriter.append( getMeasRow( "P Ons    ", Scalar[JPO], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "P Dur    ", Scalar[JPD], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "QRS Ons  ", Scalar[JQO], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "QRS Dur  ", Scalar[JQRSD], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "ST80 Amp ", Scalar[JST80], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "Q Dur    ", Scalar[JQD], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "R Dur    ", Scalar[JRD], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "S Dur    ", Scalar[JSD], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "R' Dur   ", Scalar[JRDD], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "S' Dur   ", Scalar[JSDD], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "R'' Dur  ", Scalar[JRD2D], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "S'' Dur  ", Scalar[JSD2D], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "ST Dur   ", Scalar[JSTD], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "T Ons    ", Scalar[JTO], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "T Dur    ", Scalar[JTD], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "P+ Dur   ", Scalar[JPPD], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "T+ Dur   ", Scalar[JTPD], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "QRS IntD ", Scalar[JQIDD], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "P+ Amp   ", Scalar[JPPA], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "P- Amp   ", Scalar[JPMA], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "P2P Amp  ", Scalar[JQPPA], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "R1 Amp   ", Scalar[JR1A], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "Q Amp    ", Scalar[JQA], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "R Amp    ", Scalar[JRA], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "S Amp    ", Scalar[JSA], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "R' Amp   ", Scalar[JRDA], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "S' Amp   ", Scalar[JSDA], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "R'' Amp  ", Scalar[JRD2A], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "S'' Amp  ", Scalar[JSD2A], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "ST Amp   ", Scalar[JSTA], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "STT28 Amp", Scalar[J28STA], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "STT38 Amp", Scalar[J38STA], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "T+ Amp   ", Scalar[JTPA], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "T- Amp   ", Scalar[JTMA], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "QRS Area ", Scalar[JQAR], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "P Area   ", Scalar[JPAR], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "T Area   ", Scalar[JTAR], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "P Morph  ", Scalar[JPMOR], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "T Morph  ", Scalar[JTMOR], NumberOfLeads) );

			myOutWriter.append( getMeasRow( "R Notch  ", Scalar[JRN], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "DeltaConf", Scalar[JDC], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "ST Slope ", Scalar[JSTS], NumberOfLeads) );
			myOutWriter.append( getMeasRow( "QT Int   ", Scalar[JQTI], NumberOfLeads) );

        }
		catch (java.io.IOException e)
		{

		    //do something if an IOException occurs.
			//Toast.makeText(this,"Sorry Text could't be added",Toast.LENGTH_LONG).show();
			Log.e( TAG,  "Problem writing scalar lead measurements to file");

	    }

	}

	// --------------------------------------------------------------------------

	public void printToOutputStream(OutputStreamWriter outputStream, ControlInfo controlFlags, short NumberOfLeads )
	{
    	printScalarLeadsToTextFile( outputStream, NumberOfLeads );

	}

	// --------------------------------------------------------------------------

	/**
	 * Prints the list of vector measurement and values to the given file.
	 *
	 * @param	FileName	The name of the file to which the formatted measurements are written.
	 * @param ControlFlags	The control flags used to determine which QTc formula
	 * is selected.
 	 * @param NumberOfLeads	The number of leads processed by the analysis. This
	 * will determine the numbe of columns of measurements that will be printed.

	 */
	public void printToTextFile(String FileName, ControlInfo ControlFlags, short NumberOfLeads )
	{

	     try
	     {

	    	File myFile = new File( FileName );
	    	myFile.createNewFile();
	    	FileOutputStream fOut = new FileOutputStream(myFile);

	    	OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

	    	// Print build details

	    	// Print patient info


	    	// Print individual lead measurements
	    	printScalarLeadsToTextFile( myOutWriter, NumberOfLeads );
	 		myOutWriter.append( "\n\n" );


	 		// Print global scalar measurements
	    	myOutWriter.append( "Global Measurements\n" );
			myOutWriter.append( "===================\n" );
			myOutWriter.append( "\n" );

			myOutWriter.append( String.format( "QrsFrontalAxis       %5d   PFrontalAxis %5d\nSTFrontalAxis        %5d   TFrontalAxis %5d\n",
				globalMeas.QrsFrontalAxis,
				globalMeas.PFrontalAxis,
				globalMeas.STFrontalAxis,
				globalMeas.TFrontalAxis ) );


			myOutWriter.append( String.format( "Display Heart Rate   %5d\n", globalMeas.HeartRate ) );
			myOutWriter.append( String.format( "Sinus Rate           %5d   Average RR   %5d\n",globalMeas.SinusRate, globalMeas.SinusAverageRR ) );
			myOutWriter.append( String.format( "Ventricular Rate     %5d   Average RR   %5d\n",globalMeas.VentRate, globalMeas.VentAverageRR ) );

			myOutWriter.append( String.format( "Heart Rate Variability                    %5d\n", globalMeas.HeartRateVariability ) );
			myOutWriter.append( String.format( "StdDev Normal RR Intervals                %5d\n", globalMeas.StdDevNormRRIntervals ) );

			myOutWriter.append( String.format( "QRS Pseudo Vector 4/8 Spatial Velocity    %5d\n", globalMeas.QrsPsdoVec48SpatVel ) );
			myOutWriter.append( String.format( "QRS Pseudo Vector 5/8 Spatial Velocity    %5d\n", globalMeas.QrsPsdoVec58SpatVel ) );
			myOutWriter.append( String.format( "QRS Pseudo Vector 6/8 Spatial Velocity    %5d\n", globalMeas.QrsPsdoVec68SpatVel ) );
			myOutWriter.append( String.format( "QRS Pseudo Vector 7/8 Spatial Velocity    %5d\n", globalMeas.QrsPsdoVec78SpatVel ) );
			myOutWriter.append( String.format( "QRS Pseudo Vector Maximum Amplitude       %5d\n", globalMeas.QrsPsdoVecMaxAmpl ) );


			myOutWriter.append( "\n" );

			this.printVectorToTextFile( myOutWriter );
			myOutWriter.append( "\n\n" );




			myOutWriter.append( String.format( "LVH Score            %5d   LV Strain    %5d\n", globalMeas.LVHscore, globalMeas.LVstrain ) );
			myOutWriter.append( String.format( "ST Duration          %5d\n", globalMeas.OverallSTDuration ) );
	//    		if ( Flags & GRI_FLAG_15LEADPROCESSING )
	//    			if ( Flags & GRI_FLAG_DERIVEDXYZ )
	//    				fprintf( fpOut, "QRST spatial angle from XYZ      %5d\n", pMatrix->Vector.Vstr.TAngles[19] );
			myOutWriter.append( String.format( "PR Interval          %5d\n", globalMeas.OverallPRInterval ) );
			myOutWriter.append( String.format( "QT Interval          %5d\n", globalMeas.OverallQTInterval ) );

			myOutWriter.append( String.format( "QT Dispersion        %5d\n", globalMeas.QTDispersion ) );
			myOutWriter.append( String.format( "QT Corrected (used)  %5d", globalMeas.QTc ) );
			//if ( ( Flags & GRI_FLAG_QTC_BAZETT ) == GRI_FLAG_QTC_BAZETT )
			if ( ControlFlags.isQTcBazett() )
				myOutWriter.append( String.format( " [BAZETT]\n" ) );
			//else if ( ( Flags & GRI_FLAG_QTC_FRIDERICIA ) == GRI_FLAG_QTC_FRIDERICIA )
	   		else if ( ControlFlags.isQTcFridericia() )
				myOutWriter.append( String.format( " [FRIDERICIA]\n" ) );
			//else if ( ( Flags & GRI_FLAG_QTC_FRAMINGHAM ) == GRI_FLAG_QTC_FRAMINGHAM )
	   		else if ( ControlFlags.isQTcFramingham() )
				myOutWriter.append( String.format( " [FRAMINGHAM]\n" ) );
			else
				myOutWriter.append( String.format( " [HODGE]\n" ) );
			myOutWriter.append( String.format( "P Terminal (V1)      %5d\n", globalMeas.PTerminalForceInV1 ) );
			myOutWriter.append( String.format( "\n" ) );
			myOutWriter.append( String.format( "QTc Measurements:\n" ) );
			myOutWriter.append( String.format( "    Hodge            %5d\n", globalMeas.QTcHodge ) );
			myOutWriter.append( String.format( "    Bazett           %5d\n", globalMeas.QTcBazett ) );
			myOutWriter.append( String.format( "    Fridericia       %5d\n", globalMeas.QTcFridericia ) );
			myOutWriter.append( String.format( "    Framingham       %5d\n", globalMeas.QTcFramingham ) );
			myOutWriter.append( String.format( "\n" ) );
			myOutWriter.append( String.format( "Flags        %d\n", globalMeas.Flags ) );
			myOutWriter.append( String.format( "  - P waves found           %s\n", ((globalMeas.Flags & 0x0001) == 0x0001) ? "Yes" : "No" ) );
			myOutWriter.append( String.format( "  - Indeterminate QRS axis  %s\n", ((globalMeas.Flags & 0x0002) == 0x0002) ? "Yes" : "No" ) );
			myOutWriter.append( String.format( "  - Default gender used     %s\n", ((globalMeas.Flags & 0x0004) == 0x0004) ? "Yes" : "No" ) );
			myOutWriter.append( String.format( "  - Default race used       %s\n", ((globalMeas.Flags & 0x0008) == 0x0008) ? "Yes" : "No" ) );
			myOutWriter.append( String.format( "  - Default age used        %s\n", ((globalMeas.Flags & 0x0010) == 0x0010) ? "Yes" : "No" ) );

			myOutWriter.append( String.format( "\n\n" ) );
			myOutWriter.append( String.format( "Overall Onsets, Terminations and Durations:\n" ) );
			myOutWriter.append( String.format( "------------------------------------------\n" ) );
			myOutWriter.append( String.format( "\n" ) );
			myOutWriter.append( String.format( "           Onset       Termination    Duration\n" ) );
			myOutWriter.append( String.format( " P        %5d          %5d         %5d\n",
				globalMeas.OverallPOnset,
				globalMeas.OverallPTermination,
				globalMeas.OverallPDuration ) );
			myOutWriter.append( String.format( " QRS      %5d          %5d         %5d\n",
				globalMeas.OverallQrsOnset,
				globalMeas.OverallQrsTermination,
				globalMeas.OverallQrsDuration ) );
			myOutWriter.append( String.format( " T        %5d          %5d         %5d\n",
				globalMeas.OverallTOnset,
				globalMeas.OverallTTermination,
				globalMeas.OverallTDuration ) );

			myOutWriter.append( String.format( "\n" ) );

			myOutWriter.close();
			fOut.close();

	    }
		catch (java.io.IOException e)
		{

		    //do something if an IOException occurs.
			//Toast.makeText(this,"Sorry Text could't be added",Toast.LENGTH_LONG).show();
			Log.e( TAG,  "Problem saving matrix to file");

	    }
	}

	// --------------------------------------------------------------------------

	/**
	 * Creates a string containing the headings for a table containing the list
	 * of vector measurements.
	 *
	 * @return	A string containing headings for vector measurements for use in a table.
	 */
	private String GetVectMeasHead( )
	{
		String FormattedMeas;

		FormattedMeas = "              Max P Mx QRS  Max T Max PF .03 QRS -.03 QRS 3/8    4/8    5/8    6/8 QRS(12L) T(12L)   12     13     14     15     16     17     18  SPATIAL";
		FormattedMeas = FormattedMeas + "\n";

		return FormattedMeas;
	}

	// --------------------------------------------------------------------------

	/**
	 * Prints the list of vector measurement and values to the designated
	 * OutputStreamWriter.
	 *
	 * @param	myOutWriter	The opened stream to which the vector measurements are to be printed.
	 */
	private void printVectorToTextFile( OutputStreamWriter myOutWriter )
	{
         try
         {
     		myOutWriter.append( "Vector Measurements\n" );
     		myOutWriter.append( "===================\n" );
     		myOutWriter.append( "\n" );

     		myOutWriter.append( GetVectMeasHead() );
        	myOutWriter.append( getMeasRow( "Angles (F)   ", Vector[0], GRI_MAX_VECTMATRIX_COLS) );
    		myOutWriter.append( getMeasRow( "Lengths(F)   ", Vector[1], GRI_MAX_VECTMATRIX_COLS) );
    		myOutWriter.append( getMeasRow( "Angles (RS)  ", Vector[2], GRI_MAX_VECTMATRIX_COLS) );
    		myOutWriter.append( getMeasRow( "Lengths(RS)  ", Vector[3], GRI_MAX_VECTMATRIX_COLS) );
    		myOutWriter.append( getMeasRow( "Angles (T)   ", Vector[4], GRI_MAX_VECTMATRIX_COLS) );
    		myOutWriter.append( getMeasRow( "Lengths(T)   ", Vector[5], GRI_MAX_VECTMATRIX_COLS) );
    		myOutWriter.append( getMeasRow( "Magnitude    ", Vector[6], GRI_MAX_VECTMATRIX_COLS) );
    		myOutWriter.append( getMeasRow( "Position     ", Vector[7], GRI_MAX_VECTMATRIX_COLS) );
        }
		catch (java.io.IOException e)
		{
		    //do something if an IOException occurs.
			Log.e( TAG,  "Problem printing Vector measurements to file");
	    }		
	}


	// ----------------------------------------------------------------------
	
	/**
	 * Determines if the P wave measurements are reliable which indicates
	 * whether or not the P wave measurements should be displayed on the
	 * ECG report.
	 * 
	 * In the event of an ECG exhibiting a rhythm where P wave measurement
	 * becomes unreliable, this function will indicate if the P wave
	 * measurements should be displayed or not.
	 * 
	 * @return	TRUE if the P wave measurements are reliable and can
	 * be displayed, otherwise returns FALSE.
	 */
	public boolean isPWaveMeasurementReliable()
	{
		if ( ( this.Scalar[JAI][JFLAG] & GRI_PWAVEFLAG ) == GRI_PWAVEFLAG )
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
	 * Determines if the QRS axis measurement is reliable which indicates 
	 * whether or not this measurement should be displayed on the ECG
	 * report.
	 * 
	 * In the event that the QRS axis becomes indeterminate, a flag will
	 * be set within the measurement matrix to indicate that the QRS axis
	 * should not be displayed.
	 * 
	 * @return	TRUE if the QRS axis measurement is reliable and can
	 * be displayed, otherwise returns FALSE.
	 */
	public boolean isQRSAxisReliable()
	{
		if ( ( this.Scalar[JAI][JFLAG] & GRI_QRSFLAG ) == GRI_QRSFLAG )
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	// ----------------------------------------------------------------------

	/**
	 * Determines if a default value for the patient's age was used by the
	 * analysis.
	 * 
	 * @return	TRUE if a default value for age was used, otherwise
	 * returns FALSE.
	 */
	public boolean isDefaultAgeUsed()
	{
		if ( ( this.Scalar[JAI][JFLAG] & GRI_DEFAULTAGEUSED ) == GRI_DEFAULTAGEUSED )
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
	 * Determines if a default value for the patient's gender was used by the
	 * analysis.
	 * 
	 * @return	TRUE if a default value for gender was used, otherwise
	 * returns FALSE.
	 */
	public boolean isDefaultGenderUsed()
	{
		if ( ( this.Scalar[JAI][JFLAG] & GRI_DEFAULTGENDERUSED ) == GRI_DEFAULTGENDERUSED )
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
	 * Determines if a default value for the patient's race was used by the
	 * analysis.
	 * 
	 * @return	TRUE if a default value for race was used, otherwise
	 * returns FALSE.
	 */
	public boolean isDefaultRaceUsed()
	{
		if ( ( this.Scalar[JAI][JFLAG] & GRI_DEFAULTRACEUSED ) == GRI_DEFAULTRACEUSED )
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
	 * Converts the 2-dimensional vector measurement array returned from the
	 * C ECG analysis library to a structured VectMeas instance. 
	 */
	public void VectorToVectMeas()
	{
		this.vectorMeas.MaximumQRSSpatialVelocity_3Lead  = this.Vector[JMQVR][JAV];
		this.vectorMeas.MaximumQRSSpatialVelocity_12Lead = this.Vector[JMQWR][JAV];
		this.vectorMeas.MaximumTSpatialVelocity_3Lead    = this.Vector[JMTVR][JAV];
		this.vectorMeas.MaximumTSpatialVelocity_12Lead   = this.Vector[JMTWR][JAV];
		this.vectorMeas.MaximumQRS_TSpatialAngle_3Lead   = this.Vector[JQTSAV][JAV];
		this.vectorMeas.MaximumQRS_TSpatialAngle_12Lead  = this.Vector[JQTSAW][JAV];
		

		this.vectorMeas.STTTimeNormalised_X.ST_T_18      = this.Vector[J18][JSTXA];
		this.vectorMeas.STTTimeNormalised_X.ST_T_28      = this.Vector[J28][JSTXA];
		this.vectorMeas.STTTimeNormalised_X.ST_T_38      = this.Vector[J38][JSTXA];
		this.vectorMeas.STTTimeNormalised_X.ST_T_48      = this.Vector[J48][JSTXA];
		this.vectorMeas.STTTimeNormalised_X.ST_T_58      = this.Vector[J58][JSTXA];
		this.vectorMeas.STTTimeNormalised_X.ST_T_68      = this.Vector[J68][JSTXA];
		this.vectorMeas.STTTimeNormalised_X.ST_T_78      = this.Vector[J78][JSTXA];

		this.vectorMeas.STTTimeNormalised_Y.ST_T_18      = this.Vector[J18][JSTYA];
		this.vectorMeas.STTTimeNormalised_Y.ST_T_28      = this.Vector[J28][JSTYA];
		this.vectorMeas.STTTimeNormalised_Y.ST_T_38      = this.Vector[J38][JSTYA];
		this.vectorMeas.STTTimeNormalised_Y.ST_T_48      = this.Vector[J48][JSTYA];
		this.vectorMeas.STTTimeNormalised_Y.ST_T_58      = this.Vector[J58][JSTYA];
		this.vectorMeas.STTTimeNormalised_Y.ST_T_68      = this.Vector[J68][JSTYA];
		this.vectorMeas.STTTimeNormalised_Y.ST_T_78      = this.Vector[J78][JSTYA];
	
		this.vectorMeas.STTTimeNormalised_Z.ST_T_18      = this.Vector[J18][JSTZA];
		this.vectorMeas.STTTimeNormalised_Z.ST_T_28      = this.Vector[J28][JSTZA];
		this.vectorMeas.STTTimeNormalised_Z.ST_T_38      = this.Vector[J38][JSTZA];
		this.vectorMeas.STTTimeNormalised_Z.ST_T_48      = this.Vector[J48][JSTZA];
		this.vectorMeas.STTTimeNormalised_Z.ST_T_58      = this.Vector[J58][JSTZA];
		this.vectorMeas.STTTimeNormalised_Z.ST_T_68      = this.Vector[J68][JSTZA];
		this.vectorMeas.STTTimeNormalised_Z.ST_T_78      = this.Vector[J78][JSTZA];
		
		//this.vectorMeas.Angles_Frontal.maximumP_3lead        = this.Vector[JFAV][JMPV];
		//this.vectorMeas.Angles_Frontal.maximumQRS_3lead      = this.Vector[JFAV][JMQV];
		//this.vectorMeas.Angles_Frontal.maximumT_3lead        = this.Vector[JFAV][JMTV];
		//this.vectorMeas.Angles_Frontal.maximumP_FrontalPlane = this.Vector[JFAV][JMPPV];
		//this.vectorMeas.Angles_Frontal.QRSplus_0_03          = this.Vector[JFAV][JP03QV];
		//this.vectorMeas.Angles_Frontal.QRSminus_0_03         = this.Vector[JFAV][JM03QV];
		//this.vectorMeas.Angles_Frontal.QRS38                 = this.Vector[JFAV][J38QV];
		//this.vectorMeas.Angles_Frontal.QRS48                 = this.Vector[JFAV][J48QV];
		//this.vectorMeas.Angles_Frontal.QRS58                 = this.Vector[JFAV][J58QV];
		//this.vectorMeas.Angles_Frontal.STT68                 = this.Vector[JFAV][J68STV];
		//this.vectorMeas.Angles_Frontal.maximumQRS_12lead     = this.Vector[JFAV][JMQWV];
		//this.vectorMeas.Angles_Frontal.maximumT_12lead       = this.Vector[JFAV][JMTWV];
		
		this.vectorMeas.Angles_Frontal.setMeasurements( this.Vector[JFAV] );
		this.vectorMeas.Lengths_Frontal.setMeasurements( this.Vector[JFLV] );
		this.vectorMeas.Angles_RightSagittal.setMeasurements( this.Vector[JRSAV] );
		this.vectorMeas.Lengths_RightSagittal.setMeasurements( this.Vector[JRSLV] );
		this.vectorMeas.Angles_Transverse.setMeasurements( this.Vector[JTAV] );
		this.vectorMeas.Lengths_Transverse.setMeasurements( this.Vector[JTLV] );
		this.vectorMeas.Magnitude.setMeasurements( this.Vector[JMV] );
		this.vectorMeas.Positions.setMeasurements( this.Vector[JPV] );
	
	}
	
	// ----------------------------------------------------------------------

	/**
	 * Resets all measurement matrix values to {@link RestingEcg#GRI_UNDEFINED}.
	 * This is performed on each of the {@link LeadMeas} instances as well as
	 * the {@link GlobalMeas} and {@link VectorMeas} instances and internal
	 * 2-D arrays that are populated by the C ECG analysis library through
	 * the JNI.
	 */
	public void clearMeasurements()
	{
		int	MatrixRowCount, MatrixColCount, MatrixRowNum, MatrixColNum;
		int	i;
		

		// Set everything in the Scalar matrix to GRI_UNDEFINED
		
		MatrixRowCount = GRI_MAX_MATRIX_ROWS;
		MatrixColCount = RestingEcg.GRI_MAX_ECG_LEADS;
			
		for ( MatrixRowNum = 0; MatrixRowNum < MatrixRowCount; MatrixRowNum++ )
		{
			for ( MatrixColNum = 0; MatrixColNum < MatrixColCount; MatrixColNum++ )
			{
				this.Scalar[MatrixRowNum][MatrixColNum] = RestingEcg.GRI_UNDEFINED;
			}
		}
		
		
		// Set everything in the Vector matrix to GRI_UNDEFINED
		
		for ( MatrixRowNum = 0; MatrixRowNum < GRI_MAX_VECTMATRIX_ROWS; MatrixRowNum++ )
		{
			for ( MatrixColNum = 0; MatrixColNum < GRI_MAX_VECTMATRIX_COLS; MatrixColNum++ )
			{
				this.Vector[MatrixRowNum][MatrixColNum] = RestingEcg.GRI_UNDEFINED;
			}
		}
		
		// Do the same from the break-out values in structures classes
		this.globalMeas.clearMeasurements();
		for (i = 0; i < RestingEcg.GRI_MAX_ECG_LEADS; i++ )
		{
			this.leadMeas[i].clearMeasurements();
		}
		
		this.vectorMeas.clearMeasurements();
		
		
	}
	
	// ----------------------------------------------------------------------
	
	public void printContent()
	{
		short	i;
		
		for (i = 0; i < RestingEcg.GRI_MAX_ECG_LEADS; i++ )
		{
			this.leadMeas[i].printContent();
		}
	}
};
