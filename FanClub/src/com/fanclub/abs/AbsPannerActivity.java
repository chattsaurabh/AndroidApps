package com.fanclub.abs;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.fanclub.R;
import com.fanclub.R.anim;
import com.fanclub.R.menu;
import com.fanclub.custom_views.CustomViewFlipper;
import com.fanclub.custom_views.VideoView;
import com.fanclub.utils.FanClubConstants;

public class AbsPannerActivity extends Activity implements  OnGestureListener, FanClubConstants{
	
	protected CustomViewFlipper flipper;
    
    private Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;
    
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    
    protected AbsDataModel m_dataModel = null;
    
    protected enum PAGE_INDEX {
    	PAGE_ONE,
    	PAGE_TWO
    }
    
    protected PAGE_INDEX m_currentPageIndex;
    
    private GestureDetector gestureDetector;
    
    protected LinearLayout m_firstPageLinLayout = null;
    protected LinearLayout m_secondPageLinLayout = null;
    
    protected AbsPannerView m_currentPage = null;
    
    
    public static abstract class SwipeHandler{
        public void onLeft() {}
        public void onRight() {}
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//create animations
        slide_in_left = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slide_in_right = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        slide_out_left = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        slide_out_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        
        gestureDetector = new GestureDetector(this,this);
        
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        
       
        m_firstPageLinLayout = new LinearLayout(this);
        m_firstPageLinLayout.setLayoutParams(lp);        	
        m_firstPageLinLayout.setBackgroundColor(Color.BLACK);        	
    	flipper.addView(m_firstPageLinLayout);
    	
    	
    	m_secondPageLinLayout = new LinearLayout(this);
    	m_secondPageLinLayout.setLayoutParams(lp);        	
    	m_secondPageLinLayout.setBackgroundColor(Color.BLACK);        	
    	flipper.addView(m_secondPageLinLayout);
       
    	m_currentPageIndex = PAGE_INDEX.PAGE_ONE;    	
//    	addPage();

	}

	private void toggleCurrentPageIndex() {
		if(m_currentPageIndex == PAGE_INDEX.PAGE_ONE)
			m_currentPageIndex = PAGE_INDEX.PAGE_TWO;
		else
			m_currentPageIndex = PAGE_INDEX.PAGE_ONE;
	}
	
	protected void addPage() {
		if(m_currentPage != null)
		{
			m_currentPage.clean();
			m_currentPage = null;
		}		
	}

	public boolean onTouchEvent(MotionEvent me)
    {
    	return gestureDetector.onTouchEvent(me);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}
	
	private void showNext() {
		toggleCurrentPageIndex();
		m_dataModel.nextDataSet();
		m_dataModel.calculateCurrentDataSet();
		addPage();
		flipper.setInAnimation(slide_in_right);
        flipper.setOutAnimation(slide_out_left);
        flipper.showNext();
	}
	
	private void showPrev() {
		toggleCurrentPageIndex();
		m_dataModel.prevDataSet();
		m_dataModel.calculateCurrentDataSet();
		addPage();
		flipper.setInAnimation(slide_in_left);
    	flipper.setOutAnimation(slide_out_right);
        flipper.showPrevious();
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                showNext();
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                showPrev();
            }
        } catch (Exception e) {
            // nothing
        }
        return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		/*try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE ) {
                showNext();
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE ) {
                showPrev();
            }
        } catch (Exception e) {
            // nothing
        }*/
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onDestroy() {
		m_dataModel.clean();
		super.onDestroy();
	}
}
