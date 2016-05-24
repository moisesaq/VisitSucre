package com.apaza.moises.visitsucre.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
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
import com.apaza.moises.visitsucre.provider.Category;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;
import com.apaza.moises.visitsucre.provider.HandlerDBVisitSucre;
import com.apaza.moises.visitsucre.provider.Place;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.Date;

public class ListPlaceFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private View view;
    private TextView text;
    private Button connect, delete;
    private NetworkImageView imagePost;

    private OnFragmentInteractionListener mListener;

    private HandlerDBVisitSucre handlerDBVisitSucre;

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
        handlerDBVisitSucre = HandlerDBVisitSucre.getInstance(getActivity());
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
        delete = (Button)view.findViewById(R.id.delete);
        delete.setOnClickListener(this);
        imagePost = (NetworkImageView)view.findViewById(R.id.imagePost);
    }


    @Override
    public void onClick(View view){
        if(view.getId() == connect.getId()){
            //testVolley();
            new TestDB().execute();
        }else if(view.getId() == delete.getId()){
            new TestDeleteDB().execute();
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
            Global.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
            ImageLoader imageLoader = Global.getInstance(getActivity()).getImageLoader();
            imageLoader.get(urlImage, ImageLoader.getImageListener(imagePost, R.mipmap.ic_communication, R.mipmap.default_profile));
            imagePost.setImageUrl(urlImage, imageLoader);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class TestDB extends AsyncTask<Void, Void, Boolean>{
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
            loadToast = new LoadToast(getActivity());
            loadToast.setText("Deleting...");
            loadToast.setTextColor(Color.RED).setBackgroundColor(Color.GREEN).setProgressColor(Color.BLUE);
            loadToast.setTranslationY(120);
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
