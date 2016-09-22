package com.apaza.moises.visitsucre.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.apaza.moises.visitsucre.ui.MainActivity;

public class BaseFragment extends Fragment {

    public ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    protected void setTitle(String title){
        if(actionBar != null)
            actionBar.setTitle(title);
    }

    protected void setTitle(int title){
        if(actionBar != null)
            actionBar.setTitle(title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    protected void onBack(){
        getActivity().onBackPressed();
    }
}
