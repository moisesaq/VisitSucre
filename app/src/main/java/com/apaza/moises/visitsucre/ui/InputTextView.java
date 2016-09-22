package com.apaza.moises.visitsucre.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apaza.moises.visitsucre.R;

import java.util.regex.Pattern;

public class InputTextView extends LinearLayout{

    private ImageView icon;
    private TextInputLayout textInputLayout;
    private EditText editText;

    private Drawable imageIcon;
    private boolean maxLengthEnabled;
    private int maxLength;
    private int inputType;
    private String hint;

    private int lines;

    public InputTextView(Context context) {
        super(context);
        setupView();
    }

    public InputTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.InputTextView);
        imageIcon = typedArray.getDrawable(R.styleable.InputTextView_iconImage);
        setImageIcon(imageIcon);
        maxLengthEnabled = typedArray.getBoolean(R.styleable.InputTextView_maxLengthEnabled, false);
        setMaxLengthEnabled(maxLengthEnabled);
        maxLength = typedArray.getInteger(R.styleable.InputTextView_maxLength, 0);
        setMaxLength(maxLength);
        inputType = typedArray.getInt(R.styleable.InputTextView_android_inputType, InputType.TYPE_NULL);
        setInputType(inputType);

        lines = typedArray.getInt(R.styleable.InputTextView_android_lines, 1);
        setLines(lines);
        hint = typedArray.getString(R.styleable.InputTextView_hint);
        setHint(hint);
        typedArray.recycle();
    }

    private void setupView(){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_input_text, this, true);
        icon = (ImageView)findViewById(R.id.icon);
        textInputLayout = (TextInputLayout)findViewById(R.id.textInputLayout);
        textInputLayout.setErrorEnabled(true);
        editText = (EditText)findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public String getText(){
        return editText.getText().toString();
    }

    public void clearField(){
        this.editText.getText().clear();
    }

    public void setImageIcon(Drawable imageIcon){
        if(imageIcon != null){
            icon.setVisibility(View.VISIBLE);
            icon.setImageDrawable(imageIcon);
        }
    }

    public void setMaxLengthEnabled(boolean enabled){
        textInputLayout.setCounterEnabled(enabled);
    }

    public void setMaxLength(int maxLength){
        textInputLayout.setCounterMaxLength(maxLength);
    }

    public void setInputType(int inputType){
        editText.setInputType(inputType);
    }

    public void setLines(int lines){
        if(lines > 1){
            editText.setGravity(Gravity.START|Gravity.TOP);
            editText.setLines(lines);
        }
    }
    public void setHint(String text){
        if(!text.isEmpty())
            textInputLayout.setHint(text);
    }

    public boolean isTextValid(String textError){
        String text = editText.getText().toString();
        Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");

        if(text.isEmpty()){
            textInputLayout.setError(textError + ", field empty");
            return false;
        }

        if(pattern.matcher(text).matches() && text.length() <= textInputLayout.getCounterMaxLength()){
            return true;
        } else {
            textInputLayout.setError(textError);
            return false;
        }

        //return true;
    }

    public boolean isPhoneValid(String phone){
        if(!Patterns.PHONE.matcher(phone).matches()){
            textInputLayout.setError("Phone invalid");
            return false;
        }else{
            textInputLayout.setError(null);
        }
        return true;
    }

    public boolean isEmailValid(String email){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textInputLayout.setError("Email invalid");
            return false;
        }else {
            textInputLayout.setError(null);
        }
        return true;
    }
}
