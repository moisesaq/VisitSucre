package com.apaza.moises.visitsucre.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.apaza.moises.visitsucre.fragment.base.BaseFragment;
import com.apaza.moises.visitsucre.global.ApiRest;
import com.apaza.moises.visitsucre.global.Global;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TestFragment extends BaseFragment implements View.OnClickListener{

    public static final String TAG = "TEST_FRAGMENT";

    private View view;
    private CheckBox checkBox;
    private ImageView imageTest;
    private EditText textSearch;
    private NetworkImageView imagePost;
    private TextView resultTest;

    public static TestFragment newInstance(){
        return new TestFragment();
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test2:
                if(checkBox.isChecked())
                    testRequestJsonArray(Global.urlPlace);
                else
                    testRequestJsonArray(Global.urlCategory);
                break;
            case R.id.loadImage:
                testImageLoader();
                break;
            case R.id.search:
                Log.d(TAG, ">>>>>>>>>>>>>>>>>> Test Test Test");
                String text = textSearch.getText().toString();
                if(text.isEmpty())
                    return;

                if(checkBox.isChecked()){
                    testJsonObjectRequest2(Global.urlPlaceFind, text);
                }else {
                    testJsonArrayRequest(Global.urlCategoryFind, text);
                }
                break;
        }
    }

    private void testImageLoader(){
        try{
            ImageLoader imageLoader = Global.getApiRest().getImageLoader();
            imageLoader.get(Global.urlImage, ImageLoader.getImageListener(imageTest, R.mipmap.ic_communication, R.mipmap.default_profile));
            imagePost.setImageUrl(Global.urlImage, imageLoader);
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
        ApiRest.getInstance(Global.getContext()).addToRequestQueue(jsonArrayRequest);
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
        //ApiRest.getInstance(Global.getContext()).addToRequestQueue(request);
        Global.getApiRest().addToRequestQueue(request);
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
        //ApiRest.getInstance(Global.getContext()).addToRequestQueue(request);
        Global.getApiRest().addToRequestQueue(request);
    }
}
