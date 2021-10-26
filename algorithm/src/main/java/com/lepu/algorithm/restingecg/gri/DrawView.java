package com.lepu.algorithm.restingecg.gri;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * For development and testing only.
 * 
 * @author Brian Devine
 *
 */
public class DrawView extends View {
	// --------------------------------------------------------------------------	
	private static final String TAG = "MyActivity-DrawView";
	// --------------------------------------------------------------------------	

	private int ecgHeavyGraticule, ecgLightGraticule;
	private int ecgGraticuleThickness;

	private int minorGratPerMajorGrat;
	private int graticuleBorder;
	private int graticuleLineSeparation;

    GriBuildInfo buildInfo = new GriBuildInfo();
	
	Paint paint;// = new Paint();

	int screenWidth,screenHeight;


    // ----------------------------------------------------------------------

	public DrawView(Context context)
    {
        super(context);
        
        paint = new Paint();
        
        paint.setColor(Color.BLACK);
    	Log.d(TAG,"DrawView()");
    	
    	ecgHeavyGraticule = Color.RED;
    	ecgLightGraticule = Color.rgb(255, 180, 180);	// Color.rgb(255, 150, 150);
    	ecgGraticuleThickness = 2;

    	minorGratPerMajorGrat = 5;
    	graticuleBorder = 30;
    	graticuleLineSeparation = 10;

    }

    // ----------------------------------------------------------------------

    /**
     * Draws a single 5mm x 5mm ECG graticule box
     * @param canvas	Drawing canvas
     * @param x	x co-ordinate of start of major graticule
     * @param y	y co-ordinate of start of major graticule
     * @param Closed	Boolean flag to indicate if the graticule box should
     * be completely enclosed by major graticule lines.
     */
    private void DrawGraticuleBox(Canvas canvas, int x, int y, boolean Closed )
    {
    	int	i;

    	
    	for ( i = 0; i < minorGratPerMajorGrat; i++ )
    	{
        	if ( i == 0 )
        		paint.setColor(ecgHeavyGraticule);
        	else
        		paint.setColor(ecgLightGraticule);
        	
        	canvas.drawLine( x, y+(i*graticuleLineSeparation), x+ minorGratPerMajorGrat*graticuleLineSeparation, y+(i*graticuleLineSeparation), paint);
        	canvas.drawLine( x+(i*graticuleLineSeparation), y, x+(i*graticuleLineSeparation), y+(minorGratPerMajorGrat*graticuleLineSeparation), paint);
    	}

    	if ( Closed )
    	{
    		paint.setColor(ecgHeavyGraticule);
        	canvas.drawLine( x,  y+(minorGratPerMajorGrat*graticuleLineSeparation), x+ minorGratPerMajorGrat*graticuleLineSeparation, y+(minorGratPerMajorGrat*graticuleLineSeparation), paint);
        	canvas.drawLine( x+(minorGratPerMajorGrat*graticuleLineSeparation),  y, x+(minorGratPerMajorGrat*graticuleLineSeparation), y+(minorGratPerMajorGrat*graticuleLineSeparation), paint);
    	}
    }

   

    // ----------------------------------------------------------------------

    /**
     * Draws the ECG graticule within the confines of the available display
     * dimensions.
     * @param canvas	Drawing canvas
     * @param scrwidth	Width of display in pixels
     * @param scrheight	Height of display in pixels
     */
    private void DrawGraticule(Canvas canvas, int scrwidth, int scrheight )
    {
    	int xBoxes, yBoxes;
    	boolean	CloseBox;


    	// Work out how many large graticule boxes to draw
    	xBoxes = (( scrwidth - 2 * graticuleBorder ) / ( minorGratPerMajorGrat * graticuleLineSeparation ));
    	yBoxes = (( scrwidth - 2 * graticuleBorder ) / ( minorGratPerMajorGrat * graticuleLineSeparation ));
    	
    	// Draw them
    	for ( int i = 0; i < xBoxes; i++ )
    	{
    		for ( int j = 0; j < yBoxes; j++ )
    		{
    			if ( i == xBoxes-1 || j == yBoxes-1 )
    			{
    				CloseBox = true;
    			}
    			else
    			{
    				CloseBox = false;
    			}

    			DrawGraticuleBox( canvas, graticuleBorder+i*minorGratPerMajorGrat*graticuleLineSeparation, graticuleBorder+j*minorGratPerMajorGrat*graticuleLineSeparation, CloseBox );
    		}
    	}
    	
    }
  
    // ----------------------------------------------------------------------

    @Override
    public void onDraw(Canvas canvas) {
        int	textSize = 36;
        int	lineCount = 0;
        int	marginX = 50, marginY = 50;
        
        //Display display = getWindowManager().getDefaultDisplay();
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();


        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

  	
    	DisplayMetrics metrics = new DisplayMetrics();
    	((StartDraw) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
    	screenWidth = metrics.widthPixels;
    	screenHeight = metrics.heightPixels;

    	Log.d( TAG, "y Height of display (pixels)  : " + y );
    	Log.d( TAG, "x Width of display (pixels)   : " + x );
    	Log.d( TAG, "Height of display (pixels)  : " + metrics.heightPixels );
    	Log.d( TAG, "Width of display (pixels)   : " + metrics.widthPixels );
    	
        buildInfo.getBuildInfo();
        
        ecgGraticuleThickness = 2;    	
    	paint.setStrokeWidth(ecgGraticuleThickness);

    	canvas.save();

    	screenWidth = metrics.widthPixels;
    	screenHeight = metrics.heightPixels;

    	DrawGraticule(canvas, x, y);
    	
    	canvas.restore();
   	
    	  
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        paint.setTextSize(textSize);
        canvas.drawText("Build Information:", marginX, marginY+(lineCount++)*textSize, paint);
        canvas.drawText("   Vendor: " + buildInfo.getVendor(), marginX, marginY+(lineCount++)*textSize, paint);
        canvas.drawText("   Version: " + buildInfo.getVersion(), marginX, marginY+(lineCount++)*textSize, paint);
        canvas.drawText("   Type: " + buildInfo.getType(), marginX, marginY+(lineCount++)*textSize, paint);
        canvas.drawText("   Date: " + buildInfo.getDate(), marginX, marginY+(lineCount++)*textSize, paint);
        canvas.drawText("   Time: " + buildInfo.getTime(), marginX, marginY+(lineCount++)*textSize, paint);
        canvas.drawText("   Options: " + buildInfo.getOptions(), marginX, marginY+(lineCount++)*textSize, paint);
        canvas.drawText("   ScrWid : " + Integer.toString(screenWidth), marginX, marginY +(lineCount++)*textSize, paint );
        canvas.drawText("   ScrHgt : " + Integer.toString(screenHeight), marginX, marginY +(lineCount++)*textSize, paint );

    }

}
