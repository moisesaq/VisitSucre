package com.apaza.moises.visitsucre.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.fragment.LoginFragment;
import com.apaza.moises.visitsucre.ui.fragment.OnBoardingFragment;
import com.apaza.moises.visitsucre.ui.fragment.SignUpFragment;

public class SignUpActivity extends AppCompatActivity implements OnBoardingFragment.OnBoardingFragmentListener,
                                                                    LoginFragment.OnLoginFragmentListener,
                                                                    SignUpFragment.OnSignUpFragmentListener{

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showFragment(OnBoardingFragment.newInstance());
    }

    public void showFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.replace(R.id.containerMain, fragment);
        ft.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(android.R.id.home);
        /*View view = menuItem.setAction*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Methods override of OnBoardingFragment
    @Override
    public void onSignUpClick() {
        showFragment(SignUpFragment.newInstance(""));
    }

    @Override
    public void onAccessClick() {
        showFragment(LoginFragment.newInstance());
    }

    //Methods override of LoginFragment
    @Override
    public void onForgotPasswordClick() {

    }

    @Override
    public void onSignUpFromLoginClick() {

    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
        else
            finish();
    }

    //Methods override of SignUpFragment
    @Override
    public void onCreateAccountClick() {

    }

    @Override
    public void onAccessFromSignUpClick() {

    }

}
