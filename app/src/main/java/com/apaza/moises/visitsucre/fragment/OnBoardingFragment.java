package com.apaza.moises.visitsucre.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.global.Utils;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class OnBoardingFragment extends Fragment implements View.OnClickListener, RippleView.OnRippleCompleteListener{

    private View view;
    private OnBoardingFragmentListener mListener;
    private Toolbar toolbar;

    public static OnBoardingFragment newInstance() {
        OnBoardingFragment fragment = new OnBoardingFragment();
        return fragment;
    }

    public OnBoardingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_on_boarding, container, false);
        setup();
        return view;
    }

    private void setup(){
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.viewPagerSignUp);
        ImagePageAdapter imagePageAdapter = new ImagePageAdapter(getActivity().getApplicationContext(), getItemsOnBoarding());
        viewPager.setAdapter(imagePageAdapter);

        CircleIndicator circleIndicator = (CircleIndicator)view.findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager);
        RippleView btnSignUp = (RippleView) view.findViewById(R.id.signUp);
        btnSignUp.setOnRippleCompleteListener(this);
        //btnSignUp.setOnClickListener(this);
        RippleView accessView = (RippleView)view.findViewById(R.id.accessView);
        accessView.setOnRippleCompleteListener(this);
        //accessView.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnBoardingFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentSignUpListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUp:
                mListener.onSignUpClick();
                break;
            case R.id.accessView:
                mListener.onAccessClick();
                break;
        }
    }

    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()){
            case R.id.signUp:
                mListener.onSignUpClick();
                break;
            case R.id.accessView:
                mListener.onAccessClick();
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        toolbar.setVisibility(View.GONE);
    }

    public interface OnBoardingFragmentListener {
        void onSignUpClick();
        void onAccessClick();
    }

    public class ImagePageAdapter extends PagerAdapter {
        private Context context;
        private List<ItemOnBoarding> listItems;

        public ImagePageAdapter(Context context,  List<ItemOnBoarding> listItems){
            this.context = context;
            this.listItems = listItems;
        }

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == (FrameLayout)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.pager_layout, container, false);
            ImageView imageOnBoarding = (ImageView)view.findViewById(R.id.imageOnBoarding);
            TextView title = (TextView)view.findViewById(R.id.titleOnBoarding);
            TextView subtitle = (TextView)view.findViewById(R.id.subtitleOnBoarding);
            ItemOnBoarding item = listItems.get(position);
            imageOnBoarding.setImageResource(item.getImage());
            /*Glide.with(getActivity())
                    .load(item.getImage())
                    .fitCenter()
                    .crossFade()
                    .into(imageOnBoarding);*/
            title.setText(item.getTitle());
            subtitle.setText(item.getSubtitle());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((FrameLayout)object);
        }
    }

    private List<ItemOnBoarding> getItemsOnBoarding(){
        List<ItemOnBoarding> list = new ArrayList<>();
        if(Utils.getCurrentLanguage().equals(Utils.LANGUAGE_SPANISH)){
            list.add(new ItemOnBoarding(R.mipmap.ic_launcher, R.string.hello_moises, R.string.on_boarding_subtitle1));
            list.add(new ItemOnBoarding(R.mipmap.ic_launcher, R.string.hello_moises, R.string.on_boarding_subtitle1));
            list.add(new ItemOnBoarding(R.mipmap.ic_launcher, R.string.hello_moises, R.string.on_boarding_subtitle1));
        }else{
            list.add(new ItemOnBoarding(R.mipmap.ic_launcher, R.string.hello_moises, R.string.on_boarding_subtitle1));
            list.add(new ItemOnBoarding(R.mipmap.ic_launcher, R.string.hello_moises, R.string.on_boarding_subtitle1));
            list.add(new ItemOnBoarding(R.mipmap.ic_launcher, R.string.hello_moises, R.string.on_boarding_subtitle1));
        }

        return list;
    }


    private class ItemOnBoarding{
        int image, title, subtitle;

        public ItemOnBoarding(int image, int title, int subtitle){
            this.image = image;
            this.title = title;
            this.subtitle = subtitle;
        }

        public int getImage() {
            return image;
        }

        public int getSubtitle() {
            return subtitle;
        }

        public int getTitle() {
            return title;
        }
    }
}
