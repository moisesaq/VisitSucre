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
import com.apaza.moises.visitsucre.database.CategoryDao;
import com.apaza.moises.visitsucre.provider.ContractVisitSucre;

public class CategoryAdapter extends CursorAdapter{

    public CategoryAdapter(Context context){
        super(context, null, 10);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        /*LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.category_item, parent, false);*/
        return getView(parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if(cursor != null){
            ImageView logo = (ImageView)view.findViewById(R.id.ivLogo);

            TextView name = (TextView)view.findViewById(R.id.tvName);
            name.setText(cursor.getString(cursor.getColumnIndex(CategoryDao.Properties.Name.columnName)));

            TextView description = (TextView)view.findViewById(R.id.tvDescription);
            description.setText(cursor.getString(cursor.getColumnIndex(CategoryDao.Properties.Description.columnName)));
        }
    }

    @Override
    public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
        /*return super.newDropDownView(context, cursor, parent);*/
        return getView(parent);
    }

    public long getIdCategory(int position){
        Cursor cursor = getCursor();
        if(cursor != null){
            if(cursor.moveToPosition(position))
                return cursor.getLong(0);
            else
                return 0;
        }else{
            return 0;
        }
    }

    private View getView(ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.category_item, parent, false);
    }
}
