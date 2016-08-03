package com.apaza.moises.visitsucre.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.fragment.base.BaseFragment;

public class RegisterPlaceFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private OnRegisterPlaceFragmentListener mListener;

    public static RegisterPlaceFragment newInstance(String param1) {
        RegisterPlaceFragment fragment = new RegisterPlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterPlaceFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_register_place, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRegisterPlaceClick();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnRegisterPlaceFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRegisterPlaceFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRegisterPlaceFragmentListener {
        void onRegisterPlaceClick();
    }

}
