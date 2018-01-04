package com.apaza.moises.visitsucre.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
<<<<<<< HEAD

import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.global.Utils;
import com.apaza.moises.visitsucre.ui.InputTextView;
import com.apaza.moises.visitsucre.ui.MainActivity;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.base.BaseFragment;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

public class LoginFragment extends BaseFragment implements View.OnClickListener, LoginContract.View,
                                        FacebookCallback<LoginResult>, GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "LOGIN FRAGMENT";
    private static final int RC_SIGN_IN = 1009;

=======
import android.widget.Toast;

import com.apaza.moises.visitsucre.global.Global;
import com.apaza.moises.visitsucre.ui.InputTextView;
import com.apaza.moises.visitsucre.ui.MainActivity;
import com.apaza.moises.visitsucre.R;
import com.apaza.moises.visitsucre.ui.fragment.base.BaseFragment;
import com.apaza.moises.visitsucre.ui.view.ImageTextView;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends BaseFragment implements View.OnClickListener, LoginContract.View{

>>>>>>> 83ca68189d036a9a1d75e2de47611f2d65ca65ad
    private CallBack mCallBack;
    private LoginContract.Presenter presenter;

    private View view;
    private InputTextView itvEmail, itvPassword;
    private LinearLayout layoutLogin, layoutLoading;
<<<<<<< HEAD

    /*LOGIN FACEBOOK*/
    private CallbackManager callbackManager;

    /*LOGIN GOOGLE*/
    private GoogleSignInOptions gso;
    private GoogleApiClient googleApiClient;

    /*FIRE BASE*/
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;

    public static LoginFragment newInstance(Context context, FirebaseAuth firebaseAuth) {
        LoginFragment fragment = new LoginFragment();
        LoginInteractor loginInteractor = new LoginInteractor(context, firebaseAuth);
        new LoginPresenter(fragment, loginInteractor);
        
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                //.requestIdToken("TOKEN")//TODO This is for work with FIRE BASE
                .build();

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();

        fireAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                    goToMainActivity();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        setTitle("Login");
        setupLoginWithFacebook();
        setupLoginWithGoogle();
        setup();
        return view;
    }

    private void setup(){
        layoutLogin = (LinearLayout)view.findViewById(R.id.layoutLogin);
<<<<<<< HEAD
        layoutLoading = (LinearLayout)view.findViewById(R.id.layoutLoading);
=======
        layoutLoading = (LinearLayout)view.findViewById(R.id.layoutLoading); 
        Button loginWithFace = (Button) view.findViewById(R.id.b_login_with_face);
        loginWithFace.setOnClickListener(this);
        Button loginWithG = (Button)view.findViewById(R.id.b_login_with_g);
        loginWithG.setOnClickListener(this);
>>>>>>> 83ca68189d036a9a1d75e2de47611f2d65ca65ad
        itvEmail = (InputTextView) view.findViewById(R.id.itv_email);
        itvPassword = (InputTextView) view.findViewById(R.id.itv_password);
        Button login = (Button)view.findViewById(R.id.b_login);
        login.setOnClickListener(this);
        TextView forgotPassword = (TextView) view.findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

<<<<<<< HEAD
    private void setupLoginWithFacebook(){
        LoginButton loginWithFacebook = (LoginButton) view.findViewById(R.id.lb_login_facebook);
        //loginWithFacebook.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginWithFacebook.setFragment(this);
        loginWithFacebook.registerCallback(callbackManager, this);
    }

    private void setupLoginWithGoogle(){
        SignInButton signInButton = (SignInButton)view.findViewById(R.id.b_sign_in);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(fireAuthStateListener);
    }

=======
>>>>>>> 83ca68189d036a9a1d75e2de47611f2d65ca65ad
    @Override
    public void onResume(){
        super.onResume();
        presenter.start();
<<<<<<< HEAD
    }

    @Override
    public void onStop(){
        super.onStop();
        firebaseAuth.removeAuthStateListener(fireAuthStateListener);
=======
>>>>>>> 83ca68189d036a9a1d75e2de47611f2d65ca65ad
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
<<<<<<< HEAD
            case R.id.b_sign_in:
                signIn();
=======
            case R.id.b_login_with_face:
                Toast.makeText(getActivity(), "Face", Toast.LENGTH_SHORT).show();
                break;
            case R.id.b_login_with_g:
                Toast.makeText(getActivity(), "G+", Toast.LENGTH_SHORT).show();
>>>>>>> 83ca68189d036a9a1d75e2de47611f2d65ca65ad
                break;
            case R.id.b_login:
                attemptLogin();
                break;
            case R.id.forgotPassword:
<<<<<<< HEAD
                //mCallBack.onForgotPasswordClick();
                signOut();
=======
                mCallBack.onForgotPasswordClick();
>>>>>>> 83ca68189d036a9a1d75e2de47611f2d65ca65ad
                break;
        }
    }

    private void attemptLogin() {
        if(itvEmail.isEmailValid() && itvPassword.isTextValid("Invalid"))
            presenter.attemptLogin(itvEmail.getText(), itvPassword.getText());
    }

    /*LOGIN CONTRACT VIEW*/
    @Override
<<<<<<< HEAD
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            Global.showToastMessage("Login success");
            GoogleSignInAccount account = result.getSignInAccount();
            if(account != null)
                itvEmail.setText(account.getEmail());
        }else{
            Global.showToastMessage("Login with google failed!");
        }
    }

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Utils.showToastMessage("Account sign out :/");
            }
        });
    }

    private void attemptLogin() {
        if(itvEmail.isEmailValid() && itvPassword.isTextValid("Invalid"))
            presenter.attemptLogin(itvEmail.getText(), itvPassword.getText());
    }

    /*LOGIN CONTRACT VIEW*/
    @Override
=======
>>>>>>> 83ca68189d036a9a1d75e2de47611f2d65ca65ad
    public void showProgress(boolean show) {
        layoutLoading.setVisibility(show? View.VISIBLE: View.GONE);
        layoutLogin.setVisibility(show? View.GONE: View.VISIBLE);
    }

    @Override
    public void showLoginError(String msg) {
        Global.showToastMessage(msg);
    }

    @Override
    public void showLoginSuccess() {
<<<<<<< HEAD
        goToMainActivity();
=======
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
>>>>>>> 83ca68189d036a9a1d75e2de47611f2d65ca65ad
    }

    @Override
    public void showGooglePlayServicesDialog(int codeError) {
        mCallBack.onInvokeGooglePlayServices(codeError);
    }

    @Override
    public void showGooglePlayServicesError() {
        Global.showMessage("Necessary google play service for use app");
    }

    @Override
    public void showNetWorkError() {
        Global.showToastMessage("No internet");
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        if(presenter != null){
            this.presenter = presenter;
        }else {
            throw new RuntimeException("The presenter can not null");
        }
    }

<<<<<<< HEAD
    private void goToMainActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //getActivity().finish();
    }
    /*FACEBOOK CALL BACK*/
    @Override
    public void onSuccess(LoginResult loginResult) {
        /*Utils.showToastMessage("Login facebook success " + loginResult.getAccessToken().getUserId());
        loadProfileFacebook(loginResult);*/
        handlerAccessTokenFacebook(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
        Utils.showToastMessage("Login facebook canceled");
    }

    @Override
    public void onError(FacebookException error) {
        Utils.showToastMessage("Error in login facebook");
    }

    private void handlerAccessTokenFacebook(AccessToken accessToken){
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Utils.showToastMessage("Error in login with facebook - firebase");
                }
            }
        });
    }

    public void loadProfileFacebook(LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject me, GraphResponse response) {
                Log.d(TAG, "PROFILE FACEBOOK >>> " + me.toString());
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
        request.setParameters(bundle);
        request.executeAsync();
    }

    /*GOOGLE API CLIENT LISTENER*/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

=======
>>>>>>> 83ca68189d036a9a1d75e2de47611f2d65ca65ad
    public interface CallBack {
        void onForgotPasswordClick();
        void onInvokeGooglePlayServices(int codeError);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof CallBack){
            mCallBack = (CallBack) context;
        }else {
            throw new ClassCastException(context.toString()
                    + " must implement CallBack");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }
}
