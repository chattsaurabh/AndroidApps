package com.dev.backend;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;

import com.dev.constants.LiveWallManagerConstants;

public class ImageCompresser implements LiveWallManagerConstants{

//	private String m_imgName = null;
	
	public void setImageName(String a_name) {
//		m_imgName = a_name;
	}
	
	public Bitmap extractBmp(Uri a_uri, int a_width, int a_height) {
		Bitmap l_bmp = null;
		String l_path = a_uri.toString();
		
		try
		{
		    int inWidth = 0;
		    int inHeight = 0;

		    InputStream in = new FileInputStream(l_path);

		    // decode image size (decode metadata only, not the whole image)
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(in, null, options);
		    in.close();
		    in = null;

		    // save width and height
		    inWidth = options.outWidth;
		    inHeight = options.outHeight;

		    // decode full image pre-resized
		    in = new FileInputStream(l_path);
		    options = new BitmapFactory.Options();
		    // calc rought re-size (this is no exact resize)
		    options.inSampleSize = Math.max(inWidth/a_width, inHeight/a_height);
		    // decode full image
		    Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

		    // calc exact destination size
		    Matrix m = new Matrix();
		    RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
		    RectF outRect = new RectF(0, 0, a_width, a_height);
		    m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.START);
		    float[] values = new float[9];
		    m.getValues(values);

		    // resize bitmap
		    l_bmp = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);

		    /*File direct = new File(Environment.getExternalStorageDirectory() + "/LiveWallManager");

		    if(!direct.exists())
		     {
		         direct.mkdir(); //directory is created;

		     }*/
		    
		    // save image
		    try
		    {
		    	
//		        FileOutputStream out = new FileOutputStream(l_path);
//		        l_bmp.compress(Bitmap.CompressFormat.JPEG, 80, out);
		    }
		    catch (Exception e)
		    {
		        Log.d(TAG, e.getMessage());
		    }
		}
		catch (IOException e)
		{
		    Log.d(TAG, e.getMessage());
		}
		
		return l_bmp;
	}
}
