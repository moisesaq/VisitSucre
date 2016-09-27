package com.apaza.moises.visitsucre.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.apaza.moises.visitsucre.ui.MainActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

public class BaseFragment extends Fragment {

    public ActionBar actionBar;
    public GoogleApiClient googleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Places.GEO_DATA_API)
                    .build();
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
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
