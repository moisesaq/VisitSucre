package com.apaza.moises.visitsucre.ui.fragment;

import com.apaza.moises.visitsucre.ui.BasePresenter;
import com.apaza.moises.visitsucre.ui.BaseView;

public class LoginContract {

    interface View extends BaseView<Presenter>{
        void showProgress(boolean show);
        void showLoginError(String msg);
        void showLoginSuccess();
        void showGooglePlayServicesDialog(int codeError);
        void showGooglePlayServicesError();
        void showNetWorkError();
    }

    interface Presenter extends BasePresenter{
        void attemptLogin(String email, String password);
    }
}
