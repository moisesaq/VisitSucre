package com.apaza.moises.visitsucre.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.fragment.adapter.CategoryAdapter;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;

public class CategoryListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private OnCategoryListFragmentListener mListener;

    private CategoryAdapter categoryAdapter;

    public CategoryListFragment() {
        // Required empty public constructor
    }
    public static CategoryListFragment newInstance(String param1) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        categoryAdapter = new CategoryAdapter(getActivity());
        setListAdapter(categoryAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCategoryListFragmentListener) {
            mListener = (OnCategoryListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCategoryListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),ContractVisitSucre.Category.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        categoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        categoryAdapter.swapCursor(null);
    }

    public interface OnCategoryListFragmentListener {
        void onCategoryItemClick(Uri uri);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try{
            getLoaderManager().destroyLoader(0);
            if(categoryAdapter != null){
                categoryAdapter.changeCursor(null);
                categoryAdapter = null;
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }
}
