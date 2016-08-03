package com.apaza.moises.visitsucre.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.apaza.moises.visitsucre.ui.DetailPlaceActivity;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.fragment.adapter.PlaceAdapter;
import com.apaza.moises.visitsucre.ui.fragment.base.BaseFragment;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;

public class PlaceListFragment extends BaseFragment implements PlaceAdapter.OnPlaceItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private View view;
    private RecyclerView listPlaces;
    private LinearLayoutManager linearLayoutManager;
    private PlaceAdapter adapter;

    private String[] options = {"Option1", "Option2", "Option3"};
    private int selectedPosition = 0;

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
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_filter).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_filter:
                createShowDialogFilter(selectedPosition);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createShowDialogFilter(final int selectPosition){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getString(R.string.filter));
        dialog.setSingleChoiceItems(options, selectPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedPosition = which;
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

    //This method is from adapter
    @Override
    public void onPlaceClick(PlaceAdapter.ViewHolder viewHolder, String idPlace) {
        ///Snackbar.make(getActivity().findViewById(android.R.id.content), String.format("ID place: %s", idPlace), Snackbar.LENGTH_SHORT).show();
        DetailPlaceActivity.createInstance(getActivity(), idPlace);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
