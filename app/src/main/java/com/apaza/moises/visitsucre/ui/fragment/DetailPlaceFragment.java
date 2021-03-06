package com.apaza.moises.visitsucre.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.base.BaseFragment;

public class DetailPlaceFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private View view;

    private OnFragmentInteractionListener mListener;

    public DetailPlaceFragment() {
        // Required empty public constructor
    }
    public static DetailPlaceFragment newInstance(String param1) {
        DetailPlaceFragment fragment = new DetailPlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_place, container, false);
        setup();
        return view;
    }

    private void setup(){
        CollapsingToolbarLayout collapser = (CollapsingToolbarLayout)getActivity().findViewById(R.id.collapser);
        collapser.setTitle("Test test"); // Cambiar título
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPlaceInMapFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}