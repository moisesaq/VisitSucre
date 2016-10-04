package com.apaza.moises.visitsucre.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.global.Global;

public class GalleryIconDialog extends DialogFragment implements AdapterView.OnItemClickListener{

    public static final String TAG = "GALLERY ICONS DIALOG";

    private View view;
    private IconAdapter iconAdapter;
    private OnGalleryIconDialogListener onGalleryIconDialogListener;

    public static GalleryIconDialog newInstance(OnGalleryIconDialogListener onGalleryIconDialogListener){
        GalleryIconDialog galleryIconDialog = new GalleryIconDialog();
        galleryIconDialog.setOnGalleryIconDialogListener(onGalleryIconDialogListener);
        return galleryIconDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_gallery_icon, container, false);
        setupView();
        return view;
    }

    /*@NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Icons");
        setupView();
        if(view != null)
            dialog.setView(view);
        return dialog.create();
    }*/

    private void setupView() {
        GridView gridViewIcons = (GridView)view.findViewById(R.id.gridViewIcons);
        iconAdapter = new IconAdapter(getContext());
        gridViewIcons.setAdapter(iconAdapter);
        gridViewIcons.setOnItemClickListener(this);
    }

    public void setOnGalleryIconDialogListener(OnGalleryIconDialogListener listener){
        this.onGalleryIconDialogListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        int icon = iconAdapter.getImageId(position);
        Global.showToastMessage("IMAGE " + getResources().getResourceEntryName(icon));
        if(onGalleryIconDialogListener != null){
            onGalleryIconDialogListener.onIconSelected(icon);
            onBack();
        }else{
            Log.d(TAG, " >>>> LISTENER NULL");
        }

    }

    private void onBack(){
        this.dismiss();
        getActivity().onBackPressed();
    }

    public interface OnGalleryIconDialogListener {
        void onIconSelected(int icon);
    }

    public class IconAdapter extends BaseAdapter{

        private Resources resources;
        private Context mContext;

        private Integer[] mIconIds = {
                R.mipmap.ic_airplane_grey600_24dp, R.mipmap.ic_bank_grey600_24dp, R.mipmap.ic_camera_iris_grey600_24dp,
                R.mipmap.ic_communication_call, R.mipmap.ic_taxi_model_a, R.mipmap.ic_preferences_luggage
        };

        public IconAdapter(Context c) {
            mContext = c;
            resources = getActivity().getResources();
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

        public int getImageId(int position){return mIconIds[position];}

        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.grid_item, null);
            }

            int image = mIconIds[position];
            ImageView ivIcon = (ImageView)view.findViewById(R.id.iv_icon);
            TextView tvTitle = (TextView)view.findViewById(R.id.tv_title);
            ivIcon.setImageResource(image);
            tvTitle.setText(getImageName(image));
            return view;
        }

        private String getImageName(int imageId){
            String name = "No name";
            if(resources != null)
                name = resources.getResourceEntryName(imageId);
            return name;
        }
    }
}
