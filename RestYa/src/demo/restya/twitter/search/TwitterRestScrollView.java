package demo.restya.twitter.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public  class TwitterRestScrollView extends ScrollView {

	ScrollResultsDown handler;
	void setScrollResultsDown(ScrollResultsDown handler){
		this.handler = handler;
	}

	public TwitterRestScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	public TwitterRestScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TwitterRestScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
	        View view = (View) getChildAt(getChildCount()-1);
	        int diff = (view.getBottom()-(getHeight()+getScrollY()));
	        if( diff == 0 ){  // if diff is zero, then the bottom has been reached
	        	if(handler!=null)
	        		handler.onBottomReach();
	        	
	        }
	        super.onScrollChanged(l, t, oldl, oldt);
	}
	
}
