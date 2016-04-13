package io.github.ghostwriternr.selene;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView info;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    public final static String INTENT_MESSAGE = "io.github.ghostwriternr.selene.";

    String fbauth = "http://10.117.11.116:8080/api/v1/login?";
    String ggauth = "http://10.117.11.116:8080/api/v1/googleDATA?";
//    RequestQueue queue = Volley.newRequestQueue(this);


    protected void setTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();
        setContentView(R.layout.activity_login);
        setTranslucent(true);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope("https://www.googleapis.com/auth/youtube"))
                .requestServerAuthCode("314018233988-rmjrnnu9kae01dc073jb1oqd09c13l17.apps.googleusercontent.com", false)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

//        signIn();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        final com.google.android.gms.common.SignInButton gbutton = (com.google.android.gms.common.SignInButton)findViewById(R.id.sign_in_button);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
            gbutton.setVisibility(View.GONE);
//            gbutton.setBackgroundColor(0);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
//            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                    gbutton.setVisibility(View.GONE);
//                    gbutton.setBackgroundColor(1);
                }
            });
        }

        info = (TextView) findViewById(R.id.info);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("user_friends,public_profile"));
        TextView brandinfo = (TextView) findViewById(R.id.brand_info);
        Typeface brandfont = Typeface.createFromAsset(getAssets(), "fonts/Sophia.ttf");
        brandinfo.setTypeface(brandfont);

        assert loginButton != null;
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            @Override
            public void onSuccess(LoginResult loginResult) {
                SharedPreferences sharedPref = getSharedPreferences("io.github.ghostwriternr", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.facebook),"token=" + loginResult.getAccessToken().getToken() + "&fbid=" + loginResult.getAccessToken().getUserId());
                editor.apply();
                info.setText(
                        "User ID: " + loginResult.getAccessToken().getUserId() + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken()
                );
                fbauth += "token=" + loginResult.getAccessToken().getToken() + "&fbid=" + loginResult.getAccessToken().getUserId();
                Log.v("LoginActivity", fbauth);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, fbauth,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.v("LoginActivity", response);
                                info.setText("Response is: "+ response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("LoginActivity", "That didn't work!");
                        info.setText("That didn't work!");
                    }
                });
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(stringRequest);
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // Signed in successfully, show authenticated UI.
//            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            GoogleSignInAccount acct = result.getSignInAccount();
            SharedPreferences sharedPref = getSharedPreferences("io.github.ghostwriternr", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.google),"gtoken=" + acct.getServerAuthCode());
            editor.apply();
            info.setText(getString(R.string.signed_in_fmt, acct.getServerAuthCode()));
            com.google.android.gms.common.SignInButton gbutton = (com.google.android.gms.common.SignInButton)findViewById(R.id.sign_in_button);
//            gbutton.setVisibility(View.GONE);
//            String fbstr = getResources().getString(R.string.facebook);
            String fbstr = sharedPref.getString(getString(R.string.facebook),null);
            ggauth = ggauth + fbstr + "&gtoken=" + acct.getServerAuthCode();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, ggauth,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("LoginActivity", response);
                            info.setText("Response is: "+ response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("LoginActivity", "That didn't work out!");
                    info.setText("That didn't work out!");
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(stringRequest);
        }
        else
        {
            info.setText("Login Failed!");
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        if (mGoogleApiClient.isConnected())
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button: {
                signIn();
                break;
            }
        }
    }

    public void ProceedToMain(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
