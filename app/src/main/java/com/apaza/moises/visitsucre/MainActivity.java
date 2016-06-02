package com.apaza.moises.visitsucre;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.apaza.moises.visitsucre.fragment.CategoryListFragment;
import com.apaza.moises.visitsucre.fragment.DetailPlaceFragment;
import com.apaza.moises.visitsucre.fragment.PlaceListFragment;
import com.apaza.moises.visitsucre.fragment.RegisterPlaceFragment;
import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.global.Utils;
import com.apaza.moises.visitsucre.provider.Category;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;
import com.apaza.moises.visitsucre.provider.HandlerDBVisitSucre;
import com.apaza.moises.visitsucre.provider.Place;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        RegisterPlaceFragment.OnRegisterPlaceFragmentListener,
        CategoryListFragment.OnCategoryListFragmentListener,
        PlaceListFragment.OnFragmentInteractionListener{

    private DrawerLayout drawerLayout;
    private String drawerTitle;
    private ActionBar actionBar;

    //For test
    private TextView text;
    private NetworkImageView imagePost;
    private HandlerDBVisitSucre handlerDBVisitSucre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupNavigationView();
        showFragment(PlaceListFragment.newInstance(""));
        //showFragment(DetailPlaceFragment.newInstance(""));
        handlerDBVisitSucre = HandlerDBVisitSucre.getInstance(getApplicationContext());

        /*CollapsingToolbarLayout collapser = (CollapsingToolbarLayout) findViewById(R.id.collapser);
        collapser.setTitle("Android");//place.getName()); // Cambiar título*/
    }

    private void setupNavigationView(){
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navView);
        if(navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);
                /*new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                String title = item.getTitle().toString();
                selectItem(item, title);
                return true;
            }
        });*/
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        String title = item.getTitle().toString();
        selectItem(item, title);
        return true;
    }

    private void selectItem(MenuItem item, String title){
        switch (item.getItemId()){
            case R.id.nav_register_place:
                showFragment(RegisterPlaceFragment.newInstance(""));
                break;
            case R.id.nav_all:
                showFragment(CategoryListFragment.newInstance(""));
                break;
        }
        drawerLayout.closeDrawers();
    }

    public void showFragment(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.replace(R.id.containerMain, fragment);
        ft.commit();
    }



    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_sidebar);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_new_category:
                registerNewCategory();
                return true;
            case R.id.action_test_database:
                new TestProvider().execute();
                return true;
            case R.id.action_show_collections_db:
                showDataBaseCollections();
                return true;
            case R.id.action_delete_collections_db:
                new TestDeleteDB().execute();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRegisterPlaceClick() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCategoryItemClick(Uri uri) {

    }

    public void registerNewCategory(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("New category");
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.new_category, null);
        final EditText name = (EditText)view.findViewById(R.id.name);
        final EditText description = (EditText)view.findViewById(R.id.description);
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txtName = name.getText().toString();
                String txtDescription = description.getText().toString();
                if(txtName.length() > 5 && txtDescription.length() > 10){
                    saveCategory(txtName, txtDescription);
                }else{
                    showMessage("Error missing characters");
                }
            }
        });
        dialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.create().show();

    }

    public void saveCategory(String name, String description){
        try{
            ContentValues values = new ContentValues();
            values.put(ContractVisitSucre.Category.CODE, Utils.generateCodeUnique("CATEGORY"));
            values.put(ContractVisitSucre.Category.LOGO, "logo");
            values.put(ContractVisitSucre.Category.NAME, name);
            values.put(ContractVisitSucre.Category.DATE, Utils.getCurrentDate().toString());
            values.put(ContractVisitSucre.Category.DESCRIPTION, description);
            getContentResolver().insert(ContractVisitSucre.Category.CONTENT_URI, values);
            showMessage("Category saved");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showMessage(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    private void showDataBaseCollections(){
        Log.d("CATEGORIES", "---------------------CATEGORIES----------------");
        DatabaseUtils.dumpCursor(getContentResolver().query(ContractVisitSucre.Category.CONTENT_URI, null, null, null, null));
        Log.d("PLACES", "---------------------------PLACES------------------");
        DatabaseUtils.dumpCursor(getContentResolver().query(ContractVisitSucre.Place.CONTENT_URI, null, null, null, null));
    }

    private void testVolley(){
        try{
            //RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = "http://192.168.1.42:3000/api/places";
            String urlImage = "http://vignette2.wikia.nocookie.net/ultradragonball/images/2/28/543px-MajinBuuFatNV.png/revision/latest?cb=20110330215918";
            /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            text.setText(response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            text.setText("Error: " + error.toString());
                        }
                    });*/
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            text.setText("Result: " + response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    text.setText("Error: " + error.toString());
                }
            });
            //requestQueue.add(jsonArrayRequest);
            Global.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
            ImageLoader imageLoader = Global.getInstance(getApplicationContext()).getImageLoader();
            imageLoader.get(urlImage, ImageLoader.getImageListener(imagePost, R.mipmap.ic_communication, R.mipmap.default_profile));
            imagePost.setImageUrl(urlImage, imageLoader);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class TestDB extends AsyncTask<Void, Void, Boolean> {
        LoadToast loadToast;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadToast = new LoadToast(MainActivity.this);
            loadToast.setText("Testing...");
            loadToast.setTextColor(Color.RED).setBackgroundColor(Color.GREEN).setProgressColor(Color.BLUE);
            loadToast.setTranslationY(120);
            loadToast.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Date currentDate = Calendar.getInstance().getTime();
            try{
                handlerDBVisitSucre.getDB().beginTransaction();

                //Insert data
                Category category1 = new Category(null, "code-111", "logo111", "Cathedral", "bla bla bla bla bla bla bla bla 1111111", currentDate);
                Category category2 = new Category(null, "code-222", "logo222", "Museums", "bla bla bla bla bla bla bla bla 2222222", currentDate);
                Category category3 = new Category(null, "code-333", "logo333", "Tourism", "bla bla bla bla bla bla bla bla 33333333", currentDate);

                String idCategory1 = handlerDBVisitSucre.insertCategory(category1);
                String idCategory2 = handlerDBVisitSucre.insertCategory(category2);
                String idCategory3 = handlerDBVisitSucre.insertCategory(category3);

                Place place1 = new Place(null, "code-1", "Casa de la libertad", "adress xxxx", -34.3452341, -58.123123, "Description 1111", "Image3 111",currentDate, idCategory1);
                Place place2 = new Place(null, "code-2", "Muse xxxx", "adress xxxx", -34.3452341, -58.123123, "Description 2222", "Image 222", currentDate, idCategory2);
                Place place3 = new Place(null, "code-3", "Tourism xxx tarabuco", "adress xxxx", -34.3452341, -58.123123, "Description 2222", "Image 333", currentDate, idCategory3);

                String idPlace1 = handlerDBVisitSucre.insertPlace(place1);
                String idPlace2 = handlerDBVisitSucre.insertPlace(place2);
                String idPlace3 = handlerDBVisitSucre.insertPlace(place3);

                //Delete data
                handlerDBVisitSucre.deletePlace(idPlace2);

                //Modified data
                handlerDBVisitSucre.updatePlace(place3.setIdPlace(idPlace3).setName("CARABUCO"));

                Log.d("CATEGORIES", ">>>>>>>>>>>>>>>>>> CATEGORIES");
                DatabaseUtils.dumpCursor(handlerDBVisitSucre.getCategories());
                Log.d("PLACES", ">>>>>>>>>>>>>>>>>>>>>>>>PLACES");
                DatabaseUtils.dumpCursor(handlerDBVisitSucre.getPlaces());

                handlerDBVisitSucre.getDB().setTransactionSuccessful();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            } finally {
                handlerDBVisitSucre.getDB().endTransaction();
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
            loadToast = new LoadToast(MainActivity.this);
            loadToast.setText("Deleting...");
            loadToast.setTextColor(Color.DKGRAY).setBackgroundColor(Color.WHITE).setProgressColor(Color.BLUE);
            loadToast.setTranslationY(100);
            loadToast.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                handlerDBVisitSucre.getDB().beginTransaction();

                //Delete data
                Cursor cursor = handlerDBVisitSucre.getCategories();
                if(cursor != null){
                    for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                        handlerDBVisitSucre.deleteCategory(cursor.getString(1));
                    }
                }

                Log.d("CATEGORIES", "CATEGORIES");
                DatabaseUtils.dumpCursor(handlerDBVisitSucre.getCategories());
                Log.d("PLACES", "PLACES");
                DatabaseUtils.dumpCursor(handlerDBVisitSucre.getPlaces());

                handlerDBVisitSucre.getDB().setTransactionSuccessful();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            } finally {
                handlerDBVisitSucre.getDB().endTransaction();
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

    public class TestProvider extends AsyncTask<Void, Void, Boolean>{
        LoadToast loadToast;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadToast = new LoadToast(MainActivity.this);
            loadToast.setText("Testing...");
            loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE).setProgressColor(Color.BLUE);
            loadToast.setTranslationY(120);
            loadToast.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean status = false;
            Date currentDate = Calendar.getInstance().getTime();

            ContentResolver resolver = getContentResolver();
            ArrayList<ContentProviderOperation> listOperations = new ArrayList<>();

            //Insert with custom provider
            String idCategory1 = ContractVisitSucre.Category.generateIdCategory();
            String idCategory2 = ContractVisitSucre.Category.generateIdCategory();
            String idCategory3 = ContractVisitSucre.Category.generateIdCategory();

            listOperations.add(ContentProviderOperation.newInsert(ContractVisitSucre.Category.CONTENT_URI)
                    .withValue(ContractVisitSucre.Category.ID, idCategory1)
                    .withValue(ContractVisitSucre.Category.CODE, "code-111")
                    .withValue(ContractVisitSucre.Category.LOGO, "logo111")
                    .withValue(ContractVisitSucre.Category.NAME, "Cathedral")
                    .withValue(ContractVisitSucre.Category.DESCRIPTION, "bla bla bla bla bla 11111")
                    .withValue(ContractVisitSucre.Category.DATE, currentDate.toString())
                    .build());
            listOperations.add(ContentProviderOperation.newInsert(ContractVisitSucre.Category.CONTENT_URI)
                    .withValue(ContractVisitSucre.Category.ID, idCategory2)
                    .withValue(ContractVisitSucre.Category.CODE, "code-222")
                    .withValue(ContractVisitSucre.Category.LOGO, "logo222")
                    .withValue(ContractVisitSucre.Category.NAME, "Museums")
                    .withValue(ContractVisitSucre.Category.DESCRIPTION, "bla bla bla bla bla 22222")
                    .withValue(ContractVisitSucre.Category.DATE, currentDate.toString())
                    .build());

            listOperations.add(ContentProviderOperation.newInsert(ContractVisitSucre.Category.CONTENT_URI)
                    .withValue(ContractVisitSucre.Category.ID, idCategory3)
                    .withValue(ContractVisitSucre.Category.CODE, "code-333")
                    .withValue(ContractVisitSucre.Category.LOGO, "logo333")
                    .withValue(ContractVisitSucre.Category.NAME, "Tourism")
                    .withValue(ContractVisitSucre.Category.DESCRIPTION, "bla bla bla bla bla 33333")
                    .withValue(ContractVisitSucre.Category.DATE, currentDate.toString())
                    .build());

            String idPlace1 = ContractVisitSucre.Place.generateIdPlace();
            String idPlace2 = ContractVisitSucre.Place.generateIdPlace();
            String idPlace3 = ContractVisitSucre.Place.generateIdPlace();

            listOperations.add(ContentProviderOperation.newInsert(ContractVisitSucre.Place.CONTENT_URI)
                    .withValue(ContractVisitSucre.Place.ID, idPlace1)
                    .withValue(ContractVisitSucre.Place.CODE, "code-1")
                    .withValue(ContractVisitSucre.Place.NAME, "Casa de la libertad")
                    .withValue(ContractVisitSucre.Place.ADDRESS, "address 123")
                    .withValue(ContractVisitSucre.Place.LATITUDE, -34.3452341)
                    .withValue(ContractVisitSucre.Place.LONGITUDE, -58.123123)
                    .withValue(ContractVisitSucre.Place.DESCRIPTION, "Description 1111")
                    .withValue(ContractVisitSucre.Place.PATH_IMAGE, "Image 111")
                    .withValue(ContractVisitSucre.Place.DATE, currentDate.toString())
                    .withValue(ContractVisitSucre.Place.ID_CATEGORY, idCategory1)
                    .build());

            listOperations.add(ContentProviderOperation.newInsert(ContractVisitSucre.Place.CONTENT_URI)
                    .withValue(ContractVisitSucre.Place.ID, idPlace2)
                    .withValue(ContractVisitSucre.Place.CODE, "code-2")
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
                    .withValue(ContractVisitSucre.Place.CODE, "code-3")
                    .withValue(ContractVisitSucre.Place.NAME, "Tourism Tarabuco")
                    .withValue(ContractVisitSucre.Place.ADDRESS, "address 123")
                    .withValue(ContractVisitSucre.Place.LATITUDE, -34.3452341)
                    .withValue(ContractVisitSucre.Place.LONGITUDE, -58.123123)
                    .withValue(ContractVisitSucre.Place.DESCRIPTION, "Description 333")
                    .withValue(ContractVisitSucre.Place.PATH_IMAGE, "Image 333")
                    .withValue(ContractVisitSucre.Place.DATE, currentDate.toString())
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
