package com.apaza.moises.visitsucre.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;
import com.bumptech.glide.Glide;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{

    private final Context context;
    private Cursor cursor;

    public OnPlaceItemClickListener listener;

    public interface OnPlaceItemClickListener{
        void onPlaceClick(ViewHolder viewHolder, String idPlace);
    }

    public PlaceAdapter(Context context, OnPlaceItemClickListener listener){
        this.context = context;
        this.listener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);

        String text;
        //text = cursor.getString(cursor.getColumnIndex(ContractVisitSucre.Place.PATH_IMAGE));
        //Glide.with(context).load(text).centerCrop().into(holder.imagePlace);
        text = cursor.getString(cursor.getColumnIndex(ContractVisitSucre.Place.NAME));
        holder.namePlace.setText(text);
        text = cursor.getString(cursor.getColumnIndex(ContractVisitSucre.Place.ID_CATEGORY));
        holder.category.setText(text);
        text = cursor.getString(cursor.getColumnIndex(ContractVisitSucre.Place.DESCRIPTION));
        holder.description.setText(text);
    }

    @Override
    public int getItemCount() {
        if(cursor != null)
            return cursor.getCount();
        return 0;
    }

    public String getIdPlace(int position){
        if(cursor != null){
            if(cursor.moveToPosition(position))
                return cursor.getString(cursor.getColumnIndex(ContractVisitSucre.Place.ID));
            else
                return "";
        }else{
            return "";
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imagePlace;
        private TextView namePlace, category, description;
        private Button share, explore;

        public ViewHolder(View v){
            super(v);
            imagePlace = (ImageView)v.findViewById(R.id.imagePlace);
            namePlace = (TextView)v.findViewById(R.id.namePlace);
            category = (TextView)v.findViewById(R.id.category);
            share = (Button)v.findViewById(R.id.share);
            explore = (Button)v.findViewById(R.id.explore);
            description = (TextView) v.findViewById(R.id.description);
            share.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onPlaceClick(this, getIdPlace(getAdapterPosition()));
        }
    }

    public void swapCursor(Cursor newCursor){
        if(newCursor != null){
            cursor = newCursor;
            notifyDataSetChanged();
        }
    }

    public Cursor getCursor(){
        return cursor;
    }

    /*this.idPlace = idPlace;
    this.code = code;
    this.name = name;
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
    this.description = description;
    this.pathImage = pathImage;
    this.date = date;
    this.idCategory = idCategory;*/

}
