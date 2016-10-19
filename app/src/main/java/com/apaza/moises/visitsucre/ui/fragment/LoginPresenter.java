package com.apaza.moises.visitsucre.ui.fragment;

import android.support.annotation.NonNull;

public class LoginPresenter implements LoginContract.Presenter, LoginInteractor.CallBack{

    private LoginContract.View loginView;
    private LoginInteractor loginInteractor;

    public LoginPresenter(@NonNull LoginContract.View loginView, @NonNull LoginInteractor loginInteractor){
        this.loginView = loginView;
        loginView.setPresenter(this);
        this.loginInteractor = loginInteractor;
    }

    /*METHODS LOGIN CONTRACT PRESENTER*/
    @Override
    public void start() {
        //Verify if user logged
    }

    @Override
    public void attemptLogin(String email, String password) {
        loginView.showProgress(true);
        loginInteractor.login(email, password, this);
    }

    /*METHODS LOGIN INTERACTOR CALL BACK*/
    @Override
    public void onNetworkConnectFailed() {
        loginView.showProgress(false);
        loginView.showNetWorkError();
    }

    @Override
    public void onBeUserResolvableError(int errorCode) {
        loginView.showProgress(false);
        loginView.showGooglePlayServicesDialog(errorCode);
    }

    @Override
    public void onGooglePlayServiceFailed() {
        loginView.showGooglePlayServicesError();
    }

    @Override
    public void onAuthFailed(String msg) {
        loginView.showProgress(false);
        loginView.showLoginError(msg);
    }

    @Override
    public void onAuthSuccess() {
        loginView.showProgress(false);
        loginView.showLoginSuccess();
    }


}
