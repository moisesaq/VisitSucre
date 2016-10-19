package com.apaza.moises.visitsucre.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.global.SavedState;

public class ImageTextView extends LinearLayout implements View.OnClickListener{

    private ImageView imageView;
    private TextView textView1, textView2, textView3;
    private ImageButton imageButton;

    private OnImageTextViewListener onImageTextViewListener;

    public ImageTextView(Context context) {
        super(context);
        setupView();
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ImageTextView);

        Drawable icon = typedArray.getDrawable(R.styleable.ImageTextView_iconImage);
        setIconImage(icon);

        String text1 = typedArray.getString(R.styleable.ImageTextView_text1);
        setText1(text1);

        String text2 = typedArray.getString(R.styleable.ImageTextView_text2);
        setText2(text2);

        String text3 = typedArray.getString(R.styleable.ImageTextView_text3);
        setText3(text3);

        boolean singleLine = typedArray.getBoolean(R.styleable.ImageTextView_android_singleLine, false);
        setTextViewSingleLine(singleLine);

        int styleText = typedArray.getInt(R.styleable.ImageTextView_android_textStyle, 0);
        setTextViewStyle(styleText);

        Drawable iconButton = typedArray.getDrawable(R.styleable.ImageTextView_iconButton);
        setIconButton(iconButton);

        typedArray.recycle();
    }

    private void setupView(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_image_text, this, true);

        imageView = (ImageView)findViewById(R.id.imageView);
        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);
    }

    public void setOnImageTextViewListener(OnImageTextViewListener listener){
        this.onImageTextViewListener = listener;
    }

    private void setIconImage(Drawable image){
        if(image != null){
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(image);
        }
    }

    private void setIconButton(Drawable image){
        if(image != null){
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setImageDrawable(image);
        }

    }

    public void setText1(String text){
        if(text != null)
            textView1.setText(text);
    }

    public String getText1(){
        return textView1.getText().toString();
    }

    public void setText2(String text){
        if(text != null){
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(text);
        }

    }

    public void setText3(String text){
        if(text != null){
            textView3.setVisibility(View.VISIBLE);
            textView3.setText(text);
        }
    }

    private void setTextViewSingleLine(boolean singleLine){
        textView1.setSingleLine(singleLine);
    }

    private void setTextViewStyle(int style){
        if(style > 0)
            textView1.setTypeface(null, style);
    }

    @Override
    public void onClick(View view) {
        if(onImageTextViewListener != null)
            onImageTextViewListener.onEditClick(getId());
    }

    public interface OnImageTextViewListener{
        void onEditClick(int id);
    }

    /*SAVE STATE OF THE VIEWS*/
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.childrenStates = new SparseArray();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).saveHierarchyState(ss.childrenStates);
        }
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).restoreHierarchyState(ss.childrenStates);
        }
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }


}
