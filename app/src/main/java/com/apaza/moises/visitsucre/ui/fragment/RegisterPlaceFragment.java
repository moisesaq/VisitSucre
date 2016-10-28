package com.apaza.moises.visitsucre.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.database.Place;
import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.global.Utils;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;
import com.apaza.moises.visitsucre.ui.InputTextView;
import com.apaza.moises.visitsucre.ui.MainActivity;
import com.apaza.moises.visitsucre.ui.base.BaseFragment;
import com.apaza.moises.visitsucre.ui.fragment.adapter.CategoryAdapter;

public class RegisterPlaceFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
        AdapterView.OnItemSelectedListener, PlaceInMapFragment.OnPlaceInMapFragmentListener{
    public static final String TAG = "REGISTER PLACE";
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private View view;
    private CategoryAdapter categoryAdapter;
    private long idCategory;

    private ImageView ivPlaceImage, ivStaticMap;
    private InputTextView itvName, itvDescription;
    private TextView tvLocationPlace;
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
        Spinner spCategory = (Spinner)view.findViewById(R.id.spCategory);
        categoryAdapter = new CategoryAdapter(getContext());
        spCategory.setAdapter(categoryAdapter);
        spCategory.setOnItemSelectedListener(this);
        getLoaderManager().initLoader(2, null, this);

        ivPlaceImage = (ImageView)view.findViewById(R.id.ivPlaceImage);
        FloatingActionButton fabSelectImage = (FloatingActionButton)view.findViewById(R.id.fabSelectImage);
        fabSelectImage.setOnClickListener(this);
        itvName = (InputTextView)view.findViewById(R.id.itvName);
        tvLocationPlace = (TextView)view.findViewById(R.id.tvLocationPlace);
        ImageButton iBtnSelectLocation = (ImageButton)view.findViewById(R.id.iBtnSelectLocation);
        iBtnSelectLocation.setOnClickListener(this);
        ivStaticMap = (ImageView)view.findViewById(R.id.ivStaticMap);
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
            case R.id.fabSelectImage:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(Utils.hasPermission(Utils.PERMISSION_CAMERA))
                        selectImage();
                    else
                        requestPermissions(Utils.PERMISSION_CAMERA, Utils.CODE_PERMISSION_CAMERA);
                }else{
                    selectImage();
                }
                break;
            case R.id.iBtnSelectLocation:
                PlaceInMapFragment placeInMapFragment = PlaceInMapFragment.newInstance(0);
                placeInMapFragment.setOnPlaceInMapFragmentListener(this);
                ((MainActivity)getActivity()).showFragment(placeInMapFragment);
                break;
            case R.id.btnSave:
                if(address != null)
                    loadStaticMap(address);
                /*if(itvName.isTextValid("Name invalid") && itvDescription.isTextValid("Description invalid")){
                    if(address != null){
                        savePlace();
                        //Global.showToastMessage("Data valid");
                    }else {
                        Global.showToastMessage("Select place location");
                    }
                }*/
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utils.CODE_PERMISSION_CAMERA:
                if (grantResults.length >= permissions.length && Utils.resultPermission(grantResults)){
                    selectImage();
                }else{
                    Global.showToastMessage("Is required permission");
                }
                break;
        }
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
            tvLocationPlace.setText(address.getAddressLine(0));
    }

    private void loadStaticMap(Address address){
        Global.getApiVisitSucreClient().getGoogleMapStatic(address, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if(response.getBitmap() != null){
                    ivStaticMap.setVisibility(View.VISIBLE);
                    ivStaticMap.setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ivStaticMap.setVisibility(View.GONE);
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {
                getString(R.string.take_photo),
                getString(R.string.from_gallery),
                getString(android.R.string.cancel)
        };

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(getString(R.string.add_image));
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Intent intent = null;
                switch (item){
                    case 0:
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        /*File f = new File(Utils.DIRECTORY_IMAGES, "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));*/
                        startActivityForResult(intent, Utils.REQUEST_CAMERA);
                        break;
                    case 1:
                        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select File"), Utils.SELECT_FILE);
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode){
            case Utils.REQUEST_CAMERA:
                Uri capturedImage = data.getData();
                Log.d(TAG, " URI CAMERA >>>> " + capturedImage.toString());
                ivPlaceImage.setImageURI(capturedImage);
                break;
            case Utils.SELECT_FILE:
                Uri selectedImage = data.getData();
                Log.d(TAG, " URI GALLERY >>>> " + selectedImage.toString());
                ivPlaceImage.setImageURI(selectedImage);
                break;
        }
    }

    public interface OnRegisterPlaceFragmentListener {
        void onRegisterPlaceClick();
    }

}
