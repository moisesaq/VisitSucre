package com.apaza.moises.visitsucre.ui.fragment;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.database.Category;
import com.apaza.moises.visitsucre.global.Constants;
import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.global.Utils;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;
import com.apaza.moises.visitsucre.sync.SyncAdapter;
import com.apaza.moises.visitsucre.ui.InputTextView;
import com.apaza.moises.visitsucre.ui.MainActivity;
import com.apaza.moises.visitsucre.ui.base.BaseFragment;
import com.apaza.moises.visitsucre.web.api.volley.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCategoryFragment extends BaseFragment implements View.OnClickListener, GalleryIconDialog.OnGalleryIconDialogListener{

    public static final String TAG = "ADD CATEGORY FRAGMENT";

    private View view;
    private ImageButton ibIcon;
    private InputTextView itvName, itvDescription;

    private int imageSelected = 0;

    public static AddCategoryFragment newInstance(){
        return new AddCategoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_category, container, false);
        setupView();
        return view;
    }

    private void setupView(){
        ibIcon = (ImageButton)view.findViewById(R.id.ib_icon);
        ibIcon.setOnClickListener(this);
        itvName = (InputTextView)view.findViewById(R.id.itv_name);
        itvDescription = (InputTextView)view.findViewById(R.id.itv_description);
        Button btnAddCategory = (Button)view.findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(imageSelected != 0){
            ibIcon.setImageResource(imageSelected);
            String resourceName = getResources().getResourceEntryName(imageSelected);
            ibIcon.setTag(resourceName);
            Global.showToastMessage("IMAGE " + resourceName);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_icon:
                GalleryIconDialog galleryIconDialog = GalleryIconDialog.newInstance(this);
                //galleryIconDialog.show(getActivity().getSupportFragmentManager(), "galleryDialog");
                ((MainActivity)getActivity()).showFragment(galleryIconDialog);
                break;
            case R.id.btnAddCategory:
                if(itvName.isTextValid("Name Invalid") && itvDescription.isTextValid("Description invalid")){
                    saved(itvName.getText(), itvDescription.getText());
                }
                break;
        }
    }

    public void saved(String name, String description){
        Category category = new Category();
        category.setName(name);
        if(ibIcon.getTag() != null){
            String icon = (String)ibIcon.getTag();
            category.setLogo(icon);
        }else{
            Global.showToastMessage("Icon null");
            return;
        }

        category.setDescription(description);
        category.setCreatedAt(Utils.getCurrentDate());
        long idCategory = Global.getDataBaseHandler().getDaoSession().getCategoryDao().insert(category);
        if(idCategory > 0){
            Global.showMessage("category saved");
            clearFields();
            getActivity().getContentResolver().notifyChange(ContractVisitSucre.Category.createUriCategory(String.valueOf(idCategory)), null);
        }
    }

    public void clearFields(){
        itvName.clearField();
        itvDescription.clearField();
        ibIcon.setImageResource(R.mipmap.ic_camera_burst_grey600_48dp);
        imageSelected = 0;
    }

    public void saveCategory(String name, String description){
        try{
            ContentValues values = new ContentValues();
            values.put(ContractVisitSucre.Category.LOGO, "logo");
            values.put(ContractVisitSucre.Category.NAME, name);
            values.put(ContractVisitSucre.Category.DATE, Utils.getCurrentDate().toString());
            values.put(ContractVisitSucre.Category.DESCRIPTION, description);
            values.put(ContractVisitSucre.Category.PENDING_INSERTION, 1);
            Uri uri = getActivity().getContentResolver().insert(ContractVisitSucre.Category.CONTENT_URI, values);
            if(uri != null){
                Global.showToastMessage("Category saved");
                SyncAdapter.synchronizeNow(getActivity(), true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveCategoryInDBRemote(String name, String description){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(ContractVisitSucre.Category.LOGO, "logo_999");
            jsonObject.put(ContractVisitSucre.Category.NAME, name);
            jsonObject.put(ContractVisitSucre.Category.DATE, Utils.getCurrentDate().toString());
            jsonObject.put(ContractVisitSucre.Category.DESCRIPTION, description);
            Global.showToastMessage("Category saved");

            VolleySingleton.getInstance().addToRequestQueue(
                    new JsonObjectRequest(
                            Request.Method.POST,
                            Constants.URL_CATEGORIES,
                            jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "Result: " + response.toString());
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG, "ERROR VOLLEY: " + error.getMessage());
                                }
                            }
                    ){
                        @Override
                        public Map<String, String> getHeaders(){
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            headers.put("Accept", "application/json");
                            return headers;
                        }

                        @Override
                        public String getBodyContentType(){
                            return "application/json; charset=utf-8" + getParamsEncoding();
                        }
                    }
            );

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*GALLERY DIALOG LISTENER*/
    @Override
    public void onIconSelected(int icon) {
        imageSelected = icon;
    }

}
