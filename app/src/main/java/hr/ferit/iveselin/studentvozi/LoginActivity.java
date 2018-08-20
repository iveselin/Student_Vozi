package hr.ferit.iveselin.studentvozi;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import hr.ferit.iveselin.studentvozi.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";


    @BindView(R.id.login_button)
    LoginButton loginButton;

    CallbackManager callbackManager;

    FirebaseAuth firebaseAuth;

    public static Intent getLaunchIntent(Context fromContext) {
        return new Intent(fromContext, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        setUi();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            continueToApp();
        }

    }

    private void setUi() {
        ButterKnife.bind(this);

        loginButton.setReadPermissions("email", "public_profile");

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: successful login");
                handleFacebookLogin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: user canceled the login");
                Toast.makeText(getApplicationContext(), "Kako bi pristupili aplikaciji, morate biti prijavljeni", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook login failed:" + error.toString());
            }
        });

    }

    private void handleFacebookLogin(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    continueToApp();
                } else {
                    Toast.makeText(getApplicationContext(), "FAIL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void continueToApp() {
        startActivity(MainActivity.getLaunchIntent(this));
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
