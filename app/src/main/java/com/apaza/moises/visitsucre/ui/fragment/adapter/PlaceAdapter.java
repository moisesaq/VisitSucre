package com.apaza.moises.visitsucre.ui.fragment.adapter;

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
import com.apaza.moises.visitsucre.database.CategoryDao;
import com.apaza.moises.visitsucre.database.PlaceDao;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{
    /*INDEX FOR COLUMN DETAILED PLACE*/
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_ADDRESS = 2;
    public static final int COLUMN_DESCRIPTION = 3;
    public static final int COLUMN_CATEGORY = 4;

    private final Context context;
    private Cursor cursor;

    public OnPlaceItemClickListener listener;

    public interface OnPlaceItemClickListener{
        void onPlaceClick(ViewHolder viewHolder, long idPlace);
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
        text = cursor.getString(COLUMN_NAME);
        holder.namePlace.setText(text);
        text = cursor.getString(COLUMN_CATEGORY);
        holder.category.setText(text);
        text = cursor.getString(COLUMN_DESCRIPTION);
        holder.description.setText(text);
    }

    @Override
    public int getItemCount() {
        if(cursor != null)
            return cursor.getCount();
        return 0;
    }

    public long getIdPlace(int position){
        if(cursor != null){
            if(cursor.moveToPosition(position))
                return cursor.getLong(0);
            else
                return 0;
        }else{
            return 0;
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
            explore.setOnClickListener(this);
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
