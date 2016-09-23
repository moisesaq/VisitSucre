package com.apaza.moises.visitsucre.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.database.Place;
import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.global.Utils;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;
import com.apaza.moises.visitsucre.ui.InputTextView;
import com.apaza.moises.visitsucre.ui.MainActivity;
import com.apaza.moises.visitsucre.ui.fragment.adapter.CategoryAdapter;
import com.apaza.moises.visitsucre.ui.fragment.base.BaseFragment;

public class RegisterPlaceFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
        AdapterView.OnItemSelectedListener, PlaceInMapFragment.OnPlaceInMapFragmentListener{
    public static final String TAG = "REGISTER PLACE";
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private View view;
    private CategoryAdapter categoryAdapter;
    private Spinner spCategory;
    private long idCategory;

    private InputTextView itvName, itvDescription;
    private TextView tvAddressPlace;
    private Address address;

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
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_register_place, container, false);
        setupView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setupView() {
        spCategory = (Spinner)view.findViewById(R.id.spCategory);
        spCategory.setOnItemSelectedListener(this);
        categoryAdapter = new CategoryAdapter(getContext());
        spCategory.setAdapter(categoryAdapter);
        getLoaderManager().initLoader(2, null, this);

        itvName = (InputTextView)view.findViewById(R.id.itvName);
        ImageButton iBtnSelectLocation = (ImageButton)view.findViewById(R.id.iBtnSelectLocation);
        iBtnSelectLocation.setOnClickListener(this);
        tvAddressPlace = (TextView)view.findViewById(R.id.tvAddressPlace);
        itvDescription = (InputTextView) view.findViewById(R.id.itvDescription);
        Button btnSave = (Button)view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        setupAddressSelected();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iBtnSelectLocation:
                PlaceInMapFragment placeInMapFragment = PlaceInMapFragment.newInstance(0);
                placeInMapFragment.setOnPlaceInMapFragmentListener(this);
                ((MainActivity)getActivity()).showFragment(placeInMapFragment);
                break;
            case R.id.btnSave:
                if(itvName.isTextValid("Name invalid") && itvDescription.isTextValid("Description invalid")){
                    if(address != null){
                        //savePlace();
                        Global.showToastMessage("Data valid");
                    }else {
                        Global.showToastMessage("Select place location");
                    }
                }
                break;
        }
    }

    private void savePlace(){
        Place place = new Place();
        place.setName(itvName.getText());
        if(address != null){
            place.setAddress(address.getAddressLine(0) != null ? address.getAddressLine(0) : address.getLocality());
            place.setLatitude(address.getLatitude());
            place.setLongitude(address.getLongitude());
        }

        place.setDescription(itvDescription.getText());
        place.setCreatedAt(Utils.getCurrentDate());
        place.setIdCategory(idCategory);

        long idPlace = Global.getDataBaseHandler().getDaoSession().getPlaceDao().insert(place);
        if(idPlace > 0){
            Utils.showMessageTest(getContext(), "Place saved");
            getActivity().getContentResolver().notifyChange(ContractVisitSucre.Place.createUriPlace(String.valueOf(idPlace)), null);
            clearAllFields();
        }else{
            Utils.showMessageTest(getContext(), "Error");
        }
    }

    public void clearAllFields(){
        itvName.clearField();
        itvDescription.clearField();
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

    /*ITEM SELECTED LISTENER SPINNER*/
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        idCategory = adapterView.getItemIdAtPosition(i);
        Log.d(TAG, "Category >> " + idCategory);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /*ON PLACE FRAGMENT LISTENER*/
    @Override
    public void onPlaceLocaled(Address address) {
        Log.d(TAG, " Address selected 123>>> " + address.getAddressLine(0));
        this.address = address;
        setupAddressSelected();
    }

    private void setupAddressSelected(){
        if(address != null)
            tvAddressPlace.setText(address.getAddressLine(0));
    }

    public interface OnRegisterPlaceFragmentListener {
        void onRegisterPlaceClick();
    }

}
