package com.apaza.moises.visitsucre.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by moises on 31/05/16.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{

    private final Context context;
    private Cursor cursor;

    private OnPlaceItemClickListener listener;

    interface OnPlaceItemClickListener{
        public void onPlaceClick(ViewHolder viewHolder, String idPlace);
    }

    public PlaceAdapter(Context context, OnPlaceItemClickListener listener){
        this.context = context;
        this.listener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ViewHolder(View v){
            super(v);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
