package com.apaza.moises.visitsucre.ui.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.apaza.moises.visitsucre.R;

public class MarkerAnimateView extends FrameLayout{

    private ImageView ivMarker;
    private View vShadow;
    private int positionY;

    public MarkerAnimateView(Context context) {
        super(context);
        setup();
    }

    public MarkerAnimateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup(){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_marker, this, true);
        ivMarker = (ImageView)findViewById(R.id.ivMarker);
        positionY = ivMarker.getHeight();
        vShadow = findViewById(R.id.vShadow);
    }

    public void startAnimationMarker(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(ivMarker, "translationY", 0, -90);
        anim.setInterpolator(new AnticipateOvershootInterpolator());
        anim.start();
        showShadow();
    }

    public void endAnimationMarker(){
        ObjectAnimator anim = ObjectAnimator.ofFloat(ivMarker, "translationY", -100, 0);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
        hideShadow();
    }

    private void showShadow(){
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(vShadow, "scaleX", 0f, 1.0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(vShadow, "scaleY", 0f, 1.0f);
        //ObjectAnimator animColor = ObjectAnimator.ofObject(view, "backgroundColor", new ArgbEvaluator(),Color.parseColor("#8B0000"), Color.parseColor("#FF0000"));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(anim2, anim1);
        animatorSet.start();
    }

    private void hideShadow(){
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(vShadow, "scaleX", 1.0f, 0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(vShadow, "scaleY", 1.0f, 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(anim2, anim1);
        animatorSet.start();
    }
}
