package com.apaza.moises.visitsucre.ui.fragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;

public class CategoryAdapter extends CursorAdapter{

    private Context context;
    public CategoryAdapter(Context context){
        super(context, null, 0);
        this.context = context;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.category_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView logo = (ImageView)view.findViewById(R.id.logo);
        //TODO: Here to complete for load image or logo

        TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(cursor.getString(cursor.getColumnIndex(ContractVisitSucre.Category.NAME)));

        TextView description = (TextView)view.findViewById(R.id.description);
        description.setText(cursor.getString(cursor.getColumnIndex(ContractVisitSucre.Category.DESCRIPTION)));
    }
}
