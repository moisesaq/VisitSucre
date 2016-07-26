package com.apaza.moises.visitsucre.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.fragment.base.BaseFragment;
import com.apaza.moises.visitsucre.global.Utils;

public class SignUpFragment extends BaseFragment implements RippleView.OnRippleCompleteListener, View.OnClickListener{


    private static final String PARAM_EMAIL = "email";

    private String txtEmail;

    private View view;


    private OnSignUpFragmentListener mListener;
    public static SignUpFragment newInstance(String param1) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_EMAIL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            txtEmail = getArguments().getString(PARAM_EMAIL);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();;
        if(actionBar != null){
            actionBar.show();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setup();
        return view;
    }

    private void setup(){
        RippleView loginWithFace = (RippleView)view.findViewById(R.id.loginWithFace);
        loginWithFace.setOnRippleCompleteListener(this);
        RippleView loginWithG = (RippleView)view.findViewById(R.id.loginWithG);
        loginWithG.setOnRippleCompleteListener(this);
        RippleView selectImage = (RippleView)view.findViewById(R.id.selectImage);
        selectImage.setOnClickListener(this);
        ImageView visibilityPassword = (ImageView)view.findViewById(R.id.visibilityPassword);
        visibilityPassword.setOnClickListener(this);
        RippleView createAccount = (RippleView)view.findViewById(R.id.createAccount);
        createAccount.setOnRippleCompleteListener(this);
        RippleView access = (RippleView)view.findViewById(R.id.access);
        access.setOnRippleCompleteListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()){
            case R.id.loginWithFace:
                Toast.makeText(getActivity(), "Face", Toast.LENGTH_SHORT).show();
                break;
            case R.id.loginWithG:
                Toast.makeText(getActivity(), "G+", Toast.LENGTH_SHORT).show();
                break;
            case R.id.createAccount:
                Utils.showMessageTest(getActivity(), "Create account");
                break;
            case R.id.access:
                Utils.showMessageTest(getActivity(), "Access");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectImage:
                Utils.showMessageTest(getActivity(), "Select image");
                break;
            case R.id.visibilityPassword:
                Utils.showMessageTest(getActivity(), "Visibility password");
                break;
        }
    }

    public interface OnSignUpFragmentListener {
        void onCreateAccountClick();
        void onAccessFromSignUpClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnSignUpFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSignUpFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
