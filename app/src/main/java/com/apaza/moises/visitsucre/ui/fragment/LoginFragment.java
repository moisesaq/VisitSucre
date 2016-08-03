package com.apaza.moises.visitsucre.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.apaza.moises.visitsucre.ui.MainActivity;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.fragment.base.BaseFragment;


public class LoginFragment extends BaseFragment implements View.OnClickListener, RippleView.OnRippleCompleteListener{


    private OnLoginFragmentListener mListener;

    private View view;
    //private Button loginWithFace;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
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
        RippleView login = (RippleView)view.findViewById(R.id.login);
        login.setOnRippleCompleteListener(this);
        RippleView forgotPassword = (RippleView)view.findViewById(R.id.forgotPassword);
        forgotPassword.setOnRippleCompleteListener(this);
        RippleView signUp = (RippleView)view.findViewById(R.id.signUp);
        signUp.setOnRippleCompleteListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginWithFace:
                Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();
                break;
            case R.id.loginWithG:
                break;
            case R.id.login:
                break;
            case R.id.forgotPassword:
                break;
            case R.id.signUp:
                break;
        }
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
            case R.id.login:
                Toast.makeText(getActivity(), "Login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.forgotPassword:
                mListener.onForgotPasswordClick();
                break;
            case R.id.signUp:
                mListener.onSignUpFromLoginClick();
                break;
        }
    }

    public interface OnLoginFragmentListener {
        void onForgotPasswordClick();
        void onSignUpFromLoginClick();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLoginFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
