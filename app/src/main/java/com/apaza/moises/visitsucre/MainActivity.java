package com.apaza.moises.visitsucre;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.apaza.moises.visitsucre.fragment.RegisterPlaceFragment;
import com.apaza.moises.visitsucre.global.Global;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RegisterPlaceFragment.OnRegisterPlaceFragmentListener{

    private DrawerLayout drawerLayout;
    private String drawerTitle;
    private ActionBar actionBar;
    private TextView text;
    private Button connect;
    private NetworkImageView imagePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navView);
        if(navigationView != null)
            setupDrawerContent(navigationView);

        text = (TextView)findViewById(R.id.text);
        connect = (Button)findViewById(R.id.connect);
        connect.setOnClickListener(this);
        imagePost = (NetworkImageView)findViewById(R.id.imagePost);
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                String title = item.getTitle().toString();
                selectItem(item, title);
                return true;
            }
        });
    }

    private void selectItem(MenuItem item, String title){
        switch (item.getItemId()){
            case R.id.nav_register_place:
                showFragment(RegisterPlaceFragment.newInstance(""));
                drawerLayout.closeDrawers();
                break;
        }
    }

    public void showFragment(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.replace(R.id.containerSignUp, fragment);
        ft.commit();
    }

    @Override
    public void onClick(View view){
        if(view.getId() == connect.getId()){
            testVolley();
        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void testVolley(){
        try{
            //RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = "http://192.168.1.42:3000/api/placesSucre";
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
            Global.getIntance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
            ImageLoader imageLoader = Global.getIntance(getApplicationContext()).getImageLoader();
            imageLoader.get(urlImage, ImageLoader.getImageListener(imagePost, R.mipmap.ic_communication, R.mipmap.default_profile));
            imagePost.setImageUrl(urlImage, imageLoader);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRegisterPlaceClick() {

    }

    /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
}
