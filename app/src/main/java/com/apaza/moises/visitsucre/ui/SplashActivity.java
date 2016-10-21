package com.apaza.moises.visitsucre.ui;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.apaza.moises.visitsucre.R;

public class SplashActivity extends AppCompatActivity {

    private TextView counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setContentView(R.layout.activity_splash);
        counter = (TextView)findViewById(R.id.counter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        //startSplash();
        goToSignUpActivity();
    }

    private void startSplash(){
        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
                counter.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                counter.setText("Go!");
                //DetailPlaceActivity.createInstance(SplashActivity.this, "");
                //goToMainActivity();
                goToSignUpActivity();
            }
        }.start();
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goToSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        this.finish();
    }
}
