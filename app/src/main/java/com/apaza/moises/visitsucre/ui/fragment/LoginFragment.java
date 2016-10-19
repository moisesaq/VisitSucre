package com.apaza.moises.visitsucre.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.apaza.moises.visitsucre.ui.MainActivity;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.fragment.base.BaseFragment;

public class LoginFragment extends BaseFragment implements View.OnClickListener{


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
        Button loginWithFace = (Button) view.findViewById(R.id.b_login_with_face);
        loginWithFace.setOnClickListener(this);
        Button loginWithG = (Button)view.findViewById(R.id.b_login_with_g);
        loginWithG.setOnClickListener(this);
        Button login = (Button)view.findViewById(R.id.b_login);
        login.setOnClickListener(this);
        TextView forgotPassword = (TextView) view.findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_login_with_face:
                Toast.makeText(getActivity(), "Face", Toast.LENGTH_SHORT).show();
                break;
            case R.id.b_login_with_g:
                Toast.makeText(getActivity(), "G+", Toast.LENGTH_SHORT).show();
                break;
            case R.id.b_login:
                Toast.makeText(getActivity(), "Login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.forgotPassword:
                mListener.onForgotPasswordClick();
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
