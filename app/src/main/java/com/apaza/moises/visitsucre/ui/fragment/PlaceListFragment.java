package com.apaza.moises.visitsucre.ui.fragment;

import android.content.ContentValues;
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

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.database.PlaceDao;
import com.apaza.moises.visitsucre.ui.fragment.adapter.PlaceAdapter;
import com.apaza.moises.visitsucre.ui.fragment.base.BaseFragment;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;

public class PlaceListFragment extends BaseFragment implements PlaceAdapter.OnPlaceItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{
    private static final String ID_CATEGORY = "idCategory";
    private long idCategory;

    private View view;
    private RecyclerView listPlaces;
    private LinearLayoutManager linearLayoutManager;
    private PlaceAdapter adapter;

    private String[] options = {"Option1", "Option2", "Option3"};
    private int selectedPosition = 0;

    private OnFragmentInteractionListener mListener;

    public static PlaceListFragment newInstance(long idCategory) {
        PlaceListFragment fragment = new PlaceListFragment();
        Bundle args = new Bundle();
        args.putLong(ID_CATEGORY, idCategory);
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
            idCategory = getArguments().getLong(ID_CATEGORY);
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
            case R.id.action_update_db:
                ContentValues values = new ContentValues();
                values.put(ContractVisitSucre.Place.NAME, "CASA DE LA LIBERTAD SUCRE :)");
                getActivity().getContentResolver().update(ContractVisitSucre.Place.CONTENT_URI, values, null, null);
                break;
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
    public void onPlaceClick(PlaceAdapter.ViewHolder viewHolder, long idPlace) {
        ///Snackbar.make(getActivity().findViewById(android.R.id.content), String.format("ID place: %s", idPlace), Snackbar.LENGTH_SHORT).show();
       /* ContentValues values = new ContentValues();
        values.put(ContractVisitSucre.Place.NAME, "CASA DE LA LIBERTAD SUCRE :)");
        getActivity().getContentResolver().update(ContractVisitSucre.Place.createUriPlace(idPlace), values, null, null);*/
        //DetailPlaceActivity.createInstance(getActivity(), idPlace);

        if(mListener != null)
            mListener.onFragmentInteraction(idPlace);
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
    public void onDestroy(){
        super.onDestroy();
        try{
            getLoaderManager().destroyLoader(0);
            if(adapter != null){
                adapter = null;
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        if(idCategory > 0)
            selection = PlaceDao.Properties.IdCategory.columnName + " = " + idCategory;
        return new CursorLoader(getActivity(), ContractVisitSucre.Place.CONTENT_URI_DETAILED, null, selection, null, null);
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
        void onFragmentInteraction(long idPlace);
    }
}
