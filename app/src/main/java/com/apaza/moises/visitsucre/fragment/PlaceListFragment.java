package com.apaza.moises.visitsucre.fragment;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.fragment.adapter.PlaceAdapter;
import com.apaza.moises.visitsucre.global.Utils;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;

public class PlaceListFragment extends Fragment implements PlaceAdapter.OnPlaceItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private View view;
    private RecyclerView listPlaces;
    private LinearLayoutManager linearLayoutManager;
    private PlaceAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public static PlaceListFragment newInstance(String param1) {
        PlaceListFragment fragment = new PlaceListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceListFragment() {
        // Required empty public constructor
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
        view = inflater.inflate(R.layout.fragment_place_list, container, false);
        setup();
        return view;
    }

    private void setup(){
        listPlaces = (RecyclerView)view.findViewById(R.id.listPlaces);
        listPlaces.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        listPlaces.setLayoutManager(linearLayoutManager);

        adapter = new PlaceAdapter(getActivity(), this);
        listPlaces.setAdapter(adapter);

        getLoaderManager().restartLoader(1, null, this);
    }

    //This method is from adapter
    @Override
    public void onPlaceClick(PlaceAdapter.ViewHolder viewHolder, String idPlace) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), String.format("ID place: %s", idPlace), Snackbar.LENGTH_SHORT).show();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContractVisitSucre.Place.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if(adapter != null)
                adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCategoryListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
