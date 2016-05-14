package com.apaza.moises.visitsucre.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.global.Global;

import org.json.JSONArray;

public class ListPlaceFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private View view;
    private TextView text;
    private Button connect;
    private NetworkImageView imagePost;

    private OnFragmentInteractionListener mListener;

    public static ListPlaceFragment newInstance(String param1) {
        ListPlaceFragment fragment = new ListPlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ListPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_place, container, false);
        setup();
        return view;
    }

    private void setup(){
        text = (TextView)view.findViewById(R.id.text);
        connect = (Button)view.findViewById(R.id.connect);
        connect.setOnClickListener(this);
        imagePost = (NetworkImageView)view.findViewById(R.id.imagePost);
    }


    @Override
    public void onClick(View view){
        if(view.getId() == connect.getId()){
            testVolley();
        }
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
            Global.getIntance(getActivity()).addToRequestQueue(jsonArrayRequest);
            ImageLoader imageLoader = Global.getIntance(getActivity()).getImageLoader();
            imageLoader.get(urlImage, ImageLoader.getImageListener(imagePost, R.mipmap.ic_communication, R.mipmap.default_profile));
            imagePost.setImageUrl(urlImage, imageLoader);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
