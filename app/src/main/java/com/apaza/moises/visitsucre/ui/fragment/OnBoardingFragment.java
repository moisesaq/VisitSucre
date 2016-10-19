package com.apaza.moises.visitsucre.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.fragment.base.BaseFragment;
import com.apaza.moises.visitsucre.global.Utils;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class OnBoardingFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener{

    private View view;
    private OnBoardingFragmentListener mListener;
    private ViewPager viewPager;
    private ImagePageAdapter imagePageAdapter;
    private CircleIndicator circleIndicator;
    private Toolbar toolbar;

    private ImageButton next, skip;

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
        imagePageAdapter = new ImagePageAdapter(getActivity().getApplicationContext(), getItemsOnBoarding());
        viewPager = (ViewPager)view.findViewById(R.id.viewPagerSignUp);
        viewPager.setAdapter(imagePageAdapter);
        viewPager.addOnPageChangeListener(this);

        circleIndicator = (CircleIndicator)view.findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager);

        skip = (ImageButton)view.findViewById(R.id.skip);

        skip.setOnClickListener(this);
        next = (ImageButton)view.findViewById(R.id.next);
        next.setOnClickListener(this);

        Button btnSignUp = (Button) view.findViewById(R.id.signUp);
        btnSignUp.setOnClickListener(this);
        TextView accessView = (TextView) view.findViewById(R.id.accessView);
        accessView.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.skip:
                break;
            case R.id.next:
                /*ColorGenerator generator = ColorGenerator.MATERIAL;
                TextDrawable textDrawable = TextDrawable.builder().buildRect("Hola", generator.getRandomColor());
                skip.setImageDrawable(textDrawable);*/
                if(viewPager.getCurrentItem() == imagePageAdapter.getCount()-1)
                    Toast.makeText(getActivity(), "Last item", Toast.LENGTH_SHORT).show();
                else
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                break;
            case R.id.signUp:
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

    //Method on page listener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == imagePageAdapter.getCount()-1){
            skip.setVisibility(View.GONE);
        }else {
            skip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnBoardingFragmentListener {
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
