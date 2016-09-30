package com.apaza.moises.visitsucre.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.apaza.moises.visitsucre.R;

public class GalleryIconDialog extends DialogFragment{

    public static final String TAG = "GALLERY ICONS DIALOG";

    private View view;
    private GridView gridViewIcons;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_gallery_icon, container, false);
        setupView();
        return view;
    }

    private void setupView() {
        gridViewIcons = (GridView)view.findViewById(R.id.gridViewIcons);
    }

    public class IconAdapter extends BaseAdapter{
        private Context mContext;

        private Integer[] mIconIds = {
                R.mipmap.ic_airplane_grey600_24dp, R.mipmap.ic_bank_grey600_24dp, R.mipmap.ic_camera_iris_grey600_24dp
        };

        public IconAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mIconIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public int getThumbId(int position){return mIconIds[position];}

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(90,90));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mIconIds[position]);
            return imageView;
        }
    }
}
