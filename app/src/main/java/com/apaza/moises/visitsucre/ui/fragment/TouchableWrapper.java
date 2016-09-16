package com.apaza.moises.visitsucre.ui.fragment;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchableWrapper extends FrameLayout{

    private OnTouchSupportMapListener onTouchSupportMapListener;
    public TouchableWrapper(Context context) {
        super(context);
    }

    public void setOnTouchSupportMapListener(OnTouchSupportMapListener listener){
        this.onTouchSupportMapListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.d("TOUCH MAP", " ACTION DOWN");
                if(onTouchSupportMapListener != null)
                    onTouchSupportMapListener.onTouchDownMap();
                break;

            case MotionEvent.ACTION_UP:
                Log.d("TOUCH MAP", " ACTION UP");
                if(onTouchSupportMapListener != null)
                    onTouchSupportMapListener.onTouchUpMap();
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public interface OnTouchSupportMapListener{
        void onTouchDownMap();
        void onTouchUpMap();
    }
}
