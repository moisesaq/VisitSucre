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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.fragment.CategoryListFragment;
import com.apaza.moises.visitsucre.ui.fragment.PlaceInMapFragment;
import com.apaza.moises.visitsucre.ui.fragment.PlaceListFragment;
import com.apaza.moises.visitsucre.ui.fragment.RegisterPlaceFragment;
import com.apaza.moises.visitsucre.ui.fragment.TestFragment;
import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.global.Utils;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        RegisterPlaceFragment.OnRegisterPlaceFragmentListener,
        CategoryListFragment.OnCategoryListFragmentListener,
        PlaceListFragment.OnFragmentInteractionListener,
        PlaceInMapFragment.OnPlaceInMapFragmentListener{

    private DrawerLayout drawerLayout;
    private String drawerTitle;
    private ActionBar actionBar;

    //For test
    private TextView text;
    private NetworkImageView imagePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Global.setContext(this);
        setupToolbar();
        setupNavigationView();
        //showFragment(PlaceListFragment.newInstance(""));
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
                showFragment(RegisterPlaceFragment.newInstance(""));
                break;
            case R.id.nav_all:
                showFragment(CategoryListFragment.newInstance(""));
                break;
        }
        drawerLayout.closeDrawers();
    }

    public void showFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
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

    /*PLACE IN MAP FRAGMENT LISTENER*/
    @Override
    public void onPlaceLocaled(Uri uri) {

    }

}
