package com.apaza.moises.visitsucre.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.ui.InputTextView;
import com.apaza.moises.visitsucre.ui.MainActivity;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.base.BaseFragment;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends BaseFragment implements View.OnClickListener, LoginContract.View{

    private CallBack mCallBack;
    private LoginContract.Presenter presenter;

    private View view;
    private InputTextView itvEmail, itvPassword;
    private LinearLayout layoutLogin, layoutLoading;

    public static LoginFragment newInstance(Context context, FirebaseAuth firebaseAuth) {
        LoginFragment fragment = new LoginFragment();
        LoginInteractor loginInteractor = new LoginInteractor(context, firebaseAuth);
        new LoginPresenter(fragment, loginInteractor);

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        layoutLogin = (LinearLayout)view.findViewById(R.id.layoutLogin);
        layoutLoading = (LinearLayout)view.findViewById(R.id.layoutLoading);
        itvEmail = (InputTextView) view.findViewById(R.id.itv_email);
        itvPassword = (InputTextView) view.findViewById(R.id.itv_password);
        Button login = (Button)view.findViewById(R.id.b_login);
        login.setOnClickListener(this);
        TextView forgotPassword = (TextView) view.findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_login:
                attemptLogin();
                break;
            case R.id.forgotPassword:
                mCallBack.onForgotPasswordClick();
                break;
        }
    }

    private void attemptLogin() {
        if(itvEmail.isEmailValid() && itvPassword.isTextValid("Invalid"))
            presenter.attemptLogin(itvEmail.getText(), itvPassword.getText());
    }

    /*LOGIN CONTRACT VIEW*/
    @Override
    public void showProgress(boolean show) {
        layoutLoading.setVisibility(show? View.VISIBLE: View.GONE);
        layoutLogin.setVisibility(show? View.GONE: View.VISIBLE);
    }

    @Override
    public void showLoginError(String msg) {
        Global.showToastMessage(msg);
    }

    @Override
    public void showLoginSuccess() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showGooglePlayServicesDialog(int codeError) {
        mCallBack.onInvokeGooglePlayServices(codeError);
    }

    @Override
    public void showGooglePlayServicesError() {
        Global.showMessage("Necessary google play service for use app");
    }

    @Override
    public void showNetWorkError() {
        Global.showToastMessage("No internet");
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        if(presenter != null){
            this.presenter = presenter;
        }else {
            throw new RuntimeException("The presenter can not null");
        }
    }

    public interface CallBack {
        void onForgotPasswordClick();
        void onInvokeGooglePlayServices(int codeError);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof CallBack){
            mCallBack = (CallBack) context;
        }else {
            throw new ClassCastException(context.toString()
                    + " must implement CallBack");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }
}
