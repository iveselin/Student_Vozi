package hr.ferit.iveselin.studentvozi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.ferit.iveselin.studentvozi.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";


    public static Intent getLaunchIntent(Context fromContext) {
        return new Intent(fromContext, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }


    @OnClick(R.id.sign_in)
    void onLoginClick() {
        startActivity(LoginActivity.getLaunchIntent(this));
    }

    @OnClick(R.id.locate)
    void onLocateClick() {
        startActivity(LocationActivity.getLaunchIntent(this));
    }

    @OnClick({R.id.logout})
    void onLogoutClick() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }
}
