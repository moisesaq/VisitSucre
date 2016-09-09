package com.apaza.moises.visitsucre.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;
import com.apaza.moises.visitsucre.ui.fragment.adapter.CategoryAdapter;
import com.apaza.moises.visitsucre.ui.fragment.base.BaseFragment;

public class RegisterPlaceFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private View view;
    private Spinner spCategory;
    private CategoryAdapter categoryAdapter;

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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_place, container, false);
        setupView();
        return view;
    }

    private void setupView() {
        spCategory = (Spinner)view.findViewById(R.id.spCategory);
        categoryAdapter = new CategoryAdapter(getContext());
        spCategory.setAdapter(categoryAdapter);
        getLoaderManager().initLoader(2, null, this);
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

    /*LOADER*/

    @Override
    public void onDestroy(){
        super.onDestroy();
        try{
            getLoaderManager().destroyLoader(2);
            if(categoryAdapter != null){
                categoryAdapter.changeCursor(null);
                categoryAdapter = null;
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContractVisitSucre.Category.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        categoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        categoryAdapter.swapCursor(null);
    }


    public interface OnRegisterPlaceFragmentListener {
        void onRegisterPlaceClick();
    }

}
