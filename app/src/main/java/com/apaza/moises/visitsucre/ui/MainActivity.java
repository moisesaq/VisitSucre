package com.apaza.moises.visitsucre.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.global.Constants;
import com.apaza.moises.visitsucre.global.VolleySingleton;
import com.apaza.moises.visitsucre.sync.SyncAdapter;
import com.apaza.moises.visitsucre.ui.fragment.CategoryListFragment;
import com.apaza.moises.visitsucre.ui.fragment.PlaceInMapFragment;
import com.apaza.moises.visitsucre.ui.fragment.PlaceListFragment;
import com.apaza.moises.visitsucre.ui.fragment.RegisterPlaceFragment;
import com.apaza.moises.visitsucre.ui.fragment.TestFragment;
import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.global.Utils;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        RegisterPlaceFragment.OnRegisterPlaceFragmentListener,
        CategoryListFragment.OnCategoryListFragmentListener,
        PlaceListFragment.OnFragmentInteractionListener,
        PlaceInMapFragment.OnPlaceInMapFragmentListener, FragmentManager.OnBackStackChangedListener{

    public static String TAG = "MAIN ACTIVITY";
    private DrawerLayout drawerLayout;
    private String drawerTitle;
    private ActionBar actionBar;
    private FragmentManager fragmentManager;

    //For test
    private TextView text;
    private NetworkImageView imagePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Global.setContext(this);

        SyncAdapter.setupSyncAdapter(this);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        setupToolbar();
        setupNavigationView();
        showFragment(TestFragment.newInstance());

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
                Fragment frag = fragmentManager.findFragmentByTag(RegisterPlaceFragment.class.getSimpleName());
                if(frag != null){
                    Log.d(TAG, "Fragment not null " + RegisterPlaceFragment.class.getSimpleName());
                    fragmentManager.popBackStack(RegisterPlaceFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else{
                    Log.d(TAG, "Fragment null " + RegisterPlaceFragment.class.getSimpleName());
                    showFragment(RegisterPlaceFragment.newInstance(""));
                }
                break;
            case R.id.nav_all:
                showFragment(CategoryListFragment.newInstance(""));
                break;
            case R.id.test_db:
                TestFragment testFragment = (TestFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_test);
                if(testFragment == null)
                    testFragment = TestFragment.newInstance();
                showFragment(testFragment);
                break;
        }
        drawerLayout.closeDrawers();
    }

    public void showFragment(Fragment fragment){
        //FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.replace(R.id.containerMain, fragment);
        ft.commit();
        Log.i(TAG, "STACK COUNT >>>>> " + fragmentManager.getBackStackEntryCount() + " TAG FRAGMENT " + fragment.getClass().getSimpleName());
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
                if(txtName.length() > 5 && txtDescription.length() > 5){
                    saveCategory(txtName, txtDescription);
                    //saveCategoryInDBRemote(txtName, txtDescription);
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
            values.put(ContractVisitSucre.Category.LOGO, "logo");
            values.put(ContractVisitSucre.Category.NAME, name);
            values.put(ContractVisitSucre.Category.DATE, Utils.getCurrentDate().toString());
            values.put(ContractVisitSucre.Category.DESCRIPTION, description);
            values.put(ContractVisitSucre.Category.PENDING_INSERTION, 1);
            Uri uri = getContentResolver().insert(ContractVisitSucre.Category.CONTENT_URI, values);
            if(uri != null){
                showMessage("Category saved");
                SyncAdapter.synchronizeNow(this, true);
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
            showMessage("Category saved");

            VolleySingleton.getInstance(Global.getContext()).addToRequestQueue(
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

    public void showMessage(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    /*PLACE IN MAP FRAGMENT LISTENER*/
    @Override
    public void onPlaceLocaled(Uri uri) {

    }

    @Override
    public void onBackPressed(){
        Log.d(TAG, "BACK");
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else{
            if(fragmentManager.getBackStackEntryCount() > 1){
                fragmentManager.popBackStack();
            }else{
                finish();
            }
        }
    }

    @Override
    public void onBackStackChanged() {
        Log.d(TAG, "ON BACK STACK CHANGED - STACK COUNT->" + getSupportFragmentManager().getBackStackEntryCount());
    }
}
