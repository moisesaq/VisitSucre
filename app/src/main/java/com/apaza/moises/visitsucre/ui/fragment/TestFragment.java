package com.apaza.moises.visitsucre.ui.fragment;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.global.Constants;
import com.apaza.moises.visitsucre.global.VolleySingleton;
import com.apaza.moises.visitsucre.ui.fragment.base.BaseFragment;
import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.provider.Category;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;
import com.apaza.moises.visitsucre.provider.Place;
import com.google.gson.Gson;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFragment extends BaseFragment implements View.OnClickListener{

    public static final String TAG = "TEST_FRAGMENT";

    private View view;
    private CheckBox checkBox;
    private ImageView imageTest;
    private EditText textSearch;
    private NetworkImageView imagePost;
    private TextView resultTest;

    private MenuItem insert, show, delete, update;

    public static TestFragment newInstance(){
        return new TestFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, container, false);
        setupView();
        return view;
    }

    private void setupView() {
        Button loadImage = (Button)view.findViewById(R.id.loadImage);
        checkBox = (CheckBox)view.findViewById(R.id.checkBox);
        loadImage.setOnClickListener(this);
        Button test2 = (Button)view.findViewById(R.id.test2);
        test2.setOnClickListener(this);
        textSearch = (EditText)view.findViewById(R.id.textSearch);
        Button search = (Button)view.findViewById(R.id.search);
        search.setOnClickListener(this);
        imageTest = (ImageView)view.findViewById(R.id.imageTest);
        imagePost = (NetworkImageView)view.findViewById(R.id.imagePost);
        resultTest = (TextView)view.findViewById(R.id.resultTest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        insert = menu.findItem(R.id.action_insert_db).setVisible(true);
        show = menu.findItem(R.id.action_show_db).setVisible(true);
        delete = menu.findItem(R.id.action_delete_db).setVisible(true);
        update = menu.findItem(R.id.action_update_db).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_insert_db:
                Global.showMessage("test db");
                new TestProvider().execute();
                return true;
            case R.id.action_show_db:
                showDataBaseCollections();
                return true;
            case R.id.action_delete_db:
                new TestDeleteDB().execute();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test2:
                if(checkBox.isChecked())
                    testRequestJsonObject(Constants.URL_PLACES);
                else
                    testRequestJsonObject(Constants.URL_CATEGORIES);
                break;
            case R.id.loadImage:
                testImageLoader();
                break;
            case R.id.search:
                String text = textSearch.getText().toString();
                if(text.isEmpty())
                    return;

                if(checkBox.isChecked()){
                    testJsonObjectRequest2(Constants.URL_FIND_PLACE, text);
                }else {
                    testJsonArrayRequest(Constants.URL_FIND_CATEGORY, text);
                }
                break;
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        insert.setVisible(false);
        show.setVisible(false);
        delete.setVisible(false);
        update.setVisible(false);
    }

    /*TEST VOLLEY WITH DB LOCAL*/
    private void testImageLoader(){
        try{
            ImageLoader imageLoader = Global.getVolleySingleton().getImageLoader();
            imageLoader.get(Constants.URL_IMAGE, ImageLoader.getImageListener(imageTest, R.mipmap.ic_communication, R.mipmap.default_profile));
            imagePost.setImageUrl(Constants.URL_IMAGE, imageLoader);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void testRequestJsonArray(String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        resultTest.setText("Result: " + response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        resultTest.setText("Error: " + error.toString());
                    }
        });
        VolleySingleton.getInstance(Global.getContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void testRequestJsonObject(String url){
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        resultTest.setText("Result: " + response.toString());
                        try{
                            Gson gson = new Gson();
                            JSONArray categories = response.getJSONArray(Constants.CATEGORIES);
                            Category[] res = gson.fromJson(categories != null ? categories.toString(): null, Category[].class);
                            List<Category> list = Arrays.asList(res);
                            Log.d(TAG, "Count elements " + list.size());
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultTest.setText("Error: " + error.toString());
            }
        });
        VolleySingleton.getInstance(Global.getContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void testJsonArrayRequest(String url, final String text){
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString() + " " +text);
                        resultTest.setText("Result: " +response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        resultTest.setText("Error: " + error.toString());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() {
                        // Mapping of key-value
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("name", text);

                        return headers;
                    }
        };
        //VolleySingleton.getInstance(Global.getContext()).addToRequestQueue(request);
        Global.getVolleySingleton().addToRequestQueue(request);
    }

    private void testJsonObjectRequest2(String url, final String text){
        HashMap<String, String> params = new HashMap<>();
        params.put("name", text);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(params),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString() + " " +text);
                        resultTest.setText("Result: " +response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        resultTest.setText("Error: " + error.toString());
                    }
                });
        //VolleySingleton.getInstance(Global.getContext()).addToRequestQueue(request);
        Global.getVolleySingleton().addToRequestQueue(request);
    }

    /*TEST DB NORMAL*/
    public class TestDB extends AsyncTask<Void, Void, Boolean> {
        LoadToast loadToast;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadToast = new LoadToast(getActivity());
            loadToast.setText("Testing...");
            loadToast.setTextColor(Color.RED).setBackgroundColor(Color.GREEN).setProgressColor(Color.BLUE);
            loadToast.setTranslationY(120);
            loadToast.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Date currentDate = Calendar.getInstance().getTime();
            try{
                Global.getHandlerDBVisitSucre().getDB().beginTransaction();



                //Insert data
                Category category1 = new Category(null, "logo111", "Cathedral", currentDate.toString(), "bla bla bla bla bla bla bla bla 1111111");
                Category category2 = new Category(null, "logo222", "Museums", currentDate.toString(), "bla bla bla bla bla bla bla bla 2222222");
                Category category3 = new Category(null, "logo333", "Tourism", currentDate.toString(), "bla bla bla bla bla bla bla bla 33333333");

                String idCategory1 = Global.getHandlerDBVisitSucre().insertCategory(category1);
                String idCategory2 = Global.getHandlerDBVisitSucre().insertCategory(category2);
                String idCategory3 = Global.getHandlerDBVisitSucre().insertCategory(category3);

                Place place1 = new Place(null, "code-1", "Casa de la libertad", "adress xxxx", -34.3452341, -58.123123, "Description 1111", "Image3 111",currentDate, idCategory1);
                Place place2 = new Place(null, "code-2", "Muse xxxx", "adress xxxx", -34.3452341, -58.123123, "Description 2222", "Image 222", currentDate, idCategory2);
                Place place3 = new Place(null, "code-3", "Tourism xxx tarabuco", "adress xxxx", -34.3452341, -58.123123, "Description 2222", "Image 333", currentDate, idCategory3);

                String idPlace1 = Global.getHandlerDBVisitSucre().insertPlace(place1);
                String idPlace2 = Global.getHandlerDBVisitSucre().insertPlace(place2);
                String idPlace3 = Global.getHandlerDBVisitSucre().insertPlace(place3);

                //Delete data
                Global.getHandlerDBVisitSucre().deletePlace(idPlace2);

                //Modified data
                Global.getHandlerDBVisitSucre().updatePlace(place3.setIdPlace(idPlace3).setName("CARABUCO"));

                Log.d("CATEGORIES", ">>>>>>>>>>>>>>>>>> CATEGORIES");
                DatabaseUtils.dumpCursor(Global.getHandlerDBVisitSucre().getCategories());
                Log.d("PLACES", ">>>>>>>>>>>>>>>>>>>>>>>>PLACES");
                DatabaseUtils.dumpCursor(Global.getHandlerDBVisitSucre().getPlaces());

                Global.getHandlerDBVisitSucre().getDB().setTransactionSuccessful();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            } finally {
                Global.getHandlerDBVisitSucre().getDB().endTransaction();
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result)
                loadToast.success();
            else
                loadToast.error();
        }
    }

    public class TestDeleteDB extends AsyncTask<Void, Void, Boolean>{
        LoadToast loadToast;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadToast = new LoadToast(getActivity());
            loadToast.setText("Deleting...");
            loadToast.setTextColor(Color.DKGRAY).setBackgroundColor(Color.WHITE).setProgressColor(Color.BLUE);
            loadToast.setTranslationY(100);
            loadToast.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                Global.getHandlerDBVisitSucre().getDB().beginTransaction();

                //Delete data
                Cursor cursor = Global.getHandlerDBVisitSucre().getCategories();
                if(cursor != null){
                    for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                        Global.getHandlerDBVisitSucre().deleteCategory(cursor.getString(1));
                    }
                }

                Log.d("CATEGORIES", "CATEGORIES");
                DatabaseUtils.dumpCursor(Global.getHandlerDBVisitSucre().getCategories());
                Log.d("PLACES", "PLACES");
                DatabaseUtils.dumpCursor(Global.getHandlerDBVisitSucre().getPlaces());

                Global.getHandlerDBVisitSucre().getDB().setTransactionSuccessful();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            } finally {
                Global.getHandlerDBVisitSucre().getDB().endTransaction();
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result)
                loadToast.success();
            else
                loadToast.error();
        }
    }

    /*TEST DB WITH PROVIDER*/

    private void showDataBaseCollections(){
        Log.d("CATEGORIES", "---------------------CATEGORIES----------------");
        DatabaseUtils.dumpCursor(getActivity().getContentResolver().query(ContractVisitSucre.Category.CONTENT_URI, null, null, null, null));
        Log.d("PLACES", "---------------------------PLACES------------------");
        DatabaseUtils.dumpCursor(getActivity().getContentResolver().query(ContractVisitSucre.Place.CONTENT_URI, null, null, null, null));
        Log.d("PLACES", ">>>>>>>>>>>>>>>>>>>>>>>>DETAILED PLACE ALL ");
        DatabaseUtils.dumpCursor(getActivity().getContentResolver().query(ContractVisitSucre.Place.CONTENT_URI_DETAILED, null, null, null, null));

        Log.d("PLACES", ">>>>>>>>>>>>>>>>>>>>>>>>DETAILED PLACE ALL WITH FILTER ");
        DatabaseUtils.dumpCursor(getActivity().getContentResolver().query(ContractVisitSucre.Place
                .CONTENT_URI_DETAILED.buildUpon().appendQueryParameter(ContractVisitSucre.Place.PARAMS_FILTER, ContractVisitSucre.Place.FILTER_CATEGORY).build(),
                null, null, null, null));
    }

    public class TestProvider extends AsyncTask<Void, Void, Boolean>{
        LoadToast loadToast;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadToast = new LoadToast(getContext());
            loadToast.setText("Testing...");
            loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE).setProgressColor(Color.BLUE);
            loadToast.setTranslationY(120);
            loadToast.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean status = false;
            Date currentDate = Calendar.getInstance().getTime();

            ContentResolver resolver = getActivity().getContentResolver();
            ArrayList<ContentProviderOperation> listOperations = new ArrayList<>();

            //Insert with custom provider
            String idCategory1 = ContractVisitSucre.Category.generateIdCategory();
            String idCategory2 = ContractVisitSucre.Category.generateIdCategory();
            String idCategory3 = ContractVisitSucre.Category.generateIdCategory();

            listOperations.add(ContentProviderOperation.newInsert(ContractVisitSucre.Category.CONTENT_URI)
                    .withValue(ContractVisitSucre.Category.ID, idCategory1)
                    .withValue(ContractVisitSucre.Category.LOGO, "logo111")
                    .withValue(ContractVisitSucre.Category.NAME, "Cathedral")
                    .withValue(ContractVisitSucre.Category.DESCRIPTION, "bla bla bla bla bla 11111")
                    .withValue(ContractVisitSucre.Category.DATE, currentDate.toString())
                    .build());
            listOperations.add(ContentProviderOperation.newInsert(ContractVisitSucre.Category.CONTENT_URI)
                    .withValue(ContractVisitSucre.Category.ID, idCategory2)
                    .withValue(ContractVisitSucre.Category.NAME, "Museums")
                    .build());

            listOperations.add(ContentProviderOperation.newInsert(ContractVisitSucre.Category.CONTENT_URI)
                    .withValue(ContractVisitSucre.Category.ID, idCategory3)
                    .withValue(ContractVisitSucre.Category.NAME, "Tourism")
                    .build());

            String idPlace1 = ContractVisitSucre.Place.generateIdPlace();
            String idPlace2 = ContractVisitSucre.Place.generateIdPlace();
            String idPlace3 = ContractVisitSucre.Place.generateIdPlace();

            listOperations.add(ContentProviderOperation.newInsert(ContractVisitSucre.Place.CONTENT_URI)
                    .withValue(ContractVisitSucre.Place.ID, idPlace1)
                    .withValue(ContractVisitSucre.Place.NAME, "Casa de la libertad")
                    .withValue(ContractVisitSucre.Place.ID_CATEGORY, idCategory1)
                    .build());

            listOperations.add(ContentProviderOperation.newInsert(ContractVisitSucre.Place.CONTENT_URI)
                    .withValue(ContractVisitSucre.Place.ID, idPlace2)
                    .withValue(ContractVisitSucre.Place.NAME, "Museo 555")
                    .withValue(ContractVisitSucre.Place.ADDRESS, "address 123")
                    .withValue(ContractVisitSucre.Place.LATITUDE, -34.3452341)
                    .withValue(ContractVisitSucre.Place.LONGITUDE, -58.123123)
                    .withValue(ContractVisitSucre.Place.DESCRIPTION, "Description 2222")
                    .withValue(ContractVisitSucre.Place.PATH_IMAGE, "Image 222")
                    .withValue(ContractVisitSucre.Place.DATE, currentDate.toString())
                    .withValue(ContractVisitSucre.Place.ID_CATEGORY, idCategory2)
                    .build());

            listOperations.add(ContentProviderOperation.newInsert(ContractVisitSucre.Place.CONTENT_URI)
                    .withValue(ContractVisitSucre.Place.ID, idPlace3)
                    .withValue(ContractVisitSucre.Place.NAME, "Tourism Tarabuco")
                    .withValue(ContractVisitSucre.Place.ID_CATEGORY, idCategory1)
                    .build());

            //Delete with custom provider
            listOperations.add(ContentProviderOperation.newDelete(ContractVisitSucre.Place.createUriPlace(idPlace2)).build());

            //Update with custom provider

            listOperations.add(ContentProviderOperation.newUpdate(ContractVisitSucre.Place.createUriPlace(idPlace3))
                    .withValue(ContractVisitSucre.Place.NAME, "TARABUCO").build());

            try{
                resolver.applyBatch(ContractVisitSucre.AUTHORITY, listOperations);
                status = true;
            }catch (RemoteException e){
                e.printStackTrace();
            }catch (OperationApplicationException e){
                e.printStackTrace();
            }

            Log.d("CATEGORIES", ">>>>>>>>>>>>>>>>>>> CATEGORIES WITH PROVIDER");
            DatabaseUtils.dumpCursor(resolver.query(ContractVisitSucre.Category.CONTENT_URI, null, null, null, null));
            Log.d("PLACES", ">>>>>>>>>>>>>>>>>>>>>>>>PLACES WITH PROVIDER");
            DatabaseUtils.dumpCursor(resolver.query(ContractVisitSucre.Place.CONTENT_URI, null, null, null, null));
            Log.d("PLACES", ">>>>>>>>>>>>>>>>>>>>>>>>DETAILED PLACE #1 ");
            DatabaseUtils.dumpCursor(resolver.query(ContractVisitSucre.Place.createUriForDetail(idPlace1), null, null, null, null));
            return status;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result)
                loadToast.success();
            else
                loadToast.error();
        }
    }
}
