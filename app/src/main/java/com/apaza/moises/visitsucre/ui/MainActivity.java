package com.apaza.moises.visitsucre.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.global.Utils;
import com.apaza.moises.visitsucre.ui.fragment.AddCategoryFragment;
import com.apaza.moises.visitsucre.sync.SyncAdapter;
import com.apaza.moises.visitsucre.ui.fragment.AboutSucreFragment;
import com.apaza.moises.visitsucre.ui.fragment.CategoryListFragment;
import com.apaza.moises.visitsucre.ui.fragment.LocationTrackerFragment;
import com.apaza.moises.visitsucre.ui.fragment.PlaceInMapFragment;
import com.apaza.moises.visitsucre.ui.fragment.PlaceListFragment;
import com.apaza.moises.visitsucre.ui.fragment.RegisterPlaceFragment;
import com.apaza.moises.visitsucre.ui.fragment.TestFragment;
import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.ui.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        RegisterPlaceFragment.OnRegisterPlaceFragmentListener,
        CategoryListFragment.OnCategoryListFragmentListener,
        PlaceListFragment.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener{

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            goToLoginActivity();
        else{
            Utils.showToastMessage("Login successful");
            Log.d(TAG, user.toString());
        }

        SyncAdapter.setupSyncAdapter(this);
        fragmentManager = getSupportFragmentManager();
        setupToolbar();
        setupNavigationView();
        showFragment(LocationTrackerFragment.newInstance());
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

    private void goToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //item.setChecked(true);
        String title = item.getTitle().toString();
        selectItem(item, title);
        return true;
    }

    private void selectItem(MenuItem item, String title){
        switch (item.getItemId()){
            case R.id.nav_tourist_places:
                showFragment(PlaceListFragment.newInstance(0));
                break;
            case R.id.nav_more_places:
                showFragment(CategoryListFragment.newInstance(""));
                break;
            case R.id.nav_nearby:
                //showFragment(PlaceInMapFragment.newInstance(""));
                break;

            case R.id.nav_suggest_place:
                showFragment(RegisterPlaceFragment.newInstance(""));
                /*Fragment frag = fragmentManager.findFragmentByTag(RegisterPlaceFragment.class.getSimpleName());
                if(frag != null){
                    Log.d(TAG, "Fragment not null " + RegisterPlaceFragment.class.getSimpleName());
                    fragmentManager.popBackStack(RegisterPlaceFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else{
                    Log.d(TAG, "Fragment null " + RegisterPlaceFragment.class.getSimpleName());
                    showFragment(RegisterPlaceFragment.newInstance(""));
                }*/
                break;

            case R.id.nav_about_sucre:
                showFragment(AboutSucreFragment.newInstance());
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
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.replace(R.id.fl_main_container, fragment);
        ft.commit();
        //Log.i(TAG, "STACK COUNT >>>>> " + fragmentManager.getBackStackEntryCount() + " TAG FRAGMENT " + fragment.getClass().getSimpleName());
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

    public void setTitle(String title){
        if(actionBar != null)
            actionBar.setTitle(title);
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
                showFragment(AddCategoryFragment.newInstance());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRegisterPlaceClick() {

    }

    @Override
    public void onFragmentInteraction(long idPlace) {
        showFragment(PlaceInMapFragment.newInstance(idPlace));
    }

    @Override
    public void onCategoryItemClick(long idCategory) {
        showFragment(PlaceListFragment.newInstance(idCategory));
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
